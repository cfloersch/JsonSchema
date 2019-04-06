package xpertss.json.schema.processors.data;

import xpertss.json.schema.core.report.MessageProvider;
import xpertss.json.schema.core.report.ProcessingMessage;
import xpertss.json.schema.keyword.validator.KeywordValidator;
import xpertss.json.schema.processors.build.ValidatorBuilder;
import xpertss.json.schema.processors.format.FormatProcessor;
import com.google.common.collect.ImmutableList;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Output of {@link ValidatorBuilder}, and input/output of {@link
 * FormatProcessor}
 */
public final class ValidatorList
    implements Iterable<KeywordValidator>, MessageProvider
{
    private final List<KeywordValidator> validators;
    private final SchemaContext context;

    public ValidatorList(final SchemaContext context,
        final Collection<KeywordValidator> validators)
    {
        this.context = context;
        this.validators = ImmutableList.copyOf(validators);
    }

    public SchemaContext getContext()
    {
        return context;
    }

    @Override
    public Iterator<KeywordValidator> iterator()
    {
        return validators.iterator();
    }

    @Override
    public ProcessingMessage newMessage()
    {
        return context.newMessage();
    }
}
