package xpertss.json.schema;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.github.fge.jackson.JacksonUtils;
import com.github.fge.jackson.NodeType;
import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.math.BigDecimal;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Map;

public final class SampleNodeProvider
{
    private static final JsonNodeFactory FACTORY = JacksonUtils.nodeFactory();
    private static final Map<NodeType, JsonNode> SAMPLE_DATA;

    private SampleNodeProvider()
    {
    }

    static {
        SAMPLE_DATA = Maps.newEnumMap(NodeType.class);

        SAMPLE_DATA.put(NodeType.ARRAY, FACTORY.arrayNode());
        SAMPLE_DATA.put(NodeType.BOOLEAN, FACTORY.booleanNode(true));
        SAMPLE_DATA.put(NodeType.INTEGER, FACTORY.numberNode(0));
        SAMPLE_DATA.put(NodeType.NULL, FACTORY.nullNode());
        SAMPLE_DATA.put(NodeType.NUMBER,
            FACTORY.numberNode(new BigDecimal("1.1")));
        SAMPLE_DATA.put(NodeType.OBJECT, FACTORY.objectNode());
        SAMPLE_DATA.put(NodeType.STRING, FACTORY.textNode(""));
    }


    // FIXME: IDEA warns about "overloaded vararg method" even though the types
    // differ...
    public static Iterator<Object[]> getSamples(final EnumSet<NodeType> types)
    {
        final Map<NodeType, JsonNode> map = Maps.newEnumMap(SAMPLE_DATA);
        map.keySet().retainAll(types);

        return FluentIterable.from(map.values())
            .transform(new Function<JsonNode, Object[]>()
            {
                @Override
                public Object[] apply(final JsonNode input)
                {
                    return new Object[] { input };
                }
            }).iterator();
    }

    public static Iterator<Object[]> getSamplesExcept(
        final EnumSet<NodeType> types)
    {
        return getSamples(EnumSet.complementOf(types));
    }

    public static Iterator<Object[]> getSamples(final NodeType first,
        final NodeType... other)
    {
        return getSamples(EnumSet.of(first, other));
    }

    public static Iterator<Object[]> getSamplesExcept(final NodeType first,
        final NodeType... other)
    {
        return getSamples(Sets.complementOf(EnumSet.of(first, other)));
    }
}
