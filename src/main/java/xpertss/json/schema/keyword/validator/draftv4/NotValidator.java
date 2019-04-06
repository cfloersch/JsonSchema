package xpertss.json.schema.keyword.validator.draftv4;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.jsonpointer.JsonPointer;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.processing.Processor;
import xpertss.json.schema.core.report.ListProcessingReport;
import xpertss.json.schema.core.report.LogLevel;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.core.tree.SchemaTree;
import xpertss.json.schema.keyword.validator.AbstractKeywordValidator;
import xpertss.json.schema.processors.data.FullData;
import com.github.fge.msgsimple.bundle.MessageBundle;

/**
 * Keyword validator for draft v4's {@code not}
 */
public final class NotValidator
    extends AbstractKeywordValidator
{
    private static final JsonPointer PTR = JsonPointer.of("not");

    public NotValidator(final JsonNode digest)
    {
        super("not");
    }

    @Override
    public void validate(final Processor<FullData, FullData> processor,
        final ProcessingReport report, final MessageBundle bundle,
        final FullData data)
        throws ProcessingException
    {
        final SchemaTree tree = data.getSchema();
        final ProcessingReport subReport
            = new ListProcessingReport(report.getLogLevel(), LogLevel.FATAL);

        processor.process(subReport, data.withSchema(tree.append(PTR)));

        if (subReport.isSuccess())
            report.error(newMsg(data, bundle, "err.draftv4.not.fail"));
    }

    @Override
    public String toString()
    {
        return "must not match subschema";
    }
}
