package xpertss.json.schema.processors.data;

import com.fasterxml.jackson.databind.JsonNode;
import xpertss.json.schema.core.report.MessageProvider;
import xpertss.json.schema.core.report.ProcessingMessage;
import xpertss.json.schema.processors.build.ValidatorBuilder;
import xpertss.json.schema.processors.digest.SchemaDigester;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

/**
 * Output of {@link SchemaDigester} and input of {@link ValidatorBuilder}
 *
 * <p>It bundles a {@link SchemaContext} and a map of digested nodes for keyword
 * construction.</p>
 */
public final class SchemaDigest implements MessageProvider {

    private final SchemaContext context;
    private final Map<String, JsonNode> digested;

    public SchemaDigest(SchemaContext context, Map<String, JsonNode> map)
    {
        this.context = context;
        digested = ImmutableMap.copyOf(map);
    }

    public SchemaContext getContext()
    {
        return context;
    }

    public Map<String, JsonNode> getDigests()
    {
        return digested;
    }

    @Override
    public ProcessingMessage newMessage()
    {
        return context.newMessage();
    }
}
