package xpertss.json.schema.examples;

import com.fasterxml.jackson.databind.JsonNode;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.main.JsonSchema;
import xpertss.json.schema.main.JsonSchemaFactory;

import java.io.IOException;

/**
 * Third example: draft v3 detection via {@code $schema}
 *
 * <p><a href="doc-files/Example3.java">link to source code</a></p>
 *
 * <p>This shows a basic usage example. This is the same source code as for
 * {@link Example1}, except this time the schema (<a
 * href="doc-files/fstab-draftv3.json">here</a>) conforms to draft v3 instead of
 * draft v4 (the {@code $schema} value differs).</p>
 *
 * <p>One thing to note is a difference in the validation messages: while
 * required properties are described using the {@code required} keyword, with
 * draft v3, they were in charge of the {@code properties} keyword.</p>
 */
public final class Example3
{
    public static void main(final String... args)
        throws IOException, ProcessingException
    {
        final JsonNode fstabSchema = Utils.loadResource("/fstab-draftv3.json");
        final JsonNode good = Utils.loadResource("/fstab-good.json");
        final JsonNode bad = Utils.loadResource("/fstab-bad.json");
        final JsonNode bad2 = Utils.loadResource("/fstab-bad2.json");

        final JsonSchemaFactory factory = JsonSchemaFactory.byDefault();

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
