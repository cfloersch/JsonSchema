package xpertss.json.schema.processors.validation;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jackson.JsonNumEquals;
import xpertss.json.schema.keyword.digest.Digester;
import com.google.common.base.Equivalence;
import com.google.common.collect.Lists;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import static org.testng.Assert.*;

public final class ObjectSchemaDigesterTest
{
    private static final Equivalence<JsonNode> EQUIVALENCE
        = JsonNumEquals.getInstance();

    private final Digester digester = ObjectSchemaDigester.getInstance();
    private final JsonNode testNode;

    public ObjectSchemaDigesterTest()
        throws IOException
    {
        testNode = JsonLoader.fromResource("/object/digest.json");
    }

    @DataProvider
    public Iterator<Object[]> testData()
    {
        JsonNode digest;
        final List<Object[]> list = Lists.newArrayList();

        for (final JsonNode node: testNode) {
            digest = node.get("digest");
            for (final JsonNode input: node.get("inputs"))
                list.add(new Object[] { digest, input });
        }

        return list.iterator();
    }

    @Test(dataProvider = "testData")
    public void digestsAreCorrectlyComputed(final JsonNode digest,
        final JsonNode input)
    {
        assertTrue(EQUIVALENCE.equivalent(digester.digest(input), digest),
            "digested form is incorrect");
    }
}
