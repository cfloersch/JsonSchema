package xpertss.json.schema.processors.validation;

import xpertss.json.schema.cfg.ValidationConfiguration;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.processing.Processor;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.processors.data.FullData;
import xpertss.json.schema.processors.data.SchemaContext;
import xpertss.json.schema.processors.data.ValidatorList;
import com.github.fge.msgsimple.bundle.MessageBundle;

/**
 * Main validation processor
 */
public final class ValidationProcessor
    implements Processor<FullData, FullData>
{
    private final MessageBundle syntaxMessages;
    private final MessageBundle validationMessages;
    private final Processor<SchemaContext, ValidatorList> processor;

    public ValidationProcessor(final ValidationConfiguration cfg,
        final Processor<SchemaContext, ValidatorList> processor)
    {
        syntaxMessages = cfg.getSyntaxMessages();
        validationMessages = cfg.getValidationMessages();
        this.processor = processor;
    }

    @Override
    public FullData process(final ProcessingReport report,
        final FullData input)
        throws ProcessingException
    {
        final InstanceValidator validator = new InstanceValidator(
            syntaxMessages, validationMessages, processor);
        return validator.process(report, input);
    }

    @Override
    public String toString()
    {
        return "validation processor";
    }
}
