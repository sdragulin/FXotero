package org.sk.pdfreader.model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum BibTexTypes {
    ARTICLE("article"),
    BOOK("book"),
    BOOKLET("booklet"),
    CONFERENCE("conference"),
    INBOOK("inbook"),
    INCOLLECTION("incollection"),
    INPROCEEDINGS("inproceedings"),
    MANUAL("manual"),
    MASTERSTHESIS("mastersthesis"),
    MISC("misc"),
    PHDTHESIS("phdthesis"),
    PROCEEDINGS("proceedings"),
    TECHREPORT("techreport"),
    UNPUBLISHED("unpublished");

    String name;
    BibTexTypes(String name){
        this.name=name;
    }
    public String getName(){
        return name;
    }

    public static BibTexTypes getByName(String name){
        List<BibTexTypes> collect = Arrays.stream(values()).filter(n -> n.getName().equals(name)).collect(Collectors.toList());
        if(!collect.isEmpty()) return collect.get(0);
        return MISC;
    }


}
