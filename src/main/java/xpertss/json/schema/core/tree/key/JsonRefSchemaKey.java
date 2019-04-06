package xpertss.json.schema.core.tree.key;

import xpertss.json.schema.core.ref.JsonRef;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A unique schema key for JSON Schemas loaded via URIs
 */
public final class JsonRefSchemaKey
    extends SchemaKey
{
    JsonRefSchemaKey(final JsonRef ref)
    {
        super(ref);
    }

    @Override
    public long getId()
    {
        return 0L;
    }

    @Override
    public int hashCode()
    {
        return loadingRef.hashCode();
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
        final JsonRefSchemaKey other = (JsonRefSchemaKey) obj;
        return loadingRef.equals(other.loadingRef);
    }

    @Nonnull
    @Override
    public String toString()
    {
        return "loaded from JSON ref " + loadingRef;
    }
}
