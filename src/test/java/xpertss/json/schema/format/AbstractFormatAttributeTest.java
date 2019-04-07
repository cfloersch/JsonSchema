package xpertss.json.schema.format;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jackson.JacksonUtils;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jackson.NodeType;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.report.ProcessingMessage;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.core.tree.CanonicalSchemaTree;
import xpertss.json.schema.core.tree.JsonTree;
import xpertss.json.schema.core.tree.SchemaTree;
import xpertss.json.schema.core.tree.SimpleJsonTree;
import xpertss.json.schema.core.tree.key.SchemaKey;
import xpertss.json.schema.core.util.Dictionary;
import xpertss.json.schema.messages.JsonSchemaValidationBundle;
import xpertss.json.schema.processors.data.FullData;
import com.github.fge.msgsimple.bundle.MessageBundle;
import com.github.fge.msgsimple.load.MessageBundles;
import com.google.common.collect.Lists;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static xpertss.json.schema.matchers.ProcessingMessageAssert.*;
import static org.mockito.Mockito.*;

@RunWith(JUnitParamsRunner.class)
public abstract class AbstractFormatAttributeTest {

    protected static final SchemaTree SCHEMA_TREE = new CanonicalSchemaTree(SchemaKey.anonymousKey(), JacksonUtils.nodeFactory().objectNode());
    protected static final MessageBundle BUNDLE = MessageBundles.getBundle(JsonSchemaValidationBundle.class);

    protected final FormatAttribute attribute;
    protected final String fmt;

    protected ProcessingReport report;

    private final JsonNode testNode;

    protected AbstractFormatAttributeTest(Dictionary<FormatAttribute> dict, String prefix, String fmt)
        throws IOException
    {
        this.fmt = fmt;
        testNode = JsonLoader.fromResource(String.format("/format/%s/%s.json", prefix, fmt));
        attribute = dict.entries().get(fmt);
    }

    @Before
    public final void initReport()
    {
        report = mock(ProcessingReport.class);
    }

    @Test
    public final void formatAttributeIsSupported()
    {
        assertNotNull("no support for format attribute " + fmt, attribute);
    }

    public final Object[] testData()
    {
        final List<Object[]> list = Lists.newArrayList();

        String msg;
        JsonNode msgNode, msgData, msgParams;

        for (final JsonNode node: testNode) {
            msgNode = node.get("message");
            msgData = node.get("msgData");
            msgParams = node.get("msgParams");
            msg = msgNode == null ? null
                : buildMessage(msgNode.textValue(), msgParams, msgData);
            list.add(new Object[]{ node.get("data"),
                node.get("valid").booleanValue(), msg, msgData });
        }

        return list.toArray();
    }

    @Test
    @Parameters(method = "testData")
    public final void instanceIsCorrectlyAnalyzed(JsonNode instance, boolean valid, String msg, ObjectNode msgData)
        throws ProcessingException
    {
        final JsonTree tree = new SimpleJsonTree(instance);
        final FullData data = new FullData(SCHEMA_TREE, tree);

        attribute.validate(report, BUNDLE, data);

        if (valid) {
            verifyZeroInteractions(report);
            return;
        }

        ArgumentCaptor<ProcessingMessage> captor = ArgumentCaptor.forClass(ProcessingMessage.class);

        verify(report).error(captor.capture());

        ProcessingMessage message = captor.getValue();

        assertMessage(message).isFormatMessage(fmt, msg).hasContents(msgData)
            .hasField("value", instance);
    }

    private static String buildMessage(String key, JsonNode params, JsonNode data)
    {
        ProcessingMessage message = new ProcessingMessage().setMessage(BUNDLE.getMessage(key));
        if (params != null) {
            String name;
            JsonNode value;
            for (final JsonNode node: params) {
                name = node.textValue();
                value = data.get(name);
                message.putArgument(name, valueToArgument(value));
            }
        }
        return message.getMessage();
    }

    private static Object valueToArgument(final JsonNode value)
    {
        final NodeType type = NodeType.getNodeType(value);

        switch (type) {
            case STRING:
                return value.textValue();
            case INTEGER:
                return value.bigIntegerValue();
            case NUMBER:
                return value.decimalValue().toPlainString();
            case NULL:
                return value;
            case BOOLEAN:
                return value.booleanValue();
            case ARRAY:
                final List<Object> list = Lists.newArrayList();
                for (final JsonNode element: value)
                    list.add(valueToArgument(element));
                return list;
            default:
                throw new UnsupportedOperationException();
        }
    }
}
