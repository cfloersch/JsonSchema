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
 * Keyword validator for draft v3's {@code disallow}
 */
public final class DisallowKeywordValidator
    extends DraftV3TypeKeywordValidator
{
    public DisallowKeywordValidator(final JsonNode digested)
    {
        super("disallow", digested);
    }

    @Override
    public void validate(final Processor<FullData, FullData> processor,
        final ProcessingReport report, final MessageBundle bundle,
        final FullData data)
        throws ProcessingException
    {
        final JsonNode instance = data.getInstance().getNode();
        final NodeType type = NodeType.getNodeType(instance);

        if (types.contains(type)) {
            report.error(newMsg(data, bundle, "err.draftv3.disallow.type")
                .putArgument("found", type)
                .putArgument("disallowed", toArrayNode(types)));
            return;
        }

        final SchemaTree tree = data.getSchema();
        final JsonPointer schemaPointer = tree.getPointer();
        final ObjectNode fullReport = FACTORY.objectNode();

        JsonPointer ptr;
        ListProcessingReport subReport;
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

        if (nrSuccess != 0)
            report.error(newMsg(data, bundle, "err.draftv3.disallow.schema")
                .putArgument("matched", nrSuccess)
                .putArgument("nrSchemas", schemas.size())
                .put("reports", fullReport));
    }
}
