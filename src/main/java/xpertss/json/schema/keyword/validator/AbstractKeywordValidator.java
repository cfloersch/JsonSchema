package xpertss.json.schema.keyword.validator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.github.fge.jackson.JacksonUtils;
import xpertss.json.schema.core.exceptions.ExceptionProvider;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.report.ProcessingMessage;
import xpertss.json.schema.exceptions.InvalidInstanceException;
import xpertss.json.schema.processors.data.FullData;
import com.github.fge.msgsimple.bundle.MessageBundle;

import java.util.Collection;

/**
 * Base abstract class for keyword validators
 *
 * <p>This class provides a template message for error reporting, with all
 * details about the current validation context already filled.</p>
 */
public abstract class AbstractKeywordValidator
    implements KeywordValidator
{
    private static final ExceptionProvider EXCEPTION_PROVIDER
        = new ExceptionProvider()
    {
        @Override
        public ProcessingException doException(final ProcessingMessage message)
        {
            return new InvalidInstanceException(message);
        }
    };

    protected final String keyword;

    /**
     * Protected constructor
     *
     * @param keyword the keyword's name
     */
    protected AbstractKeywordValidator(final String keyword)
    {
        this.keyword = keyword;
    }

    protected final ProcessingMessage newMsg(final FullData data)
    {
        return data.newMessage().put("domain", "validation")
            .put("keyword", keyword)
            .setExceptionProvider(EXCEPTION_PROVIDER);
    }

    protected final ProcessingMessage newMsg(final FullData data,
        final MessageBundle bundle, final String key)
    {
        return data.newMessage().put("domain", "validation")
            .put("keyword", keyword).setMessage(bundle.getMessage(key))
            .setExceptionProvider(EXCEPTION_PROVIDER);
    }

    protected static <T> JsonNode toArrayNode(final Collection<T> collection)
    {
        final ArrayNode node = JacksonUtils.nodeFactory().arrayNode();
        for (final T element: collection)
            node.add(element.toString());
        return node;
    }

    @Override
    public abstract String toString();
}
