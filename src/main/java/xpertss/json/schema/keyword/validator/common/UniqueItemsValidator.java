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
import com.google.common.collect.Sets;

import java.util.Set;

/**
 * Keyword validator for {@code uniqueItems}
 *
 * @see JsonNumEquals
 */
public final class UniqueItemsValidator
    extends AbstractKeywordValidator
{
    private static final Equivalence<JsonNode> EQUIVALENCE
        = JsonNumEquals.getInstance();

    private final boolean uniqueItems;

    public UniqueItemsValidator(final JsonNode digest)
    {
        super("uniqueItems");
        uniqueItems = digest.get(keyword).booleanValue();
    }

    @Override
    public void validate(final Processor<FullData, FullData> processor,
        final ProcessingReport report, final MessageBundle bundle,
        final FullData data)
        throws ProcessingException
    {
        if (!uniqueItems)
            return;

        final Set<Equivalence.Wrapper<JsonNode>> set = Sets.newHashSet();
        final JsonNode node = data.getInstance().getNode();

        for (final JsonNode element: node)
            if (!set.add(EQUIVALENCE.wrap(element))) {
                report.error(newMsg(data, bundle,
                    "err.common.uniqueItems.duplicateElements"));
                return;
            }
    }

    @Override
    public String toString()
    {
        return keyword + ": " + uniqueItems;
    }
}
