package xpertss.json.schema.keyword.digest.draftv4;


import com.github.fge.jackson.NodeType;

import java.io.IOException;

public final class DraftV4DependenciesDigesterTest
    extends DraftV4DigesterTest
{
    public DraftV4DependenciesDigesterTest()
        throws IOException
    {
        super("dependencies", NodeType.OBJECT);
    }
}
