package xpertss.json.schema.examples;

import com.fasterxml.jackson.databind.JsonNode;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.load.configuration.LoadingConfiguration;
import xpertss.json.schema.core.load.configuration.LoadingConfigurationBuilder;
import xpertss.json.schema.core.load.download.URIDownloader;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.main.JsonSchema;
import xpertss.json.schema.main.JsonSchemaFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

/**
 * Seventh example: custom URI scheme
 *
 * <p><a href="doc-files/Example7.java">link to source code</a></p>
 *
 * <p>This demonstrates {@link JsonSchemaFactory}'s ability to register a
 * custom URI scheme. In this example, the scheme is {@code foobar}, and it is
 * simply an alias to fetch a resource from the current package.</p>
 *
 * <p>Two things are needed:</p>
 *
 * <ul>
 *     <li>an implementation of {@link URIDownloader} for this scheme,</li>
 *     <li>registering this scheme using {@link
 *     LoadingConfigurationBuilder#addScheme(String, URIDownloader)}.</li>
 * </ul>
 *
 * <p>Once this is done, this scheme, when encountered anywhere in JSON
 * References, will use this downloader, and you are also able to use it when
 * loading schemas using {@link JsonSchemaFactory#getJsonSchema(String)}, which
 * is what this example does.</p>
 *
 * <p>The schema and files used are the same as for {@link Example2}.</p>
 */
public final class Example7
{
    public static void main(final String... args)
        throws IOException, ProcessingException
    {
        final JsonNode good = Utils.loadResource("/fstab-good.json");
        final JsonNode bad = Utils.loadResource("/fstab-bad.json");
        final JsonNode bad2 = Utils.loadResource("/fstab-bad2.json");

        final LoadingConfiguration cfg = LoadingConfiguration.newBuilder()
            .addScheme("foobar", CustomDownloader.getInstance()).freeze();

        final JsonSchemaFactory factory = JsonSchemaFactory.newBuilder()
            .setLoadingConfiguration(cfg).freeze();

        final JsonSchema schema
            = factory.getJsonSchema("foobar:/fstab.json#");

        ProcessingReport report;

        report = schema.validate(good);
        System.out.println(report);

        report = schema.validate(bad);
        System.out.println(report);

        report = schema.validate(bad2);
        System.out.println(report);
    }

    private static final class CustomDownloader
        implements URIDownloader
    {
        private static final String PREFIX;
        private static final URIDownloader INSTANCE = new CustomDownloader();

        static {
            final String pkgname = CustomDownloader.class.getPackage()
                .getName();
            PREFIX = '/' + pkgname.replace(".", "/");
        }

        public static URIDownloader getInstance()
        {
            return INSTANCE;
        }

        @Override
        public InputStream fetch(final URI source)
            throws IOException
        {
            final String path = PREFIX + source.getPath();
            final InputStream ret = getClass().getResourceAsStream(path);

            if (ret == null)
                throw new IOException("resource " + path + " not found");
            return ret;
        }
    }
}
