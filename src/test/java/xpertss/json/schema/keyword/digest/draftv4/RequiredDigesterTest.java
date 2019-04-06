package xpertss.json.schema.keyword.digest.draftv4;


import com.github.fge.jackson.NodeType;

import java.io.IOException;

public final class RequiredDigesterTest
    extends DraftV4DigesterTest
{
    public RequiredDigesterTest()
        throws IOException
    {
        super("required", NodeType.OBJECT);
    }
}
