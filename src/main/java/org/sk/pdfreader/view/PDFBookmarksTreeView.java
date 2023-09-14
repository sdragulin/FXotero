package org.sk.pdfreader.view;

import javafx.fxml.FXML;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.util.Callback;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineNode;
import org.sk.pdfreader.dao.PageServer;


public class PDFBookmarksTreeView extends SearchableTreeViewComponent<PDOutlineItem>{
    private final PDDocumentOutline bookmarksTree;
    private final PageServer pageServer;


    public PDFBookmarksTreeView(PageServer server) {
        this.pageServer=server;
        this.bookmarksTree=server.getBookmarks();
        System.out.println(server.getBookmarks());
        System.out.println(bookmarksTree.toString());
        treeView.setCellFactory(new Callback<>() {
            @Override
            public TreeCell<PDOutlineItem> call(TreeView<PDOutlineItem> pdOutlineNodeTreeView) {
                return new TreeCell<>() {
                    @Override
                    protected void updateItem(PDOutlineItem pdOutlineNode, boolean b) {
                        super.updateItem(pdOutlineNode, b);
                        if (pdOutlineNode != null && !b) {
                            setText(pdOutlineNode.getTitle());
                            setGraphic(null);
                        } else {
                            setText(null);
                            setGraphic(null);
                        }
                    }
                };
            }
        });
        TreeItem<PDOutlineItem> rootItem=new TreeItem<>();

        treeView.setRoot(rootItem);
        treeView.setShowRoot(false);
        rootItem.setExpanded(true);
        Iterable<PDOutlineItem> children = bookmarksTree.children();
        for(PDOutlineItem c:children){
            TreeItem<PDOutlineItem> cItem=new TreeItem<>(c);
            buildTree(cItem);
            rootItem.getChildren().add(cItem);
        }
    }
    @FXML
    public  void initialize(){
        super.initialize();
    }
    private void buildTree(TreeItem<PDOutlineItem> root){
        PDOutlineNode value = root.getValue();
        if(value!=null&&value.hasChildren())
            value.children().forEach((c)->{
                TreeItem<PDOutlineItem> node=new TreeItem<>(c);
                root.getChildren().add(node);
                if(c.hasChildren())
                    buildTree(node);
            });
    }

    @Override
    protected void doubleClickSecondary() {

    }

    @Override
    protected void singleClickSecondary() {

    }

    @Override
    protected void doubleClickPrimary() {

    }

    @Override
    protected void singleClickPrimary() {
        TreeItem<PDOutlineItem> selectedItem = treeView.getSelectionModel().getSelectedItem();
        if(selectedItem.getValue()!=null)
            handler.handle(selectedItem.getValue());
    }

    @Override
    public void search() {

    }



}
