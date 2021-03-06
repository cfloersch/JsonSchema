package xpertss.json.schema.main.cli;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jackson.JacksonUtils;
import xpertss.json.schema.core.exceptions.ProcessingException;
import xpertss.json.schema.core.load.configuration.LoadingConfiguration;
import xpertss.json.schema.core.load.uri.URITranslatorConfiguration;
import xpertss.json.schema.core.load.uri.URITranslatorConfigurationBuilder;
import xpertss.json.schema.core.util.URIUtils;
import xpertss.json.schema.main.JsonSchema;
import xpertss.json.schema.main.JsonSchemaFactory;
import xpertss.json.schema.processors.syntax.SyntaxValidator;
import com.google.common.collect.Lists;
import joptsimple.HelpFormatter;
import joptsimple.OptionException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static xpertss.json.schema.main.cli.RetCode.*;

public final class Main {

    private static final HelpFormatter HELP = new CustomHelpFormatter();

    private static final ObjectMapper MAPPER = JacksonUtils.newMapper();

    private final JsonSchemaFactory factory;
    private final SyntaxValidator syntaxValidator;

    public static void main(String... args)
        throws IOException, ProcessingException
    {
        final OptionParser parser = new OptionParser();
        parser.accepts("help", "show this help").forHelp();
        parser.acceptsAll(Arrays.asList("s", "brief"),
            "only show validation status (OK/NOT OK)");
        parser.acceptsAll(Arrays.asList("q", "quiet"),
            "no output; exit with the relevant return code (see below)");
        parser.accepts("syntax",
            "check the syntax of schema(s) given as argument(s)");
        parser.accepts("fakeroot",
            "pretend that the current directory is absolute URI \"uri\"")
            .withRequiredArg();
        parser.formatHelpWith(HELP);

        final OptionSet optionSet;
        final boolean isSyntax;
        final int requiredArgs;

        Reporter reporter = Reporters.DEFAULT;
        String fakeRoot = null;

        try {
            optionSet = parser.parse(args);
        } catch (OptionException e) {
            System.err.println("unrecognized option(s): "
                + CustomHelpFormatter.OPTIONS_JOINER.join(e.options()));
            parser.printHelpOn(System.err);
            System.exit(CMD_ERROR.get());
            throw new IllegalStateException("WTF??");
        }

        if (optionSet.has("help")) {
            parser.printHelpOn(System.out);
            System.exit(ALL_OK.get());
        }

        if (optionSet.has("s") && optionSet.has("q")) {
            System.err.println("cannot specify both \"--brief\" and " +
                "\"--quiet\"");
            parser.printHelpOn(System.err);
            System.exit(CMD_ERROR.get());
        }

        if (optionSet.has("fakeroot"))
            fakeRoot = (String) optionSet.valueOf("fakeroot");

        isSyntax = optionSet.has("syntax");
        requiredArgs = isSyntax ? 1 : 2;

        @SuppressWarnings("unchecked")
        final List<String> arguments
            = (List<String>) optionSet.nonOptionArguments();

        if (arguments.size() < requiredArgs) {
            System.err.println("missing arguments");
            parser.printHelpOn(System.err);
            System.exit(CMD_ERROR.get());
        }

        final List<File> files = Lists.newArrayList();
        for (final String target: arguments)
            files.add(new File(target).getCanonicalFile());

        if (optionSet.has("brief"))
            reporter = Reporters.BRIEF;
        else if (optionSet.has("quiet")) {
            System.out.close();
            System.err.close();
            reporter = Reporters.QUIET;
        }

        new Main(fakeRoot).proceed(reporter, files, isSyntax);
    }

    Main(final String fakeRoot)
        throws IOException
    {
        final URITranslatorConfigurationBuilder builder
            = URITranslatorConfiguration.newBuilder()
                .setNamespace(getCwd());
        if (fakeRoot != null)
            builder.addPathRedirect(fakeRoot, getCwd());
        final LoadingConfiguration cfg = LoadingConfiguration.newBuilder()
            .setURITranslatorConfiguration(builder.freeze()).freeze();
        factory = JsonSchemaFactory.newBuilder()
            .setLoadingConfiguration(cfg).freeze();
        syntaxValidator = factory.getSyntaxValidator();
    }

    private void proceed(Reporter reporter, List<File> files, boolean isSyntax)
        throws IOException, ProcessingException
    {
        RetCode retCode = isSyntax ? doSyntax(reporter, files) : doValidation(reporter, files);
        System.exit(retCode.get());
    }

    private RetCode doSyntax(Reporter reporter, List<File> files)
        throws IOException
    {
        RetCode retcode, ret = ALL_OK;
        String fileName;
        JsonNode node;

        for (final File file: files) {
            fileName = file.toString();
            node = MAPPER.readTree(file);
            retcode = reporter.validateSchema(syntaxValidator, fileName, node);
            if (retcode != ALL_OK)
                ret = retcode;
        }

        return ret;
    }

    private RetCode doValidation(Reporter reporter, List<File> files)
        throws IOException, ProcessingException
    {
        File schemaFile = files.remove(0);
        String uri = schemaFile.toURI().normalize().toString();
        JsonNode node;

        node = MAPPER.readTree(schemaFile);
        if (!syntaxValidator.schemaIsValid(node)) {
            System.err.println("Schema is invalid! Aborting...");
            return SCHEMA_SYNTAX_ERROR;
        }

        JsonSchema schema = factory.getJsonSchema(uri);

        RetCode ret = ALL_OK, retcode;

        for (final File file: files) {
            node = MAPPER.readTree(file);
            retcode = reporter.validateInstance(schema, file.toString(), node);
            if (retcode != ALL_OK)
                ret = retcode;
        }

        return ret;
    }

    private static String getCwd()
        throws IOException
    {
        File cwd = new File(System.getProperty("user.dir", ".")).getCanonicalFile();
        return URIUtils.normalizeURI(cwd.toURI()).toString();
    }
}
