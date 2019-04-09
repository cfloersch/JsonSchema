/**
 * Schema loading configuration
 *
 * <p>Many aspects of schema loading are configurable.</p>
 *
 * <p>For starters, you can configure your loader to read even malformed JSON
 * inputs. You can therefore have schemas with comments etc, even though JSON
 * normally does not allow them. This is done using Jackson's parser features.
 * </p>
 *
 * <p>You can also configure the way schema loading will resolve URIs to your
 * schemas: set a default URI namespace, silently redirect schema URIs to other
 * URIs or even whole URI hierarchies. You can also add or remove support for
 * URI schemes.</p>
 *
 * <p>Finally, this is also where you decide whether you will use canonical
 * dereferencing or inline dereferencing. The author recommends the former!</p>
 *
 * <p>The default loading configuration (obtained using {@link
 * xpertss.json.schema.core.load.configuration.LoadingConfiguration#byDefault()})
 * will use the default set of supported URI schemes</p>
 */
package xpertss.json.schema.core.load.configuration;

