package xpertss.json.schema.keyword.validator.common;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonNumEquals;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.processing.Processor;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.keyword.validator.AbstractKeywordValidator;
import xpertss.json.schema.processors.data.FullData;
import com.github.fge.msgsimple.bundle.MessageBundle;
import com.google.common.base.Equivalence;

/**
 * Keyword validator for {@code enum}
 *
 * @see JsonNumEquals
 */
public final class EnumValidator
    extends AbstractKeywordValidator
{
    private static final Equivalence<JsonNode> EQUIVALENCE
        = JsonNumEquals.getInstance();

    private final JsonNode values;

    public EnumValidator(final JsonNode digest)
    {
        super("enum");

        values = digest.get(keyword);
    }

    @Override
    public void validate(final Processor<FullData, FullData> processor,
        final ProcessingReport report, final MessageBundle bundle,
        final FullData data)
        throws ProcessingException
    {
        final JsonNode node = data.getInstance().getNode();

        for (final JsonNode enumValue: values)
            if (EQUIVALENCE.equivalent(enumValue, node))
                return;

        report.error(newMsg(data, bundle, "err.common.enum.notInEnum")
            .putArgument("value", node).putArgument(keyword, values));
    }

    @Override
    public String toString()
    {
        return keyword + '(' + values.size() + " possible values)";
    }
}
