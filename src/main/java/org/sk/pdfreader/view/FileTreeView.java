package org.sk.pdfreader.view;


import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.File;

import java.io.FileFilter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class FileTreeView extends SearchableTreeViewComponent<File>{
    private SearchService service;
    public static final String OPEN_FOLDER_ICON="fa-folder-open";
    public static final String CLOSE_FOLDER_ICON="fa-folder";
    public static final String PDF_FILE_ICON ="fa-file-pdf-o";

    private final FileFilter fileFilter=(f)-> ((f.isFile()&&f.getName().endsWith(".pdf")||(f.isDirectory()&&!f.isHidden()&&!f.getName().startsWith("."))));
    private final Comparator<File> sortOrder= ((o1, o2) -> {
        if (o1.isDirectory() && o2.isDirectory())
            return 0;
        if (o1.isDirectory() && o2.isFile()) return -1;
        if (o2.isDirectory() && o1.isFile()) return 1;
        return 0;
    });
    public FileTreeView(){
        super();


    }
    private FontIcon getIcon(String label){
        FontIcon icon=new FontIcon(label);
        //TODO move to css
        icon.setIconColor(Color.rgb(122,2,22,1));
        return icon;
    }
    public void initialize(){

        setTreeCellFactoryProperty(new Callback<>() {
            @Override
            public TreeCell<File> call(TreeView<File> fileTreeView) {
                return new TreeCell<>() {
                    @Override
                    protected void updateItem(File file, boolean empty) {
                        super.updateItem(file, empty);
                        if (!empty) {
                            setText(file.getName());
                            if (file.isDirectory()) {
                                if (getTreeItem().isExpanded()) {
                                    setGraphic(getIcon(OPEN_FOLDER_ICON));
                                } else
                                    setGraphic(getIcon(CLOSE_FOLDER_ICON));
                            }else
                                setGraphic(getIcon(PDF_FILE_ICON));
                        } else {setText("");setGraphic(null);}
                    }
                };
            }
        });
        treeView.rootProperty().addListener((observableValue, fileTreeItem, t1) -> {
            if(service==null){
                service=new SearchService(t1,"");
            }
            if(service.isRunning()){
                service.reset();
            }
            service.setRoot(t1);
            Task<Void> t=new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    expandFolderTree(t1);
//                t1.getChildren().forEach(this::expandFolderTree);
                    t1.setExpanded(true);
                    return null;
                }
            };
            t.run();
        });
        treeView.addEventFilter(MouseEvent.MOUSE_CLICKED,(e)->{
                    if(e.getClickCount()==1&&e.getButton().equals(MouseButton.PRIMARY))
                        singleClickPrimary();
                    if(e.getClickCount()==2&&e.getButton().equals(MouseButton.PRIMARY))
                        doubleClickPrimary();
                    if(e.getClickCount()==1&&e.getButton().equals(MouseButton.SECONDARY))
                        singleClickSecondary();
                    if(e.getClickCount()==2&&e.getButton().equals(MouseButton.SECONDARY))
                        doubleClickSecondary();
                }
        );
//        searchField.textProperty().addListener((observableValue, s, t1) -> doSearch(t1));
    }
    public void search(){
        String s = getSearchFieldTextProperty().get();
        doSearch(s);
    }
    private void doSearch(String s){
        if(s!=null&&!s.isEmpty()) {
            if(service.isRunning()){
                service.cancel();
                service.setSearch(s);
                service.reset();
            }else
                service.start();

        }
    }

    @Override
    protected void doubleClickSecondary() {

    }

    @Override
    protected void singleClickSecondary() {

    }

    @Override
    protected void doubleClickPrimary() {
        TreeItem<File> selectedItem = treeView.getSelectionModel().getSelectedItem();
        handler.handle(selectedItem.getValue());
//        AppEvent<File> newEvent=new AppEvent<>(selectedItem.getValue(),AppEvent.TREE_ITEM_SELECTED);
//        eventProperty.setValue(newEvent);
    }
    @Override
    protected void singleClickPrimary() {
        /*TreeItem<File> selectedItem = treeView.getSelectionModel().getSelectedItem();
        expandFolderTree(selectedItem);
        if(selectedItem!=null)
            selectedItem.getChildren().forEach(this::expandFolderTree);*/
    }
    public void expandFolderTree(TreeItem<File> selected){
        TreeItem<File> selectedItem=selected;
        if(selected==null)
            selectedItem=rootProperty().get();
        File file=selectedItem.getValue();
        if(!selectedItem.getChildren().isEmpty()) return;

        if(file.isDirectory()&&(!file.isHidden()&&!file.getName().startsWith("."))){//the dot thing is added for linux.
            File[] files=file.listFiles(fileFilter);
            if(files!=null) {
                List<File> fs=Arrays.asList(files);
                fs.sort(sortOrder);
                for (File s : fs) {
                    TreeItem<File> child = new TreeItem<>(s);
                    selectedItem.getChildren().add(child);
                    expandFolderTree(child);
                }
            }
        }
    }



    private class SearchService extends Service<Void> {
        private TreeItem<File> root;
        private String search;

        private SearchService(TreeItem<File> root,String search) {
            this.root = root;
            this.search=search;
        }
        private void setRoot(TreeItem<File> root){
            this.root=root;
        }
        public void setSearch(String search) {
            this.search = search;
        }
        private void searchInTree(TreeItem<File> rootItem) {
            File root=rootItem.getValue();
            if(search==null || search.isEmpty()) return;

            File[] filesArray = root.listFiles(fileFilter);
            if(filesArray ==null || filesArray.length==0) return;

            List<File> files=Arrays.asList(filesArray);
            files.sort(sortOrder);
            files.forEach((f)-> {
                TreeItem<File> child=new TreeItem<>(f);
                searchInTree(child);
                rootItem.getChildren().add(child);
            });

        }
        @Override
        protected Task<Void> createTask() {
            return new Task<>() {
                @Override
                protected Void call() {
                    searchInTree(root);
                    return null;
                }
            };
        }
    }
}
