package xpertss.json.schema;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import xpertss.json.schema.core.ref.JsonRef;

import java.io.IOException;
import java.net.URI;

/**
 * JSON Schema versions
 *
 * <p>Members of this enum offer two informations about JSON Schemas:</p>
 *
 * <ul>
 *     <li>their location (what is used in {@code $schema}),</li>
 *     <li>the meta schema (as a {@link JsonNode}).</li>
 * </ul>
 */
public enum SchemaVersion
{
    /**
     * Draft v4 (default version)
     */
    DRAFTV7("http://json-schema.org/draft-07/schema#", "/draftv7/schema"),
    /**
     * Draft v4 (default version)
     */
    DRAFTV6("http://json-schema.org/draft-06/schema#", "/draftv6/schema"),

    /**
     * Draft v4 hyperschema
     */
    DRAFTV4_HYPERSCHEMA("http://json-schema.org/draft-04/hyper-schema#", "/draftv4/hyper-schema"),
    /**
     * Draft v4 (default version)
     */
    DRAFTV4("http://json-schema.org/draft-04/schema#", "/draftv4/schema"),

    /**
     * Draft v3
     */
    DRAFTV3("http://json-schema.org/draft-03/schema#", "/draftv3/schema"),
    ;

    private final URI location;
    private final JsonNode schema;

    SchemaVersion(final String uri, final String resource)
    {
        try {
            location = URI.create(uri);
            schema = JsonLoader.fromResource(resource);
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    /**
     * Return the value of {@code $schema} as a {@link JsonRef}
     *
     * @return the JSON Reference for that schema version
     */
    public URI getLocation()
    {
        return location;
    }

    /**
     * Return the meta schema as JSON
     *
     * <p>Note: since {@link JsonNode} is mutable, this method returns a copy.
     * </p>
     *
     * @return the meta schema
     * @see JsonNode#deepCopy()
     */
    public JsonNode getSchema()
    {
        return schema.deepCopy();
    }
}
