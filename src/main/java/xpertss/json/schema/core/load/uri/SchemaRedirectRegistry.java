package xpertss.json.schema.core.load.uri;

import xpertss.json.schema.core.messages.JsonSchemaCoreMessageBundle;
import xpertss.json.schema.core.util.Registry;
import xpertss.json.schema.core.util.URIUtils;
import com.github.fge.msgsimple.bundle.MessageBundle;
import com.github.fge.msgsimple.load.MessageBundles;

import java.net.URI;

final class SchemaRedirectRegistry extends Registry<URI, URI> {

    private static final MessageBundle BUNDLE = MessageBundles.getBundle(JsonSchemaCoreMessageBundle.class);

    SchemaRedirectRegistry()
    {
        super(URIUtils.schemaURINormalizer(), URIUtils.schemaURIChecker(),
            URIUtils.schemaURINormalizer(), URIUtils.schemaURIChecker());
    }

    @Override
    protected void checkEntry(URI key, URI value)
    {
        BUNDLE.checkArgumentFormat(!key.equals(value), "schemaRedirect.selfRedirect", key);
    }
}
