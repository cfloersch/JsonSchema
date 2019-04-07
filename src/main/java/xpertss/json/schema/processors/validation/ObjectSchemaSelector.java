package xpertss.json.schema.processors.validation;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.jsonpointer.JsonPointer;
import xpertss.json.schema.core.util.RegexECMA262Helper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;

/**
 * JSON Schema selector for member values of JSON Object instances
 *
 * <p>Unlike what happens with arrays, for a given member name of an instance,
 * here there can be more than one subschema which the member value must be
 * valid against.</p>
 */
public final class ObjectSchemaSelector {

    private static final JsonPointer PROPERTIES = JsonPointer.of("properties");
    private static final JsonPointer PATTERNPROPERTIES = JsonPointer.of("patternProperties");
    private static final JsonPointer ADDITIONALPROPERTIES = JsonPointer.of("additionalProperties");

    private final List<String> properties;
    private final List<String> patternProperties;
    private final boolean hasAdditional;

    public ObjectSchemaSelector(JsonNode digest)
    {
        hasAdditional = digest.get("hasAdditional").booleanValue();

        List<String> list;

        list = Lists.newArrayList();
        for (final JsonNode node: digest.get("properties"))
            list.add(node.textValue());
        properties = ImmutableList.copyOf(list);

        list = Lists.newArrayList();
        for (final JsonNode node: digest.get("patternProperties"))
            list.add(node.textValue());
        patternProperties = ImmutableList.copyOf(list);
    }

    public Iterable<JsonPointer> selectSchemas(String memberName)
    {
        List<JsonPointer> list = Lists.newArrayList();

        if (properties.contains(memberName))
            list.add(PROPERTIES.append(memberName));

        for (final String regex: patternProperties)
            if(RegexECMA262Helper.regMatch(regex, memberName))
                list.add(PATTERNPROPERTIES.append(regex));

        if (!list.isEmpty())
            return ImmutableList.copyOf(list);

        return hasAdditional
            ? ImmutableList.of(ADDITIONALPROPERTIES)
            : Collections.<JsonPointer>emptyList();
    }
}
