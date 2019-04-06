package xpertss.json.schema.keyword.digest.common;


import com.github.fge.jackson.NodeType;

import java.io.IOException;

public final class MaxItemsDigesterTest
    extends CommonDigesterTest
{
    public MaxItemsDigesterTest()
        throws IOException
    {
        super("maxItems", NodeType.ARRAY);
    }
}
