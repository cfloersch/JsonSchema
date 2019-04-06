package xpertss.json.schema.processors.validation;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.jsonpointer.JsonPointer;
import com.google.common.collect.ImmutableList;

import java.util.Collections;

/**
 * JSON Schema subschema selector for array instances
 *
 * <p>Its role is to select which subschemas apply to a given array index of an
 * instance, given a digest built by {@link ArraySchemaDigester}.</p>
 *
 * <p>It may happen that no schemas apply at all (in which case the document at
 * the given array index will always validate successfully).</p>
 */
public final class ArraySchemaSelector
{
    private static final JsonPointer ITEMS = JsonPointer.of("items");
    private static final JsonPointer ADDITIONAL_ITEMS
        = JsonPointer.of("additionalItems");

    private final boolean hasItems;
    private final boolean itemsIsArray;
    private final int itemsSize;
    private final boolean hasAdditional;

    public ArraySchemaSelector(final JsonNode digest)
    {
        hasItems = digest.get("hasItems").booleanValue();
        itemsIsArray = digest.get("itemsIsArray").booleanValue();
        itemsSize = digest.get("itemsSize").intValue();
        hasAdditional = digest.get("hasAdditional").booleanValue();
    }

    public Iterable<JsonPointer> selectSchemas(final int index)
    {
        if (!hasItems)
            return hasAdditional
                ? ImmutableList.of(ADDITIONAL_ITEMS)
                : Collections.<JsonPointer>emptyList();

        if (!itemsIsArray)
            return ImmutableList.of(ITEMS);

        if (index < itemsSize)
            return ImmutableList.of(ITEMS.append(index));

        return hasAdditional
            ? ImmutableList.of(ADDITIONAL_ITEMS)
            : Collections.<JsonPointer>emptyList();
    }
}
