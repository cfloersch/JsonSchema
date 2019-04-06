package xpertss.json.schema.core.messages;

import com.github.fge.msgsimple.bundle.MessageBundle;
import com.github.fge.msgsimple.bundle.PropertiesBundle;
import com.github.fge.msgsimple.load.MessageBundleLoader;

public final class JsonSchemaSyntaxMessageBundle
    implements MessageBundleLoader
{
    @Override
    public MessageBundle getBundle()
    {
        return PropertiesBundle
            .forPath("xpertss/json/schema/core/syntax");
    }
}
