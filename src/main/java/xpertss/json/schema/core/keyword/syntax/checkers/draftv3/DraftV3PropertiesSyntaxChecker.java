package xpertss.json.schema.core.keyword.syntax.checkers.draftv3;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JacksonUtils;
import com.github.fge.jackson.NodeType;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.core.keyword.syntax.checkers.SyntaxChecker;
import xpertss.json.schema.core.keyword.syntax.checkers.helpers.SchemaMapSyntaxChecker;
import xpertss.json.schema.core.tree.SchemaTree;
import com.github.fge.msgsimple.bundle.MessageBundle;
import com.google.common.collect.Maps;

import java.util.Map;
import java.util.SortedMap;

/**
 * Syntax checker for draft v3's {@code properties} keyword
 */
public final class DraftV3PropertiesSyntaxChecker extends SchemaMapSyntaxChecker {

    private static final SyntaxChecker INSTANCE = new DraftV3PropertiesSyntaxChecker();

    public static SyntaxChecker getInstance()
    {
        return INSTANCE;
    }

    private DraftV3PropertiesSyntaxChecker()
    {
        super("properties");
    }

    @Override
    protected void extraChecks(ProcessingReport report, MessageBundle bundle, SchemaTree tree)
        throws ProcessingException
    {
        SortedMap<String, JsonNode> map = Maps.newTreeMap();
        map.putAll(JacksonUtils.asMap(tree.getNode().get(keyword)));

        String member;
        JsonNode required;
        NodeType type;

        for (final Map.Entry<String, JsonNode> entry: map.entrySet()) {
            member = entry.getKey();
            required = entry.getValue().get("required");
            if (required == null)
                continue;
            type = NodeType.getNodeType(required);
            if (type != NodeType.BOOLEAN) {
                report.error(newMsg(tree, bundle,
                    "draftv3.properties.required.incorrectType")
                    .putArgument("property", member).putArgument("found", type)
                    .put("expected", NodeType.BOOLEAN));
            }
        }
    }
}
