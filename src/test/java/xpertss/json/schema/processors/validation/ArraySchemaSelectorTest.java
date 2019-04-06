package xpertss.json.schema.processors.validation;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jackson.jsonpointer.JsonPointer;
import com.github.fge.jackson.jsonpointer.JsonPointerException;
import xpertss.json.schema.core.exceptions.ProcessingException;
import com.google.common.collect.Lists;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import static org.testng.Assert.*;

public final class ArraySchemaSelectorTest
{
    private final JsonNode testNode;

    public ArraySchemaSelectorTest()
        throws IOException
    {
        testNode = JsonLoader.fromResource("/array/lookup.json");
    }

    @DataProvider
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

    @Test(dataProvider = "testData")
    public void schemaPointersAreCorrectlyComputed(final JsonNode digest,
        final int elementIndex, final List<JsonPointer> ret)
    {
        final ArraySchemaSelector selector = new ArraySchemaSelector(digest);
        final List<JsonPointer> actual
            = Lists.newArrayList(selector.selectSchemas(elementIndex));
        assertEquals(actual, ret, "schema lookup differs from expectations");
    }
}
