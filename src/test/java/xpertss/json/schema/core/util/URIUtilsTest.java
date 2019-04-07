package xpertss.json.schema.core.util;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import xpertss.json.schema.core.messages.JsonSchemaCoreMessageBundle;
import com.github.fge.msgsimple.bundle.MessageBundle;
import com.github.fge.msgsimple.load.MessageBundles;
import com.google.common.collect.Lists;

import java.net.URI;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


@RunWith(JUnitParamsRunner.class)
public final class URIUtilsTest {

    private static final MessageBundle BUNDLE = MessageBundles.getBundle(JsonSchemaCoreMessageBundle.class);

    public Iterator<Object[]> schemeData()
    {
        final List<Object[]> list = Lists.newArrayList();

        String orig, dst;

        orig = "http";
        dst = "http";
        list.add(new Object[] { orig, dst });

        orig = "hTTp";
        dst = "http";
        list.add(new Object[] { orig, dst });

        orig = "GIT+ssh";
        dst = "git+ssh";
        list.add(new Object[] { orig, dst });

        list.add(new Object[] { null, null });

        return list.iterator();
    }

    @Test
    @Parameters(method = "schemeData")
    public void schemesAreCorrectlyNormalized(String orig, String dst)
    {
        assertEquals(dst, URIUtils.normalizeScheme(orig));
    }

    public Iterator<Object[]> uriData()
    {
        final List<Object[]> list = Lists.newArrayList();

        String orig, dst;

        orig = "HTTP://SLAShDOt.ORG/foo/BAR/baz";
        dst = "http://slashdot.org/foo/BAR/baz";
        list.add(new Object[] { URI.create(orig), URI.create(dst) });

        orig = "file:///hello/worlD";
        dst = "file:/hello/worlD";
        list.add(new Object[] { URI.create(orig), URI.create(dst) });

        orig = "git+ssh://Gloubi.Boulga/a/c/../b";
        dst = "git+ssh://gloubi.boulga/a/b";
        list.add(new Object[] { URI.create(orig), URI.create(dst) });


        // Issue #15
        orig =  "uRn:jsonschema:com:example:AccountRestrictionsUpdated";
        dst =  "urn:jsonschema:com:example:AccountRestrictionsUpdated";
        list.add(new Object[] { URI.create(orig), URI.create(dst) });

        list.add(new Object[] { null, null });

        return list.iterator();
    }

    @Test
    @Parameters(method = "uriData")
    public void urisAreCorrectlyNormalized(URI orig, URI dst)
    {
        assertEquals(dst, URIUtils.normalizeURI(orig));
    }

    public Iterator<Object[]> schemaURIs()
    {
        final List<Object[]> list = Lists.newArrayList();

        String orig, dst;

        orig = "a/b";
        dst = "a/b#";
        list.add(new Object[] { URI.create(orig), URI.create(dst) });

        orig = "http://my.site/schema#definitions/foo";
        dst = "http://my.site/schema#definitions/foo";
        list.add(new Object[] { URI.create(orig), URI.create(dst) });

        orig = "http://my.site/schema";
        dst = "http://my.site/schema#";
        list.add(new Object[] { URI.create(orig), URI.create(dst) });
        list.add(new Object[] { null, null });

        // Issue #15
        orig =  "uRn:jsonschema:com:example:AccountRestrictionsUpdated";
        dst =  "urn:jsonschema:com:example:AccountRestrictionsUpdated#";
        list.add(new Object[] { URI.create(orig), URI.create(dst) });
        return list.iterator();
    }


    @Test
    @Parameters(method = "schemaURIs")
    public void schemaURIsAreCorrectlyNormalized(URI orig, URI dst)
    {
        assertEquals(dst, URIUtils.normalizeSchemaURI(orig));
    }

    public Iterator<Object[]> invalidPathURIs()
    {
        final List<Object[]> list = Lists.newArrayList();

        String uri;
        String key;

        key = "uriChecks.notAbsolute";
        list.add(new Object[] { "", key });

        uri = "foo://bar/#";
        key = "uriChecks.fragmentNotNull";
        list.add(new Object[] { uri, key });

        uri = "foo://bar?baz=meh";
        key = "uriChecks.queryNotNull";
        list.add(new Object[] { uri, key });

        uri = "foo://bar/baz";
        key = "uriChecks.noEndingSlash";
        list.add(new Object[] { uri, key });

        uri = "urn:whatever:hello";
        key = "uriChecks.noPath";
        list.add(new Object[] { uri, key });

        return list.iterator();
    }

    @Test
    @Parameters(method = "invalidPathURIs")
    public void invalidPathURIsAreRejected(String uri, String key)
    {
        try {
            URIUtils.checkPathURI(URI.create(uri));
            fail("No exception thrown!");
        } catch (IllegalArgumentException e) {
            assertEquals(BUNDLE.printf(key, uri), e.getMessage());
        }
    }

    public Iterator<Object[]> invalidSchemaURIs()
    {
        final List<Object[]> list = Lists.newArrayList();

        String uri;
        String key;

        key = "uriChecks.notAbsolute";
        list.add(new Object[] { "", key });

        uri = "foo://bar/#/a";
        key = "uriChecks.notAbsoluteRef";
        list.add(new Object[] { uri, key });

        uri = "foo://bar/baz/";
        key = "uriChecks.endingSlash";
        list.add(new Object[] { uri, key });

        return list.iterator();
    }

    @Test
    @Parameters(method = "invalidSchemaURIs")
    public void invalidSchemaURIsAreRejected(String uri, String key)
    {
        try {
            URIUtils.checkSchemaURI(URI.create(uri));
            fail("No exception thrown!");
        } catch (IllegalArgumentException e) {
            assertEquals(BUNDLE.printf(key, uri), e.getMessage());
        }
    }

}
