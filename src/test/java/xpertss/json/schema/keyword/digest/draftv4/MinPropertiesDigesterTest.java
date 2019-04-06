package xpertss.json.schema.keyword.digest.draftv4;


import com.github.fge.jackson.NodeType;

import java.io.IOException;

public final class MinPropertiesDigesterTest
    extends DraftV4DigesterTest
{
    public MinPropertiesDigesterTest()
        throws IOException
    {
        super("minProperties", NodeType.OBJECT);
    }
}
