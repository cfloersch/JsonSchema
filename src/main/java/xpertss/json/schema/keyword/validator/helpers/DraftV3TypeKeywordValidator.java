package xpertss.json.schema.keyword.validator.helpers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.github.fge.jackson.JacksonUtils;
import com.github.fge.jackson.NodeType;
import xpertss.json.schema.keyword.validator.AbstractKeywordValidator;
import com.google.common.collect.Lists;

import java.util.EnumSet;
import java.util.List;

/**
 * Helper keyword validator for draft v3's {@code type} and {@code disallow}
 *
 * <p>Their validation logic differ, however their digest is the same; therefore
 * they are built in the same way.</p>
 */
public abstract class DraftV3TypeKeywordValidator
    extends AbstractKeywordValidator
{
    protected static final JsonNodeFactory FACTORY = JacksonUtils.nodeFactory();

    protected final EnumSet<NodeType> types = EnumSet.noneOf(NodeType.class);
    protected final List<Integer> schemas = Lists.newArrayList();

    protected DraftV3TypeKeywordValidator(final String keyword,
        final JsonNode digested)
    {
        super(keyword);
        for (final JsonNode element: digested.get(keyword))
            types.add(NodeType.fromName(element.textValue()));
        for (final JsonNode element: digested.get("schemas"))
            schemas.add(element.intValue());
    }

    @Override
    public final String toString()
    {
        return keyword + ": " + types + "; " + schemas.size() + " schemas";
    }
}
