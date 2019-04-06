package xpertss.json.schema;

import xpertss.json.schema.core.report.ProcessingMessage;
import xpertss.json.schema.core.report.ProcessingReport;
import xpertss.json.schema.core.tree.SchemaTree;
import org.mockito.verification.VerificationMode;

import static org.mockito.Mockito.*;

public final class TestUtils
{
    private TestUtils()
    {
    }

    public static VerificationMode onlyOnce()
    {
        return times(1);
    }

    public static ProcessingReport anyReport()
    {
        return any(ProcessingReport.class);
    }

    public static SchemaTree anySchema()
    {
        return any(SchemaTree.class);
    }

    public static ProcessingMessage anyMessage()
    {
        return any(ProcessingMessage.class);
    }
}
