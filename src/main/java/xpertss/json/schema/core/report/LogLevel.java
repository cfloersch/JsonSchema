package xpertss.json.schema.core.report;

import xpertss.json.schema.core.exceptions.ProcessingException;

/**
 * Message log levels
 *
 * <p>A special log level, {@link #NONE}, can be used by processors wishing to
 * implement "unchecked" validation (ie, capture {@link ProcessingException}s
 * and report them instead of throwing them).</p>
 *
 * <p>All messages within {@link ProcessingException}s have level {@link
 * #FATAL}.</p>
 */
public enum LogLevel {

    DEBUG("debug"),
    INFO("info"),
    WARNING("warning"),
    ERROR("error"),
    FATAL("fatal"),
    NONE("none"),
    ;

    private final String s;

    LogLevel(String s)
    {
        this.s = s;
    }

    @Override
    public String toString()
    {
        return s;
    }
}

