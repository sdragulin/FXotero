package org.sk.pdfreader.model;

import java.util.List;

public class LibraryItem {
    private Long id;
    private String title;
    private List<LibraryItem> items;
    private BibTexInfo bibTexInfo;
    private List<Tag> tags;

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

    public List<LibraryItem> getItems() {
        return items;
    }

    public void setItems(List<LibraryItem> items) {
        this.items = items;
    }

    public BibTexInfo getBibTexInfo() {
        return bibTexInfo;
    }

    public void setBibTexInfo(BibTexInfo bibTexInfo) {
        this.bibTexInfo = bibTexInfo;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }
}
