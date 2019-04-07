package xpertss.json.schema.keyword.digest.common;


import com.github.fge.jackson.NodeType;

import java.io.IOException;

public final class MaxLengthDigesterTest extends CommonDigesterTest {
    
    public MaxLengthDigesterTest()
        throws IOException
    {
        super("maxLength", NodeType.STRING);
    }
}
