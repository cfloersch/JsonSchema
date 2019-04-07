package xpertss.json.schema.validation;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import org.junit.Test;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.main.JsonSchemaFactory;
import xpertss.json.schema.main.JsonValidator;

import java.io.IOException;

import static org.junit.Assert.assertTrue;


public final class SelfValidationTest {

    private static final JsonValidator VALIDATOR = JsonSchemaFactory.byDefault().getValidator();

    private final JsonNode draftv3;
    private final JsonNode draftv4;

    public SelfValidationTest()
        throws IOException
    {
        draftv3 = JsonLoader.fromResource("/draftv3/schema");
        draftv4 = JsonLoader.fromResource("/draftv4/schema");
    }

    @Test // TODO ?? (invocationCount = 10, threadPoolSize = 4)
    public void v4ValidatesItselfButNotV3()
        throws ProcessingException
    {
        final ProcessingReport r1 = VALIDATOR.validate(draftv4, draftv4);
        final ProcessingReport r2 = VALIDATOR.validate(draftv4, draftv3);

        assertTrue(r1.isSuccess());
        assertTrue(!r2.isSuccess());
    }


    @Test // TODO ?? (invocationCount = 10, threadPoolSize = 4)
    public void v3ValidatesItself()
        throws ProcessingException
    {
        final ProcessingReport r1 = VALIDATOR.validate(draftv3, draftv3);
        assertTrue(r1.isSuccess());
    }

}
