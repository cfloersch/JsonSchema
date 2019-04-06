package xpertss.json.schema.core.ref;

import xpertss.json.schema.core.exceptions.JsonReferenceException;
import xpertss.json.schema.core.messages.JsonSchemaCoreMessageBundle;
import xpertss.json.schema.core.report.ProcessingMessage;
import com.github.fge.msgsimple.bundle.MessageBundle;
import com.github.fge.msgsimple.load.MessageBundles;
import org.testng.annotations.Test;

import java.net.URISyntaxException;

import static xpertss.json.schema.matchers.ProcessingMessageAssert.*;
import static org.testng.Assert.*;

public final class JsonRefTest
{
    private static final MessageBundle BUNDLE
        = MessageBundles.getBundle(JsonSchemaCoreMessageBundle.class);

    @Test
    public void cannotCreateRefFromNullURI()
    {
        try {
            JsonRef.fromURI(null);
            fail("No exception thrown!!");
        } catch (NullPointerException e) {
            assertEquals(e.getMessage(), BUNDLE.getMessage("jsonRef.nullURI"));
        }
    }

    @Test
    public void cannotCreateRefFromNullString()
        throws JsonReferenceException
    {
        try {
            JsonRef.fromString(null);
            fail("No exception thrown!!");
        } catch (NullPointerException e) {
            assertEquals(e.getMessage(),
                BUNDLE.getMessage("jsonRef.nullInput"));
        }
    }

    @Test
    public void illegalURIThrowsAnException()
    {
        final String input = "+24:";

        try {
            JsonRef.fromString(input);
            fail("No exception thrown!!");
        } catch (JsonReferenceException e) {
            final ProcessingMessage message = e.getProcessingMessage();
            assertMessage(message)
                .hasMessage(BUNDLE.printf("jsonRef.invalidURI", input))
                .hasField("input", input)
                .hasField("exceptionClass", URISyntaxException.class.getName());
        }
    }
}
