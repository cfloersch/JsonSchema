package xpertss.json.schema.core.report;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jackson.JacksonUtils;
import org.junit.Test;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.messages.JsonSchemaCoreMessageBundle;
import com.github.fge.msgsimple.bundle.MessageBundle;
import com.github.fge.msgsimple.load.MessageBundles;
import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static xpertss.json.schema.matchers.ProcessingMessageAssert.*;

public final class ProcessingMessageTest
{
    private static final MessageBundle BUNDLE
        = MessageBundles.getBundle(JsonSchemaCoreMessageBundle.class);

    private static final JsonNodeFactory FACTORY = JacksonUtils.nodeFactory();

    @Test
    public void defaultLogLevelIsInfo()
    {
        final ProcessingMessage msg = new ProcessingMessage();
        assertMessage(msg).hasLevel(LogLevel.INFO);
    }

    @Test
    public void settingLogThresholdWorks()
    {
        final ProcessingMessage msg = new ProcessingMessage();

        for (final LogLevel level: LogLevel.values()) {
            msg.setLogLevel(level);
            assertMessage(msg).hasLevel(level);
        }
    }

    @Test
    public void cannoSetThresholdToNull()
    {
        final ProcessingMessage msg = new ProcessingMessage();

        try {
            msg.setLogLevel(null);
            fail("No exception thrown!");
        } catch (NullPointerException e) {
            assertEquals(e.getMessage(), BUNDLE.getMessage("processing.nullLevel"));
        }
    }

    @Test
    public void msgMethodSetsMessageField()
    {
        final ProcessingMessage msg = new ProcessingMessage().setMessage("foo");
        assertMessage(msg).hasMessage("foo");
    }

    @Test
    public void settingStringFieldWorks()
    {
        final ProcessingMessage msg = new ProcessingMessage().put("foo", "bar");

        assertMessage(msg).hasField("foo", "bar");
    }

    @Test
    public void settingNullStringSetsNullNode()
    {
        final ProcessingMessage msg = new ProcessingMessage()
            .put("foo", (String) null);

        assertMessage(msg).hasNullField("foo");
    }

    @Test
    public void settingAnyObjectSetsToString()
    {
        final Object foo = new Object();
        final JsonNode node = FACTORY.textNode(foo.toString());
        final ProcessingMessage msg = new ProcessingMessage().put("foo", foo);

        assertMessage(msg).hasField("foo", node);
    }

    @Test
    public void settingNullObjectSetsNullNode()
    {
        final Object o = null;
        final ProcessingMessage msg = new ProcessingMessage().put("foo", o);

        assertMessage(msg).hasNullField("foo");
    }

    @Test
    public void settingAnyJsonNodeWorks()
    {
        final JsonNode foo = FACTORY.booleanNode(true);
        final ProcessingMessage msg = new ProcessingMessage().put("foo", foo);

        assertMessage(msg).hasField("foo", foo);
    }

    @Test
    public void nodesAreUnalteredWhenSubmitted()
    {
        final ObjectNode foo = FACTORY.objectNode();
        foo.put("a", "b");
        final JsonNode node = foo.deepCopy();

        final ProcessingMessage msg = new ProcessingMessage().put("foo", foo);
        foo.remove("a");

        assertMessage(msg).hasField("foo", node);
    }

    @Test
    public void settingNullJsonNodeSetsNullNode()
    {
        final JsonNode node = null;
        final ProcessingMessage msg = new ProcessingMessage().put("foo", node);

        assertMessage(msg).hasNullField("foo");
    }

    @Test
    public void submittedCollectionAppliesToStringToElements()
    {
        final List<Object> list = Arrays.asList(new Object(), new Object());
        final ArrayNode node = FACTORY.arrayNode();
        for (final Object o: list)
            node.add(o.toString());

        final ProcessingMessage msg = new ProcessingMessage().put("foo", list);

        assertMessage(msg).hasField("foo", node);
    }

    @Test
    public void submittingNullCollectionSetsNullNode()
    {
        final Collection<Object> foo = null;
        final ProcessingMessage msg = new ProcessingMessage().put("foo", foo);

        assertMessage(msg).hasNullField("foo");
    }

    @Test
    public void nullElementInCollectionSetsNullNode()
    {
        final List<Object> list = Lists.newArrayList(new Object(), null, new Object());
        final ArrayNode node = FACTORY.arrayNode();
        node.add(list.get(0).toString());
        node.add(FACTORY.nullNode());
        node.add(list.get(2).toString());

        final ProcessingMessage msg = new ProcessingMessage().put("foo", list);

        assertMessage(msg).hasField("foo", node);
    }

    @Test
    public void settingExceptionProviderYieldsCorrectException()
        throws ProcessingException
    {
        final ProcessingMessage testMessage = new ProcessingMessage();
        testMessage.setExceptionProvider(Foo::new);

        try {
            throw testMessage.asException();
        } catch (Foo ignored) {
            assertTrue(true);
        }
    }

    @Test
    public void cannotSetNullExceptionProvider()
    {
        try {
            new ProcessingMessage().setExceptionProvider(null);
            fail("No exception thrown!!");
        } catch (NullPointerException e) {
            assertEquals(e.getMessage(),
                BUNDLE.getMessage("processing.nullExceptionProvider"));
        }
    }

    @Test
    public void argumentsShowUpInMessage()
    {
        final ProcessingMessage message = new ProcessingMessage()
            .setMessage("Hello %s!").putArgument("greeted", "world");

        assertEquals(message.getMessage(), "Hello world!");
    }

    @Test
    public void missingFormatArgumentsCausesFormatItselfToBeReturned()
    {
        final String format = "%s%s";
        final ProcessingMessage message = new ProcessingMessage()
            .setMessage(format).putArgument("arg1", 1);

        assertEquals(message.getMessage(), format);
    }

    @Test
    public void messageInRawJsonReflectsArguments()
    {
        final ProcessingMessage message = new ProcessingMessage()
            .setMessage("Hello %s!").putArgument("greeted", "world");

        assertEquals(message.asJson().get("message").textValue(),
            "Hello world!");
    }

    @Test
    public void settingMessageAgainResetsArguments()
    {
        final String fmt2 = "message2: %s";
        final ProcessingMessage message = new ProcessingMessage()
            .setMessage("Hello %s!").putArgument("greeted", "world")
            .setMessage(fmt2).putArgument("foo", "bar");

        assertEquals(message.getMessage(), "message2: bar");
    }

    private static final class Foo
        extends ProcessingException
    {
        private Foo(final ProcessingMessage message)
        {
            super(message);
        }
    }
}
