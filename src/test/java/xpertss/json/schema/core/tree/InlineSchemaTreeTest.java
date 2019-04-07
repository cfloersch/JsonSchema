package xpertss.json.schema.core.tree;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jackson.jsonpointer.JsonPointer;
import com.github.fge.jackson.jsonpointer.JsonPointerException;
import com.google.common.collect.Sets;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import xpertss.json.schema.core.exceptions.JsonReferenceException;
import xpertss.json.schema.core.ref.JsonRef;
import xpertss.json.schema.core.tree.key.SchemaKey;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(JUnitParamsRunner.class)
public final class InlineSchemaTreeTest {

    private SchemaTree schemaTree;
    private JsonNode lookups;

    public InlineSchemaTreeTest()
        throws IOException
    {
        final JsonNode data = JsonLoader.fromResource("/tree/retrieval.json");
        lookups = data.get("lookups");

        final JsonNode schema = data.get("schema");
        schemaTree = new InlineSchemaTree(SchemaKey.anonymousKey(), schema);
    }

    public Iterator<Object[]> getLookups()
        throws JsonReferenceException, JsonPointerException
    {
        final Set<Object[]> set = Sets.newHashSet();

        for (final JsonNode lookup: lookups)
            set.add(new Object[] {
                JsonRef.fromString(lookup.get("id").textValue()),
                new JsonPointer(lookup.get("ptr").textValue())
            });

        return set.iterator();
    }

    @Test
    @Parameters(method = "getLookups")
    public void inlineSchemaTreeContainsDeclaredContext(JsonRef ref, JsonPointer ptr)
    {
        assertTrue(schemaTree.containsRef(ref));
        assertEquals(ptr, schemaTree.matchingPointer(ref));
    }
}
