package xpertss.json.schema.keyword.digest;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.github.fge.jackson.JacksonUtils;
import com.github.fge.jackson.NodeType;

import java.util.EnumSet;

/**
 * Base abstract digester class for all keyword digesters
 */
public abstract class AbstractDigester implements Digester {

    protected static final JsonNodeFactory FACTORY = JacksonUtils.nodeFactory();
    private final EnumSet<NodeType> types;
    protected final String keyword;

    protected AbstractDigester(String keyword, NodeType first, NodeType... other)
    {
        this.keyword = keyword;
        types = EnumSet.of(first, other);
    }

    @Override
    public final EnumSet<NodeType> supportedTypes()
    {
        return EnumSet.copyOf(types);
    }

    @Override
    public final String toString()
    {
        return "digester for keyword \"" + keyword + '"';
    }
}
