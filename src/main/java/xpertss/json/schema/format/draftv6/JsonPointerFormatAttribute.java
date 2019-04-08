package xpertss.json.schema.format.draftv6;

import com.github.fge.jackson.NodeType;
import com.github.fge.jackson.jsonpointer.JsonPointer;
import com.github.fge.jackson.jsonpointer.JsonPointerException;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.format.AbstractFormatAttribute;
import xpertss.json.schema.format.FormatAttribute;
import xpertss.json.schema.processors.data.FullData;
import com.github.fge.msgsimple.bundle.MessageBundle;

public final class JsonPointerFormatAttribute extends AbstractFormatAttribute {

    private static final FormatAttribute INSTANCE = new JsonPointerFormatAttribute();

    private JsonPointerFormatAttribute()
    {
        super("json-pointer", NodeType.STRING);
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
            new JsonPointer(value);
        } catch (JsonPointerException ignored) {
            report.error(newMsg(data, bundle, "err.format.jsonpointer.invalid")
                .putArgument("value", value));
        }
    }
}
