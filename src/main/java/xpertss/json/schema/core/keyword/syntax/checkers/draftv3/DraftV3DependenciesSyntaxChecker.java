package xpertss.json.schema.core.keyword.syntax.checkers.draftv3;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.NodeType;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.core.keyword.syntax.checkers.SyntaxChecker;
import xpertss.json.schema.core.keyword.syntax.checkers.helpers.DependenciesSyntaxChecker;
import xpertss.json.schema.core.tree.SchemaTree;
import com.github.fge.msgsimple.bundle.MessageBundle;
import com.google.common.base.Equivalence;
import com.google.common.collect.Sets;

import java.util.EnumSet;
import java.util.Set;

/**
 * Syntax checker for draft v3's {@code dependencies} keyword
 */
public final class DraftV3DependenciesSyntaxChecker extends DependenciesSyntaxChecker {

    private static final SyntaxChecker INSTANCE = new DraftV3DependenciesSyntaxChecker();

    public static SyntaxChecker getInstance()
    {
        return INSTANCE;
    }

    private DraftV3DependenciesSyntaxChecker()
    {
        super(NodeType.ARRAY, NodeType.STRING);
    }

    @Override
    protected void checkDependency(ProcessingReport report, MessageBundle bundle,
                                    String name, SchemaTree tree)
        throws ProcessingException
    {
        JsonNode node = getNode(tree).get(name);
        NodeType type;

        type = NodeType.getNodeType(node);

        if (type == NodeType.STRING)
            return;

        if (type != NodeType.ARRAY) {
            report.error(newMsg(tree, bundle,
                "common.dependencies.value.incorrectType")
                .putArgument("property", name)
                .putArgument("expected", dependencyTypes)
                .putArgument("found", type));
            return;
        }

        int size = node.size();

        /*
         * Yep, in draft v3, nothing prevents a dependency array from being
         * empty! This is stupid, so at least warn the user.
         */
        if (size == 0) {
            report.warn(newMsg(tree, bundle, "common.array.empty")
                .put("property", name));
            return;
        }

        Set<Equivalence.Wrapper<JsonNode>> set = Sets.newHashSet();

        JsonNode element;
        boolean uniqueElements = true;

        for (int index = 0; index < size; index++) {
            element = node.get(index);
            type = NodeType.getNodeType(element);
            uniqueElements = set.add(EQUIVALENCE.wrap(element));
            if (type == NodeType.STRING)
                continue;
            report.error(newMsg(tree, bundle,
                "common.array.element.incorrectType")
                .put("property", name).putArgument("index", index)
                .putArgument("expected", EnumSet.of(NodeType.STRING))
                .putArgument("found", type));
        }

        /*
         * Similarly, there is nothing preventing duplicates. Equally stupid,
         * so warn the user.
         */
        if (!uniqueElements)
            report.warn(newMsg(tree, bundle, "common.array.duplicateElements")
                .put("property", name));
    }
}
