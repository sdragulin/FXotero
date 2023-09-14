package org.sk.pdfreader.view;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import org.sk.pdfreader.PDFApplication;

import java.io.IOException;


public abstract class SearchableTreeViewComponent<T> extends VBox {

    public ToolBar toolBar;
    @FXML
    protected TreeView<T> treeView;
    @FXML
    protected TextField searchField;
    protected OpenHandler<T> handler;
    private T data;
    private ObjectProperty<Callback<TreeView<T>, TreeCell<T>>> cellFactoryProperty;
    protected final ObjectProperty<AppEvent<T>> eventProperty= new SimpleObjectProperty<>();
    public SearchableTreeViewComponent(){
        FXMLLoader loader=new FXMLLoader(PDFApplication.class.getResource("searchable-tree.fxml"));
        loader.setController(this);
        loader.setRoot(this);
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public ObjectProperty<AppEvent<T>> getEventProperty(){
        return eventProperty;
    }
    public ReadOnlyObjectProperty<TreeItem<T>> selectedItemProperty(){
        return treeView.getSelectionModel().selectedItemProperty();
    }
    public ObjectProperty<TreeItem<T>> rootProperty(){
        return treeView.rootProperty();
    }
    @FXML
    public void initialize(){
        toolBar.prefWidthProperty().bind(this.widthProperty());
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

    }

    public void setOpenHandler(OpenHandler<T> dataHandler){
        handler=dataHandler;
    }

    public OpenHandler<T> getHandler() {
        return handler;
    }

    protected abstract void doubleClickSecondary();

    protected abstract void singleClickSecondary();

    protected abstract void doubleClickPrimary() ;

    protected abstract void singleClickPrimary() ;

    public void setTreeCellFactoryProperty(Callback<TreeView<T>, TreeCell<T>> factory){
        treeView.setCellFactory(factory);
    }

    public StringProperty getSearchFieldTextProperty(){
        return searchField.textProperty();
    }

    @FXML
    public abstract void search();

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
