package xpertss.json.schema.core.processing;

import xpertss.json.schema.core.messages.JsonSchemaCoreMessageBundle;
import xpertss.json.schema.core.report.MessageProvider;
import com.github.fge.msgsimple.bundle.MessageBundle;
import com.github.fge.msgsimple.load.MessageBundles;
import com.google.common.base.Predicate;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * The pendant of {@link ProcessorSelector}
 *
 * <p>This is the result of {@link ProcessorSelector#when(Predicate)}. Its
 * only method, {@link #then(Processor)}, returns a {@link ProcessorSelector}.
 * </p>
 *
 * @param <IN> the input type of processors
 * @param <OUT> the output type of processors
 */
public final class ProcessorSelectorPredicate<IN extends MessageProvider, OUT extends MessageProvider> {

    private static final MessageBundle BUNDLE = MessageBundles.getBundle(JsonSchemaCoreMessageBundle.class);

    /**
     * The predicate
     */
    private final Predicate<IN> predicate;

    /**
     * The existing choices
     */
    final Map<Predicate<IN>, Processor<IN, OUT>> choices;

    /**
     * The default processor
     */
    final Processor<IN, OUT> byDefault;

    /**
     * Package local constructor
     *
     * @param selector a {@link ProcessorSelector}
     * @param predicate the new predicate
     *
     * @see ProcessorSelector#when(Predicate)
     */
    ProcessorSelectorPredicate(ProcessorSelector<IN, OUT> selector, Predicate<IN> predicate)
    {
        this.predicate = predicate;
        choices = Maps.newLinkedHashMap(selector.choices);
        byDefault = selector.byDefault;
    }

    /**
     * Associate a processor to a predicate
     *
     * @param processor the associated processor
     * @return a new {@link ProcessorSelector}
     * @throws NullPointerException the processor is null
     */
    public ProcessorSelector<IN, OUT> then(Processor<IN, OUT> processor)
    {
        BUNDLE.checkNotNull(processor, "processing.nullProcessor");
        choices.put(predicate, processor);
        return new ProcessorSelector<IN, OUT>(this);
    }
}
