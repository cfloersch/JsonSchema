package xpertss.json.schema.core.tree;

import static org.testng.Assert.assertFalse;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.collections.Sets;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.ref.JsonRef;
import xpertss.json.schema.core.tree.key.SchemaKey;

public final class CanonicalSchemaTreeTest
{
    private SchemaTree schemaTree;
    private JsonNode lookups;

    @BeforeClass
    public void loadData()
        throws IOException
    {
        final JsonNode data = JsonLoader.fromResource("/tree/retrieval.json");
        lookups = data.get("lookups");

        final JsonNode schema = data.get("schema");
        schemaTree = new CanonicalSchemaTree(SchemaKey.anonymousKey(), schema);
    }

    @DataProvider
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

    @Test(dataProvider = "getLookups")
    public void canonicalTreeDoesNotContainInlineContexts(final JsonRef ref)
    {
        assertFalse(schemaTree.containsRef(ref));
    }
}
