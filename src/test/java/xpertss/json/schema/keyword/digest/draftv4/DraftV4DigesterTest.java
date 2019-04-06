package xpertss.json.schema.keyword.digest.draftv4;

import com.github.fge.jackson.NodeType;
import xpertss.json.schema.keyword.digest.AbstractDigesterTest;
import xpertss.json.schema.library.digest.DraftV4DigesterDictionary;

import java.io.IOException;

public abstract class DraftV4DigesterTest
    extends AbstractDigesterTest
{
    protected DraftV4DigesterTest(final String keyword, final NodeType first,
        final NodeType... other)
        throws IOException
    {
        super(DraftV4DigesterDictionary.get(), "draftv4", keyword, first,
            other);
    }
}
