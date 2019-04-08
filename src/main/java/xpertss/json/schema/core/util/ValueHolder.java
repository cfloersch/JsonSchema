package xpertss.json.schema.core.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.github.fge.jackson.JacksonUtils;
import xpertss.json.schema.core.processing.Processor;
import xpertss.json.schema.core.report.MessageProvider;
import xpertss.json.schema.core.report.ProcessingMessage;

import javax.annotation.concurrent.Immutable;

/**
 * A wrapper over an arbitrary type to be used by processors
 *
 * <p>Since all inputs and outputs of a {@link Processor} need to implement
 * {@link MessageProvider}, this abstract class helps to wrap values and
 * implement this interface at the same time.</p>
 *
 * <p>Implementations need only implement the {@link #valueAsJson()} method.</p>
 *
 * @param <T> the type of the value
 */
@Immutable
public abstract class ValueHolder<T> implements MessageProvider {

    protected static final JsonNodeFactory FACTORY = JacksonUtils.nodeFactory();

    private final String name;
    protected final T value;

    public static <V> ValueHolder<V> hold(V value)
    {
        return new SimpleValueHolder<V>("value", value);
    }

    public static <V> ValueHolder<V> hold(String name, V value)
    {
        return new SimpleValueHolder<V>(name, value);
    }

    public static <V extends AsJson> ValueHolder<V> hold(V value)
    {
        return new AsJsonValueHolder<V>("value", value);
    }

    public static <V extends AsJson> ValueHolder<V> hold(String name, V value)
    {
        return new AsJsonValueHolder<V>(name, value);
    }

    /**
     * Protected constructor
     *
     * @param name the name to prefix the value with
     * @param value the value
     */
    protected ValueHolder(String name, T value)
    {
        this.name = name;
        this.value = value;
    }

    /**
     * Return a JSON representation of the value
     *
     * @return a {@link JsonNode}
     */
    protected abstract JsonNode valueAsJson();

    /**
     * Return the declared name for this value
     *
     * @return the declared name
     */
    public final String getName()
    {
        return name;
    }

    /**
     * Get the value wrapped in the instance
     *
     * @return the value
     */
    public final T getValue()
    {
        return value;
    }

    /**
     * Create a new processing message template depending on the stored value
     *
     * @return a new {@link ProcessingMessage}
     */
    @Override
    public final ProcessingMessage newMessage()
    {
        return new ProcessingMessage().put(name, valueAsJson());
    }
}
