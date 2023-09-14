package org.sk.pdfreader.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableView;

import javafx.scene.layout.VBox;
import org.sk.pdfreader.PDFApplication;

import java.io.IOException;

public abstract class SearchableListView<T> extends VBox {

    @FXML
    protected TextField searchTextField;
    @FXML
    protected ComboBox<T> searchFieldsCombo;
    @FXML
    protected TreeTableView<T> treeTableView;

    public SearchableListView(){
        FXMLLoader loader=new FXMLLoader(PDFApplication.class.getResource("searchable-list.fxml"));
        loader.setController(this);
        loader.setRoot(this);
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    public abstract void initialize();
    public void setRootItem(TreeItem<T> root){
        treeTableView.setRoot(root);
    }
}
