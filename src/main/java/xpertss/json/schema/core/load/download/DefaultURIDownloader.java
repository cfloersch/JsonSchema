package xpertss.json.schema.core.load.download;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

/**
 * Default URI downloader
 *
 * <p>{@link URL}'s API doc guarantees that an implementation can handle the
 * following schemes: {@code http}, {@code https}, {@code ftp}, {@code file}
 * and {@code jar}. This is what this downloader uses.</p>
 *
 * @see URL#openStream()
 */
public final class DefaultURIDownloader implements URIDownloader {

    private static final URIDownloader INSTANCE = new DefaultURIDownloader();

    private DefaultURIDownloader()
    {
    }

    public static URIDownloader getInstance()
    {
        return INSTANCE;
    }

    @Override
    public InputStream fetch(URI source)
        throws IOException
    {
        return source.toURL().openStream();
    }
}
