package xpertss.json.schema.keyword.special;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static xpertss.json.schema.TestUtils.anyMessage;
import static xpertss.json.schema.matchers.ProcessingMessageAssert.assertMessage;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jackson.JacksonUtils;
import com.github.fge.jackson.jsonpointer.JsonPointer;
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
import xpertss.json.schema.library.validator.DraftV3ValidatorDictionary;
import xpertss.json.schema.messages.JsonSchemaValidationBundle;
import xpertss.json.schema.processors.data.FullData;
import com.github.fge.msgsimple.bundle.MessageBundle;
import com.github.fge.msgsimple.load.MessageBundles;

public final class ExtendsKeywordTest
{
    private static final MessageBundle BUNDLE
        = MessageBundles.getBundle(JsonSchemaValidationBundle.class);
    private static final String FOO = "foo";
    private static final JsonNodeFactory FACTORY = JacksonUtils.nodeFactory();

    private final KeywordValidator validator;

    private Processor<FullData, FullData> processor;
    private FullData data;
    private ProcessingReport report;
    private ProcessingMessage msg;

    public ExtendsKeywordTest()
        throws ProcessingException
    {
        final KeywordValidatorFactory factory
            = DraftV3ValidatorDictionary.get().entries().get("extends");
        validator = factory == null ? null
            : factory.getKeywordValidator(FACTORY.nullNode());
    }

    @Before
    public void initEnvironment()
    {
        assertNotNull("no support for extends??", validator);

        final ObjectNode schema = FACTORY.objectNode();
        schema.put("extends", FACTORY.objectNode());
        final SchemaTree tree
            = new CanonicalSchemaTree(SchemaKey.anonymousKey(), schema);

        final JsonTree instance = new SimpleJsonTree(FACTORY.nullNode());
        data = new FullData(tree, instance);

        report = mock(ProcessingReport.class);
        msg = new ProcessingMessage().setMessage(FOO);
    }


    @Test
    public void exceptionIsCorrectlyThrown()
    {
        processor = new DummyProcessor(WantedState.EX, msg);

        try {
            validator.validate(processor, report, BUNDLE, data);
            fail("No exception thrown??");
        } catch (ProcessingException ignored) {
        }
    }

    @Test
    public void failingSubSchemaLeadsToFailure()
        throws ProcessingException
    {
        final ArgumentCaptor<ProcessingMessage> captor
            = ArgumentCaptor.forClass(ProcessingMessage.class);

        processor = new DummyProcessor(WantedState.KO, msg);

        validator.validate(processor, report, BUNDLE, data);

        verify(report).error(captor.capture());

        final ProcessingMessage message = captor.getValue();

        assertMessage(message).hasMessage(FOO);
    }

    @Test
    public void successfulSubSchemaLeadsToSuccess()
        throws ProcessingException
    {
        processor = new DummyProcessor(WantedState.OK, msg);

        validator.validate(processor, report, BUNDLE, data);

        verify(report, never()).error(anyMessage());
    }

    private enum WantedState {
        OK
        {
            @Override
            void doIt(final ProcessingReport report,
                final ProcessingMessage message)
                throws ProcessingException
            {
            }
        },
        KO
        {
            @Override
            void doIt(final ProcessingReport report,
                final ProcessingMessage message)
                throws ProcessingException
            {
                report.error(message);
            }
        },
        EX
        {
            @Override
            void doIt(final ProcessingReport report,
                final ProcessingMessage message)
                throws ProcessingException
            {
                throw new ProcessingException();
            }
        };

        abstract void doIt(final ProcessingReport report,
            final ProcessingMessage message)
            throws ProcessingException;
    }

    private static final class DummyProcessor
        implements Processor<FullData, FullData>
    {
        private static final JsonPointer PTR = JsonPointer.of("extends");

        private final WantedState wanted;
        private final ProcessingMessage message;

        private DummyProcessor(final WantedState wanted,
            final ProcessingMessage message)
        {
            this.wanted = wanted;
            this.message = message;
        }

        @Override
        public FullData process(final ProcessingReport report,
            final FullData input)
            throws ProcessingException
        {
            assertEquals(PTR, input.getSchema().getPointer());
            wanted.doIt(report, message);
            return input;
        }
    }
}
