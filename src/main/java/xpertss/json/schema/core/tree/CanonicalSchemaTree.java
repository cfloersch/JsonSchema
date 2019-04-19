package xpertss.json.schema.core.tree;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.jsonpointer.JsonPointer;
import xpertss.json.schema.core.ref.JsonRef;
import xpertss.json.schema.core.tree.key.SchemaKey;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.Immutable;

/**
 * A {@link SchemaTree} using canonical dereferencing
 *
 * <p>In canonical dereferencing mode, a JSON Reference resolves within a
 * schema if and only if the URI of the document and the base URI of the
 * JSON Reference match exactly.</p>
 *
 * <p>That is, {@code x://y/z#/foo/bar} resolves within the schema at URI
 * {@code x://y/z#}, but {@code x://y/t#} does not.</p>
 */
@Immutable
@ParametersAreNonnullByDefault
public final class CanonicalSchemaTree extends BaseSchemaTree {

    /**
     * Main constructor
     *
     * @param key the schema key
     * @param baseNode the base node
     */
    public CanonicalSchemaTree(SchemaKey key, JsonNode baseNode)
    {
        super(key, baseNode, JsonPointer.empty());
    }

    /**
     * Alternate constructor
     *
     * @param baseNode the base node
     * @deprecated use {@link #CanonicalSchemaTree(SchemaKey, JsonNode)} instead
     */
    @Deprecated
    public CanonicalSchemaTree(JsonNode baseNode)
    {
        this(SchemaKey.anonymousKey(), baseNode);
    }

    @Deprecated
    public CanonicalSchemaTree(JsonRef loadingRef, JsonNode baseNode)
    {
        this(SchemaKey.forJsonRef(loadingRef), baseNode);
    }

    private CanonicalSchemaTree(CanonicalSchemaTree other, JsonPointer newPointer)
    {
        super(other, newPointer);
    }

    @Override
    public SchemaTree append(JsonPointer pointer)
    {
        final JsonPointer newPointer = this.pointer.append(pointer);
        return new CanonicalSchemaTree(this, newPointer);
    }

    @Override
    public SchemaTree setPointer(JsonPointer pointer)
    {
        return new CanonicalSchemaTree(this, pointer);
    }

    @Override
    public boolean containsRef(JsonRef ref)
    {
        return key.getLoadingRef().contains(ref);
    }

    @Nullable
    @Override
    public JsonPointer matchingPointer(JsonRef ref)
    {
        if (!ref.isLegal())
            return null;
        final JsonPointer ptr = ref.getPointer();
        return ptr.path(baseNode).isMissingNode() ? null : ptr;
    }
}
