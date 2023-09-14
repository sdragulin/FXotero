package org.sk.pdfreader.view;


import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.util.Callback;

import org.kordamp.ikonli.javafx.FontIcon;
import java.io.File;

import java.util.Date;

public class FileListView extends SearchableListView<File>{

    public FileListView() {
        super();
    }

    public void initialize(){
        TreeTableColumn<File,FontIcon> iconColumn=new TreeTableColumn<>("Type");
        TreeTableColumn<File,String> nameColumn=new TreeTableColumn<>("Name");
        TreeTableColumn<File,Long> sizeColumn=new TreeTableColumn<>("Size");
        TreeTableColumn<File, Date> lastModifiedColumn=new TreeTableColumn<>("Last Modified");
        iconColumn.setCellFactory(f -> new TreeTableCell<>(){
            @Override
            protected void updateItem(FontIcon fontIcon, boolean b) {
                super.updateItem(fontIcon, b);
                if(!b){
                    File value = getTableRow().getTreeItem().getValue();
                    setText("");
                    if(value.isDirectory()) {
                        setGraphic(new FontIcon("far-folder"));
                    }else
                        setGraphic(new FontIcon("far-file-pdf"));
                }
            }
        });
        nameColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("name"));

        sizeColumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<File, Long>, ObservableValue<Long>>() {

            @Override
            public ObservableValue<Long> call(TreeTableColumn.CellDataFeatures<File, Long> size) {
                SimpleLongProperty slp=new SimpleLongProperty(size.getValue().getValue().length()/1024);
                return slp.asObject();
            }
        });
        lastModifiedColumn.setCellValueFactory(fileLocalDateCellDataFeatures -> {
            File value = fileLocalDateCellDataFeatures.getValue().getValue();
            long l = value.lastModified();
            Date date=new Date(l);
            return new SimpleObjectProperty<>(date);
        });
        treeTableView.getColumns().add(iconColumn);
        treeTableView.getColumns().add(nameColumn);
        treeTableView.getColumns().add(sizeColumn);
        treeTableView.getColumns().add(lastModifiedColumn);

    }
}
