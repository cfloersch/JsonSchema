package xpertss.json.schema.core.keyword.syntax.checkers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.MissingNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jackson.JacksonUtils;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jackson.NodeType;
import com.github.fge.jackson.jsonpointer.JsonPointer;
import com.github.fge.jackson.jsonpointer.JsonPointerException;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import xpertss.json.schema.SampleNodeProvider;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.tree.key.SchemaKey;
import xpertss.json.schema.core.util.Dictionary;
import xpertss.json.schema.core.messages.JsonSchemaSyntaxMessageBundle;
import xpertss.json.schema.core.report.ProcessingMessage;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.core.tree.CanonicalSchemaTree;
import xpertss.json.schema.core.tree.SchemaTree;
import com.github.fge.msgsimple.bundle.MessageBundle;
import com.github.fge.msgsimple.load.MessageBundles;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static xpertss.json.schema.TestUtils.*;
import static xpertss.json.schema.matchers.ProcessingMessageAssert.*;
import static org.mockito.Mockito.*;

@RunWith(JUnitParamsRunner.class)
public abstract class SyntaxCheckersTest {

    private static final MessageBundle BUNDLE = MessageBundles.getBundle(JsonSchemaSyntaxMessageBundle.class);

    /*
     * The keyword
     */
    private final String keyword;
    /*
     * The syntax checker
     */
    private final SyntaxChecker checker;
    /*
     * The set of invalid types for that keyword
     */
    private final EnumSet<NodeType> invalidTypes;
    /*
     * The value test node, if any
     */
    private final JsonNode valueTests;
    /*
     * The pointer test node, if any
     */
    private final JsonNode pointerTests;

    /*
     * Per test variables
     */
    private List<JsonPointer> pointers;
    private ProcessingReport report;

    /**
     * Constructor
     *
     * @param dict the {@link Dictionary} of {@link SyntaxChecker}s
     * @param prefix the prefix to use for resource files
     * @param keyword the keyword to test
     * @throws JsonProcessingException source JSON (if any) is not legal JSON
     */
    protected SyntaxCheckersTest(Dictionary<SyntaxChecker> dict, String prefix, String keyword)
        throws JsonProcessingException
    {
        this.keyword = keyword;
        checker = dict.entries().get(keyword);
        invalidTypes = checker == null ? null : EnumSet.complementOf(checker.getValidTypes());
        /*
         * Try and load the data and affect pointers. Barf on invalid JSON.
         *
         * If IOException, it means no file (hopefully); affect a MissingNode
         * to both valueTests and pointerTests.
         */
        JsonNode valueTestsNode, pointerTestsNode;
        try {
            String resource = "/syntax/" + prefix + '/' + keyword + ".json";
            JsonNode data = JsonLoader.fromResource(resource);
            valueTestsNode = data.path("valueTests");
            pointerTestsNode = data.path("pointerTests");
        } catch (JsonProcessingException oops) {
            throw oops;
        } catch (IOException ignored) {
            valueTestsNode = MissingNode.getInstance();
            pointerTestsNode = MissingNode.getInstance();
        }

        valueTests = valueTestsNode;
        pointerTests = pointerTestsNode;
    }

    @Before
    public final void init()
    {
        assertNotNull("keyword " + keyword + " is not supported??", checker);
        pointers = Lists.newArrayList();
        report = mock(ProcessingReport.class);
    }


    /*
     * Second test: check that invalid values are reported as such. Test common
     * to all keywords.
     */
    public Iterator<Object[]> invalidTypes()
    {
        return SampleNodeProvider.getSamples(invalidTypes);
    }

    @Test
    @Parameters(method = "invalidTypes")
    public final void invalidTypesAreReportedAsErrors(final JsonNode node)
        throws ProcessingException
    {
        SchemaTree tree = treeFromValue(keyword, node);
        NodeType type = NodeType.getNodeType(node);
        ArgumentCaptor<ProcessingMessage> captor = ArgumentCaptor.forClass(ProcessingMessage.class);

        checker.checkSyntax(pointers, BUNDLE, report, tree);

        verify(report).error(captor.capture());

        ProcessingMessage msg = captor.getValue();
        String message = BUNDLE.printf("common.incorrectType", type, EnumSet.complementOf(invalidTypes));
        assertMessage(msg).isSyntaxError(keyword, message, tree)
            .hasField("expected", EnumSet.complementOf(invalidTypes))
            .hasField("found", type);
    }

    /*
     * Third test: value tests. If no value tests were found, don't bother:
     * BasicSyntaxCheckerTest has covered that for us.
     */
    protected Iterator<Object[]> getValueTests()
    {
        if (valueTests.isMissingNode())
            return Iterators.emptyIterator();

        List<Object[]> list = Lists.newArrayList();

        String msg;
        JsonNode msgNode;
        JsonNode msgParams;
        JsonNode msgData;

        for (final JsonNode node: valueTests) {
            msgNode = node.get("message");
            msgParams = node.get("msgParams");
            msgData = node.get("msgData");
            msg = msgNode == null ? null
                : buildMessage(msgNode.textValue(), msgParams, msgData);
            list.add(new Object[]{ node.get("schema"), msg,
                node.get("valid").booleanValue(), msgData });
        }
        return list.iterator();
    }

    @Test
    @Parameters(method = "getValueTests")
    public final void valueTestsSucceed(JsonNode schema, String msg, boolean success, ObjectNode msgData)
        throws ProcessingException
    {
        SchemaTree tree = new CanonicalSchemaTree(SchemaKey.anonymousKey(), schema);

        checker.checkSyntax(pointers, BUNDLE, report, tree);

        if (success) {
            verify(report, never()).error(anyMessage());
            return;
        }

        ArgumentCaptor<ProcessingMessage> captor = ArgumentCaptor.forClass(ProcessingMessage.class);
        verify(report).error(captor.capture());

        final ProcessingMessage message = captor.getValue();

        assertMessage(message).isSyntaxError(keyword, msg, tree).hasContents(msgData);
    }

    /*
     * Fourth test: pointer lookups
     *
     * Non relevant keywrods will not have set it
     */
    protected Iterator<Object[]> getPointerTests()
    {
        if (pointerTests.isMissingNode())
            return Iterators.emptyIterator();

        final List<Object[]> list = Lists.newArrayList();

        for (final JsonNode node: pointerTests)
            list.add(new Object[] {
                node.get("schema"), node.get("pointers")
            });

        return list.iterator();
    }

    @Test
    @Parameters(method = "getPointerTests")
    public final void pointerDelegationWorksCorrectly(JsonNode schema, ArrayNode expectedPointers)
        throws ProcessingException, JsonPointerException
    {
        final SchemaTree tree
            = new CanonicalSchemaTree(SchemaKey.anonymousKey(), schema);

        checker.checkSyntax(pointers, BUNDLE, report, tree);

        final List<JsonPointer> expected = Lists.newArrayList();
        for (final JsonNode node: expectedPointers)
            expected.add(new JsonPointer(node.textValue()));

        assertEquals(expected, pointers);
    }

    /*
     * Utility methods
     */
    private static SchemaTree treeFromValue(String keyword, JsonNode node)
    {
        final ObjectNode schema = JacksonUtils.nodeFactory().objectNode();
        schema.put(keyword, node);
        return new CanonicalSchemaTree(SchemaKey.anonymousKey(), schema);
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
            case NUMBER: case NULL:
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
