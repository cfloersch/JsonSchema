package xpertss.json.schema.main.cli;

import com.fasterxml.jackson.databind.JsonNode;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.main.JsonSchema;
import xpertss.json.schema.processors.syntax.SyntaxValidator;

import java.io.IOException;

interface Reporter
{
    RetCode validateSchema(SyntaxValidator validator, String fileName, JsonNode node)
        throws IOException;
    RetCode validateInstance(JsonSchema schema, String fileName, JsonNode node)
        throws IOException, ProcessingException;
}
