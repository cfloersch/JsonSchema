package xpertss.json.schema.core.util;

import javax.annotation.concurrent.ThreadSafe;

/**
 * @see RegexECMA262Helper
 * @deprecated use {@link RegexECMA262Helper} as 1:1 replacement
 */
@ThreadSafe
@Deprecated
public final class RhinoHelper
{
    /**
     * Validate that a regex is correct
     *
     * @param regex the regex to validate
     * @return true if the regex is valid
     * @deprecated use {@link RegexECMA262Helper#regexIsValid(String)}
     */
    @Deprecated
    public static boolean regexIsValid(final String regex)
    {
        return RegexECMA262Helper.regexIsValid(regex);
    }

    /**
     * <p>Matches an input against a given regex, in the <b>real</b> sense
     * of matching, that is, the regex can match anywhere in the input. Java's
     * {@link java.util.regex} makes the unfortunate mistake to make people
     * believe that matching is done on the whole input... Which is not true.
     * </p>
     *
     * <p>Also note that the regex MUST have been validated at this point
     * (using {@link #regexIsValid(String)}).</p>
     *
     * @param regex the regex to use
     * @param input the input to match against (and again, see description)
     * @return true if the regex matches the input
     * @deprecated use {@link RegexECMA262Helper#regMatch(String, String)}
     */
    @Deprecated
    public static boolean regMatch(final String regex, final String input)
    {
        return RegexECMA262Helper.regMatch(regex, input);
    }
}
