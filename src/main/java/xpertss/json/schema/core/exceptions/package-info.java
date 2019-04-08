/**
 * Exceptions and exception provider
 *
 * <p>This package contains checked exceptions raised by the API core. The base
 * exception is {@link
 * xpertss.json.schema.core.exceptions.ProcessingException} and all other
 * exceptions, save for unchecked exceptions, inherit it.</p>
 *
 * <p>The {@link xpertss.json.schema.core.exceptions.ExceptionProvider}
 * interface can be used by your own custom processors to set custom
 * exceptions in messages: {@link
 * xpertss.json.schema.core.report.ProcessingMessage} accepts such a provider
 * and will then return the appropriate exception when its
 * {@link xpertss.json.schema.core.report.ProcessingMessage#asException()} is
 * called.</p>
 */
package xpertss.json.schema.core.exceptions;
