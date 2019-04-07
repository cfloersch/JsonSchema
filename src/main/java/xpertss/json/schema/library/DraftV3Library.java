package xpertss.json.schema.library;

import xpertss.json.schema.core.keyword.syntax.dictionaries.DraftV3SyntaxCheckerDictionary;
import xpertss.json.schema.library.digest.DraftV3DigesterDictionary;
import xpertss.json.schema.library.format.DraftV3FormatAttributesDictionary;
import xpertss.json.schema.library.validator.DraftV3ValidatorDictionary;

/**
 * Library of all draft v3 core schema keywords and format attributes
 */
public final class DraftV3Library {

    private static final Library LIBRARY = new Library(
        DraftV3SyntaxCheckerDictionary.get(),
        DraftV3DigesterDictionary.get(),
        DraftV3ValidatorDictionary.get(),
        DraftV3FormatAttributesDictionary.get()
    );

    private DraftV3Library()
    {
    }

    public static Library get()
    {
        return LIBRARY;
    }
}
