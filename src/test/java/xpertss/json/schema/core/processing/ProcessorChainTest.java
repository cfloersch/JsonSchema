package xpertss.json.schema.core.processing;

import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.messages.JsonSchemaCoreMessageBundle;
import xpertss.json.schema.core.report.AbstractProcessingReport;
import xpertss.json.schema.core.report.LogLevel;
import xpertss.json.schema.core.report.MessageProvider;
import xpertss.json.schema.core.report.ProcessingMessage;
import xpertss.json.schema.core.report.ProcessingReport;
import com.github.fge.msgsimple.bundle.MessageBundle;
import com.github.fge.msgsimple.load.MessageBundles;
import org.testng.annotations.Test;

import static xpertss.json.schema.matchers.ProcessingMessageAssert.*;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

public final class ProcessorChainTest
{
    private static final MessageBundle BUNDLE
        = MessageBundles.getBundle(JsonSchemaCoreMessageBundle.class);

    @Test
    public void cannotInitiateWithNullProcessor()
    {
        try {
            ProcessorChain.startWith(null);
            fail("No exception thrown!!");
        } catch (NullPointerException e) {
            assertEquals(e.getMessage(),
                BUNDLE.getMessage("processing.nullProcessor"));
        }
    }

    @Test
    public void cannotChainWithNullProcessor()
    {
        @SuppressWarnings("unchecked")
        final Processor<MessageProvider, MessageProvider> p
            = mock(Processor.class);
        try {
            ProcessorChain.startWith(p).chainWith(null);
            fail("No exception thrown!!");
        } catch (NullPointerException e) {
            assertEquals(e.getMessage(),
                BUNDLE.getMessage("processing.nullProcessor"));
        }
    }

    @Test
    public void failingOnErrorExitsEarly()
        throws ProcessingException
    {
        @SuppressWarnings("unchecked")
        final Processor<MessageProvider, MessageProvider> p1
            = mock(Processor.class);
        @SuppressWarnings("unchecked")
        final Processor<MessageProvider, MessageProvider> p2
            = mock(Processor.class);

        final Processor<MessageProvider, MessageProvider> processor
            = ProcessorChain.startWith(p1).failOnError().chainWith(p2)
                .getProcessor();

        final MessageProvider input = mock(MessageProvider.class);
        final ProcessingReport report = new DummyReport(LogLevel.ERROR);

        try {
            processor.process(report, input);
            fail("No exception thrown!!");
        } catch (ProcessingException e) {
            assertMessage(e.getProcessingMessage())
                .hasMessage(BUNDLE.getMessage("processing.chainStopped"));
        }

        verify(p1).process(same(report), any(MessageProvider.class));
        verify(p2, never()).process(any(ProcessingReport.class),
            any(MessageProvider.class));
    }

    @Test
    public void noFailureDoesNotTriggerEarlyExit()
        throws ProcessingException
    {
        @SuppressWarnings("unchecked")
        final Processor<MessageProvider, MessageProvider> p1
            = mock(Processor.class);
        @SuppressWarnings("unchecked")
        final Processor<MessageProvider, MessageProvider> p2
            = mock(Processor.class);

        final Processor<MessageProvider, MessageProvider> processor
            = ProcessorChain.startWith(p1).failOnError().chainWith(p2)
                .getProcessor();

        final MessageProvider input = mock(MessageProvider.class);
        final ProcessingReport report = new DummyReport(LogLevel.DEBUG);

        when(p1.process(report, input)).thenReturn(input);

        processor.process(report, input);

        verify(p1).process(same(report), any(MessageProvider.class));
        verify(p2).process(same(report), any(MessageProvider.class));
    }

    private static final class DummyReport
        extends AbstractProcessingReport
    {
        private DummyReport(final LogLevel currentLevel)
            throws ProcessingException
        {
            dispatch(new ProcessingMessage().setLogLevel(currentLevel));
        }

        @Override
        public void log(final LogLevel level, final ProcessingMessage message)
        {
        }
    }
}
