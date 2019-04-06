package xpertss.json.schema.examples;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.NodeType;
import xpertss.json.schema.cfg.ValidationConfiguration;
import xpertss.json.schema.cfg.ValidationConfigurationBuilder;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.format.AbstractFormatAttribute;
import xpertss.json.schema.format.FormatAttribute;
import xpertss.json.schema.library.DraftV4Library;
import xpertss.json.schema.library.Library;
import xpertss.json.schema.main.JsonSchema;
import xpertss.json.schema.main.JsonSchemaFactory;
import xpertss.json.schema.messages.JsonSchemaValidationBundle;
import xpertss.json.schema.processors.data.FullData;
import com.github.fge.msgsimple.bundle.MessageBundle;
import com.github.fge.msgsimple.load.MessageBundles;
import com.github.fge.msgsimple.source.MapMessageSource;
import com.github.fge.msgsimple.source.MessageSource;

import java.io.IOException;
import java.util.UUID;

/**
 * Eighth example: augmenting schemas with custom format attributes
 *
 * <p><a href="doc-files/Example8.java">link to source code</a></p>
 *
 * <p><a href="doc-files/custom-fmt.json">link to schema</a></p>
 *
 * <p>This example adds a custom format attribute named {@code uuid}, which
 * checks whether a string instance is a valid UUID.</p>
 *
 * <p>For this, you need to write an implementation of {@link FormatAttribute},
 * registering it in a {@link Library}, and feed that library to a {@link
 * ValidationConfiguration} which you submit to the {@link JsonSchemaFactory}.
 * </p>
 *
 * <p>Here, we choose to augment the draft v4 library, which we get hold of
 * using {@link DraftV4Library#get()}; we thaw it, add the new attribute and
 * freeze it again. We also choose to make this new library the default by
 * using {@link
 * ValidationConfigurationBuilder#setDefaultLibrary(String, Library)}.</p>
 *
 * <p>Note also that the schema has no {@code $schema} defined; as a result, the
 * default library is used (it is <b>not</b> recommended to omit {@code $schema}
 * in your schemas, however).</p>
 *
 * <p>Two sample files are given: the first (<a
 * href="doc-files/custom-fmt-good.json">link</a>) is valid, the other (<a
 * href="doc-files/custom-fmt-bad.json">link</a>) isn't (the provided {@code id}
 * for the second array element is invalid).</p>
 */
public final class Example8
{
    public static void main(final String... args)
        throws IOException, ProcessingException
    {
        final JsonNode customSchema = Utils.loadResource("/custom-fmt.json");
        final JsonNode good = Utils.loadResource("/custom-fmt-good.json");
        final JsonNode bad = Utils.loadResource("/custom-fmt-bad.json");

        /*
         * Build a new library with our added format attribute
         */
        final Library library = DraftV4Library.get().thaw()
            .addFormatAttribute("uuid", UUIDFormatAttribute.getInstance())
            .freeze();

        /*
         * Build a new message bundle with our added error message
         */
        final String key = "invalidUUID";
        final String value = "input is not a valid UUID";
        final MessageSource source = MapMessageSource.newBuilder()
            .put(key, value).build();
        final MessageBundle bundle
            = MessageBundles.getBundle(JsonSchemaValidationBundle.class)
            .thaw().appendSource(source).freeze();

        /*
         * Build our dedicated validation configuration
         */
        final ValidationConfiguration cfg = ValidationConfiguration.newBuilder()
            .setDefaultLibrary("http://my.site/myschema#", library)
            .setValidationMessages(bundle).freeze();

        final JsonSchemaFactory factory = JsonSchemaFactory.newBuilder()
            .setValidationConfiguration(cfg).freeze();

        final JsonSchema schema = factory.getJsonSchema(customSchema);

        ProcessingReport report;

        report = schema.validate(good);
        System.out.println(report);

        report = schema.validate(bad);
        System.out.println(report);
    }

    private static final class UUIDFormatAttribute
        extends AbstractFormatAttribute
    {
        private static final FormatAttribute INSTANCE
            = new UUIDFormatAttribute();

        private UUIDFormatAttribute()
        {
            super("uuid", NodeType.STRING);
        }

        public static FormatAttribute getInstance()
        {
            return INSTANCE;
        }

        @Override
        public void validate(final ProcessingReport report,
            final  MessageBundle bundle, final FullData data)
            throws ProcessingException
        {
            final String value = data.getInstance().getNode().textValue();
            try {
                UUID.fromString(value);
            } catch (IllegalArgumentException ignored) {
                report.error(newMsg(data, bundle, "invalidUUID")
                    .put("input", value));
            }
        }
    }
}
