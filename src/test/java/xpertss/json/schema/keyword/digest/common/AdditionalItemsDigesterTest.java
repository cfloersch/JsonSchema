package xpertss.json.schema.keyword.digest.common;


import com.github.fge.jackson.NodeType;

import java.io.IOException;

public final class AdditionalItemsDigesterTest extends CommonDigesterTest {
    
    public AdditionalItemsDigesterTest()
        throws IOException
    {
        super("additionalItems", NodeType.ARRAY);
    }
}
