package xpertss.json.schema.processors.build;

import static org.mockito.Mockito.mock;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.fail;

import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JacksonUtils;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.processing.Processor;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.core.util.Dictionary;
import xpertss.json.schema.core.util.DictionaryBuilder;
import xpertss.json.schema.keyword.validator.KeywordValidator;
import xpertss.json.schema.keyword.validator.KeywordValidatorFactory;
import xpertss.json.schema.keyword.validator.ReflectionKeywordValidatorFactory;
import xpertss.json.schema.processors.data.FullData;
import xpertss.json.schema.processors.data.SchemaDigest;
import xpertss.json.schema.processors.data.ValidatorList;
import com.github.fge.msgsimple.bundle.MessageBundle;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public final class ValidatorBuilderTest
{
    private static final String K1 = "k1";
    private static final String K2 = "k2";
    private static final String CHALLENGED = "challenged";

    private final ValidatorBuilder validatorBuilder;

    public ValidatorBuilderTest()
        throws NoSuchMethodException
    {
        final DictionaryBuilder<KeywordValidatorFactory>
            builder = Dictionary.newBuilder();

        KeywordValidatorFactory factory;

        factory = new ReflectionKeywordValidatorFactory(K1, Keyword1.class);
        builder.addEntry(K1, factory);
        factory = new ReflectionKeywordValidatorFactory(K2, Keyword2.class);
        builder.addEntry(K2, factory);
        factory = new ReflectionKeywordValidatorFactory(CHALLENGED, Challenged.class);
        builder.addEntry(CHALLENGED, factory);

        validatorBuilder = new ValidatorBuilder(builder.freeze());
    }

    @Test
    public void challengedConstructorRaisesAnException()
    {
        final Map<String, JsonNode> digests = Maps.newTreeMap();
        digests.put(K1, JacksonUtils.nodeFactory().nullNode());
        digests.put(CHALLENGED, JacksonUtils.nodeFactory().nullNode());

        final SchemaDigest digest = new SchemaDigest(null, digests);
        final ProcessingReport report = mock(ProcessingReport.class);

        try {
            validatorBuilder.process(report, digest);
            fail("No exception thrown??");
        } catch (ProcessingException ignored) {
        }
    }

    @Test
    public void onlyRelevantValidatorsAreBuilt()
        throws ProcessingException
    {
        final Map<String, JsonNode> digests = Maps.newTreeMap();
        digests.put(K1, JacksonUtils.nodeFactory().nullNode());

        final SchemaDigest digest = new SchemaDigest(null, digests);
        final ProcessingReport report = mock(ProcessingReport.class);

        final ValidatorList context
            = validatorBuilder.process(report, digest);

        final List<KeywordValidator> list = Lists.newArrayList(context);

        assertEquals(list.size(), 1);
        assertSame(list.get(0).getClass(), Keyword1.class);
    }

    @Test
    public void allRelevantValidatorsAreBuilt()
        throws ProcessingException
    {
        final Map<String, JsonNode> digests = Maps.newTreeMap();
        digests.put(K1, JacksonUtils.nodeFactory().nullNode());
        digests.put(K2, JacksonUtils.nodeFactory().nullNode());

        final SchemaDigest digest = new SchemaDigest(null, digests);
        final ProcessingReport report = mock(ProcessingReport.class);

        final ValidatorList context
            = validatorBuilder.process(report, digest);

        final List<KeywordValidator> list = Lists.newArrayList(context);

        assertEquals(list.size(), 2);
        assertSame(list.get(0).getClass(), Keyword1.class);
        assertSame(list.get(1).getClass(), Keyword2.class);
    }

    public static final class Keyword1
        implements KeywordValidator
    {
        public Keyword1(final JsonNode ignored)
        {
        }

        @Override
        public void validate(final Processor<FullData, FullData> processor,
            final ProcessingReport report, final MessageBundle bundle,
            final FullData data)
            throws ProcessingException
        {
        }
    }

    public static final class Keyword2
        implements KeywordValidator
    {
        public Keyword2(final JsonNode ignored)
        {
        }

        @Override
        public void validate(
            final Processor<FullData, FullData> processor,
            final ProcessingReport report, final MessageBundle bundle,
            final FullData data)
            throws ProcessingException
        {
        }
    }

    public static final class Challenged
        implements KeywordValidator
    {
        public Challenged(final JsonNode ignored)
        {
            throw new ExceptionInInitializerError("moo");
        }

        @Override
        public void validate(
            final Processor<FullData, FullData> processor,
            final ProcessingReport report, final MessageBundle bundle,
            final FullData data)
            throws ProcessingException
        {
        }
    }

}
