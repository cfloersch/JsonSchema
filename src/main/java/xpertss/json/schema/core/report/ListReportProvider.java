package xpertss.json.schema.core.report;

/**
 * A report provider providing {@link ListProcessingReport} instances
 */
public final class ListReportProvider
    extends AbstractReportProvider
{
    public ListReportProvider(final LogLevel logLevel,
        final LogLevel exceptionThreshold)
    {
        super(logLevel, exceptionThreshold);
    }

    @Override
    public ProcessingReport newReport()
    {
        return new ListProcessingReport(logLevel, exceptionThreshold);
    }

    @Override
    public ProcessingReport newReport(final LogLevel logLevel)
    {
        return new ListProcessingReport(logLevel);
    }

    @Override
    public ProcessingReport newReport(final LogLevel logLevel,
        final LogLevel exceptionThreshold)
    {
        return new ListProcessingReport(logLevel, exceptionThreshold);
    }
}
