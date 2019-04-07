package xpertss.json.schema.processors.syntax;

import com.fasterxml.jackson.databind.JsonNode;
import xpertss.json.schema.cfg.ValidationConfiguration;
import xpertss.json.schema.core.keyword.syntax.SyntaxProcessor;
import xpertss.json.schema.core.keyword.syntax.checkers.SyntaxChecker;
import xpertss.json.schema.core.processing.ProcessingResult;
import xpertss.json.schema.core.processing.Processor;
import xpertss.json.schema.core.processing.ProcessorMap;
import xpertss.json.schema.core.ref.JsonRef;
import xpertss.json.schema.core.report.DevNullProcessingReport;
import xpertss.json.schema.core.report.ListProcessingReport;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.core.tree.CanonicalSchemaTree;
import xpertss.json.schema.core.tree.SchemaTree;
import xpertss.json.schema.core.tree.key.SchemaKey;
import xpertss.json.schema.core.util.Dictionary;
import xpertss.json.schema.core.util.ValueHolder;
import xpertss.json.schema.library.Library;
import xpertss.json.schema.main.JsonSchemaFactory;
import com.github.fge.msgsimple.bundle.MessageBundle;
import com.google.common.base.Function;

import java.util.Map;

/**
 * Standalone syntax validator
 *
 * <p>This is the syntax validator built, and returned, by {@link
 * JsonSchemaFactory#getSyntaxValidator()}. It can be used to validate schemas
 * independently of the validation chain. Among other features, it detects
 * {@code $schema} and acts accordingly.</p>
 *
 * <p>Note that the reports used are always {@link ListProcessingReport}s.</p>
 */
// TODO REMOVE
public final class SyntaxValidator {

    private static final Function<ValueHolder<SchemaTree>, JsonRef> FUNCTION = input -> input.getValue().getDollarSchema();

    private final Processor<ValueHolder<SchemaTree>, ValueHolder<SchemaTree>> processor;

    /**
     * Constructor
     *
     * @param cfg the validation configuration to use
     */
    public SyntaxValidator(ValidationConfiguration cfg)
    {
        MessageBundle syntaxMessages = cfg.getSyntaxMessages();
        ProcessorMap<JsonRef, ValueHolder<SchemaTree>, ValueHolder<SchemaTree>> map = new ProcessorMap<JsonRef, ValueHolder<SchemaTree>, ValueHolder<SchemaTree>>(FUNCTION);

        Dictionary<SyntaxChecker> dict = cfg.getDefaultLibrary().getSyntaxCheckers();

        SyntaxProcessor byDefault = new SyntaxProcessor(cfg.getSyntaxMessages(), dict);

        map.setDefaultProcessor(byDefault);

        Map<JsonRef,Library> libraries = cfg.getLibraries();

        JsonRef ref;
        SyntaxProcessor syntaxProcessor;

        for (final Map.Entry<JsonRef, Library> entry: libraries.entrySet()) {
            ref = entry.getKey();
            dict = entry.getValue().getSyntaxCheckers();
            syntaxProcessor = new SyntaxProcessor(syntaxMessages, dict);
            map.addEntry(ref, syntaxProcessor);
        }

        processor = map.getProcessor();
    }

    /**
     * Tell whether a schema is valid
     *
     * @param schema the schema
     * @return true if the schema is valid
     */
    public boolean schemaIsValid(JsonNode schema)
    {
        ProcessingReport report = new DevNullProcessingReport();
        return getResult(schema, report).isSuccess();
    }

    /**
     * Validate a schema and return a report
     *
     * @param schema the schema
     * @return a report
     */
    public ProcessingReport validateSchema(JsonNode schema)
    {
        ProcessingReport report = new ListProcessingReport();
        return getResult(schema, report).getReport();
    }

    /**
     * Return the underlying processor
     *
     * <p>You can use this processor to chain it with your own.</p>
     *
     * @return a processor performing full syntax validation
     */
    public Processor<ValueHolder<SchemaTree>, ValueHolder<SchemaTree>> getProcessor()
    {
        return processor;
    }

    private ProcessingResult<ValueHolder<SchemaTree>> getResult(JsonNode schema, ProcessingReport report)
    {
        ValueHolder<SchemaTree> holder = holder(schema);
        return ProcessingResult.uncheckedResult(processor, report, holder);
    }

    private static ValueHolder<SchemaTree> holder(JsonNode node)
    {
        return ValueHolder.<SchemaTree>hold("schema", new CanonicalSchemaTree(SchemaKey.anonymousKey(), node));
    }
}
