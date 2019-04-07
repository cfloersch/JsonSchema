package xpertss.json.schema.keyword.digest;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.NodeType;
import xpertss.json.schema.keyword.validator.KeywordValidator;
import xpertss.json.schema.processors.digest.SchemaDigester;
import xpertss.json.schema.processors.validation.ArraySchemaDigester;
import xpertss.json.schema.processors.validation.ObjectSchemaDigester;

import java.util.EnumSet;

/**
 * Interface for a digester
 *
 * <p>A digester, as its name implies, digests a schema (which comes here as a
 * {@link JsonNode}) and returns a simplified form of it, according to its
 * context.</p>
 *
 * <p>It is mainly used for keywords, for building a simplified form of a schema
 * in order to ease the job of keyword construction; but most importantly, it
 * also reports the instance types supported by this keyword.</p>
 *
 * <p>It is also used to build a digested form of schemas for array/object
 * schema selections.</p>
 *
 * @see SchemaDigester
 * @see KeywordValidator
 * @see ArraySchemaDigester
 * @see ObjectSchemaDigester
 */
public interface Digester {

    /**
     * Return the instance types handled by this digested form
     *
     * @return a set of {@link NodeType}s
     */
    EnumSet<NodeType> supportedTypes();

    /**
     * Digest a schema into a simplified form
     *
     * @param schema the schema to digest
     * @return the digested form
     */
    JsonNode digest(JsonNode schema);
}
