package net.czpilar.gdrive.cmd.context;

import net.czpilar.gdrive.cmd.credential.PropertiesGDriveCredential;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.help.HelpFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static net.czpilar.gdrive.cmd.runner.impl.GDriveCmdRunner.*;
import static org.junit.jupiter.api.Assertions.*;

class GDriveCmdContextTest {

    private GDriveCmdContext context;

    @BeforeEach
    void before() {
        context = new GDriveCmdContext();
    }

    @Test
    void testConstants() {
        assertEquals("gdrive.uploadDir", GDriveCmdContext.UPLOAD_DIR_PROPERTY_KEY);
        assertEquals("gdrive.refreshToken", GDriveCmdContext.REFRESH_TOKEN_PROPERTY_KEY);
        assertEquals("gdrive-uploads", GDriveCmdContext.DEFAULT_UPLOAD_DIR);
    }

    @Test
    void testPropertiesGDriveCredential() {
        PropertiesGDriveCredential credential = context.propertiesGDriveCredential();

        assertNotNull(credential);
    }

    @Test
    void testPropertiesGDriveCredentialDefaultUploadDir(@TempDir Path tempDir) throws IOException {
        PropertiesGDriveCredential credential = context.propertiesGDriveCredential();
        File propertyFile = tempDir.resolve("test.properties").toFile();
        Files.writeString(propertyFile.toPath(), "");
        credential.setPropertyFile(propertyFile.getPath());

        assertEquals(GDriveCmdContext.DEFAULT_UPLOAD_DIR, credential.getUploadDir());
    }

    @Test
    void testHelpFormatter() {
        HelpFormatter formatter = context.helpFormatter();

        assertNotNull(formatter);
    }

    @Test
    void testDefaultParser() {
        DefaultParser parser = context.defaultParser();

        assertNotNull(parser);
    }

    @Test
    void testOptions() {
        Options options = context.options();

        assertNotNull(options);
    }

    @Test
    void testOptionsContainsAllExpectedOptions() {
        Options options = context.options();

        assertNotNull(options.getOption(OPTION_VERSION));
        assertNotNull(options.getOption(OPTION_HELP));
        assertNotNull(options.getOption(OPTION_LINK));
        assertNotNull(options.getOption(OPTION_AUTHORIZATION));
        assertNotNull(options.getOption(OPTION_FILE));
        assertNotNull(options.getOption(OPTION_DIRECTORY));
        assertNotNull(options.getOption(OPTION_PROPERTIES));
        assertNotNull(options.getOption(OPTION_TRASH));
    }

    @Test
    void testOptionsCount() {
        Options options = context.options();

        assertEquals(8, options.getOptions().size());
    }

    @Test
    void testVersionOption() {
        Option option = context.options().getOption(OPTION_VERSION);

        assertFalse(option.hasArg());
        assertEquals("show gDrive version", option.getDescription());
    }

    @Test
    void testHelpOption() {
        Option option = context.options().getOption(OPTION_HELP);

        assertFalse(option.hasArg());
        assertEquals("show this help", option.getDescription());
    }

    @Test
    void testLinkOption() {
        Option option = context.options().getOption(OPTION_LINK);

        assertFalse(option.hasArg());
        assertEquals("display authorization link", option.getDescription());
    }

    @Test
    void testAuthorizationOption() {
        Option option = context.options().getOption(OPTION_AUTHORIZATION);

        assertTrue(option.hasArg());
        assertTrue(option.hasOptionalArg());
        assertEquals("[code]", option.getArgName());
        assertEquals("process authorization; waits for code if not provided", option.getDescription());
    }

    @Test
    void testFileOption() {
        Option option = context.options().getOption(OPTION_FILE);

        assertTrue(option.hasArg());
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
        assertEquals("<file>", option.getArgName());
        assertEquals("upload file(s)", option.getDescription());
    }

    @Test
    void testDirectoryOption() {
        Option option = context.options().getOption(OPTION_DIRECTORY);

        assertTrue(option.hasArg());
        assertEquals("<dir>", option.getArgName());
        assertEquals("directory for upload; creates new one if no directory exists; default is gdrive-uploads", option.getDescription());
    }

    @Test
    void testPropertiesOption() {
        Option option = context.options().getOption(OPTION_PROPERTIES);

        assertTrue(option.hasArg());
        assertEquals("<props>", option.getArgName());
        assertEquals("path to gDrive properties file", option.getDescription());
    }

    @Test
    void testTrashOption() {
        Option option = context.options().getOption(OPTION_TRASH);

        assertFalse(option.hasArg());
        assertEquals("empty trash", option.getDescription());
    }
}
