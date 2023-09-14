package org.sk.pdfreader.view;

import org.sk.pdfreader.ApplicationManager;

import org.sk.pdfreader.tuples.Pair;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import org.sk.pdfreader.PDFApplication;

import java.io.IOException;
import java.util.*;

public class ViewManager {
    public static final ViewManager _instance=new ViewManager();
    public static final String MAIN="main.fxml";
    public static final String PDF_VIEWER="pdfPane.fxml";
    public static final String FILE_LIST_VIEWER="fileList.fxml";
    public static  List<String> _cache;

    private static ApplicationManager mediator;
    private ViewManager(){
        _cache=new ArrayList<>();
        mediator= ApplicationManager.getInstance();

        _cache.addAll(Arrays.asList(MAIN,PDF_VIEWER,FILE_LIST_VIEWER));
    }

    public static ViewManager getInstance(){
        return _instance;
    }


    public Pair<Scene, Controller> loadWithStageController(ViewControllerMapping mapping) throws IOException{
        FXMLLoader loader=new FXMLLoader(PDFApplication.class.getResource(mapping.getFxml()));
        Scene scene=new Scene(loader.load());
        Controller cast = mapping.getController().cast(loader.getController());
        cast.setSignalMediator(mediator);
        return Pair.of(scene,cast);
    }

}
