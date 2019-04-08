package xpertss.json.schema.core.exceptions;

import xpertss.json.schema.core.ref.JsonRef;
import xpertss.json.schema.core.report.ProcessingMessage;

/**
 * Exception associated with all JSON Reference exceptions
 *
 * <p>This exception is used by {@link JsonRef} to signify errors.</p>
 *
 * @see JsonRef
 */
public final class JsonReferenceException extends ProcessingException {

    public JsonReferenceException(ProcessingMessage message)
    {
        super(message);
    }

    public JsonReferenceException(ProcessingMessage message, Throwable e)
    {
        super(message, e);
    }
}
