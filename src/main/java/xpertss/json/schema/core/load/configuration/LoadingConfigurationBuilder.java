package xpertss.json.schema.core.load.configuration;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.Thawed;
import xpertss.json.schema.SchemaVersion;
import xpertss.json.schema.core.exceptions.JsonReferenceException;
import xpertss.json.schema.core.load.Dereferencing;
import xpertss.json.schema.core.load.SchemaLoader;
import xpertss.json.schema.core.load.URIManager;
import xpertss.json.schema.core.load.uri.URITranslatorConfiguration;
import xpertss.json.schema.core.load.download.URIDownloader;
import xpertss.json.schema.core.messages.JsonSchemaCoreMessageBundle;
import xpertss.json.schema.core.ref.JsonRef;
import com.github.fge.msgsimple.bundle.MessageBundle;
import com.github.fge.msgsimple.load.MessageBundles;
import com.google.common.collect.Maps;

import java.net.URI;
import java.util.EnumSet;
import java.util.Map;

import static com.fasterxml.jackson.core.JsonParser.*;

/**
 * Loading configuration (mutable instance)
 *
 * @see LoadingConfiguration
 */
public final class LoadingConfigurationBuilder implements Thawed<LoadingConfiguration> {

    private static final MessageBundle BUNDLE = MessageBundles.getBundle(JsonSchemaCoreMessageBundle.class);

    /**
     * Default JsonParser feature set. Unfortunately, Jackson does not use
     * EnumSets to collect them, so we have to do that...
     */
    private static final EnumSet<JsonParser.Feature> DEFAULT_PARSER_FEATURES;
    
    private static final int DEFAULT_CACHE_SIZE = 512;

    static {
        DEFAULT_PARSER_FEATURES = EnumSet.noneOf(JsonParser.Feature.class);

        for (final JsonParser.Feature feature: JsonParser.Feature.values())
            if (feature.enabledByDefault())
                DEFAULT_PARSER_FEATURES.add(feature);
    }

    /**
     * Mutable map of URI downloaders
     *
     * @see URIDownloader
     * @see URIManager
     * @see URIDownloadersRegistry
     */
    final URIDownloadersRegistry downloaders = new URIDownloadersRegistry();

    URITranslatorConfiguration translatorCfg;

    /**
     * Cache size is 512 by default
     */
    int cacheSize = DEFAULT_CACHE_SIZE;

    /**
     * Dereferencing mode
     *
     * @see SchemaLoader
     */
    Dereferencing dereferencing;

    /**
     * List of preloaded schemas
     *
     * <p>The default list of preloaded schemas consists of the draft v3 and
     * draft v4 core schemas</p>
     *
     * @see SchemaVersion
     */
    final Map<URI, JsonNode> preloadedSchemas;

    /**
     * Set of JsonParser features to be enabled while loading schemas
     */
    final EnumSet<JsonParser.Feature> parserFeatures;

    /**
     * Return a new, default mutable loading configuration
     *
     * @see LoadingConfiguration#newBuilder()
     */
    LoadingConfigurationBuilder()
    {
        translatorCfg = URITranslatorConfiguration.byDefault();
        dereferencing = Dereferencing.CANONICAL;
        preloadedSchemas = Maps.newHashMap();
        for (final SchemaVersion version: SchemaVersion.values())
            preloadedSchemas.put(version.getLocation(), version.getSchema());
        parserFeatures = EnumSet.copyOf(DEFAULT_PARSER_FEATURES);
    }

    /**
     * Build a mutable loading configuration out of a frozen one
     *
     * @param cfg the frozen configuration
     * @see LoadingConfiguration#thaw()
     */
    LoadingConfigurationBuilder(LoadingConfiguration cfg)
    {
        downloaders.putAll(cfg.downloaders);
        translatorCfg = cfg.translatorCfg;
        dereferencing = cfg.dereferencing;
        preloadedSchemas = Maps.newHashMap(cfg.preloadedSchemas);
        parserFeatures = EnumSet.copyOf(cfg.parserFeatures);
        cacheSize = cfg.cacheSize;
    }
    
    /**
     * Should we enable caching of downloaded schemas
     * 
     * @deprecated Just for backward compatibility
     *     Use cacheSize setter instead to set the maximum size of the cache
     *
     * <p>Note that this does <b>not</b> affect preloaded schemas</p>
     * 
     * @param enableCache if loaded schemas have to be cached
     * @return this
     */
    @Deprecated
    public LoadingConfigurationBuilder setEnableCache(boolean enableCache)
    {
    	this.cacheSize = enableCache ? DEFAULT_CACHE_SIZE : 0;
        return this;
    }

    /**
     * How many schemas should be cached
     * <p>Note setting to zero effectively disables the cache</p>
     * <p>Note settting to -1 creates an unlimited cache</p>
     * <p>Note that this does <b>not</b> affect preloaded schemas</p>
     *
     * @param cacheSize if loaded schemas have to be cached
     * @return this
     */
    public LoadingConfigurationBuilder setCacheSize(int cacheSize)
    {
        this.cacheSize = cacheSize;
        return this;
    }
    
    /**
     * Add a new URI downloader
     *
     * @param scheme the scheme
     * @param downloader the downloader
     * @return this
     * @throws NullPointerException scheme or downloader is null
     * @throws IllegalArgumentException illegal scheme
     */
    public LoadingConfigurationBuilder addScheme(String scheme,
        final URIDownloader downloader)
    {
        downloaders.put(scheme, downloader);
        return this;
    }

    /**
     * Remove a downloader for a given scheme
     *
     * @param scheme the scheme
     * @return this
     */
    public LoadingConfigurationBuilder removeScheme(String scheme)
    {
        /*
         * No checks for null or anything there: adding entries will have been
         * filtered out anyway, so no harm.
         */
        downloaders.remove(scheme);
        return this;
    }

    public LoadingConfigurationBuilder setURITranslatorConfiguration(URITranslatorConfiguration translatorCfg)
    {
        this.translatorCfg = translatorCfg;
        return this;
    }

    /**
     * Set the dereferencing mode for this loading configuration
     *
     * <p>By default, it is {@link Dereferencing#CANONICAL}.</p>
     *
     * @param dereferencing the dereferencing mode
     * @return this
     * @throws NullPointerException dereferencing mode is null
     */
    public LoadingConfigurationBuilder dereferencing(Dereferencing dereferencing)
    {
        BUNDLE.checkNotNull(dereferencing, "loadingCfg.nullDereferencingMode");
        this.dereferencing = dereferencing;
        return this;
    }

    /**
     * Preload a schema at a given URI
     *
     * <p>Use this if the schema you wish to preload does not have an absolute
     * {@code id} at the top level.</p>
     *
     * <p>Note that the syntax of the schema is not checked at this stage.</p>
     *
     * @param uri the URI to use
     * @param schema the schema
     * @return this
     * @throws NullPointerException the URI or schema is null
     * @throws IllegalArgumentException a schema already exists at this URI
     * @see JsonRef
     */
    public LoadingConfigurationBuilder preloadSchema(String uri, JsonNode schema)
    {
        BUNDLE.checkNotNull(schema, "loadingCfg.nullSchema");
        final URI key = getLocator(uri);
        BUNDLE.checkArgumentPrintf(preloadedSchemas.put(key, schema) == null,
            "loadingCfg.duplicateURI", key);
        return this;
    }

    /**
     * Preload a schema
     *
     * <p>Use this if the schema already has an absolute {@code id}.</p>
     *
     * @param schema the schema
     * @return this
     * @throws NullPointerException schema is null
     * @throws IllegalArgumentException schema has no {@code id}, or its {@code
     * id} is not an absolute JSON Reference
     * @see JsonRef
     */
    public LoadingConfigurationBuilder preloadSchema(JsonNode schema)
    {
        final JsonNode node = schema.path("id");
        BUNDLE.checkArgument(node.isTextual(), "loadingCfg.noIDInSchema");
        return preloadSchema(node.textValue(), schema);
    }

    /**
     * Add a JsonParser feature
     *
     * <p>Use this option to enable non-standard JSON schema source including
     * comments, single quotes, unquoted field names, etc.</p>
     *
     * @param feature the JsonParser feature to enable
     * @throws NullPointerException feature is null
     * @return this
     * @see Feature
     */
    public LoadingConfigurationBuilder addParserFeature(JsonParser.Feature feature)
    {
        BUNDLE.checkNotNull(feature, "loadingCfg.nullJsonParserFeature");
        parserFeatures.add(feature);
        return this;
    }

    /**
     * Remove a JSON parser feature
     *
     * <p>Note that attempts to remove {@link Feature#AUTO_CLOSE_SOURCE} will
     * be ignored for safety reasons.</p>
     *
     * @param feature the feature to remove
     * @throws NullPointerException feature is null
     * @return this
     * @see #addParserFeature(JsonParser.Feature)
     */
    public LoadingConfigurationBuilder removeParserFeature(JsonParser.Feature feature)
    {
        BUNDLE.checkNotNull(feature, "loadingCfg.nullJsonParserFeature");
        if (feature != JsonParser.Feature.AUTO_CLOSE_SOURCE)
            parserFeatures.remove(feature);
        return this;
    }

    /**
     * Freeze this configuration
     *
     * @return a frozen copy of this builder
     */
    @Override
    public LoadingConfiguration freeze()
    {
        return new LoadingConfiguration(this);
    }

    private static URI getLocator(String input)
    {
        try {
            JsonRef ref = JsonRef.fromString(input);
            BUNDLE.checkArgumentPrintf(ref.isAbsolute(), "jsonRef.notAbsolute", ref);
            return ref.getLocator();
        } catch (JsonReferenceException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
