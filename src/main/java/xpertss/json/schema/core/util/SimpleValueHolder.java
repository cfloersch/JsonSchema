package xpertss.json.schema.core.util;

import com.fasterxml.jackson.databind.JsonNode;

final class SimpleValueHolder<T>
    extends ValueHolder<T>
{
    SimpleValueHolder(final String name, final T value)
    {
        super(name, value);
    }

    @Override
    protected JsonNode valueAsJson()
    {
        return FACTORY.textNode(value.getClass().getCanonicalName());
    }
}
