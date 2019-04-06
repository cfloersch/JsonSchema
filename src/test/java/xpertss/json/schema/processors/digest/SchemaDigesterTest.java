package xpertss.json.schema.processors.digest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jackson.JacksonUtils;
import com.github.fge.jackson.NodeType;
import xpertss.json.schema.SampleNodeProvider;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.core.tree.CanonicalSchemaTree;
import xpertss.json.schema.core.tree.SchemaTree;
import xpertss.json.schema.core.tree.key.SchemaKey;
import xpertss.json.schema.core.util.Dictionary;
import xpertss.json.schema.core.util.DictionaryBuilder;
import xpertss.json.schema.keyword.digest.Digester;
import xpertss.json.schema.processors.data.SchemaContext;
import xpertss.json.schema.processors.data.SchemaDigest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

public final class SchemaDigesterTest
{
    private static final JsonNodeFactory FACTORY = JacksonUtils.nodeFactory();
    private static final String K1 = "k1";
    private static final String K2 = "k2";
    private static final EnumSet<NodeType> TYPES1
        = EnumSet.of(NodeType.ARRAY, NodeType.STRING);
    private static final EnumSet<NodeType> TYPES2
        = EnumSet.of(NodeType.BOOLEAN, NodeType.NUMBER, NodeType.OBJECT);

    private final ObjectNode digest1 = FACTORY.objectNode();
    private final ObjectNode digest2 = FACTORY.objectNode();

    private final ObjectNode schema;

    private Digester digester1;
    private Digester digester2;
    private SchemaDigester schemaDigester;

    public SchemaDigesterTest()
    {
        schema = FACTORY.objectNode();
        schema.put(K1, K1);
        schema.put(K2, K2);
    }

    @BeforeMethod
    public void setupDigesters()
    {
        final DictionaryBuilder<Digester> builder = Dictionary.newBuilder();

        digester1 = mock(Digester.class);
        when(digester1.digest(any(JsonNode.class))).thenReturn(digest1);
        when(digester1.supportedTypes()).thenReturn(TYPES1);
        builder.addEntry(K1, digester1);

        digester2 = mock(Digester.class);
        when(digester2.digest(any(JsonNode.class))).thenReturn(digest2);
        when(digester2.supportedTypes()).thenReturn(TYPES2);
        builder.addEntry(K2, digester2);

        schemaDigester = new SchemaDigester(builder.freeze());
    }

    @DataProvider
    public Iterator<Object[]> sampleData()
    {
        return SampleNodeProvider.getSamples(EnumSet.allOf(NodeType.class));
    }

    @Test(dataProvider = "sampleData")
    public void onlyRelevantDigestsAreBuilt(final JsonNode node)
        throws ProcessingException
    {
        final NodeType type = NodeType.getNodeType(node);
        final SchemaTree tree
            = new CanonicalSchemaTree(SchemaKey.anonymousKey(), schema);
        final SchemaContext context = new SchemaContext(tree, type);
        final ProcessingReport report = mock(ProcessingReport.class);

        final SchemaDigest digest = schemaDigester.process(report, context);
        verify(digester1).digest(schema);
        verify(digester2).digest(schema);

        final Map<String,JsonNode> digests = digest.getDigests();

        if (TYPES1.contains(type))
            assertSame(digests.get(K1), digest1);
        else
            assertFalse(digests.containsKey(K1));


        if (TYPES2.contains(type))
            assertSame(digests.get(K2), digest2);
        else
            assertFalse(digests.containsKey(K2));

    }

    @Test
    public void nonPresentKeywordDoesNotTriggerBuild()
        throws ProcessingException
    {
        final ObjectNode node = FACTORY.objectNode();
        node.put(K1, K1);
        final SchemaTree schemaTree
            = new CanonicalSchemaTree(SchemaKey.anonymousKey(), node);
        final SchemaContext context
            = new SchemaContext(schemaTree, NodeType.NULL);
        final ProcessingReport report = mock(ProcessingReport.class);

        schemaDigester.process(report, context);

        verify(digester1).digest(node);
        verify(digester2, never()).digest(any(JsonNode.class));
    }
}
