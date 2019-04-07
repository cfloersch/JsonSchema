package xpertss.json.schema.library;

import xpertss.json.schema.core.keyword.syntax.dictionaries.DraftV4SyntaxCheckerDictionary;
import xpertss.json.schema.library.digest.DraftV4DigesterDictionary;
import xpertss.json.schema.library.format.DraftV4FormatAttributesDictionary;
import xpertss.json.schema.library.validator.DraftV4ValidatorDictionary;

/**
 * Library of all draft v4 core schema keywords and format attributes
 */
public final class DraftV4Library {
    
    private static final Library LIBRARY = new Library(
        DraftV4SyntaxCheckerDictionary.get(),
        DraftV4DigesterDictionary.get(),
        DraftV4ValidatorDictionary.get(),
        DraftV4FormatAttributesDictionary.get()
    );

    private DraftV4Library()
    {
    }

    public static Library get()
    {
        return LIBRARY;
    }
}
