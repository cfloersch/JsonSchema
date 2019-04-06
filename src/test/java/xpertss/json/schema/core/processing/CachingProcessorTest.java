package xpertss.json.schema.core.processing;

import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.messages.JsonSchemaCoreMessageBundle;
import xpertss.json.schema.core.report.MessageProvider;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.core.util.equivalence.Equivalences;
import com.github.fge.msgsimple.bundle.MessageBundle;
import com.github.fge.msgsimple.load.MessageBundles;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static xpertss.json.schema.TestUtils.*;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

public final class CachingProcessorTest
{
    private static final MessageBundle BUNDLE
        = MessageBundles.getBundle(JsonSchemaCoreMessageBundle.class);

    private In input;

    private Processor<In, Out> processor;

    @BeforeMethod
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
            assertEquals(e.getMessage(),
                BUNDLE.getMessage("processing.nullProcessor"));
        }
    }

    @Test
    public void cannotInputNullEquivalence()
    {
        try {
            new CachingProcessor<In, Out>(processor, null);
            fail("No exception thrown!!");
        } catch (NullPointerException e) {
            assertEquals(e.getMessage(),
                BUNDLE.getMessage("processing.nullEquivalence"));
        }
    }

    @Test
    public void cannotInputInvalidCacheSize()
    {
        try {
            new CachingProcessor<In, Out>(processor, Equivalences.<In>identity(), -2);
            fail("No exception thrown!!");
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(),
                BUNDLE.getMessage("processing.invalidCacheSize"));
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
            assertSame(e, exception);
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
