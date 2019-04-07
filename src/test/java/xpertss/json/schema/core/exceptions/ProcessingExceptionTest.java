package xpertss.json.schema.core.exceptions;

import org.junit.Test;
import xpertss.json.schema.core.report.LogLevel;
import xpertss.json.schema.core.report.ProcessingMessage;

import static org.junit.Assert.assertEquals;
import static xpertss.json.schema.matchers.ProcessingMessageAssert.*;

public final class ProcessingExceptionTest
{
    private static final String FOO = "foo";

    @Test
    public void thrownProcessingMessagesHaveLevelFatal()
    {
        final ProcessingMessage message = new ProcessingMessage();
        new ProcessingException(message);
        assertMessage(message).hasLevel(LogLevel.FATAL);
    }

    @Test
    public void processingExceptionMessageIsSameAsProcessingMessage()
    {
        final ProcessingMessage message = new ProcessingMessage()
            .setMessage(FOO);
        final ProcessingException exception = new ProcessingException(message);
        assertEquals(exception.getMessage(), message.toString());
    }

    @Test
    public void innerExceptionClassAndMessageAreReported()
    {
        final Exception inner = new Foo(FOO);
        final ProcessingException exception
            = new ProcessingException("", inner);
        final ProcessingMessage message = exception.getProcessingMessage();
        assertMessage(message).hasField("exceptionClass", Foo.class.getName())
            .hasField("exceptionMessage", inner.getMessage());
    }

    private static class Foo
        extends Exception
    {
        private Foo(final String message)
        {
            super(message);
        }
    }
}
