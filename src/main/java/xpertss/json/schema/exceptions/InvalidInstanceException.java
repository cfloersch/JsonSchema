package xpertss.json.schema.exceptions;

import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.report.ProcessingMessage;

/**
 * Exception thrown by the validation process when an instance is invalid
 */
public final class InvalidInstanceException extends ProcessingException {
    
    public InvalidInstanceException(final ProcessingMessage message)
    {
        super(message);
    }
}
