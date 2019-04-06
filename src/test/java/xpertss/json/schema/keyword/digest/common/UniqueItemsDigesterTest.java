package xpertss.json.schema.keyword.digest.common;


import com.github.fge.jackson.NodeType;

import java.io.IOException;

public final class UniqueItemsDigesterTest
    extends CommonDigesterTest
{
    public UniqueItemsDigesterTest()
        throws IOException
    {
        super("uniqueItems", NodeType.ARRAY);
    }
}
