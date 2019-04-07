package xpertss.json.schema.keyword.digest.common;


import com.github.fge.jackson.NodeType;

import java.io.IOException;

public final class AdditionalPropertiesDigesterTest extends CommonDigesterTest {
    
    public AdditionalPropertiesDigesterTest()
        throws IOException
    {
        super("additionalProperties", NodeType.OBJECT);
    }
}
