package xpertss.json.schema.format.draftv3;

import com.github.fge.jackson.NodeType;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.format.AbstractFormatAttribute;
import xpertss.json.schema.format.FormatAttribute;
import xpertss.json.schema.processors.data.FullData;
import com.github.fge.msgsimple.bundle.MessageBundle;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;

/**
 * Attempt to validate the {@code phone} format attribute.
 *
 * <p>The draft says the phone MAY match E.123. Quite vague. Here we use
 * Google's <a href="http://code.google.com/p/libphonenumber/">libphonenumber
 * </a> as it is a library specialized in phone number recognition.</p>
 *
 * <p>It will only chek if this is a potential phone number, not whether it is
 * actually valid for your country! If you really want that, you will probably
 * want to write your own {@link FormatAttribute}.</p>
 */
//TODO: more tests?
public final class PhoneAttribute extends AbstractFormatAttribute {

    private static final PhoneNumberUtil PARSER = PhoneNumberUtil.getInstance();

    private static final FormatAttribute INSTANCE = new PhoneAttribute();

    public static FormatAttribute getInstance()
    {
        return INSTANCE;
    }

    private PhoneAttribute()
    {
        super("phone", NodeType.STRING);
    }

    @Override
    public void validate(ProcessingReport report, MessageBundle bundle, FullData data)
        throws ProcessingException
    {
        String input = data.getInstance().getNode().textValue();
        /*
         * The libphonenumber API doc says that no matter what region you put
         * when validating national phone numbers, the number is not actually
         * considered valid for a specific country without further
         * verifications. International phone numbers MUST start with a
         * "+" however, this is a constant.
         *
         * So, this is the only switching point: if it starts with a "+",
         * check with the "no zone" specification, otherwise check with any
         * country code.
         */
        try {
            String region = (input.startsWith("+")) ? "ZZ" : "FR";
            PARSER.parse(input, region);
        } catch (NumberParseException ignored) {
            report.error(newMsg(data, bundle, "err.format.invalidPhoneNumber")
                .putArgument("value", input));
        }
    }
}
