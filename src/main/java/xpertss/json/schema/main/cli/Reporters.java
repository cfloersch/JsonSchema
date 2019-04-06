package xpertss.json.schema.main.cli;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JacksonUtils;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.report.ListProcessingReport;
import xpertss.json.schema.main.JsonSchema;
import xpertss.json.schema.processors.syntax.SyntaxValidator;

import java.io.IOException;

import static xpertss.json.schema.main.cli.RetCode.*;

enum Reporters
    implements Reporter
{
    DEFAULT
    {
        @Override
        public RetCode validateSchema(final SyntaxValidator validator,
            final String fileName, final JsonNode node)
            throws IOException
        {
            final ListProcessingReport report
                = (ListProcessingReport) validator.validateSchema(node);
            final boolean success = report.isSuccess();
            System.out.println("--- BEGIN " + fileName + "---");
            System.out.println("validation: " + (success ? "SUCCESS"
                : "FAILURE"));
            if (!success)
                System.out.println(JacksonUtils.prettyPrint(report.asJson()));
            System.out.println("--- END " + fileName + "---");
            return success ? ALL_OK : SCHEMA_SYNTAX_ERROR;
        }

        @Override
        public RetCode validateInstance(final JsonSchema schema,
            final String fileName, final JsonNode node)
            throws IOException, ProcessingException
        {
            final ListProcessingReport report
                = (ListProcessingReport) schema.validate(node, true);
            final boolean success = report.isSuccess();
            System.out.println("--- BEGIN " + fileName + "---");
            System.out.println("validation: " + (success ? "SUCCESS"
                : "FAILURE"));
            if (!success)
                System.out.println(JacksonUtils.prettyPrint(report
                    .asJson()));
            System.out.println("--- END " + fileName + "---");
            return success ? ALL_OK : VALIDATION_FAILURE;
        }
    },
    BRIEF
    {
        @Override
        public RetCode validateSchema(final SyntaxValidator validator,
            final String fileName, final JsonNode node)
            throws IOException
        {
            final boolean valid = validator.schemaIsValid(node);
            System.out.printf("%s: %s\n", fileName, valid ? "OK": "NOT OK");
            return valid ? ALL_OK : SCHEMA_SYNTAX_ERROR;
        }

        @Override
        public RetCode validateInstance(final JsonSchema schema,
            final String fileName, final JsonNode node)
            throws IOException, ProcessingException
        {
            final boolean valid = schema.validInstance(node);
            System.out.printf("%s: %s\n", fileName, valid ? "OK": "NOT OK");
            return valid ? ALL_OK : VALIDATION_FAILURE;
        }
    },
    QUIET
    {
        @Override
        public RetCode validateSchema(final SyntaxValidator validator,
            final String fileName, final JsonNode node)
            throws IOException
        {
            return validator.schemaIsValid(node) ? ALL_OK : SCHEMA_SYNTAX_ERROR;
        }

        @Override
        public RetCode validateInstance(final JsonSchema schema,
            final String fileName, final JsonNode node)
            throws IOException, ProcessingException
        {
            return schema.validInstance(node) ? ALL_OK : VALIDATION_FAILURE;
        }
    }
}
