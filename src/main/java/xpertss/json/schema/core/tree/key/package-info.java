
/**
 * JSON Schema keys (unique identifiers)
 *
 * <p>Classes in this package are closely related to {@link
 * xpertss.json.schema.core.tree.SchemaTree} instances; one schema tree
 * will have one {@link xpertss.json.schema.core.tree.key.SchemaKey}.</p>
 *
 * <p>Depending on the way you will have loaded your schema, the schema will be
 * either {@link xpertss.json.schema.core.tree.key.AnonymousSchemaKey
 * anonymous} or {@link xpertss.json.schema.core.tree.key.JsonRefSchemaKey
 * linked to a URI}.</p>
 *
 * <p>The fundamental difference between these two types of trees is that when
 * using an anonymous schema, all {@link
 * xpertss.json.schema.core.ref.JsonRef JSON Reference}s found in a
 * non-anonymous schema tree will be resolved against this tree's location; if
 * the schema is anonymous, references will be resolved against the empyt URI
 * reference (which leads to the reference itself being returned).</p>
 */
package xpertss.json.schema.core.tree.key;