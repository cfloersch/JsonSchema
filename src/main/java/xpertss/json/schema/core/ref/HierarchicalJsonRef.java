package xpertss.json.schema.core.ref;

import java.net.URI;

/**
 * JSON Reference for classical, hierarchical URIs
 *
 * <p>A hierarchical URI is defined as a URI which is either not absolute, or
 * which is absolute but not opaque. Resolution of such URIs can therefore
 * proceed as described in <a href="http://tools.ietf.org/html/rfc3986">RFC 3986
 * </a>.</p>
 *
 * <p>An example of URIs which are both absolute and opaque are jar URLs, which
 * have a dedicated class for this reason ({@link JarJsonRef}).</p>
 */
final class HierarchicalJsonRef extends JsonRef {

    HierarchicalJsonRef(final URI uri)
    {
        super(uri);
    }

    @Override
    public boolean isAbsolute()
    {
        if (!legal) return false;
        return locator.isAbsolute() && pointer.isEmpty();
    }

    @Override
    public JsonRef resolve(JsonRef other)
    {
        return fromURI(uri.resolve(other.uri));
    }
}
