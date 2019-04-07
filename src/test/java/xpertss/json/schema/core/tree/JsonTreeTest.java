package xpertss.json.schema.core.tree;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jackson.JacksonUtils;
import com.github.fge.jackson.jsonpointer.JsonPointer;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertSame;
import static org.junit.Assert.assertEquals;

public final class JsonTreeTest
{
    private final JsonNodeFactory factory = JacksonUtils.nodeFactory();
    private JsonNode testNode;
    private ObjectNode childObject;

    @Before
    public void init()
    {
        childObject = factory.objectNode();
        childObject.put("a", "b");

        final ObjectNode rootNode = factory.objectNode();
        rootNode.put("object", childObject);
        testNode = rootNode;
    }

    @Test
    public void initializedNodeTreeReturnsCorrectNodeAndPointer()
    {
        final JsonTree tree = new SimpleJsonTree(testNode);
        assertSame(tree.getNode(), testNode);
        assertEquals(JsonPointer.empty(), tree.getPointer());
    }

    @Test
    public void pushdOfJsonPointerWorks()
    {
        JsonTree tree = new SimpleJsonTree(testNode);
        final JsonPointer ptr = JsonPointer.of("object", "a");
        tree = tree.append(ptr);
        assertSame(tree.getNode(), childObject.get("a"));
        assertEquals(ptr, tree.getPointer());
    }
}
