package xpertss.json.schema.library;

import xpertss.json.schema.format.FormatAttribute;
import xpertss.json.schema.messages.JsonSchemaConfigurationBundle;
import com.github.fge.msgsimple.bundle.MessageBundle;
import com.github.fge.msgsimple.load.MessageBundles;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

public final class LibraryBuilderTest
{
    private static final MessageBundle BUNDLE
        = MessageBundles.getBundle(JsonSchemaConfigurationBundle.class);

    @Test
    public void cannotAddNullKeyword()
    {
        try {
            Library.newBuilder().addKeyword(null);
            fail("No exception thrown!!");
        } catch (NullPointerException e) {
            assertEquals(e.getMessage(), BUNDLE.getMessage("nullKeyword"));
        }
    }

    @Test
    public void cannotRemoveNullKeyword()
    {
        try {
            Library.newBuilder().removeKeyword(null);
            fail("No exception thrown!!");
        } catch (NullPointerException e) {
            assertEquals(e.getMessage(), BUNDLE.getMessage("nullName"));
        }
    }

    @Test
    public void cannotAddFormatAttributeWithNullName()
    {
        try {
            Library.newBuilder().addFormatAttribute(null,
                mock(FormatAttribute.class));
            fail("No exception thrown!!");
        } catch (NullPointerException e) {
            assertEquals(e.getMessage(), BUNDLE.getMessage("nullFormat"));
        }
    }

    @Test
    public void cannotAddNullFormatAttribute()
    {
        try {
            Library.newBuilder().addFormatAttribute("foo", null);
            fail("No exception thrown!!");
        } catch (NullPointerException e) {
            assertEquals(e.getMessage(),
                BUNDLE.printf("nullAttribute", "foo"));
        }
    }

    @Test
    public void cannotRemoveFormatAttributeWithNullName()
    {
        try {
            Library.newBuilder().removeFormatAttribute(null);
            fail("No exception thrown!!");
        } catch (NullPointerException e) {
            assertEquals(e.getMessage(), BUNDLE.getMessage("nullFormat"));
        }
    }
}
