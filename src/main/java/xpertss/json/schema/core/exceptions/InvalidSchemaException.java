package xpertss.json.schema.core.exceptions;

import xpertss.json.schema.core.report.ProcessingMessage;

/**
 * Exception thrown by the validation process when a JSON Schema is invalid
 */
public final class InvalidSchemaException extends ProcessingException {
    
    public InvalidSchemaException(ProcessingMessage message)
    {
        super(message);
    }
}
