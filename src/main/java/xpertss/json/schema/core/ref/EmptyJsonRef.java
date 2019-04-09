package xpertss.json.schema.core.ref;

/**
 * A completely empty JSON Reference (ie, {@code #})
 *
 * <p>This happens in a lot of situations, it is therefore beneficial to have
 * a dedicated class for it. For instance, resolving any other reference against
 * this one always returns the other reference, and it is never absolute.</p>
 */
final class EmptyJsonRef extends JsonRef {

    private static final JsonRef INSTANCE = new EmptyJsonRef();

    private EmptyJsonRef()
    {
        super(HASHONLY_URI);
    }

    static JsonRef getInstance()
    {
        return INSTANCE;
    }

    @Override
    public boolean isAbsolute()
    {
        return false;
    }

    @Override
    public JsonRef resolve(JsonRef other)
    {
        return other;
    }
}
