package xpertss.json.schema.library;

import com.github.fge.Thawed;
import xpertss.json.schema.core.keyword.syntax.checkers.SyntaxChecker;
import xpertss.json.schema.core.util.Dictionary;
import xpertss.json.schema.core.util.DictionaryBuilder;
import xpertss.json.schema.format.FormatAttribute;
import xpertss.json.schema.keyword.digest.Digester;
import xpertss.json.schema.keyword.validator.KeywordValidatorFactory;
import xpertss.json.schema.messages.JsonSchemaConfigurationBundle;
import com.github.fge.msgsimple.bundle.MessageBundle;
import com.github.fge.msgsimple.load.MessageBundles;

/**
 * Mutable version of a library
 *
 * <p>This is what you will use when you wish to create your own metaschema and
 * add either new keywords or format attributes to it.</p>
 *
 * <p><b>Important note:</b> if you add a keyword which already existed in this
 * builder, all traces of its previous definition, if any, will be
 * <b>removed</b>.</p>
 */
public final class LibraryBuilder implements Thawed<Library> {

    private static final MessageBundle BUNDLE = MessageBundles.getBundle(JsonSchemaConfigurationBundle.class);
    /**
     * Dictionary builder of syntax checkers
     */
    final DictionaryBuilder<SyntaxChecker> syntaxCheckers;

    /**
     * Dictionary builder of digesters
     */
    final DictionaryBuilder<Digester> digesters;

    /**
     * Dictionary builder of keyword validator constructors
     */
    final DictionaryBuilder<KeywordValidatorFactory> validators;

    /**
     * Dictionary builder of format attributes
     */
    final DictionaryBuilder<FormatAttribute> formatAttributes;

    /**
     * No-arg constructor providing an empty library builder
     */
    LibraryBuilder()
    {
        syntaxCheckers = Dictionary.newBuilder();
        digesters = Dictionary.newBuilder();
        validators = Dictionary.newBuilder();
        formatAttributes = Dictionary.newBuilder();
    }

    /**
     * Constructor from an already existing library
     *
     * @param library the library
     * @see Library#thaw()
     */
    LibraryBuilder(Library library)
    {
        syntaxCheckers = library.syntaxCheckers.thaw();
        digesters = library.digesters.thaw();
        validators = library.validators.thaw();
        formatAttributes = library.formatAttributes.thaw();
    }

    /**
     * Add a new keyword to this library
     *
     * @param keyword the keyword
     * @return this
     * @throws NullPointerException keyword is null
     */
    public LibraryBuilder addKeyword(Keyword keyword)
    {
        BUNDLE.checkNotNull(keyword, "nullKeyword");
        final String name = keyword.name;
        removeKeyword(name);

        syntaxCheckers.addEntry(name, keyword.syntaxChecker);

        if (keyword.validatorFactory != null) {
            digesters.addEntry(name, keyword.digester);
            validators.addEntry(name, keyword.validatorFactory);
        }
        return this;
    }

    /**
     * Remove a keyword by its name
     *
     * @param name the name
     * @return this
     * @throws NullPointerException name is null
     */
    public LibraryBuilder removeKeyword(String name)
    {
        BUNDLE.checkNotNull(name, "nullName");
        syntaxCheckers.removeEntry(name);
        digesters.removeEntry(name);
        validators.removeEntry(name);
        return this;
    }

    /**
     * Add a format attribute
     *
     * @param name the name for this attribute
     * @param attribute the format attribute
     * @return this
     * @throws NullPointerException the name or attribute is null
     */
    public LibraryBuilder addFormatAttribute(String name, FormatAttribute attribute)
    {
        removeFormatAttribute(name);
        BUNDLE.checkNotNullPrintf(attribute, "nullAttribute", name);
        formatAttributes.addEntry(name, attribute);
        return this;
    }

    /**
     * Remove a format attribute by its name
     *
     * @param name the format attribute name
     * @return this
     * @throws NullPointerException name is null
     */
    public LibraryBuilder removeFormatAttribute(String name)
    {
        BUNDLE.checkNotNull(name, "nullFormat");
        formatAttributes.removeEntry(name);
        return this;
    }

    /**
     * Return a frozen version of this builder
     *
     * @return a new {@link Library}
     * @see Library#Library(LibraryBuilder)
     */
    @Override
    public Library freeze()
    {
        return new Library(this);
    }
}
