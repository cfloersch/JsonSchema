package xpertss.json.schema.keyword.validator.callback.draftv4;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jackson.jsonpointer.JsonPointer;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.report.ProcessingReport;

import static xpertss.json.schema.TestUtils.*;
import static org.mockito.Mockito.*;

public final class DependenciesValidatorTest
    extends DraftV4CallbackValidatorTest
{
    public DependenciesValidatorTest()
    {
        super("dependencies", JsonPointer.of("dependencies", "a"),
            JsonPointer.of("dependencies", "b"));
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
        verify(report, onlyOnce()).error(same(MSG));
    }

    @Override
    protected void checkKoKo(final ProcessingReport report)
        throws ProcessingException
    {
        verify(report, times(2)).error(same(MSG));
    }

    @Override
    protected JsonNode generateSchema()
    {
        final ObjectNode value = FACTORY.objectNode();
        value.put("a", sub1);
        value.put("b", sub2);

        final ObjectNode ret = FACTORY.objectNode();
        ret.put(keyword, value);
        return ret;
    }

    @Override
    protected JsonNode generateInstance()
    {
        final ObjectNode ret = FACTORY.objectNode();
        ret.put("a", "a");
        ret.put("b", "b");
        return ret;
    }

    @Override
    protected JsonNode generateDigest()
    {
        final ArrayNode schemaDeps = FACTORY.arrayNode();
        schemaDeps.add("a");
        schemaDeps.add("b");

        final ObjectNode ret = FACTORY.objectNode();
        ret.put("propertyDeps", FACTORY.objectNode());
        ret.put("schemaDeps", schemaDeps);
        return ret;
    }
}
