package xpertss.json.schema.library;

import com.github.fge.Frozen;
import xpertss.json.schema.SchemaVersion;
import xpertss.json.schema.cfg.ValidationConfigurationBuilder;
import xpertss.json.schema.core.keyword.syntax.checkers.SyntaxChecker;
import xpertss.json.schema.core.util.Dictionary;
import xpertss.json.schema.format.FormatAttribute;
import xpertss.json.schema.keyword.digest.Digester;
import xpertss.json.schema.keyword.validator.KeywordValidatorFactory;

/**
 * A schema keyword library
 *
 * <p>A library contains all keywords defined for a schema, but also all format
 * attributes.</p>
 *
 * @see ValidationConfigurationBuilder#addLibrary(String, Library)
 * @see ValidationConfigurationBuilder#setDefaultLibrary(String, Library)
 * @see ValidationConfigurationBuilder#setDefaultVersion(SchemaVersion)
 */
public final class Library implements Frozen<LibraryBuilder> {

    /**
     * Dictionary of syntax checkers
     */
    final Dictionary<SyntaxChecker> syntaxCheckers;

    /**
     * Dictionary of digesters
     */
    final Dictionary<Digester> digesters;

    /**
     * Dictionary of keyword validator factories
     */
    final Dictionary<KeywordValidatorFactory> validators;

    /**
     * Dictionary of format attributes
     */
    final Dictionary<FormatAttribute> formatAttributes;

    /**
     * Create a new, empty library builder
     *
     * @return a {@link LibraryBuilder}
     */
    public static LibraryBuilder newBuilder()
    {
        return new LibraryBuilder();
    }

    /**
     * Constructor from a library builder
     *
     * @param builder the builder
     * @see LibraryBuilder#freeze()
     */
    Library(LibraryBuilder builder)
    {
        syntaxCheckers = builder.syntaxCheckers.freeze();
        digesters = builder.digesters.freeze();
        validators = builder.validators.freeze();
        formatAttributes = builder.formatAttributes.freeze();
    }

    /**
     * Internal constructor, do not use!
     *
     * @param syntaxCheckers map of syntax checkers
     * @param digesters map of digesters
     * @param validators map of keyword validator constructors
     * @param formatAttributes map of format attributes
     */
    Library(Dictionary<SyntaxChecker> syntaxCheckers,
            Dictionary<Digester> digesters,
            Dictionary<KeywordValidatorFactory> validators,
            Dictionary<FormatAttribute> formatAttributes)
    {
        this.syntaxCheckers = syntaxCheckers;
        this.digesters = digesters;
        this.validators = validators;
        this.formatAttributes = formatAttributes;
    }

    /**
     * Get the dictionary of syntax checkers
     *
     * @return a dictionary
     */
    public Dictionary<SyntaxChecker> getSyntaxCheckers()
    {
        return syntaxCheckers;
    }

    /**
     * Get the dictionary of digesters
     *
     * @return a dictionary
     */
    public Dictionary<Digester> getDigesters()
    {
        return digesters;
    }

    /**
     * Get the dictionary of keyword validator constructors
     *
     * @return a dictionary
     */
    public Dictionary<KeywordValidatorFactory> getValidators()
    {
        return validators;
    }

    /**
     * Get the dictionary of format attributes
     *
     * @return a dictionary
     */
    public Dictionary<FormatAttribute> getFormatAttributes()
    {
        return formatAttributes;
    }

    /**
     * Create a mutable version of this library
     *
     * @return a {@link LibraryBuilder}
     * @see LibraryBuilder#LibraryBuilder(Library)
     */
    @Override
    public LibraryBuilder thaw()
    {
        return new LibraryBuilder(this);
    }
}
