package xpertss.json.schema.processors.format;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.NodeType;
import xpertss.json.schema.cfg.ValidationConfiguration;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.processing.Processor;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.core.util.Dictionary;
import xpertss.json.schema.format.FormatAttribute;
import xpertss.json.schema.keyword.validator.KeywordValidator;
import xpertss.json.schema.library.Library;
import xpertss.json.schema.messages.JsonSchemaValidationBundle;
import xpertss.json.schema.processors.build.ValidatorBuilder;
import xpertss.json.schema.processors.data.SchemaContext;
import xpertss.json.schema.processors.data.ValidatorList;
import com.github.fge.msgsimple.bundle.MessageBundle;
import com.github.fge.msgsimple.load.MessageBundles;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;

/**
 * Format attribute handler
 *
 * <p>This processor is run after {@link ValidatorBuilder} if and only if the
 * user has chosen to perform {@code format} validation (it is enabled by
 * default).</p>
 *
 * <p>It will append a specific {@link KeywordValidator} to the list of already
 * existing validators if and only if:</p>
 *
 * <ul>
 *     <li>there is a {@code format} keyword in the current schema;</li>
 *     <li>the specified format attribute is supported;</li>
 *     <li>the instance type is supported by this format attribute.</li>
 * </ul>
 *
 * <p>Note that it will warn if the format attribute is not recognized.</p>
 */
public final class FormatProcessor implements Processor<ValidatorList, ValidatorList> {

    private final Map<String, FormatAttribute> attributes;
    private final MessageBundle bundle;

    public FormatProcessor(Library library, ValidationConfiguration cfg)
    {
        attributes = library.getFormatAttributes().entries();
        bundle = cfg.getValidationMessages();
    }

    @VisibleForTesting
    FormatProcessor(Dictionary<FormatAttribute> dict)
    {
        attributes = dict.entries();
        bundle = MessageBundles.getBundle(JsonSchemaValidationBundle.class);
    }

    @Override
    public ValidatorList process(ProcessingReport report, ValidatorList input)
        throws ProcessingException
    {
        SchemaContext context = input.getContext();
        JsonNode node = context.getSchema().getNode().get("format");

        if (node == null)
            return input;

        String fmt = node.textValue();
        FormatAttribute attr = attributes.get(fmt);

        if (attr == null) {
            report.warn(input.newMessage().put("domain", "validation")
                .put("keyword", "format")
                .setMessage(bundle.getMessage("warn.format.notSupported"))
                .putArgument("attribute", fmt));
            return input;
        }

        NodeType type = context.getInstanceType();

        if (!attr.supportedTypes().contains(type))
            return input;

        List<KeywordValidator> validators = Lists.newArrayList(input);
        validators.add(formatValidator(attr));

        return new ValidatorList(context, validators);
    }

    private static KeywordValidator formatValidator(FormatAttribute attr)
    {
        return (processor, report, bundle, data) -> attr.validate(report, bundle, data);
    }

    @Override
    public String toString()
    {
        return "format attribute handler";
    }
}
