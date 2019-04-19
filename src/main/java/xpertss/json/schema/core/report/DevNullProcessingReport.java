package xpertss.json.schema.core.report;

/**
 * A processing report which logs absolutely nothing
 *
 * <p>Use this class if all you are interested in is the processing status.</p>
 */
public final class DevNullProcessingReport extends AbstractProcessingReport {

    public DevNullProcessingReport(LogLevel logLevel, LogLevel exceptionThreshold)
    {
        super(logLevel, exceptionThreshold);
    }

    public DevNullProcessingReport(LogLevel logLevel)
    {
        super(logLevel);
    }

    public DevNullProcessingReport()
    {
    }

    @Override
    public void log(LogLevel level, ProcessingMessage message)
    {
    }
}
