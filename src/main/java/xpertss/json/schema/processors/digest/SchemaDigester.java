package xpertss.json.schema.processors.digest;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.NodeType;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.processing.Processor;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.core.util.Dictionary;
import xpertss.json.schema.keyword.digest.Digester;
import xpertss.json.schema.library.Library;
import xpertss.json.schema.processors.data.SchemaContext;
import xpertss.json.schema.processors.data.SchemaDigest;
import xpertss.json.schema.processors.validation.ValidationChain;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.Map;

/**
 * The schema digester
 *
 * <p>This processor is called by a {@link ValidationChain} after it has made
 * sure that the schema is syntactically valid.</p>
 */
public final class SchemaDigester implements Processor<SchemaContext, SchemaDigest> {

    private final ListMultimap<NodeType, String> typeMap = ArrayListMultimap.create();
    private final Map<String, Digester> digesterMap = Maps.newHashMap();

    public SchemaDigester(Library library)
    {
        this(library.getDigesters());
    }

    public SchemaDigester(Dictionary<Digester> dict)
    {
        String keyword;
        Digester digester;

        final Map<String, Digester> map = dict.entries();

        for (final Map.Entry<String, Digester> entry: map.entrySet()) {
            keyword = entry.getKey();
            digester = entry.getValue();
            digesterMap.put(keyword, digester);
            for (final NodeType type: digester.supportedTypes())
                typeMap.put(type, keyword);
        }
    }

    @Override
    public SchemaDigest process(ProcessingReport report, SchemaContext input)
        throws ProcessingException
    {
        JsonNode schema = input.getSchema().getNode();
        NodeType type = input.getInstanceType();
        Map<String, JsonNode> map = Maps.newHashMap(buildDigests(schema));
        map.keySet().retainAll(typeMap.get(type));
        return new SchemaDigest(input, map);
    }

    private Map<String, JsonNode> buildDigests(final JsonNode schema)
    {
        ImmutableMap.Builder<String, JsonNode> builder = ImmutableMap.builder();
        Map<String, Digester> map = Maps.newHashMap(digesterMap);

        map.keySet().retainAll(Sets.newHashSet(schema.fieldNames()));

        for (final Map.Entry<String, Digester> entry: map.entrySet())
            builder.put(entry.getKey(), entry.getValue().digest(schema));

        return builder.build();
    }

    @Override
    public String toString()
    {
        return "schema digester";
    }
}
