/**
 * Navigable JSON tree representations
 *
 * <p>Classes in this package are wrappers over {@link
 * com.fasterxml.jackson.databind.JsonNode} instances with navigation
 * capabilities (using {@link
 * com.github.fge.jackson.jsonpointer.JsonPointer}).</p>
 *
 * <p>A JSON Schema is represented by a {@link
 * xpertss.json.schema.core.tree.SchemaTree} and, in addition to navigation
 * capabilities, offers other information such as the current URI context
 * defined by that schema and JSON Reference resolution.</p>
 */
package xpertss.json.schema.core.tree;
