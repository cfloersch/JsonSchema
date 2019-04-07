package xpertss.json.schema.core.keyword.syntax.checkers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jackson.JacksonUtils;
import com.github.fge.jackson.NodeType;
import com.github.fge.jackson.jsonpointer.JsonPointer;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import xpertss.json.schema.SampleNodeProvider;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.messages.JsonSchemaSyntaxMessageBundle;
import xpertss.json.schema.core.report.ProcessingMessage;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.core.tree.CanonicalSchemaTree;
import xpertss.json.schema.core.tree.SchemaTree;
import xpertss.json.schema.core.tree.key.SchemaKey;
import com.github.fge.msgsimple.bundle.MessageBundle;
import com.github.fge.msgsimple.load.MessageBundles;
import org.mockito.ArgumentCaptor;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Iterator;

import static com.github.fge.jackson.NodeType.*;
import static xpertss.json.schema.TestUtils.*;
import static xpertss.json.schema.matchers.ProcessingMessageAssert.*;
import static org.mockito.Mockito.*;


@RunWith(JUnitParamsRunner.class)
public final class BasicSyntaxCheckerTest {

    private static final MessageBundle BUNDLE = MessageBundles.getBundle(JsonSchemaSyntaxMessageBundle.class);
    private static final JsonNodeFactory FACTORY = JacksonUtils.nodeFactory();
    private static final EnumSet<NodeType> VALID_TYPES = EnumSet.of(ARRAY, INTEGER, STRING);
    private static final String KEYWORD = "foo";

    public Iterator<Object[]> validTypes()
    {
        return SampleNodeProvider.getSamples(ARRAY, INTEGER, STRING);
    }

    @Test
    @Parameters(method = "validTypes")
    public void syntaxCheckingSucceedsOnValidTypes(final JsonNode node)
        throws ProcessingException
    {
        final AbstractSyntaxChecker checker = spy(new DummyChecker());
        final ProcessingReport report = mock(ProcessingReport.class);
        final ObjectNode schema = FACTORY.objectNode();
        schema.put(KEYWORD, node);
        SchemaTree tree = new CanonicalSchemaTree(SchemaKey.anonymousKey(), schema);

        checker.checkSyntax(null, BUNDLE, report, tree);
        verify(checker).checkValue(null, BUNDLE, report, tree);
        verify(report, never()).error(anyMessage());
    }

    public Iterator<Object[]> invalidTypes()
    {
        return SampleNodeProvider.getSamplesExcept(ARRAY, INTEGER, STRING);
    }

    @Test
    @Parameters(method = "invalidTypes")
    public void syntaxCheckingFailsOnInvalidTypes(final JsonNode node)
        throws ProcessingException
    {
        final NodeType type = NodeType.getNodeType(node);
        final ObjectNode schema = FACTORY.objectNode();
        schema.put(KEYWORD, node);
        SchemaTree tree = new CanonicalSchemaTree(SchemaKey.anonymousKey(), schema);

        final AbstractSyntaxChecker checker = spy(new DummyChecker());
        final ProcessingReport report = mock(ProcessingReport.class);

        ArgumentCaptor<ProcessingMessage> captor = ArgumentCaptor.forClass(ProcessingMessage.class);

        checker.checkSyntax(null, BUNDLE, report, tree);
        verify(report).error(captor.capture());
        verify(checker, never()).checkValue(null, BUNDLE, report, tree);

        final ProcessingMessage msg = captor.getValue();
        assertMessage(msg).hasField("keyword", KEYWORD).hasField("schema", tree)
            .hasMessage(BUNDLE.printf("common.incorrectType", type, VALID_TYPES))
            .hasField("domain", "syntax")
            .hasField("expected", EnumSet.of(ARRAY, INTEGER, STRING))
            .hasField("found", NodeType.getNodeType(node));
    }

    private static class DummyChecker extends AbstractSyntaxChecker {

        private DummyChecker()
        {
            super(KEYWORD, ARRAY, INTEGER, STRING);
        }

        @Override
        protected void checkValue(final Collection<JsonPointer> pointers,
            final MessageBundle bundle, final ProcessingReport report,
            final SchemaTree tree)
            throws ProcessingException
        {
        }
    }
}
