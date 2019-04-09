package xpertss.json.schema.core.processing;

import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.report.MessageProvider;
import xpertss.json.schema.core.report.ProcessingReport;

/**
 * Main processing interface
 *
 * <p>Note that it is required that both inputs and outputs implement {@link
 * MessageProvider}: this allows a processor to grab a context-dependent
 * message to include into the report should the need arise. A {@link
 * ProcessingReport} is passed as an argument so that the processor can add
 * debug/info/warning/error messages.</p>
 *
 * <p>Ideally, processors <b>should not</b> throw unchecked exceptions.</p>
 *
 * @param <IN> input type for that processor
 * @param <OUT> output type for that processor
 */
public interface Processor<IN extends MessageProvider, OUT extends MessageProvider> {

    /**
     * Process the input
     *
     * @param report the report to use while processing
     * @param input the input for this processor
     * @return the output
     * @throws ProcessingException processing failed
     */
    OUT process(ProcessingReport report, IN input) throws ProcessingException;
}
