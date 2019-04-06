package xpertss.json.schema.main;

import xpertss.json.schema.messages.JsonSchemaConfigurationBundle;
import com.github.fge.msgsimple.bundle.MessageBundle;
import com.github.fge.msgsimple.load.MessageBundles;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public final class JsonSchemaFactoryTest
{
    private static final MessageBundle BUNDLE
        = MessageBundles.getBundle(JsonSchemaConfigurationBundle.class);

    private JsonSchemaFactoryBuilder builder;

    @BeforeMethod
    public void initBuilder()
    {
        builder = JsonSchemaFactory.newBuilder();
    }

    @Test
    public void cannotInsertNullLoadingConfiguration()
    {
        try {
            builder.setLoadingConfiguration(null);
            fail("No exception thrown!!");
        } catch (NullPointerException e) {
            assertEquals(e.getMessage(), BUNDLE.getMessage("nullLoadingCfg"));
        }
    }

    @Test
    public void cannotInsertNullValidationConfiguration()
    {
        try {
            builder.setValidationConfiguration(null);
            fail("No exception thrown!!");
        } catch (NullPointerException e) {
            assertEquals(e.getMessage(),
                BUNDLE.getMessage("nullValidationCfg"));
        }
    }

    @Test
    public void cannotInsertNullReportProvider()
    {
        try {
            builder.setReportProvider(null);
            fail("No exception thrown!!");
        } catch (NullPointerException e) {
            assertEquals(e.getMessage(),
                BUNDLE.getMessage("nullReportProvider"));
        }
    }
}
