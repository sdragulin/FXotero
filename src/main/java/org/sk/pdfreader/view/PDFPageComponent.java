package org.sk.pdfreader.view;


import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import org.sk.pdfreader.tuples.Pair;




//It's a StackPane, so the highlight and  annotation layers can come on top
public class PDFPageComponent extends StackPane {
    private ImageView imageView;
    private int pageNumber;
    private Pair<Float,Float> pageDimensions;
    private float zoom;


    public PDFPageComponent(PDFPageDTO pageWithData) {
        this.pageDimensions=pageWithData.dimension();
        this.pageNumber=pageWithData.index();
        this.zoom=pageWithData.zoom();
        init();
        this.imageView.setImage(pageWithData.image());

    }

    private void init(){
        imageView=new ImageView();
        this.setStyle("-fx-background-color: black;" +
                "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.3), 10, 0.5, 0.0, 0.0);");

        this.setMaxWidth(pageDimensions.getA());
        this.setMaxHeight(pageDimensions.getB());
        zoom=1f;
        getChildren().add(imageView);

    }

    public PDFPageComponent setImage(Image img){
        imageView.setImage(img);
        return this;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public PDFPageComponent setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
        return this;
    }
    public PDFPageComponent setPageDimensions(Pair<Float,Float> pageDimensions){
        this.pageDimensions=pageDimensions;
        return this;
    }
    public void setData(PDFPageDTO page){
        this.imageView.setImage(page.image());
        this.pageNumber=page.index();
        pageDimensions=page.dimension();
    }
    public PDFPageDTO getData(){
        return new PDFPageDTO(pageNumber,zoom,imageView.getImage(),pageDimensions);
    }

}
