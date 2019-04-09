/**
 * Schema loading and JSON Reference resolving
 *
 * <p>This package contains all components necessary to load and preload JSON
 * schemas, along with the processor in charge of JSON Reference resolving
 * ({@link xpertss.json.schema.core.load.RefResolver}).</p>
 *
 * <p>The main loading class is {@link
 * xpertss.json.schema.core.load.SchemaLoader}. It relies on downloaders
 * configured in a {@link xpertss.json.schema.core.load.URIManager} to
 * load schemas it does not already know of.</p>
 *
 * <p>Note that you can configure the latter to support an arbitrary set of URI
 * schemes, or remove support for schemes you don't want to support (for
 * security reasons or otherwise).</p>
 *
 * <p>Configuring schema loading is done using a {@link
 * xpertss.json.schema.core.load.configuration.LoadingConfiguration}. URI
 * resolving and loading is done using a {@link
 * xpertss.json.schema.core.load.uri.URITranslatorConfiguration}.</p>
 */
package xpertss.json.schema.core.load;
