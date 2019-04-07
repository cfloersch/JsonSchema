package xpertss.json.schema.keyword.digest;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jackson.JsonNumEquals;
import com.github.fge.jackson.NodeType;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import xpertss.json.schema.core.util.Dictionary;
import com.google.common.base.Equivalence;
import com.google.common.collect.Lists;

import java.io.IOException;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


@RunWith(JUnitParamsRunner.class)
public abstract class AbstractDigesterTest {

    private static final Equivalence<JsonNode> EQUIVALENCE = JsonNumEquals.getInstance();

    private final String keyword;
    private final Digester digester;
    private final EnumSet<NodeType> types;
    private final JsonNode data;

    protected AbstractDigesterTest(Dictionary<Digester> dict, String prefix, String keyword, NodeType first, NodeType... other)
        throws IOException
    {
        digester = dict.entries().get(keyword);
        types = EnumSet.of(first, other);
        this.keyword = keyword;
        String resourceName = String.format("/keyword/digest/%s/%s.json", prefix, keyword);
        data = JsonLoader.fromResource(resourceName);
    }

    @Before
    public final void keywordExists()
    {
        assertNotNull(keyword + " is not supported??", digester);
        assertEquals("keyword does not declare to support the appropriate type set", digester.supportedTypes(), types);
    }

    public final Iterator<Object[]> getTestData()
    {
        final List<Object[]> list = Lists.newArrayList();

        JsonNode digest;
        for (final JsonNode element: data) {
            digest = element.get("digest");
            for (final JsonNode node: element.get("inputs"))
                list.add(new Object[] { digest, node });
        }

        return list.iterator();
    }

    @Test
    @Parameters(method = "getTestData")
    public final void digestsAreCorrectlyComputed(JsonNode digest, JsonNode source)
    {
        assertTrue("incorrect digest form", EQUIVALENCE.equivalent(digester.digest(source), digest));
    }
}
