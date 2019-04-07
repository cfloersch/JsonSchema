package xpertss.json.schema.main;

import org.junit.Before;
import org.junit.Test;
import xpertss.json.schema.messages.JsonSchemaConfigurationBundle;
import com.github.fge.msgsimple.bundle.MessageBundle;
import com.github.fge.msgsimple.load.MessageBundles;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public final class JsonSchemaFactoryTest
{
    private static final MessageBundle BUNDLE
        = MessageBundles.getBundle(JsonSchemaConfigurationBundle.class);

    private JsonSchemaFactoryBuilder builder;

    @Before
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
            assertEquals(BUNDLE.getMessage("nullLoadingCfg"), e.getMessage());
        }
    }

    @Test
    public void cannotInsertNullValidationConfiguration()
    {
        try {
            builder.setValidationConfiguration(null);
            fail("No exception thrown!!");
        } catch (NullPointerException e) {
            assertEquals(BUNDLE.getMessage("nullValidationCfg"), e.getMessage());
        }
    }

    @Test
    public void cannotInsertNullReportProvider()
    {
        try {
            builder.setReportProvider(null);
            fail("No exception thrown!!");
        } catch (NullPointerException e) {
            assertEquals(BUNDLE.getMessage("nullReportProvider"), e.getMessage());
        }
    }
}
