package org.sk.pdfreader.dao;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.sk.pdfreader.tuples.Pair;
import org.sk.pdfreader.view.PDFPageDTO;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class PageServer {
    private final PDDocument mainDoc;
    private final PDFRenderer renderer;
    private int currentPage;
    private int numPages;
    private float zoomLevel;
    private final TreeMap<Integer, Image> _cache=new TreeMap<>();
    private static final int BUFFER_SIZE=3; //3 pages before, 3 pages after
    public PageServer(float zoom, File file) throws IOException {
        //TODO: better exception
        if(file==null) throw new RuntimeException("File cannot be null");
        this.mainDoc= Loader.loadPDF(file);
        numPages=mainDoc.getNumberOfPages();
        renderer=new PDFRenderer(mainDoc);
        currentPage=0;
        this.zoomLevel=zoom;
        _cache.putAll(getPages(zoomLevel,currentPage+BUFFER_SIZE*2));
    }


    synchronized  public Image getPage(float zoomLevel,int index) throws IOException {
        if(_cache.containsKey(index)) {
            /*TreeMap<Float, Image> floatImageTreeMap = _cache.get(index);*/

                return SwingFXUtils.toFXImage(renderer.renderImage(index,
                        zoomLevel),null);
            }


        return SwingFXUtils.toFXImage(renderer.renderImage(index,
                zoomLevel),null);
    }


    /**
     * Returns the width and height(in this order) of the page, in
     * pts, not pixels, without any scale applied
     * @param index the page number
     * @return a pair of floats [width,height>]
     */
    public Pair<Float, Float> getPageDimensions(int index){
        PDPage page = mainDoc.getPage(index);
        float width = page.getMediaBox().getWidth()*zoomLevel;
        float height = page.getMediaBox().getHeight()*zoomLevel;
        return new Pair<Float,Float>(width,height);
    }
    public TreeMap<Integer,Image> getPages(float zoom,int... pages) throws IOException {
        TreeMap<Integer,Image> _cacheEntry=new TreeMap<>();
        for(int page:pages){
            _cacheEntry.put(page,getPage(zoom,page));
        }
        return _cacheEntry;
    }
    public void setZoomLevel(float zoom){
        this.zoomLevel=zoom;
        _cache.entrySet().forEach((k)->{
            /*TreeMap<Float, Image> floatImageTreeMap = _cache.get(k);*/

                try {
                    Image page = getPage(zoom, k.getKey());
                    k.setValue(page);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

        });
    }
    public PDFPageDTO getPageWithData(int index){
        Image page;
        try {
            page = getPage(zoomLevel,index);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new PDFPageDTO(index,zoomLevel,page,getPageDimensions(index));
    }
    

    public int getNumPages(){
        return numPages;
    }

    public PDDocumentOutline getBookmarks(){
        return mainDoc.getDocumentCatalog().getDocumentOutline();
    }

    public int getDestinationPage(PDOutlineItem data) {
        try {
            PDPage destinationPage = data.findDestinationPage(mainDoc);
            return mainDoc.getPages().indexOf(destinationPage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
