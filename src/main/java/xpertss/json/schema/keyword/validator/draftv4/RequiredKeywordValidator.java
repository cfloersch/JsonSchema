package xpertss.json.schema.keyword.validator.draftv4;

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
 * Keyword validator for draft v4's {@code required}
 */
public final class RequiredKeywordValidator extends AbstractKeywordValidator {

    private final Set<String> required;

    public RequiredKeywordValidator(JsonNode digest)
    {
        super("required");
        ImmutableSet.Builder<String> builder = ImmutableSet.builder();

        for (final JsonNode element: digest.get(keyword))
            builder.add(element.textValue());

        required = builder.build();
    }

    @Override
    public void validate(Processor<FullData, FullData> processor, ProcessingReport report,
                         MessageBundle bundle, FullData data)
        throws ProcessingException
    {
        Set<String> set = Sets.newLinkedHashSet(required);
        set.removeAll(Sets.newHashSet(data.getInstance().getNode().fieldNames()));

        if (!set.isEmpty())
            report.error(newMsg(data, bundle, "err.common.object.missingMembers")
                .put("required", required)
                .putArgument("missing", toArrayNode(set)));
    }

    @Override
    public String toString()
    {
        return keyword + ": " + required.size() + " properties";
    }
}
