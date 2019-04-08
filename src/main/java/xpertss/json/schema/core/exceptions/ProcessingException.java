package xpertss.json.schema.core.exceptions;

import xpertss.json.schema.core.report.LogLevel;
import xpertss.json.schema.core.report.ProcessingMessage;

/**
 * Generic processing exception
 *
 * <p>Internally, this class always keeps its information in a {@link
 * ProcessingMessage}. Note that all messages see their log level set to
 * {@link LogLevel#FATAL}.</p>
 *
 * @see ProcessingMessage
 * @see LogLevel
 */
public class ProcessingException extends Exception {

    /**
     * The internal message
     */
    private final ProcessingMessage processingMessage;

    public ProcessingException()
    {
        this(new ProcessingMessage().setLogLevel(LogLevel.FATAL));
    }

    public ProcessingException(String message)
    {
        this(new ProcessingMessage().setMessage(message)
            .setLogLevel(LogLevel.FATAL));
    }

    public ProcessingException(ProcessingMessage message)
    {
        processingMessage = message.setLogLevel(LogLevel.FATAL);
    }

    public ProcessingException(String message, Throwable e)
    {
        processingMessage = new ProcessingMessage().setLogLevel(LogLevel.FATAL)
            .setMessage(message).put("exceptionClass", e.getClass().getName())
            .put("exceptionMessage", e.getMessage());
    }

    public ProcessingException(ProcessingMessage message, Throwable e)
    {
        processingMessage = message.setLogLevel(LogLevel.FATAL)
            .put("exceptionClass", e.getClass().getName())
            .put("exceptionMessage", e.getMessage());
    }

    @Override
    public final String getMessage()
    {
        return processingMessage.toString();
    }

    public final String getShortMessage()
    {
        return processingMessage.getMessage();
    }

    public final ProcessingMessage getProcessingMessage()
    {
        return processingMessage;
    }
}
