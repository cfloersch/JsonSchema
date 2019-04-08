package xpertss.json.schema.core.util;

import com.github.fge.Frozen;
import com.google.common.collect.ImmutableMap;

import javax.annotation.concurrent.Immutable;
import java.util.Map;

/**
 * A dictionary
 *
 * <p>This class is a wrapper over a {@link Map}, where keys are always {@link
 * String}s. The type of values is arbitrary.</p>
 *
 * <p>This class is <b>immutable</b>. If you want to build a dictionary, you
 * have two options:</p>
 *
 * <ul>
 *     <li>create a new builder using {@link #newBuilder()};</li>
 *     <li>create a builder with all elements from an existing dictionary using
 *     {@link #thaw()}.</li>
 * </ul>
 *
 * <p>For instance:</p>
 *
 * <pre>
 *     // New builder
 *     final DictionaryBuilder&lt;Foo&gt; builder = Dictionary.newBuilder();
 *     // From an existing dictionary
 *     final DictionaryBuilder&lt;Foo&gt; builder = dict.thaw();
 * </pre>
 *
 * @param <T> the type of values for this dictionary
 *
 * @see DictionaryBuilder
 */
@Immutable
public final class Dictionary<T> implements Frozen<DictionaryBuilder<T>> {
    /**
     * Entries of this dictionary
     *
     * <p>This map is <b>immutable</b>.</p>
     *
     * @see ImmutableMap
     */
    final Map<String, T> entries;

    /**
     * Return a new, empty builder for a dictionary of this type
     *
     * @param <T> the type of values
     * @return a new, empty builder
     */
    public static <T> DictionaryBuilder<T> newBuilder()
    {
        return new DictionaryBuilder<T>();
    }

    /**
     * Package local constructor to generate a dictionary from a builder
     *
     * @param builder the builder
     * @see DictionaryBuilder#freeze()
     */
    Dictionary(DictionaryBuilder<T> builder)
    {
        entries = ImmutableMap.copyOf(builder.entries);
    }

    /**
     * Return the entries from this dictionary as a map
     *
     * <p>The returned map is <b>immutable</b>.</p>
     *
     * @return an immutable map of entries
     * @see ImmutableMap
     */
    public Map<String, T> entries()
    {
        return entries;
    }

    /**
     * Return a builder with a copy of all entries from this dictionary
     *
     * @return a {@link DictionaryBuilder}
     */
    @Override
    public DictionaryBuilder<T> thaw()
    {
        return new DictionaryBuilder<T>(this);
    }
}
