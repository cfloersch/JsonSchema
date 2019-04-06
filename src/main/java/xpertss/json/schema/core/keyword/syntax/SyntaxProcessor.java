package xpertss.json.schema.core.keyword.syntax;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.NodeType;
import com.github.fge.jackson.jsonpointer.JsonPointer;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.util.Dictionary;
import xpertss.json.schema.core.processing.RawProcessor;
import xpertss.json.schema.core.report.ProcessingMessage;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.core.keyword.syntax.checkers.SyntaxChecker;
import xpertss.json.schema.core.tree.SchemaTree;
import com.github.fge.msgsimple.bundle.MessageBundle;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Syntax processor
 */
public final class SyntaxProcessor
    extends RawProcessor<SchemaTree, SchemaTree>
{
    private final MessageBundle bundle;
    private final Map<String, SyntaxChecker> checkers;

    /**
     * Constructor
     *
     * @param bundle message bundle used by this syntax checker
     * @param dict dictionary of syntax checkers
     */
    public SyntaxProcessor(final MessageBundle bundle,
        final Dictionary<SyntaxChecker> dict)
    {
        super("schema", "schema");
        this.bundle = bundle;
        checkers = dict.entries();
    }

    @Override
    public SchemaTree rawProcess(final ProcessingReport report,
        final SchemaTree input)
        throws ProcessingException
    {
        validate(report, input);
        return input;
    }

    private void validate(final ProcessingReport report, final SchemaTree tree)
        throws ProcessingException
    {
        final JsonNode node = tree.getNode();
        final NodeType type = NodeType.getNodeType(node);

        /*
         * Barf if not an object, and don't even try to go any further
         */
        if (type != NodeType.OBJECT) {
            report.error(newMsg(tree, "core.notASchema")
                .putArgument("found", type));
            return;
        }

        /*
         * Grab all checkers and object member names. Retain in checkers only
         * existing keywords, and remove from the member names set what is in
         * the checkers' key set: if non empty, some keywords are missing,
         * report them.
         */
        final Map<String, SyntaxChecker> map = Maps.newTreeMap();
        map.putAll(checkers);

        final Set<String> fields = Sets.newHashSet(node.fieldNames());
        map.keySet().retainAll(fields);
        fields.removeAll(map.keySet());

        if (!fields.isEmpty())
            report.warn(newMsg(tree, "core.unknownKeywords")
                .putArgument("ignored", Ordering.natural().sortedCopy(fields)));

        /*
         * Now, check syntax of each keyword, and collect pointers for further
         * analysis.
         */
        final List<JsonPointer> pointers = Lists.newArrayList();
        for (final SyntaxChecker checker: map.values())
            checker.checkSyntax(pointers, bundle, report, tree);

        /*
         * Operate on these pointers.
         */
        for (final JsonPointer pointer: pointers)
            validate(report, tree.append(pointer));
    }

    private ProcessingMessage newMsg(final SchemaTree tree, final String key)
    {
        return new ProcessingMessage().put("schema", tree)
            .put("domain", "syntax").setMessage(bundle.getMessage(key));

    }

    @Override
    public String toString()
    {
        return "syntax checker";
    }
}
