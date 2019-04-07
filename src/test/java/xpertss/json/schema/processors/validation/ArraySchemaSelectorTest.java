package xpertss.json.schema.processors.validation;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jackson.jsonpointer.JsonPointer;
import com.github.fge.jackson.jsonpointer.JsonPointerException;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import xpertss.json.schema.core.exceptions.ProcessingException;
import com.google.common.collect.Lists;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;


@RunWith(JUnitParamsRunner.class)
public final class ArraySchemaSelectorTest {

    private final JsonNode testNode;

    public ArraySchemaSelectorTest()
        throws IOException
    {
        testNode = JsonLoader.fromResource("/array/lookup.json");
    }

    public Iterator<Object[]> testData()
        throws ProcessingException, JsonPointerException
    {
        final List<Object[]> list = Lists.newArrayList();

        JsonNode digest;
        int elementIndex;
        List<JsonPointer> ret;
        for (final JsonNode node: testNode) {
            digest = node.get("digest");
            elementIndex = node.get("elementIndex").intValue();
            ret = Lists.newArrayList();
            for (final JsonNode element: node.get("ret"))
                ret.add(new JsonPointer(element.textValue()));
            list.add(new Object[]{ digest, elementIndex, ret });
        }

        return list.iterator();
    }

    @Test
    @Parameters(method = "testData")
    public void schemaPointersAreCorrectlyComputed(JsonNode digest, int elementIndex, List<JsonPointer> ret)
    {
        ArraySchemaSelector selector = new ArraySchemaSelector(digest);
        List<JsonPointer> actual = Lists.newArrayList(selector.selectSchemas(elementIndex));
        assertEquals("schema lookup differs from expectations", ret, actual);
    }
}
