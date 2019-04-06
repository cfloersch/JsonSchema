package xpertss.json.schema.core.report;

import xpertss.json.schema.core.exceptions.ExceptionProvider;
import xpertss.json.schema.core.exceptions.ProcessingException;

/**
 * An {@link ExceptionProvider} providing {@link ProcessingException} instances
 */
public final class SimpleExceptionProvider
    implements ExceptionProvider
{
    private static final ExceptionProvider INSTANCE
        = new SimpleExceptionProvider();

    public static ExceptionProvider getInstance()
    {
        return INSTANCE;
    }

    private SimpleExceptionProvider()
    {
    }

    @Override
    public ProcessingException doException(final ProcessingMessage message)
    {
        return new ProcessingException(message);
    }
}
