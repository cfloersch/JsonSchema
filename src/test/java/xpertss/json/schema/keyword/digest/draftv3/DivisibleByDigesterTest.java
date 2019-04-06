package xpertss.json.schema.keyword.digest.draftv3;


import com.github.fge.jackson.NodeType;

import java.io.IOException;

public final class DivisibleByDigesterTest
    extends DraftV3DigesterTest
{
    public DivisibleByDigesterTest()
        throws IOException
    {
        super("divisibleBy", NodeType.INTEGER, NodeType.NUMBER);
    }
}
