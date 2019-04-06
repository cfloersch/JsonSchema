package xpertss.json.schema.core.processing;

import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.messages.JsonSchemaCoreMessageBundle;
import xpertss.json.schema.core.report.ListProcessingReport;
import xpertss.json.schema.core.report.LogLevel;
import xpertss.json.schema.core.report.MessageProvider;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.core.util.equivalence.Equivalences;
import com.github.fge.msgsimple.bundle.MessageBundle;
import com.github.fge.msgsimple.load.MessageBundles;
import com.google.common.base.Equivalence;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.ExecutionException;

/**
 * A class caching the result of a {@link Processor}
 *
 * <p>You can use this over whichever processor of your choice. Internally, it
 * uses a {@link LoadingCache} to store results.</p>
 *
 * <p>You can optionally pass an {@link Equivalence} as an argument for cache
 * keys. By default, {@link Equivalences#equals()} will be used.</p>
 *
 * @param <IN> input type for that processor
 * @param <OUT> output type for that processor
 */
public final class CachingProcessor<IN extends MessageProvider, OUT extends MessageProvider>
    implements Processor<IN, OUT>
{
    private static final MessageBundle BUNDLE
        = MessageBundles.getBundle(JsonSchemaCoreMessageBundle.class);

    private static final int DEFAULT_CACHE_SIZE = 512;

    /**
     * The wrapped processor
     */
    private final Processor<IN, OUT> processor;

    /**
     * The equivalence to use
     */
    private final Equivalence<IN> equivalence;

    /**
     * The cache
     */
    private final LoadingCache<Equivalence.Wrapper<IN>, ProcessingResult<OUT>>
        cache;

    /**
     * Constructor
     *
     * <p>This is equivalent to calling {@link #CachingProcessor(Processor,
     * Equivalence)} with {@link Equivalences#equals()} as the second argument.
     * </p>
     *
     * @param processor the processor
     */
    public CachingProcessor(final Processor<IN, OUT> processor)
    {
        this(processor, Equivalences.<IN>equals());
    }

    public CachingProcessor(final Processor<IN, OUT> processor,
        final Equivalence<IN> equivalence)
    {
        this(processor, equivalence, DEFAULT_CACHE_SIZE);
    }
    /**
     * Main constructor
     *
     * @param processor the processor
     * @param equivalence an equivalence to use for cache keys
     * @param cacheSize the size of the cache, zero disables it
     * @throws NullPointerException processor or equivalence
     */
    public CachingProcessor(final Processor<IN, OUT> processor,
        final Equivalence<IN> equivalence, final int cacheSize)
    {
        BUNDLE.checkNotNull(processor, "processing.nullProcessor");
        BUNDLE.checkNotNull(equivalence, "processing.nullEquivalence");
        BUNDLE.checkArgument(cacheSize >= -1, "processing.invalidCacheSize");
        this.processor = processor;
        this.equivalence = equivalence;
        CacheBuilder<Object, Object> builder = CacheBuilder.newBuilder();
        if (cacheSize != -1) {
        	builder.maximumSize(cacheSize);
        }
        cache = builder.build(loader());
    }

    @Override
    public OUT process(final ProcessingReport report, final IN input)
        throws ProcessingException
    {
        final ProcessingResult<OUT> result;
        try {
            result = cache.get(equivalence.wrap(input));
        } catch (ExecutionException e) {
            throw (ProcessingException) e.getCause();
        }
        report.mergeWith(result.getReport());
        return result.getResult();
    }

    private CacheLoader<Equivalence.Wrapper<IN>, ProcessingResult<OUT>> loader()
    {
        return new CacheLoader<Equivalence.Wrapper<IN>, ProcessingResult<OUT>>()
        {
            @Override
            public ProcessingResult<OUT> load(final Equivalence.Wrapper<IN> key)
                throws ProcessingException
            {
                final IN input = key.get();
                final ListProcessingReport report
                    = new ListProcessingReport(LogLevel.DEBUG, LogLevel.NONE);
                return ProcessingResult.of(processor, report, input);
            }
        };
    }

    @Override
    public String toString()
    {
        return "CACHED[" + processor + ']';
    }
}
