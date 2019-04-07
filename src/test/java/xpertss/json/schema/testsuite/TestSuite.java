package xpertss.json.schema.testsuite;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import junitparams.Parameters;
import org.junit.Test;
import xpertss.json.schema.SchemaVersion;
import xpertss.json.schema.cfg.ValidationConfiguration;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.main.JsonSchemaFactory;
import xpertss.json.schema.main.JsonValidator;
import com.google.common.collect.Lists;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;


public abstract class TestSuite {
    
    private final JsonValidator validator;
    private final JsonNode testSuite;

    protected TestSuite(final SchemaVersion version, final String fileName)
        throws IOException
    {
        final ValidationConfiguration cfg = ValidationConfiguration.newBuilder()
            .setDefaultVersion(version).freeze();
        validator = JsonSchemaFactory.newBuilder()
            .setValidationConfiguration(cfg).freeze().getValidator();
        testSuite = JsonLoader.fromResource("/testsuite/" + fileName + ".json");
    }

    public final Iterator<Object[]> getAllTests()
    {
        final List<Object[]> list = Lists.newArrayList();

        for (final JsonNode test: testSuite)
            list.add(new Object[]{
                test.get("description").textValue(),
                test.get("schema"),
                test.get("data"),
                test.get("valid").booleanValue()
            });

        return list.iterator();
    }

    @Test
    @Parameters(method = "getAllTests")
    public final void testsFromTestSuitePass(String description, JsonNode schema, JsonNode data, boolean valid)
        throws ProcessingException
    {
        final ProcessingReport report = validator.validate(schema, data);

        assertEquals("test failed (description: " + description + ")", valid, report.isSuccess());
    }
}
