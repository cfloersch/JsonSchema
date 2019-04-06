package xpertss.json.schema.keyword.validator.draftv4;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jackson.jsonpointer.JsonPointer;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.processing.Processor;
import xpertss.json.schema.core.report.ListProcessingReport;
import xpertss.json.schema.core.report.LogLevel;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.core.tree.SchemaTree;
import xpertss.json.schema.keyword.validator.helpers.SchemaArrayValidator;
import xpertss.json.schema.processors.data.FullData;
import com.github.fge.msgsimple.bundle.MessageBundle;

/**
 * Keyword validator for draft v4's {@code oneOf}
 */
public final class OneOfValidator
    extends SchemaArrayValidator
{
    public OneOfValidator(final JsonNode digest)
    {
        super("oneOf");
    }

    @Override
    public void validate(final Processor<FullData, FullData> processor,
        final ProcessingReport report, final MessageBundle bundle,
        final FullData data)
        throws ProcessingException
    {
        final SchemaTree tree = data.getSchema();
        final JsonPointer schemaPointer = tree.getPointer();
        final JsonNode schemas = tree.getNode().get(keyword);
        final int size = schemas.size();
        final ObjectNode fullReport = FACTORY.objectNode();

        int nrSuccess = 0;
        ListProcessingReport subReport;
        JsonPointer ptr;
        FullData newData;

        for (int index = 0; index < size; index++) {
            subReport = new ListProcessingReport(report.getLogLevel(),
                LogLevel.FATAL);
            ptr = schemaPointer.append(JsonPointer.of(keyword, index));
            newData = data.withSchema(tree.setPointer(ptr));
            processor.process(subReport, newData);
            fullReport.put(ptr.toString(), subReport.asJson());
            if (subReport.isSuccess())
                nrSuccess++;
        }

        if (nrSuccess != 1)
            report.error(newMsg(data, bundle, "err.draftv4.oneOf.fail")
                .putArgument("matched", nrSuccess)
                .putArgument("nrSchemas", size)
                .put("reports", fullReport));
    }
}
