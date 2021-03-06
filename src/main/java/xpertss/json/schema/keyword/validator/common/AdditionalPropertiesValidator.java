package xpertss.json.schema.keyword.validator.common;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.github.fge.jackson.JacksonUtils;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.processing.Processor;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.core.util.RegexECMA262Helper;
import xpertss.json.schema.keyword.validator.AbstractKeywordValidator;
import xpertss.json.schema.processors.data.FullData;
import com.github.fge.msgsimple.bundle.MessageBundle;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;

import java.util.Set;

/**
 * Keyword validator for {@code additionalProperties}
 */
public final class AdditionalPropertiesValidator extends AbstractKeywordValidator {

    private static final Joiner TOSTRING_JOINER = Joiner.on("; or ");

    private final boolean additionalOK;
    private final Set<String> properties;
    private final Set<String> patternProperties;

    public AdditionalPropertiesValidator(JsonNode digest)
    {
        super("additionalProperties");
        additionalOK = digest.get(keyword).booleanValue();

        ImmutableSet.Builder<String> builder;

        builder = ImmutableSet.builder();
        for (final JsonNode node: digest.get("properties"))
            builder.add(node.textValue());
        properties = builder.build();

        builder = ImmutableSet.builder();
        for (final JsonNode node: digest.get("patternProperties"))
            builder.add(node.textValue());
        patternProperties = builder.build();
    }

    @Override
    public void validate(Processor<FullData, FullData> processor, ProcessingReport report,
                         MessageBundle bundle, FullData data)
        throws ProcessingException
    {
        if (additionalOK)
            return;

        JsonNode instance = data.getInstance().getNode();
        Set<String> fields = Sets.newHashSet(instance.fieldNames());

        fields.removeAll(properties);

        Set<String> tmp = Sets.newHashSet();

        for (final String field: fields)
            for (final String regex: patternProperties)
                if (RegexECMA262Helper.regMatch(regex, field))
                    tmp.add(field);

        fields.removeAll(tmp);

        if (fields.isEmpty())
            return;

        /*
         * Display extra properties in order in the report
         */
        ArrayNode node = JacksonUtils.nodeFactory().arrayNode();
        for (final String field: Ordering.natural().sortedCopy(fields))
            node.add(field);
        report.error(newMsg(data, bundle,
            "err.common.additionalProperties.notAllowed")
            .putArgument("unwanted", node));
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder(keyword + ": ");

        if (additionalOK)
            return sb.append("allowed").toString();

        sb.append("none");

        if (properties.isEmpty() && patternProperties.isEmpty())
            return sb.toString();

        sb.append(", unless: ");

        Set<String> further = Sets.newLinkedHashSet();

        if (!properties.isEmpty())
            further.add("one property is any of: " + properties);

        if (!patternProperties.isEmpty())
            further.add("a property matches any regex among: " + patternProperties);

        sb.append(TOSTRING_JOINER.join(further));

        return sb.toString();
    }
}
