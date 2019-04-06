package xpertss.json.schema.keyword.digest.common;


import com.github.fge.jackson.NodeType;

import java.io.IOException;

public final class MinLengthDigesterTest
    extends CommonDigesterTest
{
    public MinLengthDigesterTest()
        throws IOException
    {
        super("minLength", NodeType.STRING);
    }
}
