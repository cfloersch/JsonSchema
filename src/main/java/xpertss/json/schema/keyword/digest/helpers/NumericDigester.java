package xpertss.json.schema.keyword.digest.helpers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jackson.NodeType;
import xpertss.json.schema.keyword.digest.AbstractDigester;
import xpertss.json.schema.keyword.validator.helpers.NumericValidator;

import java.math.BigDecimal;

/**
 * A specialized digester for numeric keywords
 *
 * <p>This digester ensures that, for instance, values {@code 1}, {@code 1.0}
 * and {@code 1.00} produce the same digest. It also stores another important
 * information: whether that number can be reliably represented as a {@code
 * long}. If this is not the case, for accuracy reasons, {@link BigDecimal} is
 * used.</p>
 *
 * @see NumericValidator
 */
public abstract class NumericDigester extends AbstractDigester {

    protected NumericDigester(String keyword)
    {
        super(keyword, NodeType.INTEGER, NodeType.NUMBER);
    }

    private static boolean valueIsLong(JsonNode node)
    {
        if (!node.canConvertToLong())
            return false;

        if (NodeType.getNodeType(node) == NodeType.INTEGER)
            return true;

        return node.decimalValue().remainder(BigDecimal.ONE)
            .compareTo(BigDecimal.ZERO) == 0;
    }

    protected final ObjectNode digestedNumberNode(JsonNode schema)
    {
        ObjectNode ret = FACTORY.objectNode();

        JsonNode node = schema.get(keyword);
        boolean isLong = valueIsLong(node);
        ret.put("valueIsLong", isLong);

        if (isLong) {
            ret.put(keyword, node.canConvertToInt()
                ? FACTORY.numberNode(node.intValue())
                : FACTORY.numberNode(node.longValue()));
            return ret;
        }

        BigDecimal decimal = node.decimalValue();
        ret.put(keyword, decimal.scale() == 0
            ? FACTORY.numberNode(decimal.toBigIntegerExact())
            : node);

        return ret;
    }
}
