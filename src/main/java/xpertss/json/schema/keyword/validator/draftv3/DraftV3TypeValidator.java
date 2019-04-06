package xpertss.json.schema.keyword.validator.draftv3;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jackson.NodeType;
import com.github.fge.jackson.jsonpointer.JsonPointer;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.processing.Processor;
import xpertss.json.schema.core.report.ListProcessingReport;
import xpertss.json.schema.core.report.LogLevel;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.core.tree.SchemaTree;
import xpertss.json.schema.keyword.validator.helpers.DraftV3TypeKeywordValidator;
import xpertss.json.schema.processors.data.FullData;
import com.github.fge.msgsimple.bundle.MessageBundle;

/**
 * Keyword validator for draft v3's {@code type}
 */
public final class DraftV3TypeValidator
    extends DraftV3TypeKeywordValidator
{
    public DraftV3TypeValidator(final JsonNode digest)
    {
        super("type", digest);
    }

    @Override
    public void validate(final Processor<FullData, FullData> processor,
        final ProcessingReport report, final MessageBundle bundle,
        final FullData data)
        throws ProcessingException
    {
        final JsonNode instance = data.getInstance().getNode();
        final NodeType type = NodeType.getNodeType(instance);

        /*
         * Check the primitive type first
         */
        final boolean primitiveOK = types.contains(type);

        if (primitiveOK)
            return;

        /*
         * If not OK, check the subschemas
         */
        final ObjectNode fullReport = FACTORY.objectNode();
        final SchemaTree tree = data.getSchema();
        final JsonPointer schemaPointer = tree.getPointer();

        ListProcessingReport subReport;
        JsonPointer ptr;
        FullData newData;
        int nrSuccess = 0;

        for (final int index: schemas) {
            subReport = new ListProcessingReport(report.getLogLevel(),
                LogLevel.FATAL);
            ptr = schemaPointer.append(JsonPointer.of(keyword, index));
            newData = data.withSchema(tree.setPointer(ptr));
            processor.process(subReport, newData);
            fullReport.put(ptr.toString(), subReport.asJson());
            if (subReport.isSuccess())
                nrSuccess++;
        }

        /*
         * If at least one matched, OK
         */
        if (nrSuccess >= 1)
            return;

        /*
         * If no, failure on both counts. We reuse anyOf's message for subschema
         * failure. Also, take care not to output an error if there wasn't any
         * primitive types...
         */
        if (!types.isEmpty())
            report.error(newMsg(data, bundle, "err.common.typeNoMatch")
                .putArgument("found", type)
                .putArgument("expected", toArrayNode(types)));

        if (!schemas.isEmpty())
            report.error(newMsg(data, bundle, "err.common.schema.noMatch")
                .putArgument("nrSchemas", schemas.size())
                .put("reports", fullReport));
    }
}
