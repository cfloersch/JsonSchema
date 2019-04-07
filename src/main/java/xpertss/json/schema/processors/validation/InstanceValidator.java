package xpertss.json.schema.processors.validation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.github.fge.jackson.JacksonUtils;
import com.github.fge.jackson.jsonpointer.JsonPointer;
import xpertss.json.schema.core.exceptions.InvalidSchemaException;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.processing.Processor;
import xpertss.json.schema.core.report.ProcessingMessage;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.core.tree.JsonTree;
import xpertss.json.schema.core.tree.SchemaTree;
import xpertss.json.schema.keyword.validator.KeywordValidator;
import xpertss.json.schema.main.JsonValidator;
import xpertss.json.schema.processors.data.FullData;
import xpertss.json.schema.processors.data.SchemaContext;
import xpertss.json.schema.processors.data.ValidatorList;
import com.github.fge.msgsimple.bundle.MessageBundle;
import com.google.common.collect.Lists;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.NotThreadSafe;
import java.util.Collections;
import java.util.List;

/**
 * Processor for validating one schema/instance pair
 *
 * <p>One such processor is created for each schema/instance validation.</p>
 *
 * <p>Internally, all validation operations provided by the API (whether that
 * be a {@link JsonSchema}, via {@link JsonValidator} or using {@link
 * ValidationProcessor} directly) will eventually instantiate one of these. More
 * precisely, this is instantiated by {@link
 * ValidationProcessor#process(ProcessingReport, FullData)}.</p>
 *
 */
@NotThreadSafe
@ParametersAreNonnullByDefault
final class InstanceValidator implements Processor<FullData, FullData> {

    private final MessageBundle syntaxMessages;
    private final MessageBundle validationMessages;
    private final Processor<SchemaContext, ValidatorList> keywordBuilder;

    private final ValidationStack stack;

    /**
     * Constructor -- do not use directly!
     *
     * @param syntaxMessages the syntax message bundle
     * @param validationMessages the validation message bundle
     * @param keywordBuilder the keyword builder
     */
    InstanceValidator(MessageBundle syntaxMessages,
                        MessageBundle validationMessages,
                        Processor<SchemaContext, ValidatorList> keywordBuilder)
    {
        this.syntaxMessages = syntaxMessages;
        this.validationMessages = validationMessages;
        this.keywordBuilder = keywordBuilder;

        stack = new ValidationStack(validationMessages.getMessage("err.common.validationLoop"));
    }

    @Override
    public FullData process(ProcessingReport report, FullData input)
        throws ProcessingException
    {
        /*
         * We don't want the same validation context to appear twice, see above
         */
        stack.push(input);


        /*
         * Build a validation context, attach a report to it
         */
        SchemaContext context = new SchemaContext(input);

        /*
         * Get the full context from the cache. Inject the messages into the
         * main report.
         */
        ValidatorList fullContext = keywordBuilder.process(report, context);

        if (fullContext == null) {
            throw new InvalidSchemaException(collectSyntaxErrors(report));
        }

        /*
         * Get the calculated context. Build the data.
         */
        SchemaContext newContext = fullContext.getContext();
        FullData data = new FullData(newContext.getSchema(), input.getInstance(), input.isDeepCheck());

        /*
         * Validate against all keywords.
         */
        for (final KeywordValidator validator: fullContext)
            validator.validate(this, report, validationMessages, data);

        /*
         * At that point, if the report is a failure, we quit: there is no
         * reason to go any further. Unless the user has asked to continue even
         * in this case.
         */
        if (!(report.isSuccess() || data.isDeepCheck())) {
            stack.pop();
            return input;
        }

        /*
         * Now check whether this is a container node with a size greater than
         * 0. If not, no need to go see the children.
         */
        JsonNode node = data.getInstance().getNode();

        if (node.isContainerNode()) {
            if (node.isArray())
                processArray(report, data);
            else
                processObject(report, data);
        }

        stack.pop();
        return input;
    }

    @Override
    public String toString()
    {
        return "instance validator";
    }

    private void processArray(ProcessingReport report, FullData input)
        throws ProcessingException
    {
        SchemaTree tree = input.getSchema();
        JsonTree instance = input.getInstance();

        JsonNode schema = tree.getNode();
        JsonNode node = instance.getNode();

        JsonNode digest = ArraySchemaDigester.getInstance().digest(schema);
        ArraySchemaSelector selector = new ArraySchemaSelector(digest);

        int size = node.size();

        FullData data;
        JsonTree newInstance;

        for (int index = 0; index < size; index++) {
            newInstance = instance.append(JsonPointer.of(index));
            data = input.withInstance(newInstance);
            for (final JsonPointer ptr: selector.selectSchemas(index)) {
                data = data.withSchema(tree.append(ptr));
                process(report, data);
            }
        }
    }

    private void processObject(ProcessingReport report, FullData input)
        throws ProcessingException
    {
        SchemaTree tree = input.getSchema();
        JsonTree instance = input.getInstance();

        JsonNode schema = tree.getNode();
        JsonNode node = instance.getNode();

        JsonNode digest = ObjectSchemaDigester.getInstance().digest(schema);
        ObjectSchemaSelector selector = new ObjectSchemaSelector(digest);

        List<String> fields = Lists.newArrayList(node.fieldNames());
        Collections.sort(fields);

        FullData data;
        JsonTree newInstance;

        for (final String field: fields) {
            newInstance = instance.append(JsonPointer.of(field));
            data = input.withInstance(newInstance);
            for (final JsonPointer ptr: selector.selectSchemas(field)) {
                data = data.withSchema(tree.append(ptr));
                process(report, data);
            }
        }
    }

    private ProcessingMessage collectSyntaxErrors(ProcessingReport report)
    {
        /*
         * OK, that's for issue #99 but that's ugly nevertheless.
         *
         * We want syntax error messages to appear in the exception text.
         */
        String msg = syntaxMessages.getMessage("core.invalidSchema");
        ArrayNode arrayNode = JacksonUtils.nodeFactory().arrayNode();
        JsonNode node;
        for (final ProcessingMessage message: report) {
            node = message.asJson();
            if ("syntax".equals(node.path("domain").asText()))
                arrayNode.add(node);
        }
        StringBuilder sb = new StringBuilder(msg);
        sb.append("\nSyntax errors:\n");
        sb.append(JacksonUtils.prettyPrint(arrayNode));
        return new ProcessingMessage().setMessage(sb.toString());
    }
}
