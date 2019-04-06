package xpertss.json.schema.examples;

import com.fasterxml.jackson.databind.JsonNode;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.load.Dereferencing;
import xpertss.json.schema.core.load.configuration.LoadingConfiguration;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.main.JsonSchema;
import xpertss.json.schema.main.JsonSchemaFactory;
import xpertss.json.schema.main.JsonSchemaFactoryBuilder;

import java.io.IOException;

/**
 * Second example: inline schema addressing
 *
 * <p><a href="doc-files/Example2.java">link to source code</a></p>
 *
 * <p>This example uses the same schema with one difference: the mntent
 * subschema is now referenced via inline addressing using an {@code id}.</p>
 *
 * <p>The schema used for validation is <a href="doc-files/fstab-inline.json">
 * here</a>.</p>
 *
 * <p>In order to use inline schema addressing, we cannot use the default
 * factory: we must go through a {@link JsonSchemaFactoryBuilder} and use a
 * modified {@link LoadingConfiguration} to tell that we want to use inline
 * dereferencing.</p>
 *
 * <p>Apart from these, the files used for validation and validation results
 * are the same as {@link Example1}.</p>
 *
 * @see Dereferencing
 */
public final class Example2
{
    public static void main(final String... args)
        throws IOException, ProcessingException
    {
        final JsonNode fstabSchema = Utils.loadResource("/fstab-inline.json");
        final JsonNode good = Utils.loadResource("/fstab-good.json");
        final JsonNode bad = Utils.loadResource("/fstab-bad.json");
        final JsonNode bad2 = Utils.loadResource("/fstab-bad2.json");

        final LoadingConfiguration cfg = LoadingConfiguration.newBuilder()
            .dereferencing(Dereferencing.INLINE).freeze();
        final JsonSchemaFactory factory = JsonSchemaFactory.newBuilder()
            .setLoadingConfiguration(cfg).freeze();

        final JsonSchema schema = factory.getJsonSchema(fstabSchema);

        ProcessingReport report;

        report = schema.validate(good);
        System.out.println(report);

        report = schema.validate(bad);
        System.out.println(report);

        report = schema.validate(bad2);
        System.out.println(report);
    }
}
