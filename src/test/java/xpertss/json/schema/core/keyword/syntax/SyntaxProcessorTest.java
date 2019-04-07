package xpertss.json.schema.core.keyword.syntax;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jackson.JacksonUtils;
import com.github.fge.jackson.NodeType;
import com.github.fge.jackson.jsonpointer.JsonPointer;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import xpertss.json.schema.SampleNodeProvider;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.tree.key.SchemaKey;
import xpertss.json.schema.core.util.Dictionary;
import xpertss.json.schema.core.util.DictionaryBuilder;
import xpertss.json.schema.core.messages.JsonSchemaSyntaxMessageBundle;
import xpertss.json.schema.core.report.AbstractProcessingReport;
import xpertss.json.schema.core.report.LogLevel;
import xpertss.json.schema.core.report.ProcessingMessage;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.core.keyword.syntax.checkers.SyntaxChecker;
import xpertss.json.schema.core.tree.CanonicalSchemaTree;
import xpertss.json.schema.core.tree.SchemaTree;
import xpertss.json.schema.core.util.ValueHolder;
import com.github.fge.msgsimple.bundle.MessageBundle;
import com.github.fge.msgsimple.load.MessageBundles;
import org.mockito.ArgumentCaptor;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static xpertss.json.schema.TestUtils.*;
import static xpertss.json.schema.matchers.ProcessingMessageAssert.*;
import static org.mockito.Mockito.*;

@RunWith(JUnitParamsRunner.class)
public final class SyntaxProcessorTest {

    private static final MessageBundle BUNDLE = MessageBundles.getBundle(JsonSchemaSyntaxMessageBundle.class);
    private static final JsonNodeFactory FACTORY = JacksonUtils.nodeFactory();
    private static final String K1 = "k1";
    private static final String K2 = "k2";
    private static final String ERRMSG = "foo";

    private AbstractProcessingReport report;
    private SyntaxProcessor processor;
    private SyntaxChecker checker;

    @Before
    public void initialize()
    {
        report = spy(new TestProcessingReport());
        DictionaryBuilder<SyntaxChecker> builder = Dictionary.newBuilder();

        checker = mock(SyntaxChecker.class);
        builder.addEntry(K1, checker);
        builder.addEntry(K2, new SyntaxChecker() {
            @Override
            public EnumSet<NodeType> getValidTypes()
            {
                return EnumSet.noneOf(NodeType.class);
            }

            @Override
            public void checkSyntax(final Collection<JsonPointer> pointers,
                final MessageBundle bundle, final ProcessingReport report,
                final SchemaTree tree)
                throws ProcessingException
            {
                report.error(new ProcessingMessage().setMessage(ERRMSG));
            }
        });

        processor = new SyntaxProcessor(BUNDLE, builder.freeze());
    }

    public Iterator<Object[]> notSchemas()
    {
        return SampleNodeProvider.getSamplesExcept(NodeType.OBJECT);
    }

    @Test
    @Parameters(method = "notSchemas")
    public void syntaxProcessorYellsOnNonSchemas(final JsonNode node)
        throws ProcessingException
    {
        final ArgumentCaptor<ProcessingMessage> captor
            = ArgumentCaptor.forClass(ProcessingMessage.class);

        final SchemaTree tree
            = new CanonicalSchemaTree(SchemaKey.anonymousKey(), node);
        final ValueHolder<SchemaTree> holder = ValueHolder.hold("schema", tree);

        processor.process(report, holder);

        verify(report).log(same(LogLevel.ERROR), captor.capture());

        final ProcessingMessage message = captor.getValue();
        final NodeType type = NodeType.getNodeType(node);
        assertMessage(message)
            .hasMessage(BUNDLE.printf("core.notASchema", type))
            .hasField("found", type);
    }

    @Test
    public void unknownKeywordsAreReportedAsWarnings()
        throws ProcessingException
    {
        final ObjectNode node = FACTORY.objectNode();
        node.put("foo", "");
        node.put("bar", "");

        final SchemaTree tree
            = new CanonicalSchemaTree(SchemaKey.anonymousKey(), node);
        final ValueHolder<SchemaTree> holder = ValueHolder.hold("schema", tree);

        final ArrayNode ignored = FACTORY.arrayNode();
        // They appear in alphabetical order in the report!
        ignored.add("bar");
        ignored.add("foo");
        final Iterable<String> iterable = StreamSupport.stream(ignored.spliterator(), false).map(JsonNode::textValue).collect(Collectors.toList());

        final ArgumentCaptor<ProcessingMessage> captor
            = ArgumentCaptor.forClass(ProcessingMessage.class);

        processor.process(report, holder);
        verify(report).log(same(LogLevel.WARNING), captor.capture());

        final ProcessingMessage message = captor.getValue();

        assertMessage(message).hasField("ignored", ignored)
            .hasMessage(BUNDLE.printf("core.unknownKeywords", iterable));
    }

    @Test
    public void errorsAreCorrectlyReported()
        throws ProcessingException
    {
        final ArgumentCaptor<ProcessingMessage> captor
            = ArgumentCaptor.forClass(ProcessingMessage.class);

        final ObjectNode schema = FACTORY.objectNode();
        schema.put(K2, "");

        final SchemaTree tree
            = new CanonicalSchemaTree(SchemaKey.anonymousKey(), schema);
        final ValueHolder<SchemaTree> holder = ValueHolder.hold("schema", tree);

        processor.process(report, holder);

        verify(report).log(same(LogLevel.ERROR), captor.capture());

        final ProcessingMessage msg = captor.getValue();
        assertMessage(msg).hasMessage(ERRMSG);
    }

    @Test
    public void checkingWillNotDiveIntoUnknownKeywords()
        throws ProcessingException
    {
        final ObjectNode node = FACTORY.objectNode();
        node.put(K1, K1);
        final ObjectNode schema = FACTORY.objectNode();
        schema.put("foo", node);
        final SchemaTree tree
            = new CanonicalSchemaTree(SchemaKey.anonymousKey(), schema);
        final ValueHolder<SchemaTree> holder = ValueHolder.hold("schema", tree);

        processor.process(report, holder);
        verify(checker, never()).checkSyntax(anyCollection(),
            any(MessageBundle.class), anyReport(), anySchema());
    }

    private static class TestProcessingReport
        extends AbstractProcessingReport
    {
        @Override
        public void log(final LogLevel level, final ProcessingMessage message)
        {
        }
    }
}