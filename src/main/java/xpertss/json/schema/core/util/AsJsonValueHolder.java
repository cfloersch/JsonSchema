package xpertss.json.schema.core.util;

import com.fasterxml.jackson.databind.JsonNode;

import javax.annotation.concurrent.Immutable;

/**
 * A specialized {@link ValueHolder} for values implementing {@link AsJson}
 *
 * @param <T> the type of the value
 */
@Immutable
final class AsJsonValueHolder<T extends AsJson>
    extends ValueHolder<T>
{
    AsJsonValueHolder(final String name, final T value)
    {
        super(name, value);
    }

    @Override
    protected JsonNode valueAsJson()
    {
        return value.asJson();
    }
}
