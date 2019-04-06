package xpertss.json.schema.core.report;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.github.fge.jackson.JacksonUtils;
import xpertss.json.schema.core.util.AsJson;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

import java.util.Iterator;
import java.util.List;

/**
 * {@link List}-based implementation of a {@link ProcessingReport}
 */
public final class ListProcessingReport
    extends AbstractProcessingReport
    implements AsJson
{
    private static final JsonNodeFactory FACTORY = JacksonUtils.nodeFactory();

    private final List<ProcessingMessage> messages = Lists.newArrayList();

    public ListProcessingReport(final LogLevel logLevel,
        final LogLevel exceptionThreshold)
    {
        super(logLevel, exceptionThreshold);
    }

    public ListProcessingReport(final LogLevel logLevel)
    {
        super(logLevel);
    }

    public ListProcessingReport()
    {
    }

    public ListProcessingReport(final ProcessingReport other)
    {
        this(other.getLogLevel(), other.getExceptionThreshold());
    }

    @Override
    public void log(final LogLevel level, final ProcessingMessage message)
    {
        messages.add(message);
    }

    @Override
    public JsonNode asJson()
    {
        final ArrayNode ret = FACTORY.arrayNode();
        for (final ProcessingMessage message: messages)
            ret.add(message.asJson());
        return ret;
    }

    @Override
    public Iterator<ProcessingMessage> iterator()
    {
        return Iterators.unmodifiableIterator(messages.iterator());
    }

}
