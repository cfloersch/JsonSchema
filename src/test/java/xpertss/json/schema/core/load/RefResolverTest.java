package xpertss.json.schema.core.load;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jackson.JacksonUtils;
import org.junit.Test;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.messages.JsonSchemaCoreMessageBundle;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.core.tree.CanonicalSchemaTree;
import xpertss.json.schema.core.tree.SchemaTree;
import xpertss.json.schema.core.tree.key.SchemaKey;
import xpertss.json.schema.core.util.ValueHolder;
import com.github.fge.msgsimple.bundle.MessageBundle;
import com.github.fge.msgsimple.load.MessageBundles;

import static org.junit.Assert.fail;
import static xpertss.json.schema.matchers.ProcessingMessageAssert.*;
import static org.mockito.Mockito.*;

public final class RefResolverTest
{
    private static final MessageBundle BUNDLE
        = MessageBundles.getBundle(JsonSchemaCoreMessageBundle.class);

    private final RefResolver processor = new RefResolver(null);
    private final ProcessingReport report = mock(ProcessingReport.class);

    @Test
    public void refLoopsAreReported()
    {
        final ObjectNode node = JacksonUtils.nodeFactory().objectNode();
        node.put("$ref", "#");

        final SchemaTree tree
            = new CanonicalSchemaTree(SchemaKey.anonymousKey(), node);
        final ValueHolder<SchemaTree> holder = ValueHolder.hold("schema", tree);

        try {
            processor.process(report, holder);
            fail("No exception thrown!");
        } catch (ProcessingException e) {
            assertMessage(e.getProcessingMessage())
                .hasMessage(BUNDLE.printf("refProcessing.refLoop", "#"));
        }
    }

    @Test
    public void danglingRefsAreReported()
    {
        final ObjectNode node = JacksonUtils.nodeFactory().objectNode();
        node.put("$ref", "#/a");

        final SchemaTree tree
            = new CanonicalSchemaTree(SchemaKey.anonymousKey(), node);
        final ValueHolder<SchemaTree> holder = ValueHolder.hold("schema", tree);

        try {
            processor.process(report, holder);
            fail("No exception thrown!");
        } catch (ProcessingException e) {
            assertMessage(e.getProcessingMessage())
                .hasMessage(BUNDLE.printf("refProcessing.danglingRef", "#/a"));
        }
    }
}
