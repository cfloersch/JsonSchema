package xpertss.json.schema.format.draftv3;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.github.fge.jackson.JacksonUtils;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.report.ProcessingMessage;
import xpertss.json.schema.core.tree.JsonTree;
import xpertss.json.schema.core.tree.SimpleJsonTree;
import xpertss.json.schema.processors.data.FullData;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static xpertss.json.schema.matchers.ProcessingMessageAssert.*;
import static org.mockito.Mockito.*;

public final class UTCMillisecTest extends DraftV3FormatAttributeTest {

    private static final JsonNodeFactory FACTORY = JacksonUtils.nodeFactory();

    public UTCMillisecTest()
        throws IOException
    {
        super("utc-millisec");
    }


    // FIXME: why do I have to do that? :(
    @Before
    public void retestAttribute()
    {
        formatAttributeIsSupported();
    }

    public Iterator<Object[]> negativeValues()
    {
        final List<? extends JsonNode> list = ImmutableList.of(
            FACTORY.numberNode(new BigDecimal("-928019283")),
            FACTORY.numberNode(new BigDecimal("-928019283.01")),
            FACTORY.numberNode(-1)
        );

        return list.stream().map(input -> new Object[] { input }).collect(Collectors.toList()).iterator();
    }

    @Test
    @Parameters(method = "negativeValues")
    public void userIsWarnedAboutNegativeEpochs(final JsonNode input)
        throws ProcessingException
    {
        final JsonTree tree = new SimpleJsonTree(input);
        final FullData data = new FullData(SCHEMA_TREE, tree);

        attribute.validate(report, BUNDLE, data);

        ArgumentCaptor<ProcessingMessage> captor = ArgumentCaptor.forClass(ProcessingMessage.class);

        verify(report, only()).warn(captor.capture());

        ProcessingMessage message = captor.getValue();

        assertMessage(message).isFormatMessage(fmt,
            BUNDLE.printf("warn.format.epoch.negative", input));
    }

    public Iterator<Object[]> overflows()
    {
        final List<? extends JsonNode> list = ImmutableList.of(
            FACTORY.numberNode(new BigDecimal("2147483648000")),
            FACTORY.numberNode(new BigDecimal("2147483648000.2983"))
        );

        return list.stream().map(input -> new Object[] { input }).collect(Collectors.toList()).iterator();
    }

    @Test
    @Parameters(method = "overflows")
    public void userIsWarnedAboutPotentialOverflows(final JsonNode input)
        throws ProcessingException
    {
        final JsonTree tree = new SimpleJsonTree(input);
        final FullData data = new FullData(SCHEMA_TREE, tree);

        attribute.validate(report, BUNDLE, data);

        ArgumentCaptor<ProcessingMessage> captor = ArgumentCaptor.forClass(ProcessingMessage.class);

        verify(report, only()).warn(captor.capture());

        final ProcessingMessage message = captor.getValue();

        assertMessage(message)
            .isFormatMessage(fmt, BUNDLE.printf("warn.format.epoch.overflow",
                input));
    }
}
