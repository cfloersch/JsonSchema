package xpertss.json.schema.core.report;

import xpertss.json.schema.core.processing.Processor;

/**
 * Report provider interface
 *
 * <p>This interface can be used when wrapping a {@link Processor} into another
 * class which returns a result without providing a report.</p>
 *
 * <p><a href="https://github.com/fge/json-schema-validator">
 * json-schema-validator</a> uses this, for instance, in its main validator
 * class.</p>
 */
public interface ReportProvider
{
    /**
     * Generate a new report
     *
     * @return a new report
     */
    ProcessingReport newReport();

    /**
     * Generate a new report with an adapted log level and the same exception
     * threshold
     *
     * @param logLevel the new log level
     * @return a new report
     */
    ProcessingReport newReport(final LogLevel logLevel);

    /**
     * Generate a new report with an adapted log level and exception threshold
     *
     * @param logLevel the new log level
     * @param exceptionThreshold the new exception threshold
     * @return a new report
     */
    ProcessingReport newReport(final LogLevel logLevel,
        final LogLevel exceptionThreshold);
}
