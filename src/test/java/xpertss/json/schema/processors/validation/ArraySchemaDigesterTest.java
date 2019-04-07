package xpertss.json.schema.processors.validation;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jackson.JsonNumEquals;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import xpertss.json.schema.keyword.digest.Digester;
import com.google.common.base.Equivalence;
import com.google.common.collect.Lists;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertTrue;


@RunWith(JUnitParamsRunner.class)
public final class ArraySchemaDigesterTest {

    private static final Equivalence<JsonNode> EQUIVALENCE = JsonNumEquals.getInstance();

    private final Digester digester = ArraySchemaDigester.getInstance();
    private final JsonNode testNode;

    public ArraySchemaDigesterTest()
        throws IOException
    {
        testNode = JsonLoader.fromResource("/array/digest.json");
    }

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

    @Test
    @Parameters(method = "testData")
    public void digestsAreCorrectlyComputed(JsonNode digest, JsonNode input)
    {
        assertTrue("digested form is incorrect", EQUIVALENCE.equivalent(digester.digest(input), digest));
    }
}
