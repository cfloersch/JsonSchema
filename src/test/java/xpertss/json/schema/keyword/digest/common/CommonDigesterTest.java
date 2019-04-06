package xpertss.json.schema.keyword.digest.common;

import com.github.fge.jackson.NodeType;
import xpertss.json.schema.keyword.digest.AbstractDigesterTest;
import xpertss.json.schema.library.digest.CommonDigesterDictionary;

import java.io.IOException;

public abstract class CommonDigesterTest
    extends AbstractDigesterTest
{
    protected CommonDigesterTest(final String keyword, final NodeType first,
        final NodeType... other)
        throws IOException
    {
        super(CommonDigesterDictionary.get(), "common", keyword, first, other);
    }
}
