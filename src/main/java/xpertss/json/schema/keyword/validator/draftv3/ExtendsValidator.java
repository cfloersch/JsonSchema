package xpertss.json.schema.keyword.validator.draftv3;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.jsonpointer.JsonPointer;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.processing.Processor;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.core.tree.SchemaTree;
import xpertss.json.schema.keyword.validator.AbstractKeywordValidator;
import xpertss.json.schema.processors.data.FullData;
import com.github.fge.msgsimple.bundle.MessageBundle;

/**
 * Keyword validator for draft v3's {@code extends}
 */
public final class ExtendsValidator
    extends AbstractKeywordValidator
{
    public ExtendsValidator(final JsonNode digest)
    {
        super("extends");
    }

    @Override
    public void validate(final Processor<FullData, FullData> processor,
        final ProcessingReport report, final MessageBundle bundle,
        final FullData data)
        throws ProcessingException
    {
        final SchemaTree tree = data.getSchema();
        final JsonNode node = tree.getNode().get(keyword);

        FullData newData;

        if (node.isObject()) {
            newData = data.withSchema(tree.append(JsonPointer.of(keyword)));
            processor.process(report, newData);
            return;
        }

        /*
         * Not an object? An array
         */
        final int size = node.size();
        JsonPointer pointer;

        for (int index = 0; index < size; index++) {
            pointer = JsonPointer.of(keyword, index);
            newData = data.withSchema(tree.append(pointer));
            processor.process(report, newData);
        }
    }

    @Override
    public String toString()
    {
        return keyword;
    }

}
