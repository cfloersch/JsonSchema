package xpertss.json.schema.keyword.digest.common;


import com.github.fge.jackson.NodeType;

import java.io.IOException;

public final class PatternDigesterTest
    extends CommonDigesterTest
{
    public PatternDigesterTest()
        throws IOException
    {
        super("pattern", NodeType.STRING);
    }
}
