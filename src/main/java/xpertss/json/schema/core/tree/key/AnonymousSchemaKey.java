package xpertss.json.schema.core.tree.key;

import xpertss.json.schema.core.ref.JsonRef;
import com.google.common.primitives.Longs;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicLong;

/**
 * A unique schem key for schemas loaded anonymously
 */
public final class AnonymousSchemaKey extends SchemaKey {

    private static final AtomicLong ID_GEN = new AtomicLong(0L);

    private final long id;

    AnonymousSchemaKey()
    {
        super(JsonRef.emptyRef());
        id = ID_GEN.getAndIncrement();
    }

    @Override
    public long getId()
    {
        return id;
    }

    @Override
    public int hashCode()
    {
        return Longs.hashCode(id);
    }

    @Override
    public boolean equals(@Nullable final Object obj)
    {
        if (obj == null)
            return false;
        if (this == obj)
            return true;
        if (getClass() != obj.getClass())
            return false;
        final AnonymousSchemaKey other = (AnonymousSchemaKey) obj;
        return id == other.id;
    }

    @Nonnull
    @Override
    public String toString()
    {
        return "anonymous; id = " + id;
    }
}
