package xpertss.json.schema.keyword.digest.draftv3;


import com.github.fge.jackson.NodeType;

import java.io.IOException;

public final class ExtendsDigesterTest
    extends DraftV3DigesterTest
{
    public ExtendsDigesterTest()
        throws IOException
    {
        super("extends", NodeType.ARRAY, NodeType.values());
    }
}
