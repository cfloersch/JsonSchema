package xpertss.json.schema.main;

import com.fasterxml.jackson.databind.JsonNode;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.processing.ProcessingResult;
import xpertss.json.schema.core.processing.Processor;
import xpertss.json.schema.core.report.ListProcessingReport;
import xpertss.json.schema.core.report.MessageProvider;
import xpertss.json.schema.core.report.ProcessingReport;

/**
 * Single-schema instance validator
 *
 * <p>This is the interface you will use the most often. It is, in essence, a {@link
 * JsonValidator} initialized with a single JSON Schema. Note however that this
 * class still retains the ability to resolve JSON References.</p>
 */
public interface JsonSchema {
    /**
     * Validate an instance and return a processing report
     *
     * @param instance the instance to validate
     * @param deepCheck validate children even if container (array, object) is
     * invalid
     * @return a processing report
     * @throws ProcessingException a processing error occurred during validation
     *
     * @see JsonValidator#validate(JsonNode, JsonNode, boolean)
     *
     * @since 2.1.8
     */
    ProcessingReport validate(JsonNode instance, boolean deepCheck)
            throws ProcessingException;

    /**
     * Validate an instance and return a processing report
     *
     * <p>This calls {@link #validate(JsonNode, boolean)} with {@code false} as
     * a second argument.</p>
     *
     * @param instance the instance to validate
     * @return a processing report
     * @throws ProcessingException a processing error occurred during validation
     */
    ProcessingReport validate(JsonNode instance)
            throws ProcessingException;

    /**
     * Validate an instance and return a processing report (unchecked version)
     *
     * <p>Unchecked validation means that conditions which would normally cause
     * the processing to stop with an exception are instead inserted into the
     * resulting report.</p>
     *
     * <p><b>Warning</b>: this means that anomalous events like an unresolvable
     * JSON Reference, or an invalid schema, are <b>masked</b>!</p>
     *
     * @param instance the instance to validate
     * @param deepCheck validate children even if container (array, object) is
     * invalid
     * @return a report (a {@link ListProcessingReport} if an exception was
     * thrown during processing)
     *
     *
     * @see ProcessingResult#uncheckedResult(Processor, ProcessingReport,
     * MessageProvider)
     * @see JsonValidator#validate(JsonNode, JsonNode, boolean)
     *
     * @since 2.1.8
     */
    ProcessingReport validateUnchecked(JsonNode instance, boolean deepCheck);
    /**
     * Validate an instance and return a processing report (unchecked version)
     *
     * <p>This calls {@link #validateUnchecked(JsonNode, boolean)} with {@code
     * false} as a third argument.</p>
     *
     * @param instance the instance to validate
     * @return a report (a {@link ListProcessingReport} if an exception was
     * thrown during processing)
     */
    ProcessingReport validateUnchecked(JsonNode instance);

    /**
     * Check whether an instance is valid against this schema
     *
     * @param instance the instance
     * @return true if the instance is valid
     * @throws ProcessingException an error occurred during processing
     */
    boolean validInstance(JsonNode instance)
            throws ProcessingException;

    /**
     * Check whether an instance is valid against this schema (unchecked
     * version)
     *
     * <p>The same warnings apply as described in {@link
     * #validateUnchecked(JsonNode)}.</p>
     *
     * @param instance the instance to validate
     * @return true if the instance is valid
     */
    boolean validInstanceUnchecked(JsonNode instance);
}
