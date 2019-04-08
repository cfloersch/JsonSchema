package xpertss.json.schema.core.exceptions;

import xpertss.json.schema.core.report.ProcessingMessage;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.core.report.SimpleExceptionProvider;

/**
 * An exception provider for a {@link ProcessingMessage}
 *
 * <p>The main use of this interface is in processing messages themselves:
 * {@link ProcessingReport}, for instance, uses a message's {@link
 * ProcessingMessage#asException()} method to throw the exception associated
 * with that message. The latter method just returns the result of {@link
 * #doException(ProcessingMessage)} with {@code this} as an argument.</p>
 *
 * @see SimpleExceptionProvider
 * @see ProcessingMessage
 * @see ProcessingReport
 */
public interface ExceptionProvider {
    
    /**
     * Return an exception associated with a message
     *
     * @param message the message
     * @return the appropriate exception
     */
    ProcessingException doException(ProcessingMessage message);
}
