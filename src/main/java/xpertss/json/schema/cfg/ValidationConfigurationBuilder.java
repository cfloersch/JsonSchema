package xpertss.json.schema.cfg;

import com.github.fge.Thawed;
import xpertss.json.schema.SchemaVersion;
import xpertss.json.schema.core.exceptions.JsonReferenceException;
import xpertss.json.schema.core.messages.JsonSchemaSyntaxMessageBundle;
import xpertss.json.schema.core.ref.JsonRef;
import xpertss.json.schema.library.DraftV3Library;
import xpertss.json.schema.library.DraftV4HyperSchemaLibrary;
import xpertss.json.schema.library.DraftV4Library;
import xpertss.json.schema.library.Library;
import xpertss.json.schema.messages.JsonSchemaConfigurationBundle;
import xpertss.json.schema.messages.JsonSchemaValidationBundle;
import com.github.fge.msgsimple.bundle.MessageBundle;
import com.github.fge.msgsimple.load.MessageBundles;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Validation configuration (mutable instance)
 *
 * @see ValidationConfiguration
 */
public final class ValidationConfigurationBuilder implements Thawed<ValidationConfiguration> {

    private static final MessageBundle BUNDLE = MessageBundles.getBundle(JsonSchemaConfigurationBundle.class);

    /**
     * Default libraries to use
     *
     * <p>Those are the libraries for draft v3 core and draft v4 core.</p>
     *
     * @see SchemaVersion
     * @see DraftV3Library
     * @see DraftV4Library
     */
    private static final Map<SchemaVersion, Library> DEFAULT_LIBRARIES;

    static {
        DEFAULT_LIBRARIES = Maps.newEnumMap(SchemaVersion.class);
        DEFAULT_LIBRARIES.put(SchemaVersion.DRAFTV3, DraftV3Library.get());
        DEFAULT_LIBRARIES.put(SchemaVersion.DRAFTV4, DraftV4Library.get());
        DEFAULT_LIBRARIES.put(SchemaVersion.DRAFTV4_HYPERSCHEMA, DraftV4HyperSchemaLibrary.get());

        /* TODO
        DEFAULT_LIBRARIES.put(SchemaVersion.DRAFTV6, DraftV6Library.get());
        DEFAULT_LIBRARIES.put(SchemaVersion.DRAFTV6_HYPERSCHEMA, DraftV7HyperSchemaLibrary.get());
        DEFAULT_LIBRARIES.put(SchemaVersion.DRAFTV7, DraftV6Library.get());
        DEFAULT_LIBRARIES.put(SchemaVersion.DRAFTV7_HYPERSCHEMA, DraftV7HyperSchemaLibrary.get());
         */
    }

    /**
     * The set of libraries to use
     */
    final Map<JsonRef, Library> libraries;

    /**
     * The default library to use (draft v4 by default)
     */
    Library defaultLibrary = DEFAULT_LIBRARIES.get(SchemaVersion.DRAFTV4);

    /**
     * Whether to use {@code format} ({@code true} by default)
     */
    boolean useFormat = true;
    
    /**
     * Cache maximum size of 512 records by default
     */
    int cacheSize = 512;

    /**
     * The set of syntax messages
     */
    MessageBundle syntaxMessages;

    /**
     * The set of validation messages
     */
    MessageBundle validationMessages;

    ValidationConfigurationBuilder()
    {
        libraries = Maps.newHashMap();
        JsonRef ref;
        Library library;
        for (final Map.Entry<SchemaVersion, Library> entry:
            DEFAULT_LIBRARIES.entrySet()) {
            ref = JsonRef.fromURI(entry.getKey().getLocation());
            library = entry.getValue();
            libraries.put(ref, library);
        }
        syntaxMessages = MessageBundles.getBundle(JsonSchemaSyntaxMessageBundle.class);
        validationMessages = MessageBundles.getBundle(JsonSchemaValidationBundle.class);
    }

    /**
     * Constructor from a frozen instance
     *
     * @param cfg the frozen configuration
     * @see ValidationConfiguration#thaw()
     */
    ValidationConfigurationBuilder(ValidationConfiguration cfg)
    {
        libraries = Maps.newHashMap(cfg.libraries);
        defaultLibrary = cfg.defaultLibrary;
        useFormat = cfg.useFormat;
        cacheSize = cfg.cacheSize;
        syntaxMessages = cfg.syntaxMessages;
        validationMessages = cfg.validationMessages;
    }

    /**
     * Add a {@code $schema} and matching library to this configuration
     *
     * @param uri the value for {@code $schema}
     * @param library the library
     * @return this
     * @throws NullPointerException URI us null or library is null
     * @throws IllegalArgumentException string is not a URI, or not an absolute
     * JSON Reference; or a library already exists at this URI.
     */
    public ValidationConfigurationBuilder addLibrary(String uri, Library library)
    {
        final JsonRef ref;

        try {
            ref = JsonRef.fromString(uri);
        } catch (JsonReferenceException e) {
            throw new IllegalArgumentException(e.getMessage());
        }

        BUNDLE.checkArgumentPrintf(ref.isAbsolute(), "refProcessing.uriNotAbsolute", ref);
        BUNDLE.checkNotNull(library, "nullLibrary");
        BUNDLE.checkArgumentPrintf(libraries.put(ref, library) == null, "dupLibrary", ref);
        return this;
    }

    /**
     * Set the default schema version for this configuration
     *
     * <p>This will set the default library to use to the one registered for
     * this schema version.</p>
     *
     * @param version the version
     * @return this
     * @throws NullPointerException version is null
     */
    public ValidationConfigurationBuilder setDefaultVersion(SchemaVersion version)
    {
        BUNDLE.checkNotNull(version, "nullVersion");
        /*
         * They are always in, so this is safe
         */
        defaultLibrary = DEFAULT_LIBRARIES.get(version);
        return this;
    }

    /**
     * Add a library and sets it as the default
     *
     * @param uri the value for {@code $schema}
     * @param library the library
     * @return this
     * @see #addLibrary(String, Library)
     */
    public ValidationConfigurationBuilder setDefaultLibrary(String uri, Library library)
    {
        addLibrary(uri, library);
        defaultLibrary = library;
        return this;
    }

    /**
     * Tell whether the resulting configuration has support for {@code format}
     *
     * @param useFormat {@code true} if it must be used
     * @return this
     */
    public ValidationConfigurationBuilder setUseFormat(boolean useFormat)
    {
        this.useFormat = useFormat;
        return this;
    }

    public ValidationConfigurationBuilder setSyntaxMessages(MessageBundle syntaxMessages)
    {
        BUNDLE.checkNotNull(syntaxMessages, "nullMessageBundle");
        this.syntaxMessages = syntaxMessages;
        return this;
    }

    public ValidationConfigurationBuilder setValidationMessages(MessageBundle validationMessages)
    {
        BUNDLE.checkNotNull(validationMessages, "nullMessageBundle");
        this.validationMessages = validationMessages;
        return this;
    }

    public ValidationConfigurationBuilder setCacheSize(int cacheSize)
    {
        BUNDLE.checkArgument(cacheSize >= -1, "invalidCacheSize");
        this.cacheSize = cacheSize;
        return this;
    }

    /**
     * Return a frozen version of this configuration
     *
     * @return a {@link ValidationConfiguration}
     * @see ValidationConfiguration#ValidationConfiguration(ValidationConfigurationBuilder)
     */
    @Override
    public ValidationConfiguration freeze()
    {
        return new ValidationConfiguration(this);
    }
}
