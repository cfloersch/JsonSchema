package xpertss.json.schema.examples;

import com.fasterxml.jackson.databind.JsonNode;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.load.configuration.LoadingConfiguration;
import xpertss.json.schema.core.load.uri.URITranslatorConfiguration;
import xpertss.json.schema.core.load.uri.URITranslatorConfigurationBuilder;
import xpertss.json.schema.core.ref.JsonRef;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.main.JsonSchema;
import xpertss.json.schema.main.JsonSchemaFactory;

import java.io.IOException;

/**
 * Sixth example: URI redirection
 *
 * <p><a href="doc-files/Example6.java">link to source code</a></p>
 *
 * <p>In this example, the same schema file is used as in {@link Example1}. This
 * time, though, it is assumed that the base URI used for addressing this schema
 * is {@code http://my.site/schemas/fstab.json#}. But instead of trying to
 * fetch it from the web directly, we want to use the local copy, which is
 * located under URI {@code
 * resource:/org/eel/kitchen/jsonschema/examples/fstab.json#}.</p>
 *
 * <p>The solution here is to build a custom {@link URITranslatorConfiguration},
 * which allows to customize URI handling; in this case, a schema redirection
 * using the {@link URITranslatorConfigurationBuilder#addSchemaRedirect(String,
 * String)}. We then inject this into a custom {@link LoadingConfiguration}.</p>
 *
 * <p>The effect is that if you required a schema via URI {@code
 * http://my.site/schemas/fstab.json#}, it will silently transform this URI into
 * {@code resource:/org/eel/kitchen/jsonschema/examples/fstab.json#}
 * internally.</p>
 *
 * <p>Note that URIs must be absolute JSON references (see {@link JsonRef}).</p>
 */
public final class Example6
{
    private static final String FROM = "http://my.site/schemas/fstab.json#";
    private static final String TO
        = "resource:/com/github/fge/jsonschema/examples/fstab.json#";

    public static void main(final String... args)
        throws IOException, ProcessingException
    {
        final JsonNode good = Utils.loadResource("/fstab-good.json");
        final JsonNode bad = Utils.loadResource("/fstab-bad.json");
        final JsonNode bad2 = Utils.loadResource("/fstab-bad2.json");

        final URITranslatorConfiguration translatorCfg
            = URITranslatorConfiguration.newBuilder()
            .addSchemaRedirect(FROM, TO).freeze();
        final LoadingConfiguration cfg = LoadingConfiguration.newBuilder()
            .setURITranslatorConfiguration(translatorCfg).freeze();

        final JsonSchemaFactory factory = JsonSchemaFactory.newBuilder()
            .setLoadingConfiguration(cfg).freeze();

        final JsonSchema schema = factory.getJsonSchema(FROM);

        ProcessingReport report;

        report = schema.validate(good);
        System.out.println(report);

        report = schema.validate(bad);
        System.out.println(report);

        report = schema.validate(bad2);
        System.out.println(report);
    }
}
