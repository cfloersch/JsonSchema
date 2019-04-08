package xpertss.json.schema.core.keyword.syntax.checkers.common;

import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.core.keyword.syntax.checkers.SyntaxChecker;
import xpertss.json.schema.core.keyword.syntax.checkers.helpers.SchemaMapSyntaxChecker;
import xpertss.json.schema.core.tree.SchemaTree;
import xpertss.json.schema.core.util.RegexECMA262Helper;
import com.github.fge.msgsimple.bundle.MessageBundle;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;

import java.util.Set;

/**
 * Syntax checker for the {@code patternProperties} keyword
 *
 * @see RegexECMA262Helper
 */
public final class PatternPropertiesSyntaxChecker extends SchemaMapSyntaxChecker {

    private static final SyntaxChecker INSTANCE = new PatternPropertiesSyntaxChecker();

    public static SyntaxChecker getInstance()
    {
        return INSTANCE;
    }

    private PatternPropertiesSyntaxChecker()
    {
        super("patternProperties");
    }

    @Override
    protected void extraChecks(ProcessingReport report, MessageBundle bundle, SchemaTree tree)
        throws ProcessingException
    {
        /*
         * Check that the member names are regexes
         */
        Set<String> set = Sets.newHashSet(getNode(tree).fieldNames());

        for (final String s: Ordering.natural().sortedCopy(set))
            if (!RegexECMA262Helper.regexIsValid(s))
                report.error(newMsg(tree, bundle,
                    "common.patternProperties.member.notRegex")
                    .putArgument("propertyName", s));
    }
}
