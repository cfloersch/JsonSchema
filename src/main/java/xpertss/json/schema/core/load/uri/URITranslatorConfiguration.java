package xpertss.json.schema.core.load.uri;

import com.github.fge.Frozen;
import com.google.common.collect.ImmutableMap;

import java.net.URI;
import java.util.Map;

/**
 * Configuration for a {@link URITranslator}
 *
 * <p>This class configures all aspects of URI translation. In order to create
 * a new configuration, use {@link #newBuilder()}, or use {@link #byDefault()}
 * if you want a default, no-op, translation configuration.</p>
 *
 * @see URITranslatorConfigurationBuilder
 */
public final class URITranslatorConfiguration implements Frozen<URITranslatorConfigurationBuilder> {

    final URI namespace;
    final Map<URI, URI> pathRedirects;
    final Map<URI, URI> schemaRedirects;

    /**
     * Create a new configuration builder
     *
     * @return a new {@link URITranslatorConfigurationBuilder}
     */
    public static URITranslatorConfigurationBuilder newBuilder()
    {
        return new URITranslatorConfigurationBuilder();
    }

    /**
     * Create a default configuration
     *
     * <p>The default configuration has no namespace, no path redirections and
     * no schema redirections.</p>
     *
     * @return a default configuration
     */
    public static URITranslatorConfiguration byDefault()
    {
        return newBuilder().freeze();
    }

    URITranslatorConfiguration(URITranslatorConfigurationBuilder builder)
    {
        namespace = builder.namespace;
        pathRedirects = ImmutableMap.copyOf(builder.pathRedirects.build());
        schemaRedirects = ImmutableMap.copyOf(builder.schemaRedirects.build());
    }

    @Override
    public URITranslatorConfigurationBuilder thaw()
    {
        return new URITranslatorConfigurationBuilder(this);
    }
}
