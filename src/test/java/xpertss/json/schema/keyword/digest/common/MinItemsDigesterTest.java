package xpertss.json.schema.keyword.digest.common;


import com.github.fge.jackson.NodeType;

import java.io.IOException;

public final class MinItemsDigesterTest
    extends CommonDigesterTest
{
    public MinItemsDigesterTest()
        throws IOException
    {
        super("minItems", NodeType.ARRAY);
    }
}
