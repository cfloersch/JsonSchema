package xpertss.json.schema.processors.validation;

import xpertss.json.schema.processors.data.SchemaContext;
import com.google.common.base.Equivalence;

/**
 * Equivalence for schema contexts
 *
 * <p>This is used by {@link ValidationChain} and {@link ValidationProcessor} to
 * cache computation results. Two schema contexts are considered equivalent if:
 * </p>
 *
 * <ul>
 *     <li>schema trees are considered equivalent,</li>
 *     <li>and the type of the instance is the same.</li>
 * </ul>
 *
 */
public final class SchemaContextEquivalence
    extends Equivalence<SchemaContext>
{
    private static final Equivalence<SchemaContext> INSTANCE
        = new SchemaContextEquivalence();

    public static Equivalence<SchemaContext> getInstance()
    {
        return INSTANCE;
    }

    @Override
    protected boolean doEquivalent(final SchemaContext a, final SchemaContext b)
    {
        return a.getSchema().equals(b.getSchema())
            && a.getInstanceType() == b.getInstanceType();
    }

    @Override
    protected int doHash(final SchemaContext t)
    {
        return t.getSchema().hashCode() ^ t.getInstanceType().hashCode();
    }
}
