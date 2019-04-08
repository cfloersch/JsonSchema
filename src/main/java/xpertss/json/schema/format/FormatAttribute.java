package xpertss.json.schema.format;

import com.github.fge.jackson.NodeType;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.processors.data.FullData;
import com.github.fge.msgsimple.bundle.MessageBundle;

import java.util.EnumSet;

/**
 * Interface for a format attribute validator
 */
public interface FormatAttribute {

    /**
     * Return the set of JSON Schema types this format attribute applies to
     *
     * <p>It is important that this method be implemented correctly. Remind
     * that validation for a given format attribute and an instance which type
     * is not supported always succeeds.</p>
     *
     * @return the set of supported types
     */
    EnumSet<NodeType> supportedTypes();

    /**
     * Validate the instance against this format attribute
     *
     * @param report the report to use
     * @param bundle the message bundle to use
     * @param data the validation data
     * @throws ProcessingException an exception occurs (normally, never for a
     * format attribute)
     */
    void validate(ProcessingReport report, MessageBundle bundle, FullData data)
        throws ProcessingException;
}
