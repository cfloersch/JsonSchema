package xpertss.json.schema.core.keyword.syntax.checkers.draftv3;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonNumEquals;
import com.github.fge.jackson.NodeType;
import com.github.fge.jackson.jsonpointer.JsonPointer;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.core.keyword.syntax.checkers.AbstractSyntaxChecker;
import xpertss.json.schema.core.tree.SchemaTree;
import com.github.fge.msgsimple.bundle.MessageBundle;
import com.google.common.base.Equivalence;
import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;

import static com.github.fge.jackson.NodeType.*;

/**
 * Helper class to validate the syntax of draft v3's {@code type} and {@code
 * disallow}
 */
public final class DraftV3TypeKeywordSyntaxChecker extends AbstractSyntaxChecker {

    private static final Equivalence<JsonNode> EQUIVALENCE = JsonNumEquals.getInstance();
    private static final String ANY = "any";

    public DraftV3TypeKeywordSyntaxChecker(String keyword)
    {
        super(keyword, STRING, ARRAY);
    }

    @Override
    protected void checkValue(Collection<JsonPointer> pointers, MessageBundle bundle,
                                ProcessingReport report, SchemaTree tree)
        throws ProcessingException
    {
        JsonNode node = tree.getNode().get(keyword);

        if (node.isTextual()) {
            final String found = node.textValue();
            if (!typeIsValid(found))
                report.error(newMsg(tree, bundle,
                    "common.typeDisallow.primitiveType.unknown")
                    .putArgument("found", found)
                    .putArgument("valid", EnumSet.allOf(NodeType.class)));
            return;
        }

        int size = node.size();
        Set<Equivalence.Wrapper<JsonNode>> set = Sets.newHashSet();

        JsonNode element;
        NodeType type;
        boolean uniqueItems = true;

        for (int index = 0; index < size; index++) {
            element = node.get(index);
            type = NodeType.getNodeType(element);
            uniqueItems = set.add(EQUIVALENCE.wrap(element));
            if (type == OBJECT) {
                pointers.add(JsonPointer.of(keyword, index));
                continue;
            }
            if (type != STRING) {
                report.error(newMsg(tree, bundle,
                    "common.array.element.incorrectType")
                    .putArgument("index", index)
                    .putArgument("expected", EnumSet.of(OBJECT, STRING))
                    .putArgument("found", type));
                continue;
            }
            if (!typeIsValid(element.textValue()))
                report.error(newMsg(tree, bundle,
                    "common.typeDisallow.primitiveType.unknown")
                    .put("index", index)
                    .putArgument("found", element.textValue())
                    .putArgument("valid", EnumSet.allOf(NodeType.class)));
        }

        if (!uniqueItems)
            report.error(newMsg(tree, bundle, "common.array.duplicateElements"));
    }

    private static boolean typeIsValid(String s)
    {
        return ANY.equals(s) || NodeType.fromName(s) != null;

    }
}
