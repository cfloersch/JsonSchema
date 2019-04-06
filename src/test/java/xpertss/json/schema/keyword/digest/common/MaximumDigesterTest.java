package xpertss.json.schema.keyword.digest.common;


import com.github.fge.jackson.NodeType;

import java.io.IOException;

public final class MaximumDigesterTest
    extends CommonDigesterTest
{
    public MaximumDigesterTest()
        throws IOException
    {
        super("maximum", NodeType.INTEGER, NodeType.NUMBER);
    }
}
