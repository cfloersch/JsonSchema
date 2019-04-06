package xpertss.json.schema.library;

import com.github.fge.Frozen;
import xpertss.json.schema.core.keyword.syntax.checkers.SyntaxChecker;
import xpertss.json.schema.keyword.digest.Digester;
import xpertss.json.schema.keyword.validator.KeywordValidatorFactory;


/**
 * Frozen keyword
 *
 * @see KeywordBuilder
 */
public final class Keyword
    implements Frozen<KeywordBuilder>
{
    /**
     * Name of this keyword
     */
    final String name;

    /**
     * Syntax checker
     */
    final SyntaxChecker syntaxChecker;

    /**
     * Digester
     */
    final Digester digester;

    /**
     * Validator factory
     */
    final KeywordValidatorFactory validatorFactory;

    /**
     * Instantiate a new keyword builder
     *
     * @param name the name for this keyword
     * @return a new {@link KeywordBuilder}
     * @throws NullPointerException provided name is null
     * @see KeywordBuilder#KeywordBuilder(String)
     */
    public static KeywordBuilder newBuilder(final String name)
    {
        return new KeywordBuilder(name);
    }

    /**
     * Build a frozen keyword out of a thawed one
     *
     * @param builder the keyword builder to build from
     * @see KeywordBuilder#freeze()
     */
    Keyword(final KeywordBuilder builder)
    {
        name = builder.name;
        syntaxChecker = builder.syntaxChecker;
        digester = builder.digester;
        validatorFactory = builder.validatorFactory;
    }

    /**
     * Create a thawed version of this keyword
     *
     * @return a {@link KeywordBuilder}
     * @see KeywordBuilder#KeywordBuilder(Keyword)
     */
    @Override
    public KeywordBuilder thaw()
    {
        return new KeywordBuilder(this);
    }
}
