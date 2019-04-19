package xpertss.json.schema.core.tree;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.MissingNode;
import com.github.fge.jackson.jsonpointer.JsonPointer;
import xpertss.json.schema.core.util.AsJson;

/**
 * A JSON value decorated with JSON Pointer information
 *
 * <p>This is a {@link JsonNode} with an internal path represented as a {@link
 * JsonPointer}. The current path and node are retrievable. If the current
 * pointer points to a non existent path in the document, the retrieved node is
 * a {@link MissingNode}.</p>
 *
 * @see JsonPointer
 */
public interface SimpleTree extends AsJson {
    
    /**
     * Return the node this tree was created with
     *
     * <p>Note: in current Jackson versions, this node is unfortunately mutable,
     * so be careful...</p>
     *
     * @return the node
     */
    JsonNode getBaseNode();

    /**
     * Get the current path into the document
     *
     * @return the path as a JSON Pointer
     */
    JsonPointer getPointer();

    /**
     * Get the node at the current path
     *
     * @return the matching node (a {@link MissingNode} if there is no matching
     * node at that pointer)
     */
    JsonNode getNode();
}
