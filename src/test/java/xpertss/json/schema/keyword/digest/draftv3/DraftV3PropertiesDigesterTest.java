package xpertss.json.schema.keyword.digest.draftv3;


import com.github.fge.jackson.NodeType;

import java.io.IOException;

public final class DraftV3PropertiesDigesterTest
    extends DraftV3DigesterTest
{
    public DraftV3PropertiesDigesterTest()
        throws IOException
    {
        super("properties", NodeType.OBJECT);
    }
}
