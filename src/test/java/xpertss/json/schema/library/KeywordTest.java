package xpertss.json.schema.library;

import com.github.fge.jackson.NodeType;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.keyword.syntax.checkers.SyntaxChecker;
import xpertss.json.schema.core.processing.Processor;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.keyword.validator.KeywordValidator;
import xpertss.json.schema.keyword.validator.common.MinItemsValidator;
import xpertss.json.schema.keyword.validator.draftv4.NotValidator;
import xpertss.json.schema.messages.JsonSchemaConfigurationBundle;
import xpertss.json.schema.processors.data.FullData;
import com.github.fge.msgsimple.bundle.MessageBundle;
import com.github.fge.msgsimple.load.MessageBundles;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

public final class KeywordTest
{
    private static final MessageBundle BUNDLE
        = MessageBundles.getBundle(JsonSchemaConfigurationBundle.class);
    private static final String KEYWORD = "foo";

    private KeywordBuilder builder;

    @BeforeMethod
    public void initBuilder()
    {
        builder = Keyword.newBuilder(KEYWORD);
    }

    @Test
    public void cannotCreateKeywordWithNullName()
    {
        try {
            Keyword.newBuilder(null);
            fail("No exception thrown!!");
        } catch (NullPointerException e) {
            assertEquals(e.getMessage(), BUNDLE.getMessage("nullName"));
        }
    }

    @Test
    public void cannotInjectNullSyntaxChecker()
    {
        try {
            builder.withSyntaxChecker(null);
            fail("No exception thrown!!");
        } catch (NullPointerException e) {
            assertEquals(e.getMessage(),
                BUNDLE.printf("nullSyntaxChecker", KEYWORD));
        }
    }

    @Test
    public void cannotInjectNullDigester()
    {
        try {
            builder.withDigester(null);
            fail("No exception thrown!!");
        } catch (NullPointerException e) {
            assertEquals(e.getMessage(),
                BUNDLE.printf("nullDigester", KEYWORD));
        }
    }

    @Test
    public void identityDigesterTypesMustNotBeNull()
    {
        try {
            builder.withIdentityDigester(null);
            fail("No exception thrown!!");
        } catch (NullPointerException e) {
            assertEquals(e.getMessage(), BUNDLE.getMessage("nullType"));
        }

        try {
            builder.withIdentityDigester(NodeType.ARRAY, NodeType.OBJECT, null);
            fail("No exception thrown!!");
        } catch (NullPointerException e) {
            assertEquals(e.getMessage(), BUNDLE.getMessage("nullType"));
        }
    }

    @Test
    public void simpleDigesterTypesMustNotBeNull()
    {
        try {
            builder.withSimpleDigester(null);
            fail("No exception thrown!!");
        } catch (NullPointerException e) {
            assertEquals(e.getMessage(), BUNDLE.getMessage("nullType"));
        }

        try {
            builder.withSimpleDigester(NodeType.ARRAY, NodeType.OBJECT, null);
            fail("No exception trown!!");
        } catch (NullPointerException e) {
            assertEquals(e.getMessage(), BUNDLE.getMessage("nullType"));
        }
    }

    @Test
    public void inappropriateConstructorThrowsAppropriateError()
    {
        try {
            builder.withValidatorClass(DummyValidator.class);
            fail("No exception thrown!!");
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(),
                BUNDLE.printf("noAppropriateConstructor", KEYWORD,
                    DummyValidator.class.getCanonicalName()));
        }
    }

    @Test
    public void whenValidatorIsPresentSyntaxCheckerMustBeThere()
    {
        try {
            builder.withValidatorClass(MinItemsValidator.class).freeze();
            fail("No exception thrown!!");
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), BUNDLE.printf("noChecker", KEYWORD));
        }
    }

    @Test
    public void validatorClassMustBePairedWithDigester()
    {
        try {
            builder.withSyntaxChecker(mock(SyntaxChecker.class))
                .withValidatorClass(NotValidator.class).freeze();
            fail("No exception thrown!!");
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(),
                BUNDLE.printf("malformedKeyword", KEYWORD));
        }
    }

    public static class DummyValidator
        implements KeywordValidator
    {
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
