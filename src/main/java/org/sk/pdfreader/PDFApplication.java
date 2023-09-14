package org.sk.pdfreader;

import javafx.application.Application;

import javafx.scene.Scene;
import javafx.stage.Stage;

import org.sk.pdfreader.tuples.Pair;
import org.sk.pdfreader.view.Controller;
import org.sk.pdfreader.view.ViewControllerMapping;
import org.sk.pdfreader.view.ViewManager;


import java.io.IOException;


public class PDFApplication extends Application  {

    public PDFApplication(){

    }
    @Override
    public void start(Stage stage) throws IOException {
        Pair<Scene, Controller> pair = ViewManager.getInstance().loadWithStageController(ViewControllerMapping.MAIN);
        Scene scene = pair.getA();
        // I kinda know the main controller, so no point for more abstraction
        ApplicationManager instance = ApplicationManager.getInstance();
        instance.setStage(stage);
        pair.getB().setSignalMediator(instance);
        stage.setTitle("PDFX Reader");
        stage.setScene(scene);
        stage.show();
    }

}