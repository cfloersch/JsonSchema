package xpertss.json.schema.core.util;

import com.fasterxml.jackson.databind.JsonNode;
import xpertss.json.schema.core.tree.SchemaTree;

/**
 * Interface implemented by classes having a JSON representation
 *
 * <p>This representation needs not be complete. For instance, {@link
 * SchemaTree} implements it to provide an object with the summary of its main
 * characteristics (loading URI, current pointer).</p>
 */
public interface AsJson {
    
    /**
     * Return a JSON representation of this object
     *
     * @return a {@link JsonNode}
     */
    JsonNode asJson();
}
