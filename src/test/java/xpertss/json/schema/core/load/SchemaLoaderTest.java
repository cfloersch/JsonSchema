package xpertss.json.schema.core.load;

import com.github.fge.jackson.JacksonUtils;
import org.junit.Test;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.load.configuration.LoadingConfiguration;
import xpertss.json.schema.core.load.configuration.LoadingConfigurationBuilder;
import xpertss.json.schema.core.load.uri.URITranslatorConfiguration;
import xpertss.json.schema.core.load.download.URIDownloader;
import xpertss.json.schema.core.messages.JsonSchemaCoreMessageBundle;
import xpertss.json.schema.core.ref.JsonRef;
import xpertss.json.schema.core.report.LogLevel;
import xpertss.json.schema.core.tree.SchemaTree;
import com.github.fge.msgsimple.bundle.MessageBundle;
import com.github.fge.msgsimple.load.MessageBundles;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static xpertss.json.schema.matchers.ProcessingMessageAssert.*;
import static org.mockito.Mockito.*;

public final class SchemaLoaderTest
{
    private static final MessageBundle BUNDLE
        = MessageBundles.getBundle(JsonSchemaCoreMessageBundle.class);

    private static final byte[] BYTES = JacksonUtils.nodeFactory().objectNode()
        .toString().getBytes();

    @Test
    public void namespacesAreRespected()
        throws ProcessingException, IOException
    {
        final URI fullPath = URI.create("foo:/baz#");
        final URIDownloader downloader = spy(new URIDownloader()
        {
            @Override
            public InputStream fetch(final URI source)
                throws IOException
            {
                if (!fullPath.equals(source))
                    throw new IOException();
                return new ByteArrayInputStream(BYTES);
            }
        });

        final String namespace = "foo:///bar/../bar/";
        final URITranslatorConfiguration translatorCfg
            = URITranslatorConfiguration.newBuilder()
                .setNamespace(namespace).freeze();
        final LoadingConfiguration cfg = LoadingConfiguration.newBuilder()
            .addScheme("foo", downloader)
            .setURITranslatorConfiguration(translatorCfg)
            .freeze();

        final URI rootns = URI.create(namespace);

        final SchemaLoader loader = new SchemaLoader(cfg);

        final URI uri = URI.create("../baz");
        loader.get(uri);
        final JsonRef ref = JsonRef.fromURI(rootns.resolve(uri));
        verify(downloader).fetch(rootns.resolve(ref.toURI()));
    }

    @Test
    public void URIsAreNormalizedBehindTheScenes()
        throws ProcessingException
    {
        final String location = "http://toto/a/../b";
        final LoadingConfiguration cfg = LoadingConfiguration.newBuilder()
            .preloadSchema(location, JacksonUtils.nodeFactory().objectNode())
            .freeze();

        final SchemaLoader loader = new SchemaLoader(cfg);

        final SchemaTree tree = loader.get(URI.create(location));

        assertEquals(URI.create("http://toto/b#"), tree.getLoadingRef().toURI());
    }

    @Test
    public void NonAbsoluteURIsAreRefused()
    {
        final SchemaLoader loader = new SchemaLoader();

        final URI target = URI.create("moo#");

        try {
            loader.get(target);
            fail("No exception thrown!");
        } catch (ProcessingException e) {
            assertMessage(e.getProcessingMessage())
                .hasMessage(BUNDLE.printf("refProcessing.uriNotAbsolute",
                    target))
                .hasLevel(LogLevel.FATAL).hasField("uri", target);
        }
    }

    @Test
    public void preloadedSchemasAreNotFetchedAgain()
        throws ProcessingException, IOException
    {
        final String location = "http://foo.bar/baz#";
        final URI uri = URI.create(location);
        final URIDownloader mock = mock(URIDownloader.class);
        final LoadingConfigurationBuilder builder = LoadingConfiguration
            .newBuilder().addScheme("http", mock)
            .preloadSchema(location, JacksonUtils.nodeFactory().objectNode());

        LoadingConfiguration cfg;
        SchemaLoader registry;

        cfg = builder.freeze();
        registry = new SchemaLoader(cfg);
        registry.get(uri);
        verify(mock, never()).fetch(uri);

        //even if cache is disabled
        cfg = builder.setCacheSize(0).freeze();
        registry = new SchemaLoader(cfg);
        registry.get(uri);
        verify(mock, never()).fetch(uri);        
    }

    @Test
    public void schemasAreFetchedOnceNotTwice()
        throws ProcessingException, IOException
    {
        final URI uri = URI.create("foo:/baz#");
        final URIDownloader downloader = spy(new URIDownloader() {
            @Override
            public InputStream fetch(final URI source)
                    throws IOException {
                return new ByteArrayInputStream(BYTES);
            }
        });

        final LoadingConfiguration cfg = LoadingConfiguration.newBuilder()
            .addScheme("foo", downloader).freeze();
        final SchemaLoader loader = new SchemaLoader(cfg);

        loader.get(uri);
        loader.get(uri);
        verify(downloader, times(1)).fetch(uri);
    }
    
    @Test
    public void schemasCacheCanBeDisabled()
        throws ProcessingException, IOException
    {
        final URI uri = URI.create("foo:/baz#");
        final URIDownloader downloader = spy(new URIDownloader() {
            @Override
            public InputStream fetch(final URI source)
                    throws IOException {
                return new ByteArrayInputStream(BYTES);
            }
        });

        final LoadingConfiguration cfg = LoadingConfiguration.newBuilder()
            .addScheme("foo", downloader)
            .setCacheSize(0)
            .freeze();
        final SchemaLoader loader = new SchemaLoader(cfg);

        loader.get(uri);
        loader.get(uri);
        verify(downloader, times(2)).fetch(uri);
    }

    @Test
    public void schemasCacheCanBeDisabledViaCacheSize()
        throws ProcessingException, IOException
    {
        final URI uri = URI.create("foo:/baz#");
        final URIDownloader downloader = spy(new URIDownloader()
        {
            @Override
            public InputStream fetch(final URI source)
                throws IOException
            {
                return new ByteArrayInputStream(BYTES);
            }
        });

        final LoadingConfiguration cfg = LoadingConfiguration.newBuilder()
            .addScheme("foo", downloader)
            .setCacheSize(0)
            .freeze();
        final SchemaLoader loader = new SchemaLoader(cfg);

        loader.get(uri);
        loader.get(uri);
        verify(downloader, times(2)).fetch(uri);
    }
}
