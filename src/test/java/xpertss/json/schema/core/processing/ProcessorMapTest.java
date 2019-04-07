package xpertss.json.schema.core.processing;

import org.junit.Before;
import org.junit.Test;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.messages.JsonSchemaCoreMessageBundle;
import xpertss.json.schema.core.report.MessageProvider;
import xpertss.json.schema.core.report.ProcessingReport;
import com.github.fge.msgsimple.bundle.MessageBundle;
import com.github.fge.msgsimple.load.MessageBundles;
import com.google.common.base.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static xpertss.json.schema.TestUtils.*;
import static xpertss.json.schema.matchers.ProcessingMessageAssert.*;
import static org.mockito.Mockito.*;

public final class ProcessorMapTest
{
    private static final MessageBundle BUNDLE
        = MessageBundles.getBundle(JsonSchemaCoreMessageBundle.class);

    private Processor<In, Out> processor1;
    private Processor<In, Out> processor2;
    private Processor<In, Out> byDefault;
    private Function<In, Key> fn;
    private In input;
    private ProcessingReport report;

    @Before
    @SuppressWarnings("unchecked")
    public void initProcessors()
    {
        processor1 = mock(Processor.class);
        processor2 = mock(Processor.class);
        byDefault = mock(Processor.class);
        fn = mock(Function.class);
        input = mock(In.class);
        report = mock(ProcessingReport.class);
    }

    @Test
    public void cannotInputNullKey()
    {
        try {
            new ProcessorMap<Key, In, Out>(fn).addEntry(null, null);
            fail("No exception thrown!!");
        } catch (NullPointerException e) {
            assertEquals(BUNDLE.getMessage("processing.nullKey"), e.getMessage());
        }
    }

    @Test
    public void cannotInputNullProcessor()
    {
        try {
            new ProcessorMap<Key, In, Out>(fn).addEntry(Key.ONE, null);
            fail("No exception thrown!!");
        } catch (NullPointerException e) {
            assertEquals(BUNDLE.getMessage("processing.nullProcessor"), e.getMessage());
        }
    }

    @Test
    public void cannotInputNullDefaultProcessor()
    {
        try {
            new ProcessorMap<Key, In, Out>(fn).addEntry(Key.ONE, processor1)
                .setDefaultProcessor(null);
            fail("No exception thrown!!");
        } catch (NullPointerException e) {
            assertEquals(BUNDLE.getMessage("processing.nullProcessor"), e.getMessage());
        }
    }

    @Test
    public void nullFunctionRaisesBuildError()
    {
        try {
            new ProcessorMap<Key, In, Out>(null).getProcessor();
            fail("No exception thrown!!");
        } catch (NullPointerException e) {
            assertEquals(BUNDLE.getMessage("processing.nullFunction"), e.getMessage());
        }
    }

    @Test
    public void appropriateProcessorIsSelectedAndRun()
        throws ProcessingException
    {
        final ProcessorMap<Key, In, Out> processorMap
            = new ProcessorMap<Key, In, Out>(fn)
            .addEntry(Key.ONE, processor1).addEntry(Key.TWO, processor2)
            .setDefaultProcessor(byDefault);

        when(fn.apply(input)).thenReturn(Key.ONE);

        final Processor<In, Out> processor = processorMap.getProcessor();

        processor.process(report, input);

        verify(processor1, only()).process(report, input);
        verify(processor2, never()).process(anyReport(), any(In.class));
        verify(byDefault, never()).process(anyReport(), any(In.class));
    }

    @Test
    public void noMatchingKeyAndNoDefaultProcessorThrowsException()
    {
        final ProcessorMap<Key, In, Out> processorMap
            = new ProcessorMap<Key, In, Out>(fn)
            .addEntry(Key.ONE, processor1).addEntry(Key.TWO, processor2);

        when(fn.apply(input)).thenReturn(Key.THREE);

        final Processor<In, Out> processor = processorMap.getProcessor();

        try {
            processor.process(report, input);
            fail("No exception thrown!!");
        } catch (ProcessingException e) {
            assertMessage(e.getProcessingMessage())
                .hasMessage(BUNDLE.getMessage("processing.noProcessor"))
                .hasField("key", Key.THREE);
        }
    }

    @Test
    public void noMatchingKeyCallsDefaultProcessorWhenSet()
        throws ProcessingException
    {
        final ProcessorMap<Key, In, Out> processorMap
            = new ProcessorMap<Key, In, Out>(fn)
            .addEntry(Key.ONE, processor1).addEntry(Key.TWO, processor2)
            .setDefaultProcessor(byDefault);

        when(fn.apply(input)).thenReturn(Key.THREE);

        final Processor<In, Out> processor = processorMap.getProcessor();

        processor.process(report, input);

        verify(processor1, never()).process(anyReport(), any(In.class));
        verify(processor2, never()).process(anyReport(), any(In.class));
        verify(byDefault, only()).process(report, input);
    }

    private enum Key { ONE, TWO, THREE }

    private interface In extends MessageProvider
    {
    }

    private interface Out extends MessageProvider
    {
    }
}
