package org.sk.pdfreader;

import javafx.application.Platform;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

import org.kordamp.ikonli.javafx.FontIcon;

import org.sk.pdfreader.view.Controller;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.List;

public class MainController implements Controller{
    public MenuItem openMenuButton;
    public AnchorPane leftPane;
    public AnchorPane centralPane;
    public AnchorPane rightPane;
    public Label leftStatus;
    public Pane bottomPane;
    public Label rightStatus;
    public TreeView<File> fileBrowser;
    public TabPane centralTabPane;
    private final ObjectProperty<File> fileProperty=new SimpleObjectProperty<>();


    public MainController() {
        mediatorProperty=new SimpleObjectProperty<>();
    }

    @FXML
    public void initialize(){
        mediatorProperty.addListener((observableValue, signalMediator, t1) -> {
            fileProperty.addListener(t1.getFilePropertyListener());
            t1.openedFilesProperty().addListener((ListChangeListener<File>) change -> {
                if(change.wasAdded()){
                    List<? extends File> addedSubList = change.getAddedSubList();
                    File file = addedSubList.get(0);
                    if(file.isDirectory()){

                    }
                }
            });
        });

        centralTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);
        fileBrowser.setCellFactory(new Callback<TreeView<File>, TreeCell<File>>() {
            @Override
            public TreeCell<File> call(TreeView<File> fileTreeView) {
                return new TreeCell<>(){
                    @Override
                    protected void updateItem(File file, boolean empty) {
                        super.updateItem(file, empty);
                        if(!empty){
                            setText(file.getName());
                            if(file.isDirectory()){
                                FontIcon icon;
                                if(getTreeItem().isExpanded()) {
                                    icon= new FontIcon("far-folder-open");
                                }else
                                    icon=new FontIcon("far-folder");
                                setGraphic(icon);
                            }
                        }else setText("");
                    }
                };
            }
        });
        fileBrowser.addEventFilter(MouseEvent.MOUSE_CLICKED,new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                TreeItem<File> selectedItem = fileBrowser.getSelectionModel().getSelectedItem();
                File file = selectedItem.getValue();
                if(mouseEvent.getClickCount()==1){
                    if(!selectedItem.getChildren().isEmpty()) {
                        return;
                    }
                    if(file.isDirectory()&&selectedItem.getChildren().isEmpty()){
                        Platform.runLater(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        expandFolderTree(selectedItem);
                                        selectedItem.expandedProperty().set(true);
                                    }
                                }
                        );
                    }
                }
                if(mouseEvent.getClickCount()==2&& mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                    fileProperty.setValue(file);
                }
                if(mouseEvent.getClickCount()==1&& mouseEvent.getButton().equals(MouseButton.SECONDARY)){
                    ContextMenu menu=new ContextMenu();
                    MenuItem open=new MenuItem("Open");
                    File selectedFile = fileBrowser.getSelectionModel().getSelectedItem().getValue();
                    open.setDisable(selectedFile.isDirectory());
                    open.setOnAction(actionEvent -> {
                            if(!selectedFile.isDirectory())
                                fileProperty.setValue(selectedFile);
                    });
                    menu.getItems().add(open);
                }
            }
        });
        TreeItem<File> root=new TreeItem<>();
        root.setValue(new File(System.getProperty("user.home")));

        fileBrowser.rootProperty().addListener(new ChangeListener<TreeItem<File>>() {
            @Override
            public void changed(ObservableValue<? extends TreeItem<File>> observableValue, TreeItem<File> fileTreeItem, TreeItem<File> t1) {
                expandFolderTree(t1);
                root.setExpanded(true);
            }
        });
        fileBrowser.rootProperty().setValue(root);
    }
    public Stage stage;
    public void openFolderDialog(ActionEvent actionEvent) {
        DirectoryChooser directoryChooser=new DirectoryChooser();
        directoryChooser.setTitle("Open folder");
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File file=directoryChooser.showDialog(stage);
        TreeItem<File> newRoot=new TreeItem<>(file);
        fileBrowser.rootProperty().setValue(newRoot);
    }

    public void expandFolderTree(TreeItem<File> selectedItem){
        File file=selectedItem.getValue();
        if(!selectedItem.getChildren().isEmpty()) return;
        if(file.isDirectory()&&!file.isHidden()&&!file.getName().startsWith(".")){//the dot thing is added for linux.
            File[] files = file.listFiles(new FilenameFilter(){
                @Override
                public boolean accept(File dir, String name) {
                    if (dir.isDirectory())
                        return !dir.isHidden();
                    else {
                        return name.endsWith(".pdf");
                    }
                }
            });
            if(files!=null)
                Arrays.asList(files).forEach((s)->{
                    TreeItem<File> child=new TreeItem<>(s);
                    selectedItem.getChildren().add(child);
                });
        }
    }


    private ObjectProperty<ApplicationManager> mediatorProperty;
    @Override
    public void setSignalMediator(ApplicationManager mediator) {
        mediatorProperty.set(mediator);
    }
}
