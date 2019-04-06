package xpertss.json.schema.keyword.digest.draftv3;


import com.github.fge.jackson.NodeType;

import java.io.IOException;

public final class DraftV3DependenciesDigesterTest
    extends DraftV3DigesterTest
{
    public DraftV3DependenciesDigesterTest()
        throws IOException
    {
        super("dependencies", NodeType.OBJECT);
    }
}
