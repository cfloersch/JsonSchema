package xpertss.json.schema.keyword.validator.draftv6;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonNumEquals;
import com.github.fge.msgsimple.bundle.MessageBundle;
import com.google.common.base.Equivalence;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.processing.Processor;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.keyword.validator.AbstractKeywordValidator;
import xpertss.json.schema.processors.data.FullData;

/**
 * Keyword validator for {@code const}
 *
 * @see JsonNumEquals
 */
public final class ConstValidator extends AbstractKeywordValidator {

    private static final Equivalence<JsonNode> EQUIVALENCE = JsonNumEquals.getInstance();

    private final JsonNode values;

    public ConstValidator(JsonNode digest)
    {
        super("const");
        values = digest.get(keyword);
    }

    @Override
    public void validate(Processor<FullData, FullData> processor, ProcessingReport report,
                         MessageBundle bundle, FullData data)
        throws ProcessingException
    {
        JsonNode node = data.getInstance().getNode();

        String constant = data.getSchema().getNode().get(keyword).textValue();
        String value = data.getInstance().getNode().textValue();

        if(constant.equals(value)) return;

        // TODO Need new error
        report.error(newMsg(data, bundle, "err.common.enum.notInEnum")
            .putArgument("value", node).putArgument(keyword, values));
    }

    @Override
    public String toString()
    {
        return keyword + '(' + values.size() + " possible values)";
    }
}
