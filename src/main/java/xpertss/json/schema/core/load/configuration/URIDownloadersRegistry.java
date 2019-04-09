package xpertss.json.schema.core.load.configuration;


import com.google.common.collect.ImmutableMap;
import xpertss.json.schema.core.load.download.DefaultURIDownloader;
import xpertss.json.schema.core.load.download.ResourceURIDownloader;
import xpertss.json.schema.core.load.download.URIDownloader;
import xpertss.json.schema.core.util.ArgumentChecker;
import xpertss.json.schema.core.util.Registry;
import xpertss.json.schema.core.util.URIUtils;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.function.Function;

final class URIDownloadersRegistry extends Registry<String, URIDownloader> {

    private static final Map<String, URIDownloader> DEFAULT_DOWNLOADERS;

    static {
        ImmutableMap.Builder<String, URIDownloader> builder = ImmutableMap.builder();

        builder.put("http", DefaultURIDownloader.getInstance());
        builder.put("https", DefaultURIDownloader.getInstance());
        builder.put("file", DefaultURIDownloader.getInstance());
        builder.put("ftp", DefaultURIDownloader.getInstance());
        builder.put("jar", DefaultURIDownloader.getInstance());
        builder.put("resource", ResourceURIDownloader.getInstance());

        DEFAULT_DOWNLOADERS = builder.build();
    }

    public URIDownloadersRegistry()
    {
        super(URIUtils.schemeNormalizer(), URIUtils.schemeChecker(), identity(), ArgumentChecker.anythingGoes());
        putAll(DEFAULT_DOWNLOADERS);
    }

    @Override
    protected void checkEntry(String key, URIDownloader value)
    {
    }




    /**
     * Returns the identity function.
     */
    // implementation is "fully variant"; E has become a "pass-through" type
    @SuppressWarnings("unchecked")
    public static <E> Function<E, E> identity() {
        return (Function<E, E>) IdentityFunction.INSTANCE;
    }

    // enum singleton pattern
    private enum IdentityFunction implements Function<Object, Object> {
        INSTANCE;

        @Override
        @Nullable
        public Object apply(@Nullable Object o) {
            return o;
        }

        @Override public String toString() {
            return "identity";
        }
    }

}
