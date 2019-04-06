package xpertss.json.schema.keyword.digest.common;


import com.github.fge.jackson.NodeType;

import java.io.IOException;

public final class EnumDigesterTest
    extends CommonDigesterTest
{
    public EnumDigesterTest()
        throws IOException
    {
        super("enum", NodeType.ARRAY, NodeType.values());
    }
}
