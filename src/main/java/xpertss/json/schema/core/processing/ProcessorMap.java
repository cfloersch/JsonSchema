package xpertss.json.schema.core.processing;

import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.messages.JsonSchemaCoreMessageBundle;
import xpertss.json.schema.core.report.MessageProvider;
import xpertss.json.schema.core.report.ProcessingMessage;
import xpertss.json.schema.core.report.ProcessingReport;
import com.github.fge.msgsimple.bundle.MessageBundle;
import com.github.fge.msgsimple.load.MessageBundles;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * {@link Map}-based processor selector, with an optional default processor
 *
 * <p>The processor produced by this class works as follows:</p>
 *
 * <ul>
 *     <li>a key, of type {@code K}, is computed from the processor input, of
 *     type {@code IN}, using a {@link Function};</li>
 *     <li>the processor then looks up this key in a {@link Map}, whose values
 *     are {@link Processor}s;</li>
 *     <li>if the key exists, the appropriate procesor is executed; otherwise,
 *     the default action is performed.</li>
 * </ul>
 *
 * <p>The default action depends on whether a default processor has been
 * supplied: if none exists, a {@link ProcessingException} is thrown.</p>
 *
 * <p>The {@link Function} used to extract a key from an input is the only
 * argument of the constructor. It cannot be null.</p>
 *
 * <p>Note that <b>null keys are not allowed</b>.</p>
 *
 * @param <K> the type of keys in the map
 * @param <IN> the input type of processors
 * @param <OUT> the output type of processors
 */
public final class ProcessorMap<K, IN extends MessageProvider, OUT extends MessageProvider> {

    private static final MessageBundle BUNDLE = MessageBundles.getBundle(JsonSchemaCoreMessageBundle.class);

    private final Function<IN, K> keyFunction;
    /**
     * The map of processors
     */
    private final Map<K, Processor<IN, OUT>> processors = Maps.newHashMap();

    /**
     * The default processor
     */
    private Processor<IN, OUT> defaultProcessor = null;

    /**
     * Constructor
     *
     * @param keyFunction function to extract a key from an input
     * @throws NullPointerException key function is null
     */
    public ProcessorMap(Function<IN, K> keyFunction)
    {
        BUNDLE.checkNotNull(keyFunction, "processing.nullFunction");
        this.keyFunction = keyFunction;
    }

    /**
     * Add an entry to the processor map
     *
     * @param key the key to match against
     * @param processor the processor for that key
     * @return this
     * @throws NullPointerException either the key or the processor are null
     */
    public ProcessorMap<K, IN, OUT> addEntry(K key, Processor<IN, OUT> processor)
    {
        BUNDLE.checkNotNull(key, "processing.nullKey");
        BUNDLE.checkNotNull(processor, "processing.nullProcessor");
        processors.put(key, processor);
        return this;
    }

    /**
     * Set the default processor if no matching key is found
     *
     * @param defaultProcessor the default processor
     * @return this
     * @throws NullPointerException processor is null
     */
    public ProcessorMap<K, IN, OUT> setDefaultProcessor(Processor<IN, OUT> defaultProcessor)
    {
        BUNDLE.checkNotNull(defaultProcessor, "processing.nullProcessor");
        this.defaultProcessor = defaultProcessor;
        return this;
    }

    /**
     * Build the resulting processor from this map selector
     *
     * <p>The resulting processor is immutable: reusing a map builder after
     * getting the processor by calling this method will not alter the
     * processor you grabbed.</p>
     *
     * @return the processor for this map selector
     */
    public Processor<IN, OUT> getProcessor()
    {
        return new Mapper<K, IN, OUT>(processors, keyFunction, defaultProcessor);
    }

    private static final class Mapper<K, IN extends MessageProvider, OUT extends MessageProvider>
        implements Processor<IN, OUT>
    {
        private final Map<K, Processor<IN, OUT>> processors;
        private final Function<IN, K> f;
        private final Processor<IN, OUT> defaultProcessor;

        private Mapper(Map<K, Processor<IN, OUT>> processors, Function<IN, K> f, Processor<IN, OUT> defaultProcessor)
        {
            this.processors = ImmutableMap.copyOf(processors);
            this.f = f;
            this.defaultProcessor = defaultProcessor;
        }

        @Override
        public OUT process(ProcessingReport report, IN input)
            throws ProcessingException
        {
            K key = f.apply(input);
            Processor<IN, OUT> processor = processors.get(key);

            if (processor == null)
                processor = defaultProcessor;

            if (processor == null) // Not even a default processor. Ouch.
                throw new ProcessingException(new ProcessingMessage()
                    .setMessage(BUNDLE.getMessage("processing.noProcessor"))
                    .put("key", key));

            return processor.process(report, input);
        }

        @Override
        public String toString()
        {
            StringBuilder sb = new StringBuilder("map[")
                .append(processors.size()).append(" entries with ");
            if (defaultProcessor == null)
                sb.append("no ");
            return sb.append("default processor]").toString();
        }
    }
}

