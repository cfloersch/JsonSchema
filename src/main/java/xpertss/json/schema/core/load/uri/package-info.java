/**
 * URI translation
 *
 * <p>A URI translation happens each time you load a schema from a URI, either
 * from user code or because of a JSON Reference encountered in a schema.</p>
 *
 * <p>You have three ways to alter URIs:</p>
 *
 * <ul>
 *     <li>resolve against a default namespace;</li>
 *     <li>perform path redirections;</li>
 *     <li>perform schema URI redirections.</li>
 * </ul>
 *
 * <p>See {@link xpertss.json.schema.core.load.uri.URITranslatorConfigurationBuilder}
 * for more details.</p>
 */
package xpertss.json.schema.core.load.uri;