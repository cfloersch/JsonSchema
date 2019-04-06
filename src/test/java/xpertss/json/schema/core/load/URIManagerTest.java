package xpertss.json.schema.core.load;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonNumEquals;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.load.configuration.LoadingConfiguration;
import xpertss.json.schema.core.load.download.URIDownloader;
import xpertss.json.schema.core.messages.JsonSchemaCoreMessageBundle;
import xpertss.json.schema.core.ref.JsonRef;
import xpertss.json.schema.core.report.LogLevel;
import com.github.fge.msgsimple.bundle.MessageBundle;
import com.github.fge.msgsimple.load.MessageBundles;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import static xpertss.json.schema.matchers.ProcessingMessageAssert.*;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

public final class URIManagerTest
{
    private static final MessageBundle BUNDLE
        = MessageBundles.getBundle(JsonSchemaCoreMessageBundle.class);

    private URIDownloader mock;

    @BeforeMethod
    public void setUp()
    {
        mock = mock(URIDownloader.class);
    }

    @Test
    public void unhandledSchemeShouldBeReportedAsSuch()
    {
        final URI uri = URI.create("bar://baz");
        final URIManager manager = new URIManager();

        try {
            manager.getContent(uri);
        } catch (ProcessingException e) {
            assertMessage(e.getProcessingMessage())
                .hasMessage(BUNDLE.printf("refProcessing.unhandledScheme",
                    "bar", uri))
                .hasField("scheme", "bar").hasField("uri", uri)
                .hasLevel(LogLevel.FATAL);
        }
    }

    @Test
    public void downloaderProblemsShouldBeReportedAsSuch()
        throws IOException
    {
        final URI uri = URI.create("foo://bar");
        final Exception foo = new IOException("foo");

        when(mock.fetch(any(URI.class))).thenThrow(foo);

        final LoadingConfiguration cfg = LoadingConfiguration.newBuilder()
            .addScheme("foo", mock).freeze();

        final URIManager manager = new URIManager(cfg);

        try {
            manager.getContent(uri);
        } catch (ProcessingException e) {
            assertMessage(e.getProcessingMessage())
                .hasMessage(BUNDLE.printf("uriManager.uriIOError", uri))
                .hasField("uri", uri).hasLevel(LogLevel.FATAL)
                .hasField("exceptionMessage", "foo");
        }
    }

    @Test
    public void nonJSONInputShouldBeReportedAsSuch()
        throws IOException
    {
        final URI uri = URI.create("foo://bar");
        final InputStream sampleStream
            = new ByteArrayInputStream("}".getBytes());

        when(mock.fetch(any(URI.class))).thenReturn(sampleStream);

        final LoadingConfiguration cfg = LoadingConfiguration.newBuilder()
            .addScheme("foo", mock).freeze();

        final URIManager manager = new URIManager(cfg);

        try {
            manager.getContent(uri);
        } catch (ProcessingException e) {
            assertMessage(e.getProcessingMessage())
                .hasMessage(BUNDLE.printf("uriManager.uriNotJson", uri))
                .hasTextField("parsingMessage").hasLevel(LogLevel.FATAL)
                .hasField("uri", uri);
        }
    }

    @Test
    void managerParsesNonstandardJSON()
        throws IOException, ProcessingException
    {
        // get resource URIs for standard and nonstandard sources
        final String wellFormed = "resource:/load/standard-source.json";
        final URI uri1 = JsonRef.fromString(wellFormed).getLocator();
        final String illFormed = "resource:/load/nonstandard-source.json";
        final URI uri2 = JsonRef.fromString(illFormed).getLocator();

        // get URIManager configured to parse nonstandard sources
        final LoadingConfiguration cfg = LoadingConfiguration.newBuilder()
            .addParserFeature(JsonParser.Feature.ALLOW_COMMENTS)
            .addParserFeature(JsonParser.Feature.ALLOW_SINGLE_QUOTES)
            .addParserFeature(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES)
            .freeze();
        final URIManager manager = new URIManager(cfg);

        // load JSON nodes from sources using nonstandard manager
        final JsonNode node1 = manager.getContent(uri1);
        final JsonNode node2 = manager.getContent(uri2);

        // validate correctness of loaded equivalent sources
        assertTrue(JsonNumEquals.getInstance().equivalent(node1, node2));
    }
}
