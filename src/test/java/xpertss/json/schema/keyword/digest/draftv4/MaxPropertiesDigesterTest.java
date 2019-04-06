package xpertss.json.schema.keyword.digest.draftv4;


import com.github.fge.jackson.NodeType;

import java.io.IOException;

public final class MaxPropertiesDigesterTest
    extends DraftV4DigesterTest
{
    public MaxPropertiesDigesterTest()
        throws IOException
    {
        super("maxProperties", NodeType.OBJECT);
    }
}
