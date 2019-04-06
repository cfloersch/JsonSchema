package xpertss.json.schema.keyword.validator.callback.draftv3;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jackson.jsonpointer.JsonPointer;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.report.ProcessingMessage;
import xpertss.json.schema.core.report.ProcessingReport;
import org.mockito.ArgumentCaptor;

import static xpertss.json.schema.TestUtils.*;
import static xpertss.json.schema.matchers.ProcessingMessageAssert.*;
import static org.mockito.Mockito.*;

public final class DraftV3TypeValidatorTest
    extends DraftV3CallbackValidatorTest
{
    public DraftV3TypeValidatorTest()
    {
        super("type", JsonPointer.of("type", 0), JsonPointer.of("type", 1));
    }

    @Override
    protected void checkOkOk(final ProcessingReport report)
        throws ProcessingException
    {
        verify(report, never()).error(anyMessage());
    }

    @Override
    protected void checkOkKo(final ProcessingReport report)
        throws ProcessingException
    {
        verify(report, never()).error(anyMessage());
    }

    @Override
    protected void checkKoKo(final ProcessingReport report)
        throws ProcessingException
    {
        final ArgumentCaptor<ProcessingMessage> captor
            = ArgumentCaptor.forClass(ProcessingMessage.class);

        verify(report).error(captor.capture());

        final ProcessingMessage message = captor.getValue();
        final ObjectNode reports = FACTORY.objectNode();

        final ArrayNode oneReport = FACTORY.arrayNode();
        oneReport.add(MSG.asJson());
        reports.put(ptr1.toString(), oneReport);
        reports.put(ptr2.toString(), oneReport);

        assertMessage(message)
            .isValidationError(keyword,
                BUNDLE.printf("err.common.schema.noMatch", 2))
            .hasField("reports", reports);
    }

    @Override
    protected JsonNode generateSchema()
    {
        final ArrayNode schemas = FACTORY.arrayNode();
        schemas.add(sub1);
        schemas.add(sub2);
        final ObjectNode ret = FACTORY.objectNode();
        ret.put(keyword, schemas);
        return ret;
    }

    @Override
    protected JsonNode generateInstance()
    {
        return FACTORY.nullNode();
    }

    @Override
    protected JsonNode generateDigest()
    {
        final ObjectNode ret = FACTORY.objectNode();
        ret.put(keyword, FACTORY.arrayNode());
        final ArrayNode schemas = FACTORY.arrayNode();
        schemas.add(0);
        schemas.add(1);
        ret.put("schemas", schemas);
        return ret;
    }
}
