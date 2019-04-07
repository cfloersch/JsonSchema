package xpertss.json.schema.processors.data;


import xpertss.json.schema.core.report.MessageProvider;
import xpertss.json.schema.core.report.ProcessingMessage;
import xpertss.json.schema.core.tree.JsonTree;
import xpertss.json.schema.core.tree.SchemaTree;

import javax.annotation.concurrent.Immutable;

/**
 * Validation data for a validation processor
 *
 * <p>The included data are the schema (in the shape of a {@link SchemaTree},
 * the instance to validate (in the shape of a {@link JsonTree} and a boolean
 * indicating whether validation should go as deep as posssible.</p>
 *
 * <p>If the boolean argument is false, then container children (array elements
 * or object members) will not be validated if the container itself fails
 * validation.</p>
 *
 * <p>The {@link ProcessingMessage} template generated contains information
 * about both the schema and instance.</p>
 */
@Immutable
public final class FullData implements MessageProvider {

    private final SchemaTree schema;
    private final JsonTree instance;
    private final boolean deepCheck;

    public FullData(SchemaTree schema, JsonTree instance, boolean deepCheck)
    {
        this.schema = schema;
        this.instance = instance;
        this.deepCheck = deepCheck;
    }

    public FullData(SchemaTree schema, JsonTree instance)
    {
        this(schema, instance, false);
    }

    public SchemaTree getSchema()
    {
        return schema;
    }

    public JsonTree getInstance()
    {
        return instance;
    }

    public boolean isDeepCheck()
    {
        return deepCheck;
    }

    /**
     * Return a new full data with another schema
     *
     * @param schema the schema
     * @return a new full data instance
     */
    public FullData withSchema(final SchemaTree schema)
    {
        return new FullData(schema, instance, deepCheck);
    }

    /**
     * Return a new full data with another instance
     *
     * @param instance the new instance
     * @return a new full data instance
     */
    public FullData withInstance(final JsonTree instance)
    {
        return new FullData(schema, instance, deepCheck);
    }

    @Override
    public ProcessingMessage newMessage()
    {
        final ProcessingMessage ret = new ProcessingMessage();
        if (schema != null)
            ret.put("schema", schema);
        if (instance != null)
            ret.put("instance", instance);
        return ret;
    }
}
