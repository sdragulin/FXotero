package org.sk.pdfreader.view;

import javafx.application.Platform;

import javafx.beans.property.*;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;

import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Callback;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;
import org.sk.pdfreader.ApplicationManager;
import org.sk.pdfreader.PDFApplication;
import org.sk.pdfreader.dao.PageServer;
import org.sk.pdfreader.tuples.Pair;

import java.io.File;
import java.io.IOException;
import java.util.*;

import java.util.concurrent.Executors;
import java.util.stream.IntStream;


public class PDFPaneComponent extends VBox implements Controller{
    /**
     * Visual fields
     */
    public ComboBox<Integer> pageNumberCombobox;
    public ComboBox<String> zoomLevelCombobox;
    @FXML
    private  VBox container;
    @FXML
    private  ScrollPane scrollPane;
    private PDFBookmarksTreeView pdfInfoTreeView;

    private final ObjectProperty<ApplicationManager> mediatorProperty=new SimpleObjectProperty<>();

    /**
     * Model fields.
     */
//    private  ApplicationManager manager;

    private volatile SortedMap<Integer, PDFPageComponent> pageDeque;
    private int bufferSize =ApplicationManager.PDF_IMAGE_BUFFER_SIZE;
    private final int numPages;
    private final ObjectProperty<Integer> currentPage;
    private final ObjectProperty<Float> zoomLevelProperty;
    private final PageServer pageServer;
    //TODO move this in a separate class


    public PDFPaneComponent(File file) {
        if(file==null||!file.getName().endsWith(".pdf"))
            throw new UnsupportedOperationException("""
                    Cannot open pdf, since pdf file is null
                    Please check file exists before instantiating PDFPaneComponent
                    """);
        try {
            pageServer=new PageServer(1f, file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        pageDeque=Collections.synchronizedSortedMap(new TreeMap<>());
        currentPage=new SimpleObjectProperty<>(0);
        this.numPages=pageServer.getNumPages();
        zoomLevelProperty=new SimpleObjectProperty<>(1f);

        /*
         * The model must be initialized before the view
         */
        FXMLLoader loader=new FXMLLoader(PDFApplication.class.getResource("pdfPane.fxml"));
        loader.setController(this);
        loader.setRoot(this);
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private float computeScale(float scale,float delta){
        return scale+delta/100;
    }
    @FXML
    private void initialize(){

        scrollPane.setFitToWidth(true);
        container.alignmentProperty().set(Pos.CENTER);
        container.setSpacing(20);//TODO WIll have to extract this.

        pageNumberCombobox.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Integer> call(ListView<Integer> integerListView) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Integer integer, boolean b) {
                        super.updateItem(integer, b);
                        if (!b)
                            setText(integer.toString());
                        else
                            setText(null);
                    }
                };
            }
        });
        pageNumberCombobox.setValue(0);
        pageNumberCombobox.setEditable(true);
        currentPage.bindBidirectional(pageNumberCombobox.valueProperty());

        currentPage.addListener((observableValue, number, t1) -> {
            System.out.println("CHANGING TO PAGE "+t1);
            turnPage(number,t1);
        });

        zoomLevelCombobox.getItems().addAll("50","75","90","100","125","150","175","200");
        zoomLevelCombobox.setEditable(true);
        //TODO add validation on combobox editor;

        zoomLevelCombobox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(t1==null||t1.isEmpty()) return;
                float newValue = Float.parseFloat(t1);
                Float v = zoomLevelProperty.get();
                zoomLevelProperty.setValue(v+newValue/100);
            }
        });


        zoomLevelProperty.addListener((observableValue, number, t1) -> {
            System.out.println("Scale value" +t1);
            if(t1 >2){
                zoomLevelProperty.set(3f);
                return;
            }
            if(t1 <-2){
                zoomLevelProperty.set(-3f);
                return;
            }
            pageServer.setZoomLevel(t1);
            zoomLevelCombobox.valueProperty().set(t1*100+"");
            pageDeque.forEach((key, value) -> {
                try {
                    value.setData(new PDFPageDTO(
                            key, t1, pageServer.getPage(t1, key),
                            pageServer.getPageDimensions(key)
                    ));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
        });

        scrollPane.setOnScroll(scrollEvent -> {
            if(!scrollEvent.isControlDown()){
                double newValue = scrollPane.getVvalue();
                double pageHeight = container.getBoundsInLocal().getHeight() / pageServer.getNumPages();
                //is the current page in bounds?
                double x = newValue * container.getBoundsInLocal().getHeight();
                int index = Math.toIntExact(Math.abs(Math.round(x / pageHeight)));
                pageNumberCombobox.getSelectionModel().select(index);
                if (index != currentPage.get())
                    currentPage.setValue(index);
            }
        });

        try {
            openPdf();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        pdfInfoTreeView = new PDFBookmarksTreeView(pageServer);
        pdfInfoTreeView.setOpenHandler(new OpenHandler<PDOutlineItem>() {
            @Override
            public void handle(PDOutlineItem data) {
                int destinationPage = pageServer.getDestinationPage(data);
                double containerHeight = container.getBoundsInLocal().getHeight();

                currentPage.setValue(destinationPage);
                double newValue = scrollPane.getVvalue();
                double pageHeight = containerHeight / numPages;
                //is the current page in bounds?
                scrollPane.vvalueProperty().set(0.0);
                double v = pageHeight * destinationPage / containerHeight;
                scrollPane.setVvalue(v);
            }
        });
    }
    private Pane getDummy(Pair<Float,Float> dimensions){
        StackPane sp=new StackPane();
        sp.getChildren().add(new Rectangle(dimensions.getA()*zoomLevelProperty.get(),
                dimensions.getB()*zoomLevelProperty.get(),Color.WHITE));
        sp.setStyle("-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.3), 10, 0.5, 0.0, 0.0);");

        return sp;
    }
    private void pdfPaneDecorator(PDFPageComponent pdfPage){
        pdfPage.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent scrollEvent) {
                if(scrollEvent.isControlDown()){
                    double deltaY = scrollEvent.getDeltaY();
                    float scale = (float) ((float) zoomLevelProperty.get() + deltaY / 100);
                    zoomLevelProperty.setValue(scale);
                    scrollEvent.consume();
                }
            }
        });
    }
    private void turnPage(int prev, int nextPage) {

        Platform.runLater(()->{
            System.out.println("Turning page");
            int next=nextPage;
            next=nextPage+ bufferSize >(numPages-1)?numPages- bufferSize -1:next;
            next=nextPage- bufferSize <0? bufferSize :next;

            int dequeFirst=pageDeque.firstKey();
            int dequeLast=pageDeque.lastKey();

            //this should happen only at the limits (0,numPage)
            if(next- bufferSize ==dequeFirst&&dequeLast==next+ bufferSize) return;

            ArrayList<Integer> list = new ArrayList<>(pageDeque.keySet().stream().toList());
            List<Integer> newIndices = IntStream.range(next - bufferSize, next + bufferSize).boxed().toList();
            TreeMap<Integer,PDFPageComponent> toBeShuffled=new TreeMap<>();
            list.forEach((e)->{
                if(!newIndices.contains(e)){
                    PDFPageComponent remove = pageDeque.remove(e);
                    pdfPaneDecorator(remove);
                    toBeShuffled.put(e,remove);
                }
            });
            newIndices.forEach((e)->{
                if(!list.contains(e)) {
                    Map.Entry<Integer, PDFPageComponent> entry = toBeShuffled.pollLastEntry();
                    PDFPageComponent pdfPage = entry.getValue();
                    container.getChildren().remove(pdfPage.getPageNumber());
                    container.getChildren().add(pdfPage.getPageNumber(),
                            getDummy(pdfPage.getData().dimension()));
                    pdfPage.setData(pageServer.getPageWithData(e));
                    pageDeque.put(e,pdfPage);
                    container.getChildren().remove(e.intValue());
                    container.getChildren().add(e,pdfPage);
                }
            });

        });

    }

    @FXML
    public void nextPage(ActionEvent actionEvent) {

    }
    @FXML
    public void previousPage(ActionEvent actionEvent) {

    }
    @Override
    public void setSignalMediator(ApplicationManager mediator) {
        mediatorProperty.setValue(mediator);
    }


    private void openPdf() throws IOException {

        Bounds viewportBounds = scrollPane.getViewportBounds();
        double height= viewportBounds.getHeight();//this should be multiplied by zoom factor
        Pair<Float, Float> pageDimensions = pageServer.getPageDimensions(0);
        double ratio = height / pageDimensions.getB();
            /*
                if the page is bigger than the viewport we only push 5 pages
                or even less into the deque
             */
        int numPages = pageServer.getNumPages();
        if(ratio<1){
            bufferSize = Math.min(bufferSize, numPages);
        }else{
            bufferSize =Math.min(Math.toIntExact(Math.round(bufferSize *ratio)),numPages);
        }
        for (int i = 0; i<(2* bufferSize)+1; i++){
            PDFPageComponent pdfPage=new PDFPageComponent(pageServer.getPageWithData(i));
            pdfPaneDecorator(pdfPage);
            container.getChildren().add(pdfPage);
            pageDeque.put(i,pdfPage);
        }
        //we fill the rest with dummy placeholders
        for (int i = 2* bufferSize +1; i < numPages; i++) {
            Pair<Float, Float> pd = pageServer.getPageDimensions(i);
            container.getChildren().add(getDummy(pd));
        }
        Platform.runLater(() -> {
            for(int i=0; i<numPages;i++) {
                pageNumberCombobox.getItems().add(i+1);
            }
        });

    }
    public Node getInfoPane(){
        return pdfInfoTreeView;
    }
}
