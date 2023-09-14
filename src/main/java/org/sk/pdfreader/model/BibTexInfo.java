package org.sk.pdfreader.model;

import java.util.List;

public class BibTexInfo {
    private Long id;
    private String title;
    private String inBook;
    private List<Author> authors;
    private Publisher publisher;
    private String volume;
    private String bibKey;
    private Journal journal;
    private String journalIssue;
    private String DOI;
    private BibTexTypes bibTexType;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getBibKey() {
        return bibKey;
    }

    public void setBibKey(String bibKey) {
        this.bibKey = bibKey;
    }

    public Journal getJournal() {
        return journal;
    }

    public void setJournal(Journal journal) {
        this.journal = journal;
    }

    public String getJournalIssue() {
        return journalIssue;
    }

    public void setJournalIssue(String journalIssue) {
        this.journalIssue = journalIssue;
    }

    public String getDOI() {
        return DOI;
    }

    public BibTexTypes getBibTexType() {
        return bibTexType;
    }

    public void setBibTexType(BibTexTypes bibTexType) {
        this.bibTexType = bibTexType;
    }

    public void setDOI(String DOI) {
        this.DOI = DOI;
    }

    public String getInBook() {
        return inBook;
    }

    public void setInBook(String inBook) {
        this.inBook = inBook;
    }
}
