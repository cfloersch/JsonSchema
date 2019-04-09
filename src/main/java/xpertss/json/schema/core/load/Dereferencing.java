package xpertss.json.schema.core.load;

import com.fasterxml.jackson.databind.JsonNode;
import xpertss.json.schema.core.ref.JsonRef;
import xpertss.json.schema.core.tree.CanonicalSchemaTree;
import xpertss.json.schema.core.tree.InlineSchemaTree;
import xpertss.json.schema.core.tree.SchemaTree;
import xpertss.json.schema.core.tree.key.SchemaKey;

/**
 * Dereferencing modes
 *
 * <p>Draft v4 defines two dereferencing modes: canonical and inline. This enum
 * defines those two modes, along with methods to generate appropriate schema
 * trees.</p>
 *
 * @see InlineSchemaTree
 * @see CanonicalSchemaTree
 */
public enum Dereferencing {

    /**
     * Canonical dereferencing
     *
     * @see CanonicalSchemaTree
     */
    CANONICAL("canonical")
    {
        @Override
        protected SchemaTree newTree(final SchemaKey key, final JsonNode node)
        {
            return new CanonicalSchemaTree(key, node);
        }
    },
    /**
     * Inline dereferencing
     *
     * @see InlineSchemaTree
     */
    INLINE("inline")
    {
        @Override
        protected SchemaTree newTree(final SchemaKey key, final JsonNode node)
        {
            return new InlineSchemaTree(key, node);
        }
    };

    private final String name;

    /**
     * Create a new schema tree with a given loading URI and JSON Schema
     *
     * @param ref the location
     * @param node the schema
     * @return a new tree
     */
    public SchemaTree newTree(final JsonRef ref, final JsonNode node)
    {
        return newTree(SchemaKey.forJsonRef(ref), node);
    }

    /**
     * Create a new schema tree with an empty loading URI
     *
     * @param node the schema
     * @return a new tree
     */
    public SchemaTree newTree(JsonNode node)
    {
        return newTree(SchemaKey.anonymousKey(), node);
    }

    protected abstract SchemaTree newTree(SchemaKey key, JsonNode node);

    Dereferencing(String name)
    {
        this.name = name;
    }

    @Override
    public String toString()
    {
        return name;
    }
}
