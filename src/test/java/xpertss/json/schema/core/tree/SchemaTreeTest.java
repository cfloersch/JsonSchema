package xpertss.json.schema.core.tree;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jackson.JacksonUtils;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jackson.NodeType;
import com.github.fge.jackson.jsonpointer.JsonPointer;
import com.github.fge.jackson.jsonpointer.JsonPointerException;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import xpertss.json.schema.SampleNodeProvider;
import xpertss.json.schema.core.exceptions.JsonReferenceException;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.ref.JsonRef;
import xpertss.json.schema.core.tree.key.SchemaKey;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import java.io.IOException;
import java.net.URI;
import java.util.Iterator;
import java.util.Set;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertSame;
import static org.junit.Assert.assertEquals;


@RunWith(JUnitParamsRunner.class)
public final class SchemaTreeTest {

    private static final JsonNodeFactory FACTORY = JacksonUtils.nodeFactory();

    private final JsonNodeFactory factory = JacksonUtils.nodeFactory();
    private JsonNode data;
    private JsonNode schema;

    public SchemaTreeTest()
        throws IOException
    {
        data = JsonLoader.fromResource("/tree/context.json");
        schema = data.get("schema");
    }

    @Test
    public void loadingRefIsReturnedWhenNoIdAtTopLevel()
    {
        SchemaTree schemaTree;

        final ObjectNode node = FACTORY.objectNode();

        schemaTree = new CanonicalSchemaTree(SchemaKey.anonymousKey(), node);
        assertSame(schemaTree.getContext(), JsonRef.emptyRef());

        final URI uri = URI.create("foo://bar");
        final JsonRef ref = JsonRef.fromURI(uri);

        schemaTree = new CanonicalSchemaTree(SchemaKey.forJsonRef(ref), node);
        assertSame(schemaTree.getContext(), ref);
        assertEquals("CanonicalSchemaTree{key=loaded from JSON ref foo://bar#, pointer=, URI context=foo://bar#}", schemaTree.toString());
    }

    public Iterator<Object[]> sampleIds()
    {
        return ImmutableSet.of(
            new Object[] { "", "http://foo.bar" },
            new Object[] { "http://foo.bar/baz", "meh#la" },
            new Object[] { "ftp://ftp.lip6.fr/schema", "x://y" }
        ).iterator();
    }

    @Test
    @Parameters(method = "sampleIds")
    public void topMostIdIsResolvedAgainstLoadingRef(String loading, String id)
        throws ProcessingException
    {
        final JsonRef loadingRef = JsonRef.fromString(loading);
        final JsonRef idRef = JsonRef.fromString(id);
        final JsonRef resolved = loadingRef.resolve(idRef);

        final ObjectNode node = factory.objectNode();
        node.put("id", id);

        final SchemaTree tree
            = new CanonicalSchemaTree(SchemaKey.forJsonRef(loadingRef), node);
        assertEquals(tree.getContext(), resolved);
    }

    public Iterator<Object[]> getContexts()
    {
        final JsonNode node = data.get("lookups");

        final Set<Object[]> set = Sets.newHashSet();

        for (final JsonNode element: node)
            set.add(new Object[] {
                element.get("pointer").textValue(),
                element.get("scope").textValue()
            });

        return set.iterator();
    }

    @Test
    @Parameters(method = "getContexts")
    public void pointerAppendCorrectlyCalculatesContext(String path, String s)
        throws JsonPointerException, JsonReferenceException
    {
        final JsonPointer ptr = new JsonPointer(path);
        final JsonRef scope = JsonRef.fromString(s);
        SchemaTree tree = new CanonicalSchemaTree(SchemaKey.anonymousKey(), schema);

        assertEquals(tree.append(ptr).getContext(), scope);
    }

    @Test
    @Parameters(method = "getContexts")
    public void pointerSetCorrectlyCalculatesContext(String path, String s)
        throws JsonPointerException, JsonReferenceException
    {
        final JsonPointer ptr = new JsonPointer(path);
        final JsonRef scope = JsonRef.fromString(s);
        SchemaTree tree = new CanonicalSchemaTree(SchemaKey.anonymousKey(), schema);
        final JsonRef origRef = tree.getContext();

        tree = tree.setPointer(ptr);
        assertEquals(tree.getContext(), scope);
        tree = tree.setPointer(JsonPointer.empty());
        assertEquals(tree.getContext(), origRef);
    }

    public Iterator<Object[]> nonSchemas()
    {
        return SampleNodeProvider.getSamplesExcept(NodeType.OBJECT);
    }

    @Test
    @Parameters(method = "nonSchemas")
    public void nonSchemasYieldAnEmptyRef(final JsonNode node)
    {
        final SchemaTree tree
            = new CanonicalSchemaTree(SchemaKey.anonymousKey(), node);
        assertEquals(tree.getDollarSchema(), JsonRef.emptyRef());
    }

    public Iterator<Object[]> nonStringDollarSchemas()
    {
        return SampleNodeProvider.getSamples(NodeType.STRING);
    }

    @Test
    public void schemaWithoutDollarSchemaYieldsAnEmptyRef()
    {
        final ObjectNode node = FACTORY.objectNode();
        final SchemaTree tree
            = new CanonicalSchemaTree(SchemaKey.anonymousKey(), node);
        assertEquals(tree.getDollarSchema(), JsonRef.emptyRef());
    }

    @Test
    @Parameters(method = "nonStringDollarSchemas")
    public void nonTextualDollarSchemasYieldAnEmptyRef(final JsonNode node)
    {
        final ObjectNode testNode = FACTORY.objectNode();
        testNode.put("$schema", node);

        SchemaTree tree = new CanonicalSchemaTree(SchemaKey.anonymousKey(), testNode);
        assertEquals(tree.getDollarSchema(), JsonRef.emptyRef());
    }

    public Iterator<Object[]> nonLegalDollarSchemas()
    {
        return ImmutableList.of(
            new Object[] { "" },
            new Object[] { "foo#" },
            new Object[] { "http://my.site/myschema#a" }
        ).iterator();
    }

    @Test
    @Parameters(method = "nonLegalDollarSchemas")
    public void nonAbsoluteDollarSchemasYieldAnEmptyRef(final String s)
    {
        final ObjectNode testNode = FACTORY.objectNode();
        testNode.put("$schema", FACTORY.textNode(s));

        SchemaTree tree = new CanonicalSchemaTree(SchemaKey.anonymousKey(), testNode);
        assertEquals(tree.getDollarSchema(), JsonRef.emptyRef());
    }

    public Iterator<Object[]> legalDollarSchemas()
    {
        return ImmutableList.of(
            new Object[] { "http://json-schema.org/schema#" },
            new Object[] { "http://json-schema.org/draft-03/schema" },
            new Object[] { "http://json-schema.org/draft-04/schema#" },
            new Object[] { "http://me.org/myschema" }
        ).iterator();
    }

    @Test
    @Parameters(method = "legalDollarSchemas")
    public void legalDollarSchemasAreReturnedCorrectly(final String s)
        throws JsonReferenceException
    {
        final JsonRef ref = JsonRef.fromString(s);
        final ObjectNode testNode = FACTORY.objectNode();
        testNode.put("$schema", FACTORY.textNode(s));

        SchemaTree tree = new CanonicalSchemaTree(SchemaKey.anonymousKey(), testNode);
        assertEquals(tree.getDollarSchema(), ref);
    }

    @Test
    public void twoAnonymouslyLoadedSchemasAreNotEqualEvenIfJsonIsEqual()
        throws IOException
    {
        final JsonNode node = JsonLoader.fromResource("/draftv4/schema");
        SchemaTree tree = new CanonicalSchemaTree(SchemaKey.anonymousKey(), node);
        SchemaTree tree2 = new CanonicalSchemaTree(SchemaKey.anonymousKey(), node);

        assertFalse(tree.equals(tree2));
    }
}
