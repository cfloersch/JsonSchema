package xpertss.json.schema.core.util;

import com.google.common.collect.ImmutableList;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Iterator;

import static org.junit.Assert.assertEquals;


@RunWith(JUnitParamsRunner.class)
public final class RegexECMA262HelperTest {

    public Iterator<Object[]> ecma262regexes()
    {
        return ImmutableList.of(
            new Object[] { "[^]", true },
            new Object[] { "(?<=foo)bar", false },
            new Object[] { "", true },
            new Object[] { "[a-z]+(?!foo)(?=bar)", true }
        ).iterator();
    }

    @Test
    @Parameters(method = "ecma262regexes")
    public void regexesAreCorrectlyAnalyzed(String regex, boolean valid)
    {
        assertEquals(valid, RegexECMA262Helper.regexIsValid(regex));
    }

    public Iterator<Object[]> regexTestCases()
    {
        return ImmutableList.of(
            new Object[] { "[^a-z]", "9am", true },
            new Object[] { "bar\\d+", "foobar19ae", true },
            new Object[] { "^bar\\d+", "bar", false },
            new Object[] { "^(?:[A-Za-z0-9+/]{4})*(?:[A-Za-z0-9+/]{2}==|[A-Za-z0-9+/]{3}=)?$", "QmFzZTY0IFN0cmluZwo=", true }, // Base64 regex match
            new Object[] { "^(?:[A-Za-z0-9+/]{4})*(?:[A-Za-z0-9+/]{2}==|[A-Za-z0-9+/]{3}=)?$", "QmFzZTY0IFN0cmluZwo", false }, // Base64 regex match
            new Object[] { "[a-z]+(?!foo)(?=bar)", "3aaaabar", true }
        ).iterator();
    }

    @Test
    @Parameters(method = "regexTestCases")
    public void regexMatchingIsDoneCorrectly(String regex, String input, boolean valid)
    {
        assertEquals(valid, RegexECMA262Helper.regMatch(regex, input));
    }
}
