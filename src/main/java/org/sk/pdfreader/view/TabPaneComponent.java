package org.sk.pdfreader.view;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class TabPaneComponent extends TabPane {
    private  final Map<File,Tab> openFiles;
    protected final ObjectProperty<AppEvent<Node>> eventProperty= new SimpleObjectProperty<>();
    private DisplayInfoHandler displayInfoHandler;

    public TabPaneComponent() {
        super();
        openFiles=new HashMap<>();
        init();
    }

    public TabPaneComponent(Tab... tabs) {
        super(tabs);
        openFiles=new HashMap<>();

        init();
    }
    private void init(){

    }
    public ObjectProperty<AppEvent<Node>> getEventProperty(){
        return eventProperty;
    }
    /**
     * LOGIC SECTION
     **/
    public void openFile(File data) {
        if(data==null||openFiles.containsKey(data)) return;
        Tab t=null;
        if(data.isDirectory()){
            t=openListFolderPane(data);
        }else
        if(data.isFile()){
            t=openPDFPane(data);
        }

        if(t!=null)
            openFiles.put(data,t);
    }

    /**
     * Doing the low-level display stuff
     *
     * @return A Tab containing the pdf
     */
    private Tab openPDFPane(File data) {
        //check it again, in case
        if(data.isFile()&&data.getName().endsWith(".pdf")){
            Tab fileTab=new Tab(data.getName());
            getTabs().add(fileTab);
            fileTab.setStyle("-fx-pref-width: 250");

            getSelectionModel().select(fileTab);
            Task<PDFPaneComponent> pdfTask=new Task<PDFPaneComponent>() {
                @Override
                protected PDFPaneComponent call() throws Exception {

                    return new PDFPaneComponent(data);
                }

            };
            pdfTask.setOnScheduled((e)->{
                ProgressIndicator pi=new ProgressIndicator();
                StackPane sp=new StackPane();
                sp.getChildren().add(pi);
                sp.setAlignment(Pos.CENTER);
                pi.progressProperty().bind(pdfTask.progressProperty());
                fileTab.setContent(sp);
            });

            pdfTask.setOnSucceeded((t)->{
                    System.out.println("OPEN PDF SUCCEEDED");
                    Platform.runLater(()->{
                        fileTab.setContent(null);
                        try {
                            PDFPaneComponent pdfPaneComponent = pdfTask.get();
                            fileTab.setContent(pdfPaneComponent);
                            displayInfoHandler.handle(pdfPaneComponent.getInfoPane());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            throw new RuntimeException(e);
                        } catch (ExecutionException e) {
                            e.printStackTrace();

                            throw new RuntimeException(e);
                        }
                    });

            });
            pdfTask.setOnFailed((e)-> {
                Throwable exception = pdfTask.getException();
                exception.printStackTrace();
                fileTab.setContent(getPDFErrorNode(e,exception));
            });
            Thread t=new Thread(pdfTask);
            t.start();
            return fileTab;
        }
        return null;
    }
//TODO move this in PDFPaneComponent
    private Node getPDFErrorNode(WorkerStateEvent e, Throwable exception) {
        StackPane sp=new StackPane();
        sp.setAlignment(Pos.TOP_LEFT);
        StringBuilder sb=new StringBuilder();
        sb.append(exception.getMessage()+"\n");
        sb.append(exception.getCause()+"\n");
        Arrays.asList(exception.getStackTrace()).forEach((s)->{
            sb.append(s.getClassName()+"."+s.getMethodName()+" at "+s.getLineNumber()+"\n");
                }
        );
        String message = sb.toString();

        Label l=new Label("COULD NOT OPEN PDF FILE BECAUSE \n"+message);
        l.setStyle("-fx-font-size: 12;");
        sp.getChildren().add(l);

        return sp;
    }


    private Tab openListFolderPane(File data) {
        //check is folder again, in case I'l ever call it from somewhere else

        if(data.isDirectory()){
            Tab fileTab=new Tab(data.getName());
            ProgressIndicator indicator=new ProgressIndicator(0);
            fileTab.setContent(indicator);
            FolderListTask task=new FolderListTask(data,fileTab);
            indicator.progressProperty().bind(task.progressProperty());
            getTabs().add(fileTab);
            task.setOnSucceeded((e)->{
                openFiles.put(data,fileTab);
            });
            task.run();
            return fileTab;
        }
        return null;
    }

    public void addDisplayInfoHandler(DisplayInfoHandler displayInfoHandler) {
        this.displayInfoHandler=displayInfoHandler;
    }

    //TODO  this should be moved in listview
    private static class FolderListTask extends Task<Void>{
        private final File folder;
        private final Tab fileTab;
        public FolderListTask(File folder, Tab tab) {
            this.folder = folder;
            fileTab=tab;
        }
        @Override
        protected Void call() throws Exception {
            FileListView listView=new FileListView();

            File[] files = folder.listFiles(pathname -> pathname.getName().endsWith(".pdf")||pathname.isDirectory());
            if(files!=null&&files.length>0) {
                List<File> list = Arrays.asList(files);
                TreeItem<File> root=new TreeItem<>(folder);
                list.forEach(f->{
                    System.out.println(f.getName());
                    TreeItem<File> treeItem=new TreeItem<>(f);
                    root.getChildren().add(treeItem);
                });

                listView.setRootItem(root);
                Platform.runLater(()->{
                    fileTab.setContent(listView);

                });
            }
            return null;
        }
    }


}
