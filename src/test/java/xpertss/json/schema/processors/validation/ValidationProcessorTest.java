package xpertss.json.schema.processors.validation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jackson.JacksonUtils;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jackson.NodeType;
import com.github.fge.jackson.jsonpointer.JsonPointer;
import com.github.fge.jackson.jsonpointer.JsonPointerException;
import org.junit.Before;
import org.junit.Test;
import xpertss.json.schema.cfg.ValidationConfiguration;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.keyword.syntax.checkers.SyntaxChecker;
import xpertss.json.schema.core.processing.Processor;
import xpertss.json.schema.core.report.ProcessingMessage;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.core.tree.CanonicalSchemaTree;
import xpertss.json.schema.core.tree.JsonTree;
import xpertss.json.schema.core.tree.SchemaTree;
import xpertss.json.schema.core.tree.SimpleJsonTree;
import xpertss.json.schema.core.tree.key.SchemaKey;
import xpertss.json.schema.keyword.validator.AbstractKeywordValidator;
import xpertss.json.schema.library.DraftV4Library;
import xpertss.json.schema.library.Keyword;
import xpertss.json.schema.library.Library;
import xpertss.json.schema.main.JsonSchemaFactory;
import xpertss.json.schema.main.JsonValidator;
import xpertss.json.schema.messages.JsonSchemaValidationBundle;
import xpertss.json.schema.processors.data.FullData;
import com.github.fge.msgsimple.bundle.MessageBundle;
import com.github.fge.msgsimple.load.MessageBundles;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static xpertss.json.schema.matchers.ProcessingMessageAssert.assertMessage;
import static org.mockito.Mockito.mock;

public final class ValidationProcessorTest
{
    private static final String K1 = "k1";
    private static final ObjectNode RAWSCHEMA;
    private static final ArrayNode RAWINSTANCE;
    private static final AtomicInteger COUNT = new AtomicInteger(0);

    static {
        final JsonNodeFactory factory = JacksonUtils.nodeFactory();

        RAWSCHEMA = factory.objectNode();
        RAWSCHEMA.put("minItems", 2)
            .put("items", factory.objectNode().put(K1, 0));

        RAWINSTANCE = factory.arrayNode();
        RAWINSTANCE.add(1);
    }

    private Processor<FullData, FullData> processor;

    @Before
    public void init()
    {
        final Keyword keyword = Keyword.newBuilder(K1)
            .withSyntaxChecker(mock(SyntaxChecker.class))
            .withIdentityDigester(NodeType.ARRAY, NodeType.values())
            .withValidatorClass(K1Validator.class)
            .freeze();
        final Library library = DraftV4Library.get().thaw()
            .addKeyword(keyword).freeze();
        final ValidationConfiguration cfg = ValidationConfiguration.newBuilder()
            .setDefaultLibrary("foo://bar#", library).freeze();
        final JsonSchemaFactory factory = JsonSchemaFactory.newBuilder()
            .setValidationConfiguration(cfg).freeze();
        processor = factory.getProcessor();
        COUNT.set(0);
    }

    @Test
    public void childrenAreNotExploredByDefaultIfContainerFails()
        throws ProcessingException
    {
        final SchemaTree schema
            = new CanonicalSchemaTree(SchemaKey.anonymousKey(), RAWSCHEMA);
        final JsonTree instance = new SimpleJsonTree(RAWINSTANCE);
        final FullData data = new FullData(schema, instance);
        final ProcessingReport report = mock(ProcessingReport.class);
        processor.process(report, data);
        assertEquals(COUNT.get(), 0);
    }

    @Test
    public void childrenAreExploredOnDemandEvenIfContainerFails()
        throws ProcessingException
    {
        final SchemaTree schema
            = new CanonicalSchemaTree(SchemaKey.anonymousKey(), RAWSCHEMA);
        final JsonTree instance = new SimpleJsonTree(RAWINSTANCE);
        final FullData data = new FullData(schema, instance, true);
        final ProcessingReport report = mock(ProcessingReport.class);
        processor.process(report, data);
        assertEquals(COUNT.get(), 1);
    }

    @Test(timeout = 1000)
    public void circularReferencingDuringValidationIsDetected()
        throws IOException, ProcessingException, JsonPointerException
    {
        final JsonNode schemaNode
            = JsonLoader.fromResource("/other/issue102.json");
        final JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
        final JsonValidator validator = factory.getValidator();
        final MessageBundle bundle
            = MessageBundles.getBundle(JsonSchemaValidationBundle.class);

        try {
            validator.validate(schemaNode,
                JacksonUtils.nodeFactory().nullNode());
            fail("No exception thrown!");
        } catch (ProcessingException e) {
            final URI uri = URI.create("#/oneOf/1");
            final ProcessingMessage message = e.getProcessingMessage();
            final String expectedMessage
                = bundle.printf("err.common.validationLoop", uri, "");
            assertMessage(message)
                .hasMessage(expectedMessage)
                .hasField("alreadyVisited", uri)
                .hasField("instancePointer", JsonPointer.empty().toString())
                .hasField("validationPath",
                    Arrays.asList("#", "#/oneOf/1"));
        }
        assertTrue(true);
    }

    /*
     * Issue #112: what was called a "validation loop" in issue #102 was in fact
     * not really one; it is possible to enter the same subschema using
     * different paths.
     *
     * The real thing which must be checked for is a full schema pointer loop.
     */
    @Test
    public void enteringSamePointerWithDifferentPathsDoesNotThrowException()
        throws IOException, ProcessingException
    {
        final JsonNode node = JsonLoader.fromResource("/other/issue112.json");
        final JsonNode schemaNode = node.get("schema");
        final JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
        final JsonValidator validator = factory.getValidator();

        final JsonNode instance = node.get("instance");

        assertTrue(validator.validate(schemaNode, instance).isSuccess());
        assertTrue(true);
    }

    public static final class K1Validator
        extends AbstractKeywordValidator
    {
        public K1Validator(final JsonNode digest)
        {
            super(K1);
        }

        @Override
        public void validate(final Processor<FullData, FullData> processor,
            final ProcessingReport report, final MessageBundle bundle,
            final FullData data)
            throws ProcessingException
        {
            COUNT.incrementAndGet();
        }

        @Override
        public String toString()
        {
            return K1;
        }
    }
}
