package xpertss.json.schema.core.load.download;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

/**
 * URI downloader for a given scheme
 */
public interface URIDownloader {

    /**
     * Fetch the content at a given URI
     *
     * @param source the URI
     * @return an {@link InputStream}
     * @throws IOException unable to find an input stream
     */
    InputStream fetch(URI source) throws IOException;
}
