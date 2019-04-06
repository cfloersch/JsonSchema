package xpertss.json.schema.core.util.equivalence;

import xpertss.json.schema.core.tree.SchemaTree;
import xpertss.json.schema.core.tree.key.SchemaKey;
import com.google.common.base.Equivalence;

/**
 * Schema tree equivalence
 *
 * <p>Two schema trees are considered equivant if their loading URI, current
 * URI context, base node and pointers are equivalent.</p>
 *
 * @deprecated see {@link SchemaKey}
 */
// TODO move
@Deprecated
public final class SchemaTreeEquivalence
    extends Equivalence<SchemaTree>
{
    private static final Equivalence<SchemaTree> INSTANCE
        = new SchemaTreeEquivalence();

    public static Equivalence<SchemaTree> getInstance()
    {
        return INSTANCE;
    }

    private SchemaTreeEquivalence()
    {
    }

    @Override
    protected boolean doEquivalent(final SchemaTree a, final SchemaTree b)
    {
        return a.getLoadingRef().equals(b.getLoadingRef())
            && a.getContext().equals(b.getContext())
            && a.getPointer().equals(b.getPointer())
            && a.getBaseNode().equals(b.getBaseNode());
    }

    @Override
    protected int doHash(final SchemaTree t)
    {
        int ret = t.getLoadingRef().hashCode();
        ret = 31 * ret + t.getContext().hashCode();
        ret = 31 * ret + t.getPointer().hashCode();
        ret = 31 * ret + t.getBaseNode().hashCode();
        return ret;
    }
}
