package xpertss.json.schema.core.processing;

import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.report.MessageProvider;
import xpertss.json.schema.core.report.ProcessingMessage;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.core.util.ValueHolder;

/**
 * Processor wrapper class
 *
 * <p>This class allows to declare a {@link Processor} with "raw" types, that
 * is inputs and outputs which do not implement {@link MessageProvider}. Inputs
 * and outputs are automatically wrapped into a {@link ValueHolder}.</p>
 *
 * <p>Implementations of this class are only required to provide a name by which
 * the input and output will be identified in a processing message.</p>
 *
 * @param <IN> type of input
 * @param <OUT> type of output
 */
public abstract class RawProcessor<IN, OUT>
    implements Processor<ValueHolder<IN>, ValueHolder<OUT>>
{
    private final String inputName;
    private final String outputName;

    /**
     * Protected constructor
     *
     * @param inputName name of the input
     * @param outputName name of the output
     */
    protected RawProcessor(final String inputName, final String outputName)
    {
        this.inputName = inputName;
        this.outputName = outputName;
    }

    /**
     * Process a raw input, return a raw output
     *
     * @param report the report to use
     * @param input the raw input
     * @return the raw output
     * @throws ProcessingException processing failure
     * @see #newMessage(Object)
     */
    protected abstract OUT rawProcess(ProcessingReport report, IN input)
        throws ProcessingException;

    @Override
    public final ValueHolder<OUT> process(final ProcessingReport report,
        final ValueHolder<IN> input)
        throws ProcessingException
    {
        final IN rawInput = input.getValue();
        final OUT rawOutput = rawProcess(report, rawInput);
        return ValueHolder.hold(outputName, rawOutput);
    }

    /**
     * Create a new processing message for reporting purposes
     *
     * @param rawInput the raw input
     * @return a new message
     */
    protected final ProcessingMessage newMessage(final IN rawInput)
    {
        return new ProcessingMessage().put(inputName, rawInput);
    }
}
