package xpertss.json.schema.format.draftv6;

import com.github.fge.jackson.NodeType;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.format.AbstractFormatAttribute;
import xpertss.json.schema.format.FormatAttribute;
import xpertss.json.schema.processors.data.FullData;
import com.github.fge.msgsimple.bundle.MessageBundle;
import com.github.fge.uritemplate.URITemplate;
import com.github.fge.uritemplate.URITemplateParseException;

public final class URITemplateFormatAttribute extends AbstractFormatAttribute {

    private static final FormatAttribute INSTANCE = new URITemplateFormatAttribute();

    private URITemplateFormatAttribute()
    {
        super("uri-template", NodeType.STRING);
    }

    public static FormatAttribute getInstance()
    {
        return INSTANCE;
    }

    @Override
    public void validate(ProcessingReport report, MessageBundle bundle, FullData data)
        throws ProcessingException
    {
        String value = data.getInstance().getNode().textValue();
        try {
            new URITemplate(value);
        } catch (URITemplateParseException ignored) {
            report.error(newMsg(data, bundle, "err.format.uriTemplate.invalid")
                .putArgument("value", value));
        }
    }
}
