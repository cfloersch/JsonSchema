package xpertss.json.schema.processors.validation;

import xpertss.json.schema.cfg.ValidationConfiguration;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.keyword.syntax.SyntaxProcessor;
import xpertss.json.schema.core.load.RefResolver;
import xpertss.json.schema.core.processing.CachingProcessor;
import xpertss.json.schema.core.processing.Processor;
import xpertss.json.schema.core.processing.ProcessorChain;
import xpertss.json.schema.core.report.ListProcessingReport;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.core.tree.SchemaTree;
import xpertss.json.schema.core.util.ValueHolder;
import xpertss.json.schema.library.Library;
import xpertss.json.schema.processors.build.ValidatorBuilder;
import xpertss.json.schema.processors.data.SchemaContext;
import xpertss.json.schema.processors.data.ValidatorList;
import xpertss.json.schema.processors.digest.SchemaDigester;
import xpertss.json.schema.processors.format.FormatProcessor;
import com.google.common.base.Equivalence;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * A validation chain
 *
 * <p>This processor performs the following:</p>
 *
 * <ul>
 *     <li>perform reference lookup then syntax validation;</li>
 *     <li>throw an exception if the previous step fails;</li>
 *     <li>then perform schema digesting and keyword building.</li>
 * </ul>
 *
 * <p>A validation chain handles one schema version. Switching schema versions
 * is done by {@link ValidationProcessor}.</p>
 */
public final class ValidationChain implements Processor<SchemaContext, ValidatorList> {

    private final Processor<ValueHolder<SchemaTree>, ValueHolder<SchemaTree>> resolver;
    private final Processor<SchemaContext, ValidatorList> builder;

    public ValidationChain(RefResolver refResolver, Library library, ValidationConfiguration cfg)
    {
        SyntaxProcessor syntaxProcessor = new SyntaxProcessor(cfg.getSyntaxMessages(), library.getSyntaxCheckers());
        ProcessorChain<ValueHolder<SchemaTree>, ValueHolder<SchemaTree>> chain1 = ProcessorChain.startWith(refResolver).chainWith(syntaxProcessor);

        resolver = new CachingProcessor<>(chain1.getProcessor(), SchemaHolderEquivalence.INSTANCE, cfg.getCacheSize());

        SchemaDigester digester = new SchemaDigester(library);
        ValidatorBuilder keywordBuilder = new ValidatorBuilder(library);

        ProcessorChain<SchemaContext, ValidatorList> chain2 = ProcessorChain.startWith(digester).chainWith(keywordBuilder);

        if (cfg.getUseFormat()) {
            final FormatProcessor format = new FormatProcessor(library, cfg);
            chain2 = chain2.chainWith(format);
        }

        builder = new CachingProcessor<>(chain2.getProcessor(), SchemaContextEquivalence.getInstance(), cfg.getCacheSize());
    }

    @Override
    public ValidatorList process(ProcessingReport report, SchemaContext input)
        throws ProcessingException
    {
        ValueHolder<SchemaTree> in = ValueHolder.hold("schema", input.getSchema());

        /*
         * We have to go through an intermediate report. If we re-enter this
         * function with a report already telling there is an error, we don't
         * want to wrongly report that the schema is invalid.
         */
        ProcessingReport r = new ListProcessingReport(report);
        ValueHolder<SchemaTree> out = resolver.process(r, in);
        report.mergeWith(r);
        if (!r.isSuccess())
            return null;

        SchemaContext output = new SchemaContext(out.getValue(), input.getInstanceType());
        return builder.process(report, output);
    }

    @Override
    public String toString()
    {
        return resolver + " -> " + builder;
    }

    @ParametersAreNonnullByDefault
    private static final class SchemaHolderEquivalence extends Equivalence<ValueHolder<SchemaTree>> {

        private static final Equivalence<ValueHolder<SchemaTree>> INSTANCE = new SchemaHolderEquivalence();

        @Override
        protected boolean doEquivalent(ValueHolder<SchemaTree> a, ValueHolder<SchemaTree> b)
        {
            return a.getValue().equals(b.getValue());
        }

        @Override
        protected int doHash(ValueHolder<SchemaTree> t)
        {
            return t.getValue().hashCode();
        }
    }
}
