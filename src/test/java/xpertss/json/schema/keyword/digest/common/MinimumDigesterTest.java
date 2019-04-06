package xpertss.json.schema.keyword.digest.common;


import com.github.fge.jackson.NodeType;

import java.io.IOException;

public final class MinimumDigesterTest
    extends CommonDigesterTest
{
    public MinimumDigesterTest()
        throws IOException
    {
        super("minimum", NodeType.INTEGER, NodeType.NUMBER);
    }
}
