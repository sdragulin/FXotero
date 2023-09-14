/**
 * NOT IN USE YET
 * TODO: add mappings for all other fields, as well as validation and distinguish between optional
 * and not optional ones.
 */

package org.sk.pdfreader.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BibTexFieldMapping {
    private static final Map<BibTexTypes, List<String>> requiredFieldsPerType=new HashMap<>();

    {
        requiredFieldsPerType
                .put(BibTexTypes.ARTICLE, Arrays.asList("author", "title", "journal", "year", "volume", "DOI", "issue"));
        requiredFieldsPerType
                .put(BibTexTypes.BOOK, Arrays.asList("author", "title", "publisher", "year", "volume", "DOI"));
        requiredFieldsPerType
                .put(BibTexTypes.INBOOK, Arrays.asList("author", "title", "publisher", "year", "volume", "DOI"));
    }
    public static List<String> getMappingsForType(BibTexTypes type){
        return requiredFieldsPerType.get(type);
    }
}
