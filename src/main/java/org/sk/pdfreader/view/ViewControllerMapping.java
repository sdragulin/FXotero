package org.sk.pdfreader.view;

import org.sk.pdfreader.MainAppController;
import org.sk.pdfreader.MainController;

public enum ViewControllerMapping {
    
    MAIN("mainWindow.fxml", MainAppController.class)
    ;

    private final String fxml;
    private final Class<? extends Controller> controller;

    ViewControllerMapping(String path,Class<? extends Controller> controllerClass){
        this.fxml=path;
        this.controller=controllerClass;
    }
    public String getFxml(){
        return fxml;
    }
    public Class<? extends Controller> getController(){
        return controller;
    }
}
