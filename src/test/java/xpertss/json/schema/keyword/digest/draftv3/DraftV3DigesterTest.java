package xpertss.json.schema.keyword.digest.draftv3;

import com.github.fge.jackson.NodeType;
import xpertss.json.schema.keyword.digest.AbstractDigesterTest;
import xpertss.json.schema.library.digest.DraftV3DigesterDictionary;

import java.io.IOException;

public abstract class DraftV3DigesterTest
    extends AbstractDigesterTest
{
    protected DraftV3DigesterTest(final String keyword, final NodeType first,
        final NodeType... other)
        throws IOException
    {
        super(DraftV3DigesterDictionary.get(), "draftv3", keyword, first,
            other);
    }
}
