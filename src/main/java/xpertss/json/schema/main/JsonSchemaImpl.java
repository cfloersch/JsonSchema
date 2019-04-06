package xpertss.json.schema.main;

import com.fasterxml.jackson.databind.JsonNode;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.processing.ProcessingResult;
import xpertss.json.schema.core.processing.Processor;
import xpertss.json.schema.core.report.ListProcessingReport;
import xpertss.json.schema.core.report.MessageProvider;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.core.report.ReportProvider;
import xpertss.json.schema.core.tree.SchemaTree;
import xpertss.json.schema.core.tree.SimpleJsonTree;
import xpertss.json.schema.processors.data.FullData;
import xpertss.json.schema.processors.validation.ValidationProcessor;

import javax.annotation.concurrent.Immutable;

/**
 * Single-schema instance validator
 *
 * <p>This is the class you will use the most often. It is, in essence, a {@link
 * JsonValidator} initialized with a single JSON Schema. Note however that this
 * class still retains the ability to resolve JSON References.</p>
 *
 * <p>It has no public constructors: you should use the appropriate methods in
 * {@link JsonSchemaFactory} to obtain an instance of this class.</p>
 */
@Immutable
final class JsonSchemaImpl implements JsonSchema
{
    private final ValidationProcessor processor;
    private final SchemaTree schema;
    private final ReportProvider reportProvider;

    /**
     * Package private constructor
     *
     * @param processor the validation processor
     * @param schema the schema to bind to this instance
     * @param reportProvider the report provider
     */
    JsonSchemaImpl(final ValidationProcessor processor, final SchemaTree schema,
                   final ReportProvider reportProvider)
    {
        this.processor = processor;
        this.schema = schema;
        this.reportProvider = reportProvider;
    }

    private ProcessingReport doValidate(final JsonNode node,
        final boolean deepCheck)
        throws ProcessingException
    {
        final FullData data = new FullData(schema, new SimpleJsonTree(node),
            deepCheck);
        final ProcessingReport report = reportProvider.newReport();
        final ProcessingResult<FullData> result
            =  ProcessingResult.of(processor, report, data);
        return result.getReport();
    }

    private ProcessingReport doValidateUnchecked(final JsonNode node,
        final boolean deepCheck)
    {
        final FullData data = new FullData(schema, new SimpleJsonTree(node),
            deepCheck);
        final ProcessingReport report = reportProvider.newReport();
        final ProcessingResult<FullData> result
            =  ProcessingResult.uncheckedResult(processor, report, data);
        return result.getReport();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProcessingReport validate(final JsonNode instance,
        final boolean deepCheck)
        throws ProcessingException
    {
        return doValidate(instance, deepCheck);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProcessingReport validate(final JsonNode instance)
        throws ProcessingException
    {
        return validate(instance, false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProcessingReport validateUnchecked(final JsonNode instance,
        final boolean deepCheck)
    {
        return doValidateUnchecked(instance, deepCheck);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProcessingReport validateUnchecked(final JsonNode instance)
    {
        return doValidateUnchecked(instance, false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validInstance(final JsonNode instance)
        throws ProcessingException
    {
        return doValidate(instance, false).isSuccess();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validInstanceUnchecked(final JsonNode instance)
    {
        return doValidateUnchecked(instance, false).isSuccess();
    }
}
