package xpertss.json.schema.keyword.validator.callback;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static xpertss.json.schema.TestUtils.anyReport;
import static xpertss.json.schema.TestUtils.onlyOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jackson.JacksonUtils;
import com.github.fge.jackson.jsonpointer.JsonPointer;
import org.junit.Before;
import org.junit.Test;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.processing.Processor;
import xpertss.json.schema.core.report.LogLevel;
import xpertss.json.schema.core.report.ProcessingMessage;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.core.tree.CanonicalSchemaTree;
import xpertss.json.schema.core.tree.JsonTree;
import xpertss.json.schema.core.tree.SchemaTree;
import xpertss.json.schema.core.tree.SimpleJsonTree;
import xpertss.json.schema.core.tree.key.SchemaKey;
import xpertss.json.schema.core.util.Dictionary;
import xpertss.json.schema.keyword.validator.KeywordValidator;
import xpertss.json.schema.keyword.validator.KeywordValidatorFactory;
import xpertss.json.schema.messages.JsonSchemaValidationBundle;
import xpertss.json.schema.processors.data.FullData;
import com.github.fge.msgsimple.bundle.MessageBundle;
import com.github.fge.msgsimple.load.MessageBundles;

public abstract class CallbackValidatorTest {

    protected static final MessageBundle BUNDLE = MessageBundles.getBundle(JsonSchemaValidationBundle.class);
    protected static final JsonNodeFactory FACTORY = JacksonUtils.nodeFactory();
    protected static final ProcessingMessage MSG = new ProcessingMessage();

    protected static final ObjectNode sub1 = FACTORY.objectNode();
    protected static final ObjectNode sub2 = FACTORY.objectNode();

    protected final String keyword;
    private final KeywordValidatorFactory factory;
    protected final JsonPointer ptr1;
    protected final JsonPointer ptr2;

    private Processor<FullData, FullData> processor;
    private FullData data;
    private ProcessingReport report;
    private KeywordValidator validator;

    protected CallbackValidatorTest(Dictionary<KeywordValidatorFactory> dict, String keyword, JsonPointer ptr1, JsonPointer ptr2)
    {
        this.keyword = keyword;
        factory = dict.entries().get(keyword);
        this.ptr1 = ptr1;
        this.ptr2 = ptr2;
    }

    @Before
    public final void initEnvironment()
        throws ProcessingException
    {
        assertNotNull("no support for " + keyword + "??", factory);

        SchemaTree tree = new CanonicalSchemaTree(SchemaKey.anonymousKey(), generateSchema());
        JsonTree instance = new SimpleJsonTree(generateInstance());
        data = new FullData(tree, instance);
        report = mock(ProcessingReport.class);
        when(report.getLogLevel()).thenReturn(LogLevel.DEBUG);
        validator = factory.getKeywordValidator(generateDigest());
    }


    @Test
    public final void exceptionOnFirstProcessingWorks()
        throws ProcessingException
    {
        processor = spy(new DummyProcessor(WantedState.EX, WantedState.OK, ptr1, ptr2));

        try {
            validator.validate(processor, report, BUNDLE, data);
            fail("No exception thrown!!");
        } catch (ProcessingException ignored) {
        }

        verify(processor, onlyOnce()).process(anyReport(), any(FullData.class));
    }

    @Test
    public final void exceptionOnSecondProcessingWorks()
        throws ProcessingException
    {
        processor = spy(new DummyProcessor(WantedState.OK, WantedState.EX, ptr1, ptr2));

        try {
            validator.validate(processor, report, BUNDLE, data);
            fail("No exception thrown!!");
        } catch (ProcessingException ignored) {
        }

        verify(processor, times(2)).process(anyReport(), any(FullData.class));
    }

    @Test
    public final void OkThenOkWorks()
        throws ProcessingException
    {
        processor = spy(new DummyProcessor(WantedState.OK, WantedState.OK, ptr1, ptr2));

        validator.validate(processor, report, BUNDLE, data);
        verify(processor, times(2)).process(anyReport(), any(FullData.class));

        checkOkOk(report);
    }

    protected abstract void checkOkOk(final ProcessingReport report)
        throws ProcessingException;

    @Test
    public final void OkThenKoWorks()
        throws ProcessingException
    {
        processor = spy(new DummyProcessor(WantedState.OK, WantedState.KO, ptr1, ptr2));

        validator.validate(processor, report, BUNDLE, data);
        verify(processor, times(2)).process(anyReport(), any(FullData.class));

        checkOkKo(report);
    }

    protected abstract void checkOkKo(final ProcessingReport report)
        throws ProcessingException;

    @Test
    public final void KoThenKoWorks()
        throws ProcessingException
    {
        processor = spy(new DummyProcessor(WantedState.KO, WantedState.KO, ptr1, ptr2));

        validator.validate(processor, report, BUNDLE, data);
        verify(processor, times(2)).process(anyReport(), any(FullData.class));

        checkKoKo(report);
    }

    protected abstract void checkKoKo(final ProcessingReport report)
        throws ProcessingException;

    protected abstract JsonNode generateSchema();

    protected abstract JsonNode generateInstance();

    protected abstract JsonNode generateDigest();

    private enum WantedState {
        OK
        {
            @Override
            void doIt(final ProcessingReport report)
                throws ProcessingException
            {
            }
        },
        KO
        {
            @Override
            void doIt(final ProcessingReport report)
                throws ProcessingException
            {
                report.error(MSG);
            }
        },
        EX
        {
            @Override
            void doIt(final ProcessingReport report)
                throws ProcessingException
            {
                throw new ProcessingException();
            }
        };

        abstract void doIt(final ProcessingReport report)
            throws ProcessingException;
    }

    private static class DummyProcessor
        implements Processor<FullData, FullData>
    {
        private final WantedState wanted1;
        private final WantedState wanted2;
        private final JsonPointer ptr1;
        private final JsonPointer ptr2;

        private DummyProcessor(final WantedState wanted1,
            final WantedState wanted2, final JsonPointer ptr1,
            final JsonPointer ptr2)
        {
            this.wanted1 = wanted1;
            this.wanted2 = wanted2;
            this.ptr1 = ptr1;
            this.ptr2 = ptr2;
        }

        @Override
        public FullData process(final ProcessingReport report,
            final FullData input)
            throws ProcessingException
        {
            final JsonNode schema = input.getSchema().getNode();

            final JsonPointer ptr = schema == sub1 ? ptr1 : ptr2;
            assertEquals("schema pointer differs from expectations", input.getSchema().getPointer(), ptr);

            final WantedState wanted = schema == sub1 ? wanted1 : wanted2;
            wanted.doIt(report);
            return input;
        }
    }
}
