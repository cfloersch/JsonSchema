package xpertss.json.schema.core.keyword.syntax.checkers.draftv4;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonNumEquals;
import com.github.fge.jackson.NodeType;
import com.github.fge.jackson.jsonpointer.JsonPointer;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.core.keyword.syntax.checkers.AbstractSyntaxChecker;
import xpertss.json.schema.core.keyword.syntax.checkers.SyntaxChecker;
import xpertss.json.schema.core.tree.SchemaTree;
import com.github.fge.msgsimple.bundle.MessageBundle;
import com.google.common.base.Equivalence;
import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;

/**
 * Syntax checker for draft v4's {@code type} keyword
 */
public final class DraftV4TypeSyntaxChecker extends AbstractSyntaxChecker {

    private static final EnumSet<NodeType> ALL_TYPES = EnumSet.allOf(NodeType.class);

    private static final Equivalence<JsonNode> EQUIVALENCE = JsonNumEquals.getInstance();

    private static final SyntaxChecker INSTANCE = new DraftV4TypeSyntaxChecker();

    public static SyntaxChecker getInstance()
    {
        return INSTANCE;
    }

    private DraftV4TypeSyntaxChecker()
    {
        super("type", NodeType.ARRAY, NodeType.STRING);
    }

    @Override
    protected void checkValue(Collection<JsonPointer> pointers, MessageBundle bundle,
                                ProcessingReport report, SchemaTree tree)
        throws ProcessingException
    {
        JsonNode node = getNode(tree);


        if (node.isTextual()) {
            String s = node.textValue();
            if (NodeType.fromName(s) == null)
                report.error(newMsg(tree, bundle,
                    "common.typeDisallow.primitiveType.unknown")
                    .putArgument("found", s).putArgument("valid", ALL_TYPES));
            return;
        }

        int size = node.size();

        if (size == 0) {
            report.error(newMsg(tree, bundle, "common.array.empty"));
            return;
        }

        Set<Equivalence.Wrapper<JsonNode>> set = Sets.newHashSet();

        JsonNode element;
        NodeType type;
        boolean uniqueElements = true;

        for (int index = 0; index <size; index++) {
            element = node.get(index);
            type = NodeType.getNodeType(element);
            uniqueElements = set.add(EQUIVALENCE.wrap(element));
            if (type != NodeType.STRING) {
                report.error(newMsg(tree, bundle,
                    "common.array.element.incorrectType")
                    .putArgument("index", index)
                    .putArgument("expected", NodeType.STRING)
                    .putArgument("found", type));
                continue;
            }
            String found = element.textValue();
            if (NodeType.fromName(found) == null)
                report.error(newMsg(tree, bundle,
                    "common.typeDisallow.primitiveType.unknown")
                    .put("index", index).putArgument("found", found)
                    .putArgument("valid", ALL_TYPES));
        }

        if (!uniqueElements)
            report.error(newMsg(tree, bundle, "common.array.duplicateElements"));
    }
}
