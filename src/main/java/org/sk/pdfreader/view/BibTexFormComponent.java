package org.sk.pdfreader.view;

import com.dlsc.formsfx.model.structure.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.VBox;
import org.sk.pdfreader.model.Author;
import org.sk.pdfreader.model.BibTexTypes;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

public class BibTexFormComponent extends VBox {
    private Map<BibTexTypes, Group> formGroups=new TreeMap<>();
    private BibTexFormModel model=new BibTexFormModel();
    private Form form;
    public BibTexFormComponent(){
        StringField title = Field.ofStringType(model.bibKeyPropertyProperty())
                .label("Title").required(true);
        StringField bibKey=Field.ofStringType(model.bibKeyPropertyProperty())
                .label("bibKey").required(true);
        StringField volume=Field.ofStringType(model.volumePropertyProperty())
                .label("Volume");
        MultiSelectionField<Author> authors = Field.ofMultiSelectionType(model.getAuthorsObservableList())
                .label("Author(s)").required(true);
        DateField publicationDate = Field.ofDate(model.dateProperty()).label("Publication date")
                .required(true);
        StringField publisher = Field.ofStringType(model.publisherPropertyProperty().toString())
                .label("Publisher");
        StringField journal = Field.ofStringType(model.journalPropertyProperty().toString())
                .label("Journal");
        StringField issue = Field.ofStringType(model.journalIssuePropertyProperty())
                .label("Issue");
        StringField doi = Field.ofStringType(model.DOIPropertyProperty())
                .label("DOI");
        StringField bibtextCode = Field.ofStringType(model.asBibTexEntryPropertyProperty())
                .label("Bibtext code").multiline(true);
        SingleSelectionField<Stream<String>> type = Field.ofSingleSelectionType(Arrays.asList(Arrays.stream(BibTexTypes.values()).map(new Function<BibTexTypes, String>() {
            @Override
            public String apply(BibTexTypes bibTexTypes) {
                return bibTexTypes.getName();
            }
        })), 1).label("Type").required(true);


        //TODO: do the others


        Map<BibTexTypes, Group> inputsByGroup=new HashMap<>();
        Group articleGroup=Group.of(type,bibKey,authors,title,journal,publicationDate,volume,doi,issue,bibtextCode);
        Group bookGroup=Group.of(type,bibKey,authors,title,publisher,publicationDate, volume,doi,bibtextCode);
        Group inbookGroup=Group.of(type,bibKey,authors,title,publisher,publicationDate, volume,doi,bibtextCode);
        formGroups.put(BibTexTypes.ARTICLE,articleGroup);
        formGroups.put(BibTexTypes.BOOK,bookGroup);
        formGroups.put(BibTexTypes.INBOOK, inbookGroup);
        type.changedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                if(t1){
                    String s = type.getSelection().toList().get(0);
                    BibTexTypes keyType=BibTexTypes.getByName(s);
                    form=Form.of(formGroups.get(keyType));
                }
            }
        });
        form=Form.of(articleGroup);
//        this.getChildren().add(form);
    }
}
