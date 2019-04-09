package xpertss.json.schema.core.ref;

import java.net.URI;

/**
 * Special case of a JSON Reference with a JAR URL
 *
 * <p>These URLs are legal URIs; trouble is, while they are absolute, they are
 * also opaque (meaning their path component does not start with a {@code /},
 * see {@link URI}).</p>
 *
 * <p>This class therefore adds a special case for URI resolution by extracting
 * the "real" path component out of the JAR URL and applying path resolution
 * against that extracted path. While this works, this is a violation of the
 * URI RFC.</p>
 *
 * @see HierarchicalJsonRef
 */
final class JarJsonRef extends JsonRef {

    /**
     * The URL part with the {@code !} included
     */
    private final String jarPrefix;

    /**
     * Everything after the {@code !}
     */
    private final URI pathURI;

    /**
     * Build a JSON Reference form a JAR URL
     *
     * @param uri the URI
     */
    JarJsonRef(URI uri)
    {
        super(uri);
        final String str = uri.toString();
        final int index = str.indexOf('!');
        jarPrefix = str.substring(0, index + 1);

        final String path = str.substring(index + 1);
        pathURI = URI.create(path);
    }

    /**
     * Specialized constructor used when resolving against a relative URI
     *
     * @param uri the final URI
     * @param jarPrefix the jar prefix
     * @param pathURI the path
     */
    private JarJsonRef(URI uri, String jarPrefix, URI pathURI)
    {
        super(uri);
        this.jarPrefix = jarPrefix;
        this.pathURI = pathURI;
    }

    @Override
    public boolean isAbsolute()
    {
        return legal && pointer.isEmpty();
    }

    @Override
    public JsonRef resolve(JsonRef other)
    {
        if (other.uri.isAbsolute())
            return other;

        URI targetPath = pathURI.resolve(other.uri);
        URI targetURI = URI.create(jarPrefix + targetPath.toString());
        return new JarJsonRef(targetURI, jarPrefix, targetPath);
    }
}
