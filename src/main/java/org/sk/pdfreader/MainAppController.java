package org.sk.pdfreader;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import org.sk.pdfreader.view.*;

import java.io.File;

public class MainAppController implements Controller {

    public MenuItem openMenuButton;
    public AnchorPane rightPane;
    public Pane bottomPane;
    public Label rightStatus;
    @FXML
    private FileTreeView leftPane;

    @FXML
    private TabPaneComponent centralTabPane;

    @FXML
    public void initialize(){
        leftPane.getEventProperty().addListener((o, t, t1) -> centralTabPane.openFile(t1.getData()));
        String property = System.getProperty("user.home");
        leftPane.rootProperty().setValue(new TreeItem<>(new File(property)));
        leftPane.setOpenHandler((f)->{
            centralTabPane.openFile(f);
        });
        centralTabPane.addDisplayInfoHandler((n)->{
            rightPane.getChildren().add(n);
            AnchorPane.setBottomAnchor(n,0.0);
            AnchorPane.setTopAnchor(n,0.0);
            AnchorPane.setRightAnchor(n,0.0);
            AnchorPane.setLeftAnchor(n,0.0);
        });
    }

    public void openFolderDialog() {

    }

    @Override
    public void setSignalMediator(ApplicationManager mediator) {

    }
}
