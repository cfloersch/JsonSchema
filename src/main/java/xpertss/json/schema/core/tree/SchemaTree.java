package xpertss.json.schema.core.tree;

import com.github.fge.jackson.jsonpointer.JsonPointer;
import xpertss.json.schema.core.ref.JsonRef;
import xpertss.json.schema.core.tree.key.SchemaKey;

import javax.annotation.Nullable;

/**
 * Tree representation of a JSON Schema
 *
 * <p>In addition to navigation capabilities and node retrieval, this tree
 * returns URI context information and JSON Reference resolution.</p>
 */
public interface SchemaTree extends SimpleTree {

    /**
     * Relocate the tree relatively to the current tree's pointer
     *
     * @param pointer the pointer to append
     * @return a new tree
     * @see JsonPointer#append(JsonPointer)
     */
    SchemaTree append(JsonPointer pointer);

    /**
     * Relocate the tree with an absolute pointer
     *
     * @param pointer the pointer
     * @return a new tree
     */
    SchemaTree setPointer(JsonPointer pointer);

    /**
     * Resolve a JSON Reference against the current resolution context
     *
     * @param other the JSON Reference to resolve
     * @return the resolved reference
     * @see JsonRef#resolve(JsonRef)
     */
    JsonRef resolve(JsonRef other);

    /**
     * Tell whether a JSON Reference is contained within this schema tree
     *
     * <p>This method will return {@code true} if the caller can <i>attempt</i>
     * to retrieve the JSON value addressed by this reference from the schema
     * tree directly.</p>
     *
     * <p>Note that the reference <b>must</b> be fully resolved for this method
     * to work.</p>
     *
     * @param ref the target reference
     * @return see description
     * @see #resolve(JsonRef)
     */
    boolean containsRef(JsonRef ref);

    /**
     * Return a matching pointer in this tree for a fully resolved reference
     *
     * <p>This must be called <b>only</b> when {@link #containsRef(JsonRef)}
     * returns {@code true}. Otherwise, its result is undefined.</p>
     *
     * @param ref the reference
     * @return the matching pointer, or {@code null} if not found
     */
    @Nullable
    JsonPointer matchingPointer(JsonRef ref);

    /**
     * DO NOT USE!
     *
     * @return something...
     *
     * @deprecated superseded by {@link SchemaKey}
     */
    @Deprecated
    long getId();

    /**
     * Return the metaschema URI for that schema (ie, {@code $schema})
     *
     * <p>Note: it is <b>required</b> that if present, {@code $schema} be an
     * absolute JSON Reference. If this keyword is not present and/or is
     * malformed, an empty reference is returned.</p>
     *
     * @return the contents of {@code $schema} as a {@link JsonRef}
     */
    JsonRef getDollarSchema();

    /**
     * Get the loading URI for that schema
     *
     * @return the loading URI as a {@link JsonRef}
     */
    JsonRef getLoadingRef();

    /**
     * Get the current resolution context
     *
     * @return the context as a {@link JsonRef}
     */
    JsonRef getContext();
}
