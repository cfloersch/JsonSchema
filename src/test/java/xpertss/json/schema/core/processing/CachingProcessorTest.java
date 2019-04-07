package xpertss.json.schema.core.processing;

import org.junit.Before;
import org.junit.Test;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.messages.JsonSchemaCoreMessageBundle;
import xpertss.json.schema.core.report.MessageProvider;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.core.util.equivalence.Equivalences;
import com.github.fge.msgsimple.bundle.MessageBundle;
import com.github.fge.msgsimple.load.MessageBundles;

import static junit.framework.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static xpertss.json.schema.TestUtils.*;
import static org.mockito.Mockito.*;

public final class CachingProcessorTest
{
    private static final MessageBundle BUNDLE
        = MessageBundles.getBundle(JsonSchemaCoreMessageBundle.class);

    private In input;

    private Processor<In, Out> processor;

    @Before
    @SuppressWarnings("unchecked")
    public void init()
    {
        input = mock(In.class);
        processor = mock(Processor.class);
    }

    @Test
    public void cannotInputNullProcessor()
    {
        try {
            new CachingProcessor<In, Out>(null);
            fail("No exception thrown!!");
        } catch (NullPointerException e) {
            assertEquals(BUNDLE.getMessage("processing.nullProcessor"), e.getMessage());
        }
    }

    @Test
    public void cannotInputNullEquivalence()
    {
        try {
            new CachingProcessor<In, Out>(processor, null);
            fail("No exception thrown!!");
        } catch (NullPointerException e) {
            assertEquals(BUNDLE.getMessage("processing.nullEquivalence"), e.getMessage());
        }
    }

    @Test
    public void cannotInputInvalidCacheSize()
    {
        try {
            new CachingProcessor<In, Out>(processor, Equivalences.<In>identity(), -2);
            fail("No exception thrown!!");
        } catch (IllegalArgumentException e) {
            assertEquals(BUNDLE.getMessage("processing.invalidCacheSize"), e.getMessage());
        }
    }

    @Test
    public void cachedValueIsNotProcessedTwiceButReportedTwice()
        throws ProcessingException
    {
        final Processor<In, Out> p = new CachingProcessor<In, Out>(processor,
            Equivalences.<In>identity());

        final ProcessingReport report = mock(ProcessingReport.class);

        p.process(report, input);
        p.process(report, input);

        verify(processor, only()).process(anyReport(), same(input));
        verify(report, times(2)).mergeWith(anyReport());
    }


    @Test
    public void cachedValueIsProcessedTwiceWithMaximumSizeZero()
        throws ProcessingException
    {
        final Processor<In, Out> p = new CachingProcessor<In, Out>(processor,
            Equivalences.<In>identity(), 0);

        final ProcessingReport report = mock(ProcessingReport.class);

        p.process(report, input);
        p.process(report, input);

        verify(processor, times(2)).process(anyReport(), same(input));
        verify(report, times(2)).mergeWith(anyReport());
    }

    @Test
    public void exceptionIsThrownCorrectly()
        throws ProcessingException
    {
        final Processor<In, Out> p = new CachingProcessor<In, Out>(processor,
            Equivalences.<In>identity());
        final ProcessingReport report = mock(ProcessingReport.class);
        final ProcessingException exception = new Foo();

        when(processor.process(anyReport(), any(In.class)))
            .thenThrow(exception);

        try {
            p.process(report, input);
            fail("No exception thrown!!");
        } catch (ProcessingException e) {
            assertSame(exception, e);
        }
    }

    private static final class Foo
        extends ProcessingException
    {
    }

    private interface In
        extends MessageProvider
    {
    }

    private interface Out
        extends MessageProvider
    {
    }
}
