package xpertss.json.schema;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JacksonUtils;
import com.github.fge.jackson.JsonLoader;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.main.JsonSchema;
import xpertss.json.schema.main.JsonSchemaFactory;

import java.io.IOException;
import java.util.Map;

public final class NewAPIPerfTest
{
    private static final JsonSchema SCHEMA;

    static {
        try {
            SCHEMA = JsonSchemaFactory.byDefault()
                .getJsonSchema(SchemaVersion.DRAFTV4.getSchema());
        } catch (ProcessingException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private NewAPIPerfTest()
    {
    }

    public static void main(final String... args)
        throws IOException, ProcessingException
    {
        final JsonNode googleAPI
            = JsonLoader.fromResource("/other/google-json-api.json");
        final Map<String, JsonNode> googleSchemas
            = JacksonUtils.asMap(googleAPI.get("schemas"));

        long begin, current;
        begin = System.currentTimeMillis();
        doValidate(googleSchemas, -1);
        current = System.currentTimeMillis();

        System.out.println("Initial validation :" + (current - begin) + " ms");

        begin = System.currentTimeMillis();
        for (int i = 0; i < 500; i++) {
            doValidate(googleSchemas, i);
            if (i % 20 == 0) {
                current = System.currentTimeMillis();
                System.out.println(String.format("Iteration %d (in %d ms)", i,
                    current - begin));
            }
        }

        final long end = System.currentTimeMillis();
        System.out.println("END -- time in ms: " + (end - begin));
        System.exit(0);
    }

    private static void doValidate(final Map<String, JsonNode> schemas,
        final int i)
        throws ProcessingException
    {
        String name;
        JsonNode value;
        ProcessingReport report;

        for (final Map.Entry<String, JsonNode> entry: schemas.entrySet()) {
            name = entry.getKey();
            value = entry.getValue();
            report = SCHEMA.validate(value);
            if (!report.isSuccess()) {
                System.err.println("ERROR: schema " + name + " did not "
                    + "validate (iteration " + i + ')');
                System.exit(1);
            }
        }
    }
}
