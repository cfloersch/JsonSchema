package xpertss.json.schema.core.tree;


import java.io.IOException;
import java.util.Iterator;
import java.util.Set;


import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.google.common.collect.Sets;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.ref.JsonRef;
import xpertss.json.schema.core.tree.key.SchemaKey;

import static junit.framework.Assert.assertFalse;

@RunWith(JUnitParamsRunner.class)
public final class CanonicalSchemaTreeTest {
    
    private SchemaTree schemaTree;
    private JsonNode lookups;

    public CanonicalSchemaTreeTest()
        throws IOException
    {
        final JsonNode data = JsonLoader.fromResource("/tree/retrieval.json");
        lookups = data.get("lookups");

        final JsonNode schema = data.get("schema");
        schemaTree = new CanonicalSchemaTree(SchemaKey.anonymousKey(), schema);
    }

    public Iterator<Object[]> getLookups()
        throws ProcessingException
    {
        final Set<Object[]> set = Sets.newHashSet();

        for (final JsonNode lookup: lookups)
            set.add(new Object[] {
                JsonRef.fromString(lookup.get("id").textValue())
            });

        return set.iterator();
    }

    @Test
    @Parameters(method = "getLookups")
    public void canonicalTreeDoesNotContainInlineContexts(final JsonRef ref)
    {
        assertFalse(schemaTree.containsRef(ref));
    }
}
