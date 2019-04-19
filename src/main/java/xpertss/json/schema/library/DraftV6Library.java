/*
 * Copyright 2019 XpertSoftware
 *
 * Created By: cfloersch
 * Date: 4/19/2019
 */
package xpertss.json.schema.library;

import xpertss.json.schema.core.keyword.syntax.dictionaries.DraftV4SyntaxCheckerDictionary;
import xpertss.json.schema.library.digest.DraftV4DigesterDictionary;
import xpertss.json.schema.library.format.DraftV6FormatAttributesDictionary;
import xpertss.json.schema.library.validator.DraftV4ValidatorDictionary;

public final class DraftV6Library {

   private static final Library LIBRARY = new Library(
      DraftV4SyntaxCheckerDictionary.get(),
      DraftV4DigesterDictionary.get(),
      DraftV4ValidatorDictionary.get(),
      DraftV6FormatAttributesDictionary.get()
   );

   private DraftV6Library()
   {
   }

   public static Library get()
   {
      return LIBRARY;
   }


}
