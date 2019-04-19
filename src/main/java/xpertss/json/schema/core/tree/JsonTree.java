package xpertss.json.schema.core.tree;

import com.github.fge.jackson.jsonpointer.JsonPointer;

/**
 * A {@link SimpleTree} with a navigation operation
 */
public interface JsonTree extends SimpleTree {

    /**
     * Append a JSON pointer to that tree and return a new tree
     *
     * @param pointer the pointer
     * @return a new tree, with the pointer appended to the current pointer
     */
    JsonTree append(JsonPointer pointer);
}

