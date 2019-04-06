package xpertss.json.schema.keyword.validator.draftv3;

import com.fasterxml.jackson.databind.JsonNode;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.processing.Processor;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.keyword.validator.AbstractKeywordValidator;
import xpertss.json.schema.processors.data.FullData;
import com.github.fge.msgsimple.bundle.MessageBundle;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import java.util.Set;

/**
 * Keyword validator for draft v3's {@code properties}
 */
public final class PropertiesValidator
    extends AbstractKeywordValidator
{
    private final Set<String> required;

    public PropertiesValidator(final JsonNode digest)
    {
        super("properties");
        final ImmutableSet.Builder<String> builder = ImmutableSet.builder();

        for (final JsonNode element: digest.get("required"))
            builder.add(element.textValue());

        required = builder.build();
    }

    @Override
    public void validate(final Processor<FullData, FullData> processor,
        final ProcessingReport report, final MessageBundle bundle,
        final FullData data)
        throws ProcessingException
    {
        final Set<String> set = Sets.newLinkedHashSet(required);
        set.removeAll(Sets.newHashSet(data.getInstance().getNode()
            .fieldNames()));

        if (!set.isEmpty())
            report.error(newMsg(data, bundle, "err.common.object.missingMembers")
                .put("required", required)
                .putArgument("missing", toArrayNode(set)));
    }

    @Override
    public String toString()
    {
        return "required: " + required.size() + " properties";
    }
}
