package xpertss.json.schema.core.load;

import com.fasterxml.jackson.databind.JsonNode;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.load.configuration.LoadingConfiguration;
import xpertss.json.schema.core.load.configuration.LoadingConfigurationBuilder;
import xpertss.json.schema.core.load.uri.URITranslator;
import xpertss.json.schema.core.load.uri.URITranslatorConfiguration;
import xpertss.json.schema.core.messages.JsonSchemaCoreMessageBundle;
import xpertss.json.schema.core.ref.JsonRef;
import xpertss.json.schema.core.report.ProcessingMessage;
import xpertss.json.schema.core.tree.SchemaTree;
import com.github.fge.msgsimple.bundle.MessageBundle;
import com.github.fge.msgsimple.load.MessageBundles;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableMap;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * JSON Schema loader
 *
 * <p>This class is the central loading point for generating a {@link
 * SchemaTree} out of a "raw" JSON Schema (ie, a {@link JsonNode}); it handles
 * the creation of both anonymous (ie, no loading URI) and non anonymous JSON
 * schemas.</p>
 *
 * <p>Depending on your configuration, you may, or may not, be able to use
 * relative URIs to load your schemas; see {@link URITranslator} and {@link
 * LoadingConfigurationBuilder#setURITranslatorConfiguration(URITranslatorConfiguration)}
 * for more details.</p>
 */
@ThreadSafe
public final class SchemaLoader {

    private static final MessageBundle BUNDLE = MessageBundles.getBundle(JsonSchemaCoreMessageBundle.class);

    /**
     * The URI manager
     */
    private final URIManager manager;

    /**
     * The URI translator
     */
    private final URITranslator translator;

    /**
     * Schema cache
     */
    private final LoadingCache<URI, JsonNode> cache;

    /**
     * Our dereferencing mode
     */
    private final Dereferencing dereferencing;

    /**
     * Map of preloaded schemas
     */
    private final Map<URI, JsonNode> preloadedSchemas;

    /**
     * Create a new schema loader with a given loading configuration
     *
     * @param cfg the configuration
     * @see LoadingConfiguration
     * @see LoadingConfigurationBuilder
     */
    public SchemaLoader(LoadingConfiguration cfg)
    {
        translator = new URITranslator(cfg.getTranslatorConfiguration());
        dereferencing = cfg.getDereferencing();
        manager = new URIManager(cfg);
        preloadedSchemas = ImmutableMap.copyOf(cfg.getPreloadedSchemas());

        CacheBuilder<Object, Object> builder = CacheBuilder.newBuilder();
        if (cfg.getCacheSize() != -1) {
        	builder.maximumSize(cfg.getCacheSize());
        }
        cache = builder.build(new CacheLoader<URI, JsonNode>()
            {
                @Nonnull
                @Override
                public JsonNode load(@Nonnull final URI key)
                    throws ProcessingException
                {
                    return manager.getContent(key);
                }
            });
    }

    /**
     * Create a new schema loader with the default loading configuration
     */
    public SchemaLoader()
    {
        this(LoadingConfiguration.byDefault());
    }

    /**
     * Create a new tree from a schema
     *
     * <p>Note that it will always create an "anonymous" tree, that is a tree
     * with an empty loading URI.</p>
     *
     * @param schema the schema
     * @return a new tree
     * @see Dereferencing#newTree(JsonNode)
     * @throws NullPointerException schema is null
     */
    public SchemaTree load(JsonNode schema)
    {
        BUNDLE.checkNotNull(schema, "loadingCfg.nullSchema");
        return dereferencing.newTree(schema);
    }

    /**
     * Get a schema tree from the given URI
     *
     * <p>Note that if the URI is relative, it will be resolved against this
     * registry's namespace, if any.</p>
     *
     * @param uri the URI
     * @return a schema tree
     * @throws ProcessingException URI is not an absolute JSON reference, or
     * failed to dereference this URI
     * @throws NullPointerException URI is null
     */
    public SchemaTree get(URI uri)
        throws ProcessingException
    {
        JsonRef ref = JsonRef.fromURI(translator.translate(uri));

        if (!ref.isAbsolute())
            throw new ProcessingException(new ProcessingMessage()
                .setMessage(BUNDLE.getMessage("refProcessing.uriNotAbsolute"))
                .putArgument("uri", ref));

        URI realURI = ref.toURI();

        try {
            JsonNode node = preloadedSchemas.get(realURI);
            if (node == null)
                node = cache.get(realURI);
            return dereferencing.newTree(ref, node);
        } catch (ExecutionException e) {
            throw (ProcessingException) e.getCause();
        }
    }

    @Override
    public String toString()
    {
        return cache.toString();
    }
}
