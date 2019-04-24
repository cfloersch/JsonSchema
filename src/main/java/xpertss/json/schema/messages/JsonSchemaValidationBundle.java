package xpertss.json.schema.messages;

import com.github.fge.msgsimple.bundle.MessageBundle;
import com.github.fge.msgsimple.bundle.PropertiesBundle;
import com.github.fge.msgsimple.load.MessageBundleLoader;

public final class JsonSchemaValidationBundle implements MessageBundleLoader {

    private static final String PATH = "xpertss/json/schema/validator/validation.properties";

    @Override
    public MessageBundle getBundle()
    {
        return PropertiesBundle.forPath(PATH);
    }
}
