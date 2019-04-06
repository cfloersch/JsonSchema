package xpertss.json.schema.keyword.validator.common;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JacksonUtils;
import com.github.fge.jackson.jsonpointer.JsonPointer;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.processing.Processor;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.core.tree.SchemaTree;
import xpertss.json.schema.keyword.validator.AbstractKeywordValidator;
import xpertss.json.schema.processors.data.FullData;
import com.github.fge.msgsimple.bundle.MessageBundle;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/*
 * In spite of syntax differences, the digested data is the same, which is why
 * this validator is in common/
 */
/**
 * Keyword validator for draft v4 and v3 {@code dependencies}
 *
 * <p>In spite of syntax differences, the digested data used to build the
 * validator is the same, which is why this validator is located here.</p>
 */
public final class DependenciesValidator
    extends AbstractKeywordValidator
{
    private final Multimap<String, String> propertyDeps;
    private final Set<String> schemaDeps;

    public DependenciesValidator(final JsonNode digest)
    {
        super("dependencies");

        /*
         * Property dependencies
         */
        final ImmutableMultimap.Builder<String, String> mapBuilder
            = ImmutableMultimap.builder();
        final Map<String, JsonNode> map
            = JacksonUtils.asMap(digest.get("propertyDeps"));

        String key;
        for (final Map.Entry<String, JsonNode> entry: map.entrySet()) {
            key = entry.getKey();
            for (final JsonNode element: entry.getValue())
                mapBuilder.put(key, element.textValue());
        }

        propertyDeps = mapBuilder.build();

        /*
         * Schema dependencies
         */
        final ImmutableSet.Builder<String> setBuilder
            = ImmutableSet.builder();

        for (final JsonNode node: digest.get("schemaDeps"))
            setBuilder.add(node.textValue());

        schemaDeps = setBuilder.build();
    }

    @Override
    public void validate(final Processor<FullData, FullData> processor,
        final ProcessingReport report, final MessageBundle bundle,
        final FullData data)
        throws ProcessingException
    {
        final JsonNode instance = data.getInstance().getNode();
        final Set<String> fields = Sets.newHashSet(instance.fieldNames());

        Collection<String> collection;
        Set<String> set;

        for (final String field: propertyDeps.keySet()) {
            if (!fields.contains(field))
                continue;
            collection = propertyDeps.get(field);
            set = Sets.newLinkedHashSet(collection);
            set.removeAll(fields);
            if (!set.isEmpty())
                report.error(newMsg(data, bundle,
                    "err.common.dependencies.missingPropertyDeps")
                    .putArgument("property", field)
                    .putArgument("required", toArrayNode(collection))
                    .putArgument("missing", toArrayNode(set)));
        }

        if (schemaDeps.isEmpty())
            return;

        final SchemaTree tree = data.getSchema();
        FullData newData;
        JsonPointer pointer;

        for (final String field: schemaDeps) {
            if (!fields.contains(field))
                continue;
            pointer = JsonPointer.of(keyword, field);
            newData = data.withSchema(tree.append(pointer));
            processor.process(report, newData);
        }
    }

    @Override
    public String toString()
    {
        return keyword + ": " + propertyDeps.size() + " property dependencies, "
            + schemaDeps.size() + " schema dependencies";
    }
}
