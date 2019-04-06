package xpertss.json.schema.core.report;

/**
 * Base implementation of a {@link ReportProvider}
 *
 * <p>This base implementation takes a log level and exception threshold, and
 * generates a new processing report according to these parameters.</p>
 */
public abstract class AbstractReportProvider
    implements ReportProvider
{
    protected final LogLevel logLevel;
    protected final LogLevel exceptionThreshold;

    /**
     * Protected constructor
     *
     * @param logLevel the log level to use when generating a new report
     * @param exceptionThreshold the exception threshold to use
     */
    protected AbstractReportProvider(final LogLevel logLevel,
        final LogLevel exceptionThreshold)
    {
        this.logLevel = logLevel;
        this.exceptionThreshold = exceptionThreshold;
    }
}
