package xpertss.json.schema.keyword.digest.draftv4;


import com.github.fge.jackson.NodeType;

import java.io.IOException;

public final class MultipleOfDigesterTest
    extends DraftV4DigesterTest
{
    public MultipleOfDigesterTest()
        throws IOException
    {
        super("multipleOf", NodeType.INTEGER, NodeType.NUMBER);
    }
}
