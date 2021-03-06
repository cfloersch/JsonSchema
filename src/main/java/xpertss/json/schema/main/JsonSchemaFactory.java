package xpertss.json.schema.main;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.MissingNode;
import com.github.fge.Frozen;
import com.github.fge.jackson.jsonpointer.JsonPointer;
import com.github.fge.jackson.jsonpointer.JsonPointerException;
import xpertss.json.schema.cfg.ValidationConfiguration;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.load.RefResolver;
import xpertss.json.schema.core.load.SchemaLoader;
import xpertss.json.schema.core.load.configuration.LoadingConfiguration;
import xpertss.json.schema.core.messages.JsonSchemaCoreMessageBundle;
import xpertss.json.schema.core.processing.CachingProcessor;
import xpertss.json.schema.core.processing.Processor;
import xpertss.json.schema.core.processing.ProcessorMap;
import xpertss.json.schema.core.ref.JsonRef;
import xpertss.json.schema.core.report.ReportProvider;
import xpertss.json.schema.library.Library;
import xpertss.json.schema.messages.JsonSchemaConfigurationBundle;
import xpertss.json.schema.processors.data.FullData;
import xpertss.json.schema.processors.data.SchemaContext;
import xpertss.json.schema.processors.data.ValidatorList;
import xpertss.json.schema.processors.syntax.SyntaxValidator;
import xpertss.json.schema.processors.validation.SchemaContextEquivalence;
import xpertss.json.schema.processors.validation.ValidationChain;
import xpertss.json.schema.processors.validation.ValidationProcessor;
import com.github.fge.msgsimple.bundle.MessageBundle;
import com.github.fge.msgsimple.load.MessageBundles;
import com.google.common.base.Function;

import javax.annotation.concurrent.Immutable;
import java.util.Map;

/**
 * The main validator provider
 *
 * <p>From an instance of this factory, you can obtain the following:</p>
 *
 * <ul>
 *     <li>a {@link SyntaxValidator}, to validate schemas;</li>
 *     <li>a {@link JsonValidator}, to validate an instance against a schema;
 *     </li>
 *     <li>a {@link JsonSchemaImpl}, to validate instances against a fixed schema.
 *     </li>
 * </ul>
 *
 * @see JsonSchemaFactoryBuilder
 */
@Immutable
public final class JsonSchemaFactory implements Frozen<JsonSchemaFactoryBuilder> {

    private static final MessageBundle BUNDLE = MessageBundles.getBundle(JsonSchemaConfigurationBundle.class);
    private static final MessageBundle CORE_BUNDLE = MessageBundles.getBundle(JsonSchemaCoreMessageBundle.class);

    private static final Function<SchemaContext, JsonRef> FUNCTION = input -> input.getSchema().getDollarSchema();

    /*
     * Elements provided by the builder
     */
    final ReportProvider reportProvider;
    final LoadingConfiguration loadingCfg;
    final ValidationConfiguration validationCfg;

    /*
     * Generated elements
     */
    private final SchemaLoader loader;
    private final JsonValidator validator;
    private final SyntaxValidator syntaxValidator;

    /**
     * Return a default factory
     *
     * <p>This default factory has validators for both draft v4 and draft v3. It
     * defaults to draft v4.</p>
     *
     * @return a factory with default settings
     * @see JsonSchemaFactoryBuilder#JsonSchemaFactoryBuilder()
     */
    public static JsonSchemaFactory byDefault()
    {
        return newBuilder().freeze();
    }

    /**
     * Return a factory builder
     *
     * @return a {@link JsonSchemaFactoryBuilder}
     */
    public static JsonSchemaFactoryBuilder newBuilder()
    {
        return new JsonSchemaFactoryBuilder();
    }

    /**
     * Package private constructor to build a factory out of a builder
     *
     * @param builder the builder
     * @see JsonSchemaFactoryBuilder#freeze()
     */
    JsonSchemaFactory(JsonSchemaFactoryBuilder builder)
    {
        reportProvider = builder.reportProvider;
        loadingCfg = builder.loadingCfg;
        validationCfg = builder.validationCfg;

        loader = new SchemaLoader(loadingCfg);
        Processor<SchemaContext, ValidatorList> processor = buildProcessor();
        validator = new JsonValidator(loader, new ValidationProcessor(validationCfg, processor), reportProvider);
        syntaxValidator = new SyntaxValidator(validationCfg);
    }

    /**
     * Return the main schema/instance validator provided by this factory
     *
     * @return a {@link JsonValidator}
     */
    public JsonValidator getValidator()
    {
        return validator;
    }

    /**
     * Return the syntax validator provided by this factory
     *
     * @return a {@link SyntaxValidator}
     */
    public SyntaxValidator getSyntaxValidator()
    {
        return syntaxValidator;
    }

    /**
     * Build an instance validator tied to a schema
     *
     * <p>Note that the validity of the schema is <b>not</b> checked. Use {@link
     * #getSyntaxValidator()} if you are not sure.</p>
     *
     * @param schema the schema
     * @return a {@link JsonSchema}
     * @throws ProcessingException schema is a {@link MissingNode}
     * @throws NullPointerException schema is null
     */
    public JsonSchema getJsonSchema(JsonNode schema)
        throws ProcessingException
    {
        BUNDLE.checkNotNull(schema, "nullSchema");
        return validator.buildJsonSchema(schema, JsonPointer.empty());
    }

    /**
     * Build an instance validator tied to a subschema from a main schema
     *
     * <p>Note that the validity of the schema is <b>not</b> checked. Use {@link
     * #getSyntaxValidator()} if you are not sure.</p>
     *
     * @param schema the schema
     * @param ptr a JSON Pointer as a string
     * @return a {@link JsonSchema}
     * @throws ProcessingException {@code ptr} is not a valid JSON Pointer, or
     * resolving the pointer against the schema leads to a {@link MissingNode}
     * @throws NullPointerException schema is null, or pointer is null
     */
    public JsonSchema getJsonSchema(JsonNode schema, String ptr)
        throws ProcessingException
    {
        BUNDLE.checkNotNull(schema, "nullSchema");
        CORE_BUNDLE.checkNotNull(ptr, "nullPointer");
        try {
            JsonPointer pointer = new JsonPointer(ptr);
            return validator.buildJsonSchema(schema, pointer);
        } catch (JsonPointerException ignored) {
            // Cannot happen
        }
        throw new IllegalStateException("How did I get there??");
    }

    /**
     * Build an instance validator out of a schema loaded from a URI
     *
     * @param uri the URI
     * @return a {@link JsonSchema}
     * @throws ProcessingException failed to load from this URI
     * @throws NullPointerException URI is null
     */
    public JsonSchema getJsonSchema(String uri)
        throws ProcessingException
    {
        CORE_BUNDLE.checkNotNull(uri, "nullURI");
        return validator.buildJsonSchema(uri);
    }

    /**
     * Return the raw validation processor
     *
     * <p>This will allow you to chain the full validation processor with other
     * processors of your choice. Useful if, for instance, you wish to add post
     * checking which JSON Schema cannot do by itself.</p>
     *
     * @return the processor.
     */
    public Processor<FullData, FullData> getProcessor()
    {
        return validator.getProcessor();
    }

    /**
     * Return a thawed instance of that factory
     *
     * @return a {@link JsonSchemaFactoryBuilder}
     * @see JsonSchemaFactoryBuilder#JsonSchemaFactoryBuilder(JsonSchemaFactory)
     */
    @Override
    public JsonSchemaFactoryBuilder thaw()
    {
        return new JsonSchemaFactoryBuilder(this);
    }

    private Processor<SchemaContext, ValidatorList> buildProcessor()
    {
        RefResolver resolver = new RefResolver(loader);

        Map<JsonRef, Library> libraries = validationCfg.getLibraries();
        Library defaultLibrary = validationCfg.getDefaultLibrary();
        ValidationChain defaultChain = new ValidationChain(resolver, defaultLibrary, validationCfg);
        ProcessorMap<JsonRef, SchemaContext, ValidatorList> map = new ProcessorMap<JsonRef, SchemaContext, ValidatorList>(FUNCTION);
        map.setDefaultProcessor(defaultChain);

        JsonRef ref;
        ValidationChain chain;

        for (final Map.Entry<JsonRef, Library> entry: libraries.entrySet()) {
            ref = entry.getKey();
            chain = new ValidationChain(resolver, entry.getValue(),
                validationCfg);
            map.addEntry(ref, chain);
        }

        Processor<SchemaContext, ValidatorList> processor = map.getProcessor();
        return new CachingProcessor<>(processor, SchemaContextEquivalence.getInstance(), validationCfg.getCacheSize());
    }
}
