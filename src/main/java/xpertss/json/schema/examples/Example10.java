package xpertss.json.schema.examples;

import com.fasterxml.jackson.databind.JsonNode;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.load.configuration.LoadingConfiguration;
import xpertss.json.schema.core.load.configuration.LoadingConfigurationBuilder;
import xpertss.json.schema.core.ref.JsonRef;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.main.JsonSchema;
import xpertss.json.schema.main.JsonSchemaFactory;

import java.io.IOException;

/**
 * Tenth example: registering schemas
 *
 * <p><a href="doc-files/Example10.java">link to source code</a></p>
 *
 * <p>In this example, we register a custom schema with a given URI, and
 * initiate the {@link JsonSchema} instance using that URI. This is done by
 * customizing a {@link LoadingConfiguration} and registering schemas using
 * {@link LoadingConfigurationBuilder#preloadSchema(String, JsonNode)}.</p>
 *
 * <p>The only necessary condition for the URI is for it to be an absolute JSON
 * reference (see {@link JsonRef#isAbsolute()}), and you can register as many
 * schemas as you want. Here, we register both schemas from {@link Example5}.
 * You will notice that the scheme for these URIs is {@code xxx}: it does not
 * matter in the slightest that it is not a supported scheme by default, the
 * schema is registered all the same.</p>
 *
 * <p>This also shows that reference resolution still works in such a case,
 * since the {@code mntent} schema is referred to via a relative URI from the
 * {@code fstab} schema.</p>
 *
 */
public final class Example10
{
    private static final String URI_BASE = "xxx://foo.bar/path/to/";

    public static void main(final String... args)
        throws IOException, ProcessingException
    {
        final LoadingConfigurationBuilder builder
            = LoadingConfiguration.newBuilder();

        JsonNode node;
        String uri;

        node = Utils.loadResource("/split/fstab.json");
        uri = URI_BASE + "fstab.json";
        builder.preloadSchema(uri, node);

        node = Utils.loadResource("/split/mntent.json");
        uri = URI_BASE + "mntent.json";
        builder.preloadSchema(uri, node);

        final JsonSchemaFactory factory = JsonSchemaFactory.newBuilder()
            .setLoadingConfiguration(builder.freeze()).freeze();

        final JsonSchema schema
            = factory.getJsonSchema(URI_BASE + "fstab.json");

        final JsonNode good = Utils.loadResource("/fstab-good.json");
        final JsonNode bad = Utils.loadResource("/fstab-bad.json");
        final JsonNode bad2 = Utils.loadResource("/fstab-bad2.json");

        ProcessingReport report;

        report = schema.validate(good);
        System.out.println(report);

        report = schema.validate(bad);
        System.out.println(report);

        report = schema.validate(bad2);
        System.out.println(report);
    }
}
