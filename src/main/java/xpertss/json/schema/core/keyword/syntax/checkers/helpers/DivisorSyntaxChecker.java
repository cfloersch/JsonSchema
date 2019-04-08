package xpertss.json.schema.core.keyword.syntax.checkers.helpers;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.NodeType;
import com.github.fge.jackson.jsonpointer.JsonPointer;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.core.keyword.syntax.checkers.AbstractSyntaxChecker;
import xpertss.json.schema.core.tree.SchemaTree;
import com.github.fge.msgsimple.bundle.MessageBundle;

import java.math.BigDecimal;
import java.util.Collection;

/**
 * Helper class to check the syntax of {@code multipleOf} (draft v4) and {@code
 * divisibleBy} (draft v3)
 */
public final class DivisorSyntaxChecker extends AbstractSyntaxChecker {

    public DivisorSyntaxChecker(String keyword)
    {
        super(keyword, NodeType.INTEGER, NodeType.NUMBER);
    }

    @Override
    protected void checkValue(Collection<JsonPointer> pointers, MessageBundle bundle,
                                ProcessingReport report, SchemaTree tree)
        throws ProcessingException
    {
        JsonNode node = getNode(tree);
        BigDecimal divisor = node.decimalValue();

        if (divisor.compareTo(BigDecimal.ZERO) <= 0)
            report.error(newMsg(tree, bundle, "common.divisor.notPositive")
                .put("found", node));
    }
}
