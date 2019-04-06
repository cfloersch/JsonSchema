package xpertss.json.schema.keyword.special;

import static xpertss.json.schema.TestUtils.anyMessage;
import static xpertss.json.schema.matchers.ProcessingMessageAssert.assertMessage;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertNotNull;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;

import org.mockito.ArgumentCaptor;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jackson.JsonLoader;
import xpertss.json.schema.TestUtils;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.processing.Processor;
import xpertss.json.schema.core.report.ProcessingMessage;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.core.tree.CanonicalSchemaTree;
import xpertss.json.schema.core.tree.JsonTree;
import xpertss.json.schema.core.tree.SchemaTree;
import xpertss.json.schema.core.tree.SimpleJsonTree;
import xpertss.json.schema.core.tree.key.SchemaKey;
import xpertss.json.schema.keyword.validator.KeywordValidator;
import xpertss.json.schema.keyword.validator.KeywordValidatorFactory;
import xpertss.json.schema.library.validator.CommonValidatorDictionary;
import xpertss.json.schema.messages.JsonSchemaValidationBundle;
import xpertss.json.schema.processors.data.FullData;
import com.github.fge.msgsimple.bundle.MessageBundle;
import com.github.fge.msgsimple.load.MessageBundles;
import com.google.common.collect.Lists;

public final class PatternKeywordTest
{
    /*
     * A special testing class is needed for all keywords which use the null
     * digester, since we cannot feed them with a digest: its information
     * comes with the validation data itself (the schema to be precise).
     */

    private static final MessageBundle BUNDLE
        = MessageBundles.getBundle(JsonSchemaValidationBundle.class);

    private final KeywordValidatorFactory factory;
    private final JsonNode testData;

    public PatternKeywordTest()
        throws IOException
    {
        factory = CommonValidatorDictionary.get().entries().get("pattern");
        testData = JsonLoader.fromResource("/keyword/special/pattern.json");
    }

    @Test
    public void keywordExists()
    {
        assertNotNull(factory, "no support for pattern??");
    }

    @DataProvider
    public Iterator<Object[]> getValueTests()
    {
        final List<Object[]> list = Lists.newArrayList();

        String msg;
        JsonNode msgNode, msgData, msgParams;

        for (final JsonNode node: testData) {
            msgNode = node.get("message");
            msgData = node.get("msgData");
            msgParams = node.get("msgParams");
            msg = msgNode == null ? null
                : TestUtils.buildMessage(BUNDLE, msgNode.textValue(),
                    msgParams, msgData);
            list.add(new Object[]{ node.get("schema"), node.get("data"), msg,
                node.get("valid").booleanValue(), node.get("msgData") });
        }
        return list.iterator();
    }

    @Test(dataProvider = "getValueTests", dependsOnMethods = "keywordExists")
    public void instancesAreValidatedCorrectly(final JsonNode schema,
        final JsonNode node, final String msg,
        final boolean valid, final ObjectNode msgData)
        throws IllegalAccessException, InvocationTargetException,
        InstantiationException, ProcessingException
    {
        final SchemaTree tree
            = new CanonicalSchemaTree(SchemaKey.anonymousKey(), schema);
        final JsonTree instance = new SimpleJsonTree(node);
        final FullData data = new FullData(tree, instance);

        final ProcessingReport report = mock(ProcessingReport.class);
        @SuppressWarnings("unchecked")
        final Processor<FullData, FullData> processor =  mock(Processor.class);

        // It is a null node which is ignored by the constructor, so we can
        // do that
        final KeywordValidator validator = factory.getKeywordValidator(schema);
        validator.validate(processor, report, BUNDLE, data);

        if (valid) {
            verify(report, never()).error(anyMessage());
            return;
        }

        final ArgumentCaptor<ProcessingMessage> captor
            = ArgumentCaptor.forClass(ProcessingMessage.class);

        verify(report).error(captor.capture());

        final ProcessingMessage message = captor.getValue();

        assertMessage(message).isValidationError("pattern", msg)
            .hasContents(msgData);
    }
}
