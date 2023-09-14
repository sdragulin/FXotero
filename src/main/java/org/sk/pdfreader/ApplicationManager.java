package org.sk.pdfreader;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;

public class ApplicationManager {
    private static final ApplicationManager _instance=new ApplicationManager();
    public static final float MAX_ZOOM_SUPPORTED=2f;
    public static final int PDF_IMAGE_BUFFER_SIZE=3;
    private ObservableList<File> filesObservableList = FXCollections.observableList(new ArrayList<>());
    private ApplicationManager(){
        filesObservableList.addListener(new ListChangeListener<File>() {
            @Override
            public void onChanged(Change<? extends File> change) {
                if(change.next()){
                    if(change.wasAdded()){

                    }if(change.wasRemoved()){}//TODO what should happen here?
                }
            }
        });
    }

    public static ApplicationManager getInstance(){
        return _instance;
    }
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage=stage;
    }
    public ChangeListener<? super File> getFilePropertyListener() {
        return new ChangeListener<File>() {
            @Override
            public void changed(ObservableValue<? extends File> observableValue, File file, File t1) {
                filesObservableList.add(t1);
            }
        };
    }
    public ObservableList<File> openedFilesProperty(){
        return filesObservableList;
    }
}
