package xpertss.json.schema.core.tree;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.jsonpointer.JsonPointer;

import javax.annotation.concurrent.Immutable;

/**
 * A simple {@link JsonTree}
 */
@Immutable
public final class SimpleJsonTree
    extends BaseJsonTree
{
    public SimpleJsonTree(final JsonNode baseNode)
    {
        super(baseNode);
    }

    private SimpleJsonTree(final JsonNode baseNode, final JsonPointer pointer)
    {
        super(baseNode, pointer);
    }

    @Override
    public SimpleJsonTree append(final JsonPointer pointer)
    {
        return new SimpleJsonTree(baseNode, this.pointer.append(pointer));
    }

    @Override
    public JsonNode asJson()
    {
        return FACTORY.objectNode()
            .set("pointer", FACTORY.textNode(pointer.toString()));
    }

    @Override
    public String toString()
    {
        return "current pointer: \"" + pointer + '"';
    }
}
