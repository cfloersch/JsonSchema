package xpertss.json.schema.examples;

import com.fasterxml.jackson.databind.JsonNode;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.load.SchemaLoader;
import xpertss.json.schema.core.load.configuration.LoadingConfiguration;
import xpertss.json.schema.core.load.configuration.LoadingConfigurationBuilder;
import xpertss.json.schema.core.load.uri.URITranslatorConfiguration;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.main.JsonSchema;
import xpertss.json.schema.main.JsonSchemaFactory;
import xpertss.json.schema.main.JsonSchemaFactoryBuilder;

import java.io.IOException;

/**
 * Fifth example: setting a URI namespace; relative URI resolution
 *
 * <p><a href="doc-files/Example5.java">link to source code</a></p>
 *
 * <p>This example demonstrates another capability of {@link JsonSchemaFactory}:
 * the ability to set a URI namespace. This requires to customize the factory,
 * and therefore go through {@link JsonSchemaFactoryBuilder} again.</p>
 *
 * <p>In order to set a URI namespace, we must grab a {@link
 * LoadingConfigurationBuilder}, set the namespace, freeze it, and pass it to
 * the factory builder and then freeze the factory.</p>
 *
 * <p>The net effect is that all schema loading done by {@link SchemaLoader}
 * will now resolve against this namespace, and this includes arguments to
 * {@link JsonSchemaFactory#getJsonSchema(String)}.</p>
 *
 * <p>The schemas are split in two:</p>
 *
 * <ul>
 *     <li>one describing fstab: <a href="doc-files/split/fstab.json">here</a>;
 *     </li>
 *     <li>another describing an entry: <a href="doc-files/split/mntent.json">
 *     here</a>.</li>
 * </ul>
 *
 * <p>The first refers to the second one via the relative URI {@code
 * mntent.json}. This works precisely because a URI namespace has been set: all
 * URIs are resolved against this namespace.</p>
 *
 * <p>Files validated, and the validation outputs, are the same as for {@link
 * Example2}.</p>
 */
public final class Example5
{
    private static final String NAMESPACE
        = "resource:/com/github/fge/jsonschema/examples/split/";

    public static void main(final String... args)
        throws IOException, ProcessingException
    {
        final JsonNode good = Utils.loadResource("/fstab-good.json");
        final JsonNode bad = Utils.loadResource("/fstab-bad.json");
        final JsonNode bad2 = Utils.loadResource("/fstab-bad2.json");

        final URITranslatorConfiguration translatorCfg
            = URITranslatorConfiguration.newBuilder()
            .setNamespace(NAMESPACE).freeze();
        final LoadingConfiguration cfg = LoadingConfiguration.newBuilder()
            .setURITranslatorConfiguration(translatorCfg).freeze();

        final JsonSchemaFactory factory = JsonSchemaFactory.newBuilder()
            .setLoadingConfiguration(cfg).freeze();

        final JsonSchema schema = factory.getJsonSchema("fstab.json");

        ProcessingReport report;

        report = schema.validate(good);
        System.out.println(report);

        report = schema.validate(bad);
        System.out.println(report);

        report = schema.validate(bad2);
        System.out.println(report);
    }
}
