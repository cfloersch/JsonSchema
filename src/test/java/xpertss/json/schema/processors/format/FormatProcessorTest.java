package xpertss.json.schema.processors.format;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jackson.JacksonUtils;
import com.github.fge.jackson.NodeType;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import xpertss.json.schema.SampleNodeProvider;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.processing.Processor;
import xpertss.json.schema.core.report.ProcessingMessage;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.core.tree.CanonicalSchemaTree;
import xpertss.json.schema.core.tree.JsonTree;
import xpertss.json.schema.core.tree.SchemaTree;
import xpertss.json.schema.core.tree.SimpleJsonTree;
import xpertss.json.schema.core.tree.key.SchemaKey;
import xpertss.json.schema.core.util.Dictionary;
import xpertss.json.schema.format.FormatAttribute;
import xpertss.json.schema.keyword.validator.KeywordValidator;
import xpertss.json.schema.messages.JsonSchemaValidationBundle;
import xpertss.json.schema.processors.data.FullData;
import xpertss.json.schema.processors.data.SchemaContext;
import xpertss.json.schema.processors.data.ValidatorList;
import com.github.fge.msgsimple.bundle.MessageBundle;
import com.github.fge.msgsimple.load.MessageBundles;
import com.google.common.collect.Lists;
import org.mockito.ArgumentCaptor;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static xpertss.json.schema.matchers.ProcessingMessageAssert.*;
import static org.mockito.Mockito.*;

@RunWith(JUnitParamsRunner.class)
public final class FormatProcessorTest {

    private static final EnumSet<NodeType> SUPPORTED = EnumSet.of(NodeType.INTEGER, NodeType.NUMBER, NodeType.BOOLEAN);
    private static final MessageBundle BUNDLE = MessageBundles.getBundle(JsonSchemaValidationBundle.class);
    private static final JsonNodeFactory FACTORY = JacksonUtils.nodeFactory();
    private static final String FMT = "fmt";

    private FormatAttribute attribute;
    private FormatProcessor processor;
    private ProcessingReport report;

    @Before
    public void init()
    {
        attribute = mock(FormatAttribute.class);
        when(attribute.supportedTypes()).thenReturn(SUPPORTED);
        report = mock(ProcessingReport.class);
        Dictionary<FormatAttribute> dictionary
            = Dictionary.<FormatAttribute>newBuilder().addEntry(FMT, attribute)
                .freeze();
        processor = new FormatProcessor(dictionary);
    }

    @Test
    public void noFormatInSchemaIsANoOp()
        throws ProcessingException
    {
        final ObjectNode schema = FACTORY.objectNode();
        SchemaTree tree = new CanonicalSchemaTree(SchemaKey.anonymousKey(), schema);
        final SchemaContext context = new SchemaContext(tree, NodeType.NULL);
        ValidatorList in = new ValidatorList(context, Collections.<KeywordValidator>emptyList());

        final ValidatorList out = processor.process(report, in);

        assertTrue(Lists.newArrayList(out).isEmpty());

        verifyZeroInteractions(report);
    }

    @Test
    public void unknownFormatAttributesAreReportedAsWarnings()
        throws ProcessingException
    {
        final ObjectNode schema = FACTORY.objectNode();
        schema.put("format", "foo");
        SchemaTree tree = new CanonicalSchemaTree(SchemaKey.anonymousKey(), schema);
        final SchemaContext context = new SchemaContext(tree, NodeType.NULL);
        ValidatorList in = new ValidatorList(context, Collections.<KeywordValidator>emptyList());

        ArgumentCaptor<ProcessingMessage> captor = ArgumentCaptor.forClass(ProcessingMessage.class);

        final ValidatorList out = processor.process(report, in);

        assertTrue(Lists.newArrayList(out).isEmpty());

        verify(report).warn(captor.capture());

        final ProcessingMessage message = captor.getValue();

        assertMessage(message)
            .hasMessage(BUNDLE.printf("warn.format.notSupported", "foo"))
            .hasField("domain", "validation").hasField("keyword", "format")
            .hasField("attribute", "foo");
    }

    @Test
    public void attributeIsBeingAskedWhatIsSupports()
        throws ProcessingException
    {
        final ObjectNode schema = FACTORY.objectNode();
        schema.put("format", FMT);
        SchemaTree tree = new CanonicalSchemaTree(SchemaKey.anonymousKey(), schema);
        final SchemaContext context = new SchemaContext(tree, NodeType.NULL);
        ValidatorList in = new ValidatorList(context, Collections.<KeywordValidator>emptyList());

        processor.process(report, in);
        verify(attribute).supportedTypes();
    }

    public Iterator<Object[]> supported()
    {
        return SampleNodeProvider.getSamples(SUPPORTED);
    }

    @Test
    @Parameters(method = "supported")
    public void supportedNodeTypesTriggerAttributeBuild(final JsonNode node)
        throws ProcessingException
    {
        final ObjectNode schema = FACTORY.objectNode();
        schema.put("format", FMT);
        SchemaTree tree = new CanonicalSchemaTree(SchemaKey.anonymousKey(), schema);
        final JsonTree instance = new SimpleJsonTree(node);
        final FullData data = new FullData(tree, instance);
        final SchemaContext context = new SchemaContext(data);
        ValidatorList in = new ValidatorList(context, Collections.<KeywordValidator>emptyList());

        final ValidatorList out = processor.process(report, in);

        final List<KeywordValidator> validators = Lists.newArrayList(out);

        assertEquals(1, validators.size());

        @SuppressWarnings("unchecked")
        final Processor<FullData, FullData> p = mock(Processor.class);

        validators.get(0).validate(p, report, BUNDLE, data);
        verify(attribute).validate(report, BUNDLE, data);
    }

    public Iterator<Object[]> unsupported()
    {
        return SampleNodeProvider.getSamplesExcept(SUPPORTED);
    }

    @Test
    @Parameters(method = "unsupported")
    public void unsupportedTypeDoesNotTriggerValidatorBuild(final JsonNode node)
        throws ProcessingException
    {
        final ObjectNode schema = FACTORY.objectNode();
        schema.put("format", FMT);
        SchemaTree tree = new CanonicalSchemaTree(SchemaKey.anonymousKey(), schema);
        SchemaContext context = new SchemaContext(tree, NodeType.getNodeType(node));
        ValidatorList in = new ValidatorList(context, Collections.<KeywordValidator>emptyList());

        final ValidatorList out = processor.process(report, in);

        final List<KeywordValidator> validators = Lists.newArrayList(out);

        assertTrue(validators.isEmpty());
    }
}
