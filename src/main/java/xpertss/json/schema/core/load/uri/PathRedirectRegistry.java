package xpertss.json.schema.core.load.uri;

import xpertss.json.schema.core.messages.JsonSchemaCoreMessageBundle;
import xpertss.json.schema.core.util.Registry;
import xpertss.json.schema.core.util.URIUtils;
import com.github.fge.msgsimple.bundle.MessageBundle;
import com.github.fge.msgsimple.load.MessageBundles;

import java.net.URI;

final class PathRedirectRegistry
    extends Registry<URI, URI>
{
    private static final MessageBundle BUNDLE
        = MessageBundles.getBundle(JsonSchemaCoreMessageBundle.class);

    PathRedirectRegistry()
    {
        super(URIUtils.uriNormalizer(), URIUtils.pathURIChecker(),
            URIUtils.uriNormalizer(), URIUtils.pathURIChecker());
    }

    @Override
    protected void checkEntry(final URI key, final URI value)
    {
        BUNDLE.checkArgumentFormat(!key.equals(value),
            "pathRedirect.selfRedirect", key);
    }
}
