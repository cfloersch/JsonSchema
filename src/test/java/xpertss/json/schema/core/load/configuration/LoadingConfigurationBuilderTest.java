package xpertss.json.schema.core.load.configuration;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JacksonUtils;
import com.github.fge.jackson.JsonNumEquals;
import xpertss.json.schema.SchemaVersion;
import xpertss.json.schema.core.load.download.URIDownloader;
import xpertss.json.schema.core.messages.JsonSchemaCoreMessageBundle;
import com.github.fge.msgsimple.bundle.MessageBundle;
import com.github.fge.msgsimple.load.MessageBundles;
import com.google.common.collect.Lists;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.net.URI;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

public final class LoadingConfigurationBuilderTest
{
    private static final MessageBundle BUNDLE
        = MessageBundles.getBundle(JsonSchemaCoreMessageBundle.class);

    private final URIDownloader downloader = mock(URIDownloader.class);
    private final LoadingConfigurationBuilder cfg
        = LoadingConfiguration.newBuilder();

    @Test
    public void cannotRegisterIllegalScheme()
    {
        final String scheme = "+24";
        try {
            cfg.addScheme(scheme, downloader);
            fail("No exception thrown!!");
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(),
                BUNDLE.printf("loadingCfg.illegalScheme", scheme));
        }
    }

    @Test
    public void registeringAndUnregisteringSchemeWorks()
    {
        final String scheme = "foo";

        cfg.addScheme(scheme, downloader);
        assertNotNull(cfg.freeze().getDownloaderMap().get(scheme));

        cfg.removeScheme(scheme);
        assertNull(cfg.freeze().getDownloaderMap().get(scheme));
    }

    @Test
    public void cannotSetNullDereferencingMode()
    {
        try {
            cfg.dereferencing(null);
            fail("No exception thrown!!");
        } catch (NullPointerException e) {
            assertEquals(e.getMessage(),
                BUNDLE.getMessage("loadingCfg.nullDereferencingMode"));
        }
    }

    @DataProvider
    public Iterator<Object[]> schemaVersions()
    {
        final List<Object[]> list = Lists.newArrayList();

        for (final SchemaVersion version: SchemaVersion.values())
            list.add(new Object[] { version });

        return list.iterator();
    }

    // Mysteriously fails _only some times_ when run with gradle...
    @Test(dataProvider = "schemaVersions", enabled = false)
    public void basicConfigurationContainsCoreSchemas(
        final SchemaVersion version)
    {
        final Map<URI, JsonNode> map = cfg.freeze().getPreloadedSchemas();

        final JsonNode actual = map.get(version.getLocation());
        final JsonNode expected = version.getSchema();
        assertTrue(JsonNumEquals.getInstance().equivalent(actual, expected));
    }

    @Test
    public void cannotOverwriteAnAlreadyPresentSchema()
    {
        final String input = "http://json-schema.org/draft-04/schema#";
        try {
            cfg.preloadSchema(input, JacksonUtils.nodeFactory().objectNode());
            fail("No exception thrown!!");
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(),
                BUNDLE.printf("loadingCfg.duplicateURI", input));
        }
    }

    @Test
    public void cannotPreloadSchemaWithoutTopLevelId()
    {
        try {
            cfg.preloadSchema(JacksonUtils.nodeFactory().objectNode());
            fail("No exception thrown!!");
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(),
               BUNDLE.getMessage("loadingCfg.noIDInSchema"));
        }
    }
}
