package xpertss.json.schema.core.load;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jackson.JsonNodeReader;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.load.configuration.LoadingConfiguration;
import xpertss.json.schema.core.load.download.URIDownloader;
import xpertss.json.schema.core.messages.JsonSchemaCoreMessageBundle;
import xpertss.json.schema.core.report.ProcessingMessage;
import com.github.fge.msgsimple.bundle.MessageBundle;
import com.github.fge.msgsimple.load.MessageBundles;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Map;

/**
 * Class to fetch JSON documents
 *
 * <p>This uses a map of {@link URIDownloader} instances to fetch the contents
 * of a URI as an {@link InputStream}, then tries and turns this content into
 * JSON using an {@link ObjectMapper}.</p>
 *
 * <p>Normally, you will never use this class directly.</p>
 *
 * @see SchemaLoader
 */
public final class URIManager {

    private static final MessageBundle BUNDLE = MessageBundles.getBundle(JsonSchemaCoreMessageBundle.class);

    private final Map<String, URIDownloader> downloaders;

    private final JsonNodeReader reader;

    public URIManager()
    {
        this(LoadingConfiguration.byDefault());
    }

    public URIManager(LoadingConfiguration cfg)
    {
        downloaders = cfg.getDownloaderMap();
        reader = cfg.getReader();
    }

    /**
     * Get the content at a given URI as a {@link JsonNode}
     *
     * @param uri the URI
     * @return the content
     * @throws NullPointerException provided URI is null
     * @throws ProcessingException scheme is not registered, failed to get
     * content, or content is not JSON
     */
    public JsonNode getContent(URI uri)
        throws ProcessingException
    {
        BUNDLE.checkNotNull(uri, "jsonRef.nullURI");

        if (!uri.isAbsolute())
            throw new ProcessingException(new ProcessingMessage()
                .setMessage(BUNDLE.getMessage("refProcessing.uriNotAbsolute"))
                .put("uri", uri));

        String scheme = uri.getScheme();

        URIDownloader downloader = downloaders.get(scheme);

        if (downloader == null)
            throw new ProcessingException(new ProcessingMessage()
                .setMessage(BUNDLE.getMessage("refProcessing.unhandledScheme"))
                .putArgument("scheme", scheme).putArgument("uri", uri));

        try (InputStream in = downloader.fetch(uri)) {
            return reader.fromInputStream(in);
        } catch (JsonMappingException e) {
            throw new ProcessingException(new ProcessingMessage()
                .setMessage(e.getOriginalMessage()).put("uri", uri));
        } catch (JsonParseException e) {
            throw new ProcessingException(new ProcessingMessage()
                .setMessage(BUNDLE.getMessage("uriManager.uriNotJson"))
                .putArgument("uri", uri)
                .put("parsingMessage", e.getOriginalMessage()));
        } catch (IOException e) {
            throw new ProcessingException(new ProcessingMessage()
                .setMessage(BUNDLE.getMessage("uriManager.uriIOError"))
                .putArgument("uri", uri)
                .put("exceptionMessage", e.getMessage()));
        }
    }
}
