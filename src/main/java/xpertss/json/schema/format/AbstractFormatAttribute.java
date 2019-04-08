package xpertss.json.schema.format;

import com.github.fge.jackson.NodeType;
import xpertss.json.schema.core.report.ProcessingMessage;
import xpertss.json.schema.processors.data.FullData;
import com.github.fge.msgsimple.bundle.MessageBundle;

import java.util.EnumSet;

/**
 * Base abstract class for a format attribute
 *
 * <p>You should really use this class instead of implementing {@link
 * FormatAttribute} directly. Its main, but important, helping role is to
 * build the list of supported types for you.</p>
 */
public abstract class AbstractFormatAttribute implements FormatAttribute {

    /**
     * The set of supported types
     */
    private final EnumSet<NodeType> supported;

    /**
     * The name of the format attribute
     */
    private final String fmt;

    /**
     * Protected constructor
     *
     * @param fmt the name for this format attribute
     * @param first first supported type
     * @param other other supported types, if any
     *
     * @see #supportedTypes()
     */
    protected AbstractFormatAttribute(String fmt, NodeType first, NodeType... other)
    {
        this.fmt = fmt;
        supported = EnumSet.of(first, other);
    }

    @Override
    public final EnumSet<NodeType> supportedTypes()
    {
        return EnumSet.copyOf(supported);
    }

    /**
     * Return a new message for this format attribute
     *
     * @param data the validation context
     * @param key key in the format bundle message
     * @return a new message
     */
    protected final ProcessingMessage newMsg(FullData data, MessageBundle bundle, String key)
    {
        return data.newMessage().put("domain", "validation")
            .put("keyword", "format").put("attribute", fmt)
            .setMessage(bundle.getMessage(key))
            .put("value", data.getInstance().getNode());
    }
}
