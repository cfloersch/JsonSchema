package xpertss.json.schema.core.tree;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.github.fge.jackson.JacksonUtils;
import com.github.fge.jackson.jsonpointer.JsonPointer;

/**
 * Base implementation of a {@link JsonTree}
 */
public abstract class BaseJsonTree
    implements JsonTree
{
    protected static final JsonNodeFactory FACTORY = JacksonUtils.nodeFactory();

    /**
     * The initial node
     */
    protected final JsonNode baseNode;

    /**
     * The current JSON Pointer into the node. Starts empty.
     */
    protected final JsonPointer pointer;

    /**
     * The current node.
     */
    private final JsonNode node;

    /**
     * Protected constructor
     *
     * <p>This is equivalent to calling {@link
     * BaseJsonTree#BaseJsonTree(JsonNode, JsonPointer)} with an empty pointer.
     * </p>
     *
     * @param baseNode the base node
     */
    protected BaseJsonTree(final JsonNode baseNode)
    {
        this(baseNode, JsonPointer.empty());
    }

    /**
     * Main constructor
     *
     * @param baseNode the base node
     * @param pointer the pointer into the base node
     */
    protected BaseJsonTree(final JsonNode baseNode, final JsonPointer pointer)
    {
        this.baseNode = baseNode;
        node = pointer.path(baseNode);
        this.pointer = pointer;
    }

    @Override
    public final JsonNode getBaseNode()
    {
        return baseNode;
    }

    @Override
    public final JsonPointer getPointer()
    {
        return pointer;
    }

    @Override
    public final JsonNode getNode()
    {
        return node;
    }

    @Override
    public abstract String toString();
}

