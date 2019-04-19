package xpertss.json.schema.core.report;

/**
 * A simple processing report printing its messages to {@code System.out}
 */
public final class ConsoleProcessingReport extends AbstractProcessingReport {

    public ConsoleProcessingReport(LogLevel logLevel, LogLevel exceptionThreshold)
    {
        super(logLevel, exceptionThreshold);
    }

    public ConsoleProcessingReport(LogLevel logLevel)
    {
        super(logLevel);
    }

    public ConsoleProcessingReport()
    {
    }

    @Override
    public void log(LogLevel level, ProcessingMessage message)
    {
        System.out.println(message);
    }
}
