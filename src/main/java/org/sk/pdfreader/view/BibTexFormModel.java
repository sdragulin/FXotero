package org.sk.pdfreader.view;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.sk.pdfreader.model.*;

import java.time.LocalDate;
import java.util.ArrayList;

public class BibTexFormModel {
    public LocalDate getDate() {
        return date.get();
    }

    public ObjectProperty<LocalDate> dateProperty() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date.set(date);
    }

    public BibTexInfo getModelProperty() {

        return modelProperty.get();
    }

    public ObjectProperty<BibTexInfo> modelPropertyProperty() {
        return modelProperty;
    }

    public void setModelProperty(BibTexInfo modelProperty) {

        this.modelProperty.set(modelProperty);
    }

    public String getTitleProperty() {
        return titleProperty.get();
    }

    public StringProperty titlePropertyProperty() {
        return titleProperty;
    }

    public void setTitleProperty(String titleProperty) {
        this.titleProperty.set(titleProperty);
    }

    public ObservableList<Author> getAuthorsObservableList() {
        return authorsObservableList;
    }

    public void setAuthorsObservableList(ObservableList<Author> authorsObservableList) {
        this.authorsObservableList = authorsObservableList;
    }

    public Publisher getPublisherProperty() {
        return publisherProperty.get();
    }

    public ObjectProperty<Publisher> publisherPropertyProperty() {
        return publisherProperty;
    }

    public void setPublisherProperty(Publisher publisherProperty) {
        this.publisherProperty.set(publisherProperty);
    }

    public String getVolumeProperty() {
        return volumeProperty.get();
    }

    public StringProperty volumePropertyProperty() {
        return volumeProperty;
    }

    public void setVolumeProperty(String volumeProperty) {
        this.volumeProperty.set(volumeProperty);
    }

    public String getBibKeyProperty() {
        return bibKeyProperty.get();
    }

    public StringProperty bibKeyPropertyProperty() {
        return bibKeyProperty;
    }

    public void setBibKeyProperty(String bibKeyProperty) {
        this.bibKeyProperty.set(bibKeyProperty);
    }

    public Journal getJournalProperty() {
        return journalProperty.get();
    }

    public ObjectProperty<Journal> journalPropertyProperty() {
        return journalProperty;
    }

    public void setJournalProperty(Journal journalProperty) {
        this.journalProperty.set(journalProperty);
    }

    public String getJournalIssueProperty() {
        return journalIssueProperty.get();
    }

    public StringProperty journalIssuePropertyProperty() {
        return journalIssueProperty;
    }

    public void setJournalIssueProperty(String journalIssueProperty) {
        this.journalIssueProperty.set(journalIssueProperty);
    }

    public String getDOIProperty() {
        return DOIProperty.get();
    }

    public StringProperty DOIPropertyProperty() {
        return DOIProperty;
    }

    public void setDOIProperty(String DOIProperty) {
        this.DOIProperty.set(DOIProperty);
    }

    private ObjectProperty<BibTexInfo> modelProperty=new SimpleObjectProperty<>();

    private StringProperty titleProperty=new SimpleStringProperty();
    private ObservableList<Author> authorsObservableList = FXCollections.observableList(new ArrayList<>());
    private ObjectProperty<Publisher> publisherProperty=new SimpleObjectProperty<>();
    private StringProperty volumeProperty=new SimpleStringProperty();
    private StringProperty bibKeyProperty=new SimpleStringProperty();
    private ObjectProperty<Journal> journalProperty=new SimpleObjectProperty<>();
    private StringProperty journalIssueProperty=new SimpleStringProperty();
    private StringProperty DOIProperty=new SimpleStringProperty();

    private ObjectProperty<LocalDate> date=new SimpleObjectProperty<>();

    public BibTexTypes getBibTexType() {
        return bibTexType.get();
    }

    public ObjectProperty<BibTexTypes> bibTexTypeProperty() {
        return bibTexType;
    }

    public void setBibTexType(BibTexTypes bibTexType) {
        this.bibTexType.set(bibTexType);
    }

    private ObjectProperty<BibTexTypes> bibTexType=new SimpleObjectProperty<>();
    private StringProperty asBibTexEntryProperty=new SimpleStringProperty();

    public String getAsBibTexEntryProperty() {
        return asBibTexEntryProperty.get();
    }

    public StringProperty asBibTexEntryPropertyProperty() {
        return asBibTexEntryProperty;
    }

    public void setAsBibTexEntryProperty(String asBibTexEntryProperty) {
        this.asBibTexEntryProperty.set(asBibTexEntryProperty);
    }
}
