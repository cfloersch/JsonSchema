package xpertss.json.schema.keyword.digest.draftv4;


import com.github.fge.jackson.NodeType;

import java.io.IOException;

public final class AnyOfDigesterTest
    extends DraftV4DigesterTest
{
    public AnyOfDigesterTest()
        throws IOException
    {
        super("anyOf", NodeType.ARRAY, NodeType.values());
    }
}
