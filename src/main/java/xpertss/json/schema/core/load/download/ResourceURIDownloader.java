package xpertss.json.schema.core.load.download;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

/**
 * A downloader for the custom {@code resource} URI scheme
 *
 * <p>Here, {@code resource} is to be interpreted as a Java resource, exactly
 * what you would obtain using {@link Class#getResourceAsStream(String)}.</p>
 *
 * <p>And in fact, this is what this downloader does: it takes whatever is in
 * the provided URI's path (using {@link URI#getPath()}) and tries to make an
 * input stream of it. The difference is that an {@link IOException} will be
 * thrown if the resource cannot be found (instead of returning {@code null}).
 * </p>
 */
public final class ResourceURIDownloader implements URIDownloader {

    private static final Class<ResourceURIDownloader> MYSELF = ResourceURIDownloader.class;

    private static final URIDownloader INSTANCE = new ResourceURIDownloader();

    private ResourceURIDownloader()
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
        String resource = source.getPath();
        InputStream in = MYSELF.getResourceAsStream(resource);

        if (in == null)
            throw new IOException("resource " + resource + " not found");

        return in;
    }
}
