package xpertss.json.schema.main;

import com.github.fge.Thawed;
import xpertss.json.schema.cfg.ValidationConfiguration;
import xpertss.json.schema.core.load.configuration.LoadingConfiguration;
import xpertss.json.schema.core.report.ListReportProvider;
import xpertss.json.schema.core.report.LogLevel;
import xpertss.json.schema.core.report.ReportProvider;
import xpertss.json.schema.messages.JsonSchemaConfigurationBundle;
import com.github.fge.msgsimple.bundle.MessageBundle;
import com.github.fge.msgsimple.load.MessageBundles;

import javax.annotation.concurrent.NotThreadSafe;

/**
 * Thawed instance of a {@link JsonSchemaFactory}
 *
 * <p>This is the class you will use to configure a schema factory before use,
 * should you need to. In most cases, the default factory will be enough.</p>
 *
 * <p>In order to obtain an instance of this builder class, use {@link
 * JsonSchemaFactory#newBuilder()}.</p>
 *
 * @see JsonSchemaFactory#byDefault()
 * @see LoadingConfiguration
 * @see ValidationConfiguration
 * @see ReportProvider
 */
@NotThreadSafe
public final class JsonSchemaFactoryBuilder implements Thawed<JsonSchemaFactory> {

    private static final MessageBundle BUNDLE = MessageBundles.getBundle(JsonSchemaConfigurationBundle.class);

    ReportProvider reportProvider;
    LoadingConfiguration loadingCfg;
    ValidationConfiguration validationCfg;

    /**
     * A builder with the default configuration
     *
     * @see JsonSchemaFactory#newBuilder()
     */
    JsonSchemaFactoryBuilder()
    {
        reportProvider = new ListReportProvider(LogLevel.INFO, LogLevel.FATAL);
        loadingCfg = LoadingConfiguration.byDefault();
        validationCfg = ValidationConfiguration.byDefault();
    }

    /**
     * A builder spawned from an existing {@link JsonSchemaFactory}
     *
     * @param factory the factory
     * @see JsonSchemaFactory#thaw()
     */
    JsonSchemaFactoryBuilder(final JsonSchemaFactory factory)
    {
        reportProvider = factory.reportProvider;
        loadingCfg = factory.loadingCfg;
        validationCfg = factory.validationCfg;
    }

    /**
     * Set a new report provider for this factory
     *
     * @param reportProvider the report provider
     * @return this
     * @throws NullPointerException provider is null
     */
    public JsonSchemaFactoryBuilder setReportProvider(ReportProvider reportProvider)
    {
        BUNDLE.checkNotNull(reportProvider, "nullReportProvider");
        this.reportProvider = reportProvider;
        return this;
    }

    /**
     * Set a new loading configuration for this factory
     *
     * @param loadingCfg the loading configuration
     * @return this
     * @throws NullPointerException configuration is null
     */
    public JsonSchemaFactoryBuilder setLoadingConfiguration(LoadingConfiguration loadingCfg)
    {
        BUNDLE.checkNotNull(loadingCfg, "nullLoadingCfg");
        this.loadingCfg = loadingCfg;
        return this;
    }

    /**
     * Set a new validation configuration for this factory
     *
     * @param validationCfg the validation configuration
     * @return this
     * @throws NullPointerException configuration is null
     */
    public JsonSchemaFactoryBuilder setValidationConfiguration(ValidationConfiguration validationCfg)
    {
        BUNDLE.checkNotNull(validationCfg, "nullValidationCfg");
        this.validationCfg = validationCfg;
        return this;
    }

    /**
     * Build a frozen instance of this factory configuration
     *
     * @return a new {@link JsonSchemaFactory}
     * @see JsonSchemaFactory#JsonSchemaFactory(JsonSchemaFactoryBuilder)
     */
    @Override
    public JsonSchemaFactory freeze()
    {
        return new JsonSchemaFactory(this);
    }
}
