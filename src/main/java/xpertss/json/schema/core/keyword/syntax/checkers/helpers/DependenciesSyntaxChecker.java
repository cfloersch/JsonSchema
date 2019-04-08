package xpertss.json.schema.core.keyword.syntax.checkers.helpers;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JacksonUtils;
import com.github.fge.jackson.JsonNumEquals;
import com.github.fge.jackson.NodeType;
import com.github.fge.jackson.jsonpointer.JsonPointer;
import xpertss.json.schema.core.exceptions.InvalidSchemaException;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.core.keyword.syntax.checkers.AbstractSyntaxChecker;
import xpertss.json.schema.core.tree.SchemaTree;
import com.github.fge.msgsimple.bundle.MessageBundle;
import com.google.common.base.Equivalence;
import com.google.common.collect.Maps;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Map;

/**
 * Helper class for syntax checking of draft v4 and v3 {@code dependencies}
 *
 * <p>The validation check also fills the JSON Pointer list with the
 * appropriate paths when schema dependencies are encountered.</p>
 */
public abstract class DependenciesSyntaxChecker extends AbstractSyntaxChecker {
    /**
     * JSON Schema equivalence
     */
    protected static final Equivalence<JsonNode> EQUIVALENCE = JsonNumEquals.getInstance();

    /**
     * Valid types for one dependency value
     */
    protected final EnumSet<NodeType> dependencyTypes;

    /**
     * Protected constructor
     *
     * @param depTypes valid types for one dependency value
     */
    protected DependenciesSyntaxChecker(NodeType... depTypes)
    {
        super("dependencies", NodeType.OBJECT);
        dependencyTypes = EnumSet.of(NodeType.OBJECT, depTypes);
    }

    @Override
    protected final void checkValue(Collection<JsonPointer> pointers, MessageBundle bundle,
                                        ProcessingReport report, SchemaTree tree)
        throws ProcessingException
    {
        JsonNode node = getNode(tree);
        Map<String, JsonNode> map = Maps.newTreeMap();
        map.putAll(JacksonUtils.asMap(node));

        String key;
        JsonNode value;

        for (final Map.Entry<String, JsonNode> entry: map.entrySet()) {
            key = entry.getKey();
            value = entry.getValue();
            if (value.isObject())
                pointers.add(JsonPointer.of(keyword, key));
            else
                checkDependency(report, bundle, entry.getKey(), tree);
        }

    }

    /**
     * Check one dependency which is not a schema dependency
     *
     * @param report the processing report to use
     * @param bundle the message bundle to use
     * @param name the property dependency name
     * @param tree the schema
     * @throws InvalidSchemaException keyword is invalid
     */
    protected abstract void checkDependency(ProcessingReport report, MessageBundle bundle,
                                                String name, SchemaTree tree)
        throws ProcessingException;
}
