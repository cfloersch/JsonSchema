
/**
 * Processing report infrastructure
 *
 * <p>The core components of reporting are these three classes:</p>
 *
 * <ul>
 *     <li>{@link xpertss.json.schema.core.report.ProcessingMessage} (an
 *     individual message);</li>
 *     <li>{@link xpertss.json.schema.core.report.LogLevel} (the log level of a
 *     message);</li>
 *     <li>{@link xpertss.json.schema.core.report.ProcessingReport} (interface
 *     to a processing report).</li>
 * </ul>
 *
 * <p>The other important interface in this package is {@link
 * xpertss.json.schema.core.report.MessageProvider}: all inputs and outputs
 * of processors are required to implement it; its goal is for processors to
 * be able to grab a message template reflecting the current processing context.
 * </p>
 */
package xpertss.json.schema.core.report;
