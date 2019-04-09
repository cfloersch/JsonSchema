package xpertss.json.schema.core.load.uri;

import xpertss.json.schema.core.load.SchemaLoader;
import xpertss.json.schema.core.ref.JsonRef;
import xpertss.json.schema.core.util.URIUtils;
import com.google.common.collect.ImmutableMap;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

/**
 * URI translation
 *
 * <p>When it is required that a URI be dereferenced (either by yourself,
 * using {@link SchemaLoader#get(URI)}, or when a JSON Reference is encountered
 * in a JSON Schema), this class is in charge of translating the
 * <em>resolved</em> URI into a more suitable URI for your environment.</p>
 *
 * <p>Translation is done in three steps:</p>
 *
 * <ul>
 *     <li>resolving against the default namespace,</li>
 *     <li>translating the path to another one (if applicable),</li>
 *     <li>translating the full schema URI to another one (if applicable).</li>
 * </ul>
 *
 * <p>By default, the namespace is empty and no path or schema translations are
 * defined.</p>
 *
 * @see URITranslatorConfiguration
 */
public final class URITranslator {

    private final URI namespace;
    private final Map<URI, URI> pathRedirects;
    private final Map<URI, URI> schemaRedirects;

    public URITranslator(URITranslatorConfiguration cfg)
    {
        namespace = cfg.namespace;
        pathRedirects = ImmutableMap.copyOf(cfg.pathRedirects);
        schemaRedirects = ImmutableMap.copyOf(cfg.schemaRedirects);
    }

    public URI translate(URI source)
    {
        URI uri = URIUtils.normalizeURI(namespace.resolve(source));
        String fragment = uri.getFragment();

        try {
            uri = new URI(uri.getScheme(), uri.getSchemeSpecificPart(), null);
        } catch (URISyntaxException e) {
            throw new IllegalStateException("How did I get there??", e);
        }

        for (final Map.Entry<URI, URI> entry: pathRedirects.entrySet()) {
            final URI relative = entry.getKey().relativize(uri);
            if (!relative.equals(uri))
                uri = entry.getValue().resolve(relative);
        }

        uri = JsonRef.fromURI(uri).getLocator();

        if (schemaRedirects.containsKey(uri))
            uri = schemaRedirects.get(uri);

        try {
            return new URI(uri.getScheme(), uri.getSchemeSpecificPart(), fragment);
        } catch (URISyntaxException e) {
            throw new IllegalStateException("How did I get there??", e);
        }
    }
}
