package xpertss.json.schema.keyword.validator;

import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.processing.Processor;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.exceptions.InvalidInstanceException;
import xpertss.json.schema.processors.data.FullData;
import com.github.fge.msgsimple.bundle.MessageBundle;

/**
 * Interface for a keyword validator
 *
 * <p>Some keywords may have to ask the validation process to validate some
 * subschemas for them -- and in fact, some keywords, such as {@code allOf},
 * {@code not} or {@code extends}, for instance, do this exclusively.</p>
 *
 * <p>Therefore they are passed the main validator (as a {@link Processor} as
 * an argument. They take the responsibility of building the appropriate {@link
 * FullData} and calling the processor again.</p>
 */
public interface KeywordValidator
{
    /**
     * Validate the instance
     *
     * @param processor the main validation processor
     * @param report the report to use
     * @param bundle the message bundle to use
     * @param data the validation data
     * @throws InvalidInstanceException instance is invalid, and the report has
     * been configured to throw an exception instead of logging errors
     */
    void validate(final Processor<FullData, FullData> processor,
        final ProcessingReport report, final MessageBundle bundle,
        final FullData data)
        throws ProcessingException;
}
