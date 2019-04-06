package xpertss.json.schema.keyword.digest.draftv4;


import com.github.fge.jackson.NodeType;

import java.io.IOException;

public final class OneOfDigesterTest
    extends DraftV4DigesterTest
{
    public OneOfDigesterTest()
        throws IOException
    {
        super("oneOf", NodeType.ARRAY, NodeType.values());
    }
}
