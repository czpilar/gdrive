package net.czpilar.gdrive.cmd.runner.impl;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.drive.model.File;
import net.czpilar.gdrive.cmd.credential.PropertiesGDriveCredential;
import net.czpilar.gdrive.core.service.IAuthorizationService;
import net.czpilar.gdrive.core.service.IFileService;
import net.czpilar.gdrive.core.service.ITrashService;
import net.czpilar.gdrive.core.setting.GDriveSetting;
import org.apache.commons.cli.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

/**
 * @author David Pilar (david@czpilar.net)
 */
public class GDriveCmdRunnerTest {

    private final GDriveCmdRunner runner = new GDriveCmdRunner();
    @Mock
    private CommandLineParser commandLineParser;
    @Mock
    private Options options;
    @Mock
    private HelpFormatter helpFormatter;
    @Mock
    private IAuthorizationService authorizationService;
    @Mock
    private IFileService fileService;
    @Mock
    private ITrashService trashService;
    @Mock
    private GDriveSetting gDriveSetting;
    @Mock
    private PropertiesGDriveCredential propertiesGDriveCredential;
    @Mock
    private CommandLine commandLine;
    @Mock
    private AuthorizationCodeWaiter codeWaiter;

    private AutoCloseable autoCloseable;

    @BeforeEach
    public void before() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        runner.setCommandLineParser(commandLineParser);
        runner.setOptions(options);
        runner.setHelpFormatter(helpFormatter);
        runner.setAuthorizationService(authorizationService);
        runner.setFileService(fileService);
        runner.setTrashService(trashService);
        runner.setGDriveSetting(gDriveSetting);
        runner.setPropertiesGDriveCredential(propertiesGDriveCredential);
        runner.setCodeWaiter(codeWaiter);
    }

    @AfterEach
    public void after() throws Exception {
        autoCloseable.close();
    }

    @Test
    public void testRunWhereCommandLineParsingFails() throws ParseException {
        String appName = "application-name";
        String[] args = {"arg1", "arg2"};
        when(commandLineParser.parse(any(Options.class), any(String[].class))).thenThrow(ParseException.class);
        when(gDriveSetting.getApplicationName()).thenReturn(appName);

        runner.run(args);

        verify(commandLineParser).parse(options, args);
        verify(helpFormatter).printHelp(appName, options, true);
        verify(gDriveSetting).getApplicationName();

        verifyNoMoreInteractions(commandLineParser);
        verifyNoMoreInteractions(helpFormatter);
        verifyNoMoreInteractions(gDriveSetting);

        verifyNoInteractions(options);
        verifyNoInteractions(authorizationService);
        verifyNoInteractions(fileService);
        verifyNoInteractions(propertiesGDriveCredential);
        verifyNoInteractions(commandLine);
        verifyNoInteractions(trashService);
        verifyNoInteractions(codeWaiter);
    }

    @Test
    public void testRunWhereCommandLineHasEmptyOptions() throws ParseException {
        String appName = "application-name";
        String[] args = {"arg1", "arg2"};
        Option[] optionList = {};
        when(commandLineParser.parse(any(Options.class), any(String[].class))).thenReturn(commandLine);
        when(commandLine.getOptions()).thenReturn(optionList);
        when(gDriveSetting.getApplicationName()).thenReturn(appName);

        runner.run(args);

        verify(commandLineParser).parse(options, args);
        verify(helpFormatter).printHelp(appName, options, true);
        verify(commandLine).getOptions();
        verify(gDriveSetting).getApplicationName();

        verifyNoMoreInteractions(commandLineParser);
        verifyNoMoreInteractions(helpFormatter);
        verifyNoMoreInteractions(gDriveSetting);
        verifyNoMoreInteractions(commandLine);

        verifyNoInteractions(options);
        verifyNoInteractions(authorizationService);
        verifyNoInteractions(fileService);
        verifyNoInteractions(propertiesGDriveCredential);
        verifyNoInteractions(trashService);
        verifyNoInteractions(codeWaiter);
    }

    @Test
    public void testRunWhereCommandLineHasOnlyPropertiesOption() throws ParseException {
        String appName = "application-name";
        String[] args = {"arg1", "arg2"};
        Option[] optionList = {mock(Option.class)};
        when(commandLineParser.parse(any(Options.class), any(String[].class))).thenReturn(commandLine);
        when(commandLine.getOptions()).thenReturn(optionList);
        when(commandLine.hasOption(GDriveCmdRunner.OPTION_PROPERTIES)).thenReturn(true);
        when(gDriveSetting.getApplicationName()).thenReturn(appName);

        runner.run(args);

        verify(commandLineParser).parse(options, args);
        verify(helpFormatter).printHelp(appName, options, true);
        verify(commandLine).getOptions();
        verify(commandLine).hasOption(GDriveCmdRunner.OPTION_PROPERTIES);
        verify(gDriveSetting).getApplicationName();

        verifyNoMoreInteractions(commandLineParser);
        verifyNoMoreInteractions(helpFormatter);
        verifyNoMoreInteractions(gDriveSetting);
        verifyNoMoreInteractions(commandLine);

        verifyNoInteractions(options);
        verifyNoInteractions(authorizationService);
        verifyNoInteractions(fileService);
        verifyNoInteractions(propertiesGDriveCredential);
        verifyNoInteractions(trashService);
        verifyNoInteractions(codeWaiter);
    }

    @Test
    public void testRunWhereCommandLineHasPropertiesAndVersionOptions() throws ParseException {
        String appVersion = "application-version";
        String propertiesValue = "test-properties-value";
        String[] args = {"arg1", "arg2"};
        Option[] optionList = {mock(Option.class), mock(Option.class)};
        when(commandLineParser.parse(any(Options.class), any(String[].class))).thenReturn(commandLine);
        when(commandLine.getOptions()).thenReturn(optionList);
        when(commandLine.hasOption(GDriveCmdRunner.OPTION_PROPERTIES)).thenReturn(true);
        when(commandLine.hasOption(GDriveCmdRunner.OPTION_VERSION)).thenReturn(true);
        when(commandLine.hasOption(GDriveCmdRunner.OPTION_HELP)).thenReturn(false);
        when(commandLine.hasOption(GDriveCmdRunner.OPTION_LINK)).thenReturn(false);
        when(commandLine.hasOption(GDriveCmdRunner.OPTION_AUTHORIZATION)).thenReturn(false);
        when(commandLine.hasOption(GDriveCmdRunner.OPTION_FILE)).thenReturn(false);
        when(commandLine.hasOption(GDriveCmdRunner.OPTION_TRASH)).thenReturn(false);
        when(commandLine.getOptionValue(GDriveCmdRunner.OPTION_PROPERTIES)).thenReturn(propertiesValue);
        when(gDriveSetting.getApplicationVersion()).thenReturn(appVersion);

        runner.run(args);

        verify(commandLineParser).parse(options, args);
        verify(commandLine).getOptions();
        verify(commandLine).hasOption(GDriveCmdRunner.OPTION_PROPERTIES);
        verify(commandLine).hasOption(GDriveCmdRunner.OPTION_VERSION);
        verify(commandLine).hasOption(GDriveCmdRunner.OPTION_HELP);
        verify(commandLine).hasOption(GDriveCmdRunner.OPTION_LINK);
        verify(commandLine).hasOption(GDriveCmdRunner.OPTION_AUTHORIZATION);
        verify(commandLine).hasOption(GDriveCmdRunner.OPTION_FILE);
        verify(commandLine).hasOption(GDriveCmdRunner.OPTION_TRASH);
        verify(commandLine).getOptionValue(GDriveCmdRunner.OPTION_PROPERTIES);
        verify(gDriveSetting).getApplicationVersion();
        verify(propertiesGDriveCredential).setPropertyFile(propertiesValue);

        verifyNoMoreInteractions(commandLineParser);
        verifyNoMoreInteractions(helpFormatter);
        verifyNoMoreInteractions(gDriveSetting);
        verifyNoMoreInteractions(commandLine);
        verifyNoMoreInteractions(propertiesGDriveCredential);

        verifyNoInteractions(options);
        verifyNoInteractions(authorizationService);
        verifyNoInteractions(fileService);
        verifyNoInteractions(trashService);
        verifyNoInteractions(codeWaiter);
    }

    @Test
    public void testRunWhereCommandLineHasPropertiesAndHelpOptions() throws ParseException {
        String appName = "application-name";
        String propertiesValue = "test-properties-value";
        String[] args = {"arg1", "arg2"};
        Option[] optionList = {mock(Option.class), mock(Option.class)};
        when(commandLineParser.parse(any(Options.class), any(String[].class))).thenReturn(commandLine);
        when(commandLine.getOptions()).thenReturn(optionList);
        when(commandLine.hasOption(GDriveCmdRunner.OPTION_PROPERTIES)).thenReturn(true);
        when(commandLine.hasOption(GDriveCmdRunner.OPTION_VERSION)).thenReturn(false);
        when(commandLine.hasOption(GDriveCmdRunner.OPTION_HELP)).thenReturn(true);
        when(commandLine.hasOption(GDriveCmdRunner.OPTION_LINK)).thenReturn(false);
        when(commandLine.hasOption(GDriveCmdRunner.OPTION_AUTHORIZATION)).thenReturn(false);
        when(commandLine.hasOption(GDriveCmdRunner.OPTION_FILE)).thenReturn(false);
        when(commandLine.hasOption(GDriveCmdRunner.OPTION_TRASH)).thenReturn(false);
        when(commandLine.getOptionValue(GDriveCmdRunner.OPTION_PROPERTIES)).thenReturn(propertiesValue);
        when(gDriveSetting.getApplicationName()).thenReturn(appName);

        runner.run(args);

        verify(commandLineParser).parse(options, args);
        verify(helpFormatter).printHelp(appName, options, true);
        verify(commandLine).getOptions();
        verify(commandLine).hasOption(GDriveCmdRunner.OPTION_PROPERTIES);
        verify(commandLine).hasOption(GDriveCmdRunner.OPTION_VERSION);
        verify(commandLine).hasOption(GDriveCmdRunner.OPTION_HELP);
        verify(commandLine).hasOption(GDriveCmdRunner.OPTION_LINK);
        verify(commandLine).hasOption(GDriveCmdRunner.OPTION_AUTHORIZATION);
        verify(commandLine).hasOption(GDriveCmdRunner.OPTION_FILE);
        verify(commandLine).hasOption(GDriveCmdRunner.OPTION_TRASH);
        verify(commandLine).getOptionValue(GDriveCmdRunner.OPTION_PROPERTIES);
        verify(gDriveSetting).getApplicationName();
        verify(propertiesGDriveCredential).setPropertyFile(propertiesValue);

        verifyNoMoreInteractions(commandLineParser);
        verifyNoMoreInteractions(helpFormatter);
        verifyNoMoreInteractions(gDriveSetting);
        verifyNoMoreInteractions(commandLine);
        verifyNoMoreInteractions(propertiesGDriveCredential);

        verifyNoInteractions(options);
        verifyNoInteractions(authorizationService);
        verifyNoInteractions(fileService);
        verifyNoInteractions(trashService);
        verifyNoInteractions(codeWaiter);
    }

    @Test
    public void testRunWhereCommandLineHasPropertiesAndLinkOptions() throws ParseException {
        String propertiesValue = "test-properties-value";
        String[] args = {"arg1", "arg2"};
        Option[] optionList = {mock(Option.class), mock(Option.class)};
        when(commandLineParser.parse(any(Options.class), any(String[].class))).thenReturn(commandLine);
        when(commandLine.getOptions()).thenReturn(optionList);
        when(commandLine.hasOption(GDriveCmdRunner.OPTION_PROPERTIES)).thenReturn(true);
        when(commandLine.hasOption(GDriveCmdRunner.OPTION_VERSION)).thenReturn(false);
        when(commandLine.hasOption(GDriveCmdRunner.OPTION_HELP)).thenReturn(false);
        when(commandLine.hasOption(GDriveCmdRunner.OPTION_LINK)).thenReturn(true);
        when(commandLine.hasOption(GDriveCmdRunner.OPTION_AUTHORIZATION)).thenReturn(false);
        when(commandLine.hasOption(GDriveCmdRunner.OPTION_FILE)).thenReturn(false);
        when(commandLine.hasOption(GDriveCmdRunner.OPTION_TRASH)).thenReturn(false);
        when(commandLine.getOptionValue(GDriveCmdRunner.OPTION_PROPERTIES)).thenReturn(propertiesValue);
        when(authorizationService.getAuthorizationURL()).thenReturn("test-authorization-url");

        runner.run(args);

        verify(commandLineParser).parse(options, args);
        verify(commandLine).getOptions();
        verify(commandLine).hasOption(GDriveCmdRunner.OPTION_PROPERTIES);
        verify(commandLine).hasOption(GDriveCmdRunner.OPTION_VERSION);
        verify(commandLine).hasOption(GDriveCmdRunner.OPTION_HELP);
        verify(commandLine).hasOption(GDriveCmdRunner.OPTION_LINK);
        verify(commandLine).hasOption(GDriveCmdRunner.OPTION_AUTHORIZATION);
        verify(commandLine).hasOption(GDriveCmdRunner.OPTION_FILE);
        verify(commandLine).hasOption(GDriveCmdRunner.OPTION_TRASH);
        verify(commandLine).getOptionValue(GDriveCmdRunner.OPTION_PROPERTIES);
        verify(propertiesGDriveCredential).setPropertyFile(propertiesValue);
        verify(authorizationService).getAuthorizationURL();

        verifyNoMoreInteractions(commandLineParser);
        verifyNoMoreInteractions(helpFormatter);
        verifyNoMoreInteractions(gDriveSetting);
        verifyNoMoreInteractions(commandLine);
        verifyNoMoreInteractions(propertiesGDriveCredential);
        verifyNoMoreInteractions(authorizationService);

        verifyNoInteractions(options);
        verifyNoInteractions(fileService);
        verifyNoInteractions(trashService);
        verifyNoInteractions(codeWaiter);
    }

    @Test
    public void testRunWhereCommandLineHasPropertiesAndAuthorizationOptionsAndReturnNullCredential() throws ParseException {
        String propertiesValue = "test-properties-value";
        String authorizationValue = "test-authorization-value";
        String[] args = {"arg1", "arg2"};
        Option[] optionList = {mock(Option.class), mock(Option.class)};
        when(commandLineParser.parse(any(Options.class), any(String[].class))).thenReturn(commandLine);
        when(commandLine.getOptions()).thenReturn(optionList);
        when(commandLine.hasOption(GDriveCmdRunner.OPTION_PROPERTIES)).thenReturn(true);
        when(commandLine.hasOption(GDriveCmdRunner.OPTION_VERSION)).thenReturn(false);
        when(commandLine.hasOption(GDriveCmdRunner.OPTION_HELP)).thenReturn(false);
        when(commandLine.hasOption(GDriveCmdRunner.OPTION_LINK)).thenReturn(false);
        when(commandLine.hasOption(GDriveCmdRunner.OPTION_AUTHORIZATION)).thenReturn(true);
        when(commandLine.hasOption(GDriveCmdRunner.OPTION_FILE)).thenReturn(false);
        when(commandLine.hasOption(GDriveCmdRunner.OPTION_TRASH)).thenReturn(false);
        when(commandLine.getOptionValue(GDriveCmdRunner.OPTION_PROPERTIES)).thenReturn(propertiesValue);
        when(commandLine.getOptionValue(GDriveCmdRunner.OPTION_AUTHORIZATION)).thenReturn(authorizationValue);
        when(authorizationService.authorize(authorizationValue)).thenReturn(null);

        runner.run(args);

        verify(commandLineParser).parse(options, args);
        verify(commandLine).getOptions();
        verify(commandLine).hasOption(GDriveCmdRunner.OPTION_PROPERTIES);
        verify(commandLine).hasOption(GDriveCmdRunner.OPTION_VERSION);
        verify(commandLine).hasOption(GDriveCmdRunner.OPTION_HELP);
        verify(commandLine).hasOption(GDriveCmdRunner.OPTION_LINK);
        verify(commandLine).hasOption(GDriveCmdRunner.OPTION_AUTHORIZATION);
        verify(commandLine).hasOption(GDriveCmdRunner.OPTION_FILE);
        verify(commandLine).hasOption(GDriveCmdRunner.OPTION_TRASH);
        verify(commandLine).getOptionValue(GDriveCmdRunner.OPTION_PROPERTIES);
        verify(commandLine).getOptionValue(GDriveCmdRunner.OPTION_AUTHORIZATION);
        verify(propertiesGDriveCredential).setPropertyFile(propertiesValue);
        verify(authorizationService).authorize(authorizationValue);

        verifyNoMoreInteractions(commandLineParser);
        verifyNoMoreInteractions(helpFormatter);
        verifyNoMoreInteractions(gDriveSetting);
        verifyNoMoreInteractions(commandLine);
        verifyNoMoreInteractions(propertiesGDriveCredential);
        verifyNoMoreInteractions(authorizationService);

        verifyNoInteractions(options);
        verifyNoInteractions(fileService);
        verifyNoInteractions(trashService);
        verifyNoInteractions(codeWaiter);
    }

    @Test
    public void testRunWhereCommandLineHasPropertiesAndAuthorizationOptionsAndReturnCredential() throws ParseException {
        String propertiesValue = "test-properties-value";
        String authorizationValue = "test-authorization-value";
        String[] args = {"arg1", "arg2"};
        Option[] optionList = {mock(Option.class), mock(Option.class)};
        when(commandLineParser.parse(any(Options.class), any(String[].class))).thenReturn(commandLine);
        when(commandLine.getOptions()).thenReturn(optionList);
        when(commandLine.hasOption(GDriveCmdRunner.OPTION_PROPERTIES)).thenReturn(true);
        when(commandLine.hasOption(GDriveCmdRunner.OPTION_VERSION)).thenReturn(false);
        when(commandLine.hasOption(GDriveCmdRunner.OPTION_HELP)).thenReturn(false);
        when(commandLine.hasOption(GDriveCmdRunner.OPTION_LINK)).thenReturn(false);
        when(commandLine.hasOption(GDriveCmdRunner.OPTION_AUTHORIZATION)).thenReturn(true);
        when(commandLine.hasOption(GDriveCmdRunner.OPTION_FILE)).thenReturn(false);
        when(commandLine.hasOption(GDriveCmdRunner.OPTION_TRASH)).thenReturn(false);
        when(commandLine.getOptionValue(GDriveCmdRunner.OPTION_PROPERTIES)).thenReturn(propertiesValue);
        when(commandLine.getOptionValue(GDriveCmdRunner.OPTION_AUTHORIZATION)).thenReturn(authorizationValue);
        when(authorizationService.authorize(authorizationValue)).thenReturn(mock(Credential.class));

        runner.run(args);

        verify(commandLineParser).parse(options, args);
        verify(commandLine).getOptions();
        verify(commandLine).hasOption(GDriveCmdRunner.OPTION_PROPERTIES);
        verify(commandLine).hasOption(GDriveCmdRunner.OPTION_VERSION);
        verify(commandLine).hasOption(GDriveCmdRunner.OPTION_HELP);
        verify(commandLine).hasOption(GDriveCmdRunner.OPTION_LINK);
        verify(commandLine).hasOption(GDriveCmdRunner.OPTION_AUTHORIZATION);
        verify(commandLine).hasOption(GDriveCmdRunner.OPTION_FILE);
        verify(commandLine).hasOption(GDriveCmdRunner.OPTION_TRASH);
        verify(commandLine).getOptionValue(GDriveCmdRunner.OPTION_PROPERTIES);
        verify(commandLine).getOptionValue(GDriveCmdRunner.OPTION_AUTHORIZATION);
        verify(propertiesGDriveCredential).setPropertyFile(propertiesValue);
        verify(authorizationService).authorize(authorizationValue);

        verifyNoMoreInteractions(commandLineParser);
        verifyNoMoreInteractions(helpFormatter);
        verifyNoMoreInteractions(gDriveSetting);
        verifyNoMoreInteractions(commandLine);
        verifyNoMoreInteractions(propertiesGDriveCredential);
        verifyNoMoreInteractions(authorizationService);

        verifyNoInteractions(options);
        verifyNoInteractions(fileService);
        verifyNoInteractions(trashService);
        verifyNoInteractions(codeWaiter);
    }

    @Test
    public void testRunWhereCommandLineHasPropertiesAndAuthorizationOptionsNoValueAndReturnNullCredential() throws ParseException {
        String propertiesValue = "test-properties-value";
        String authorizationValue = "test-authorization-value";
        String[] args = {"arg1", "arg2"};
        Option[] optionList = {mock(Option.class), mock(Option.class)};
        when(commandLineParser.parse(any(Options.class), any(String[].class))).thenReturn(commandLine);
        when(commandLine.getOptions()).thenReturn(optionList);
        when(commandLine.hasOption(GDriveCmdRunner.OPTION_PROPERTIES)).thenReturn(true);
        when(commandLine.hasOption(GDriveCmdRunner.OPTION_VERSION)).thenReturn(false);
        when(commandLine.hasOption(GDriveCmdRunner.OPTION_HELP)).thenReturn(false);
        when(commandLine.hasOption(GDriveCmdRunner.OPTION_LINK)).thenReturn(false);
        when(commandLine.hasOption(GDriveCmdRunner.OPTION_AUTHORIZATION)).thenReturn(true);
        when(commandLine.hasOption(GDriveCmdRunner.OPTION_FILE)).thenReturn(false);
        when(commandLine.hasOption(GDriveCmdRunner.OPTION_TRASH)).thenReturn(false);
        when(commandLine.getOptionValue(GDriveCmdRunner.OPTION_PROPERTIES)).thenReturn(propertiesValue);
        when(commandLine.getOptionValue(GDriveCmdRunner.OPTION_AUTHORIZATION)).thenReturn(null);
        when(authorizationService.authorize(authorizationValue)).thenReturn(null);
        when(codeWaiter.getCode()).thenReturn(Optional.of(authorizationValue));

        runner.run(args);

        verify(commandLineParser).parse(options, args);
        verify(commandLine).getOptions();
        verify(commandLine).hasOption(GDriveCmdRunner.OPTION_PROPERTIES);
        verify(commandLine).hasOption(GDriveCmdRunner.OPTION_VERSION);
        verify(commandLine).hasOption(GDriveCmdRunner.OPTION_HELP);
        verify(commandLine).hasOption(GDriveCmdRunner.OPTION_LINK);
        verify(commandLine).hasOption(GDriveCmdRunner.OPTION_AUTHORIZATION);
        verify(commandLine).hasOption(GDriveCmdRunner.OPTION_FILE);
        verify(commandLine).hasOption(GDriveCmdRunner.OPTION_TRASH);
        verify(commandLine).getOptionValue(GDriveCmdRunner.OPTION_PROPERTIES);
        verify(commandLine).getOptionValue(GDriveCmdRunner.OPTION_AUTHORIZATION);
        verify(propertiesGDriveCredential).setPropertyFile(propertiesValue);
        verify(authorizationService).authorize(authorizationValue);
        verify(codeWaiter).getCode();

        verifyNoMoreInteractions(commandLineParser);
        verifyNoMoreInteractions(helpFormatter);
        verifyNoMoreInteractions(gDriveSetting);
        verifyNoMoreInteractions(commandLine);
        verifyNoMoreInteractions(propertiesGDriveCredential);
        verifyNoMoreInteractions(codeWaiter);
        verifyNoMoreInteractions(authorizationService);

        verifyNoInteractions(options);
        verifyNoInteractions(fileService);
        verifyNoInteractions(trashService);
    }

    @Test
    public void testRunWhereCommandLineHasPropertiesAndAuthorizationOptionsNoValueAndReturnCredential() throws ParseException {
        String propertiesValue = "test-properties-value";
        String authorizationValue = "test-authorization-value";
        String[] args = {"arg1", "arg2"};
        Option[] optionList = {mock(Option.class), mock(Option.class)};
        when(commandLineParser.parse(any(Options.class), any(String[].class))).thenReturn(commandLine);
        when(commandLine.getOptions()).thenReturn(optionList);
        when(commandLine.hasOption(GDriveCmdRunner.OPTION_PROPERTIES)).thenReturn(true);
        when(commandLine.hasOption(GDriveCmdRunner.OPTION_VERSION)).thenReturn(false);
        when(commandLine.hasOption(GDriveCmdRunner.OPTION_HELP)).thenReturn(false);
        when(commandLine.hasOption(GDriveCmdRunner.OPTION_LINK)).thenReturn(false);
        when(commandLine.hasOption(GDriveCmdRunner.OPTION_AUTHORIZATION)).thenReturn(true);
        when(commandLine.hasOption(GDriveCmdRunner.OPTION_FILE)).thenReturn(false);
        when(commandLine.hasOption(GDriveCmdRunner.OPTION_TRASH)).thenReturn(false);
        when(commandLine.getOptionValue(GDriveCmdRunner.OPTION_PROPERTIES)).thenReturn(propertiesValue);
        when(commandLine.getOptionValue(GDriveCmdRunner.OPTION_AUTHORIZATION)).thenReturn(null);
        when(authorizationService.authorize(authorizationValue)).thenReturn(mock(Credential.class));
        when(codeWaiter.getCode()).thenReturn(Optional.of(authorizationValue));

        runner.run(args);

        verify(commandLineParser).parse(options, args);
        verify(commandLine).getOptions();
        verify(commandLine).hasOption(GDriveCmdRunner.OPTION_PROPERTIES);
        verify(commandLine).hasOption(GDriveCmdRunner.OPTION_VERSION);
        verify(commandLine).hasOption(GDriveCmdRunner.OPTION_HELP);
        verify(commandLine).hasOption(GDriveCmdRunner.OPTION_LINK);
        verify(commandLine).hasOption(GDriveCmdRunner.OPTION_AUTHORIZATION);
        verify(commandLine).hasOption(GDriveCmdRunner.OPTION_FILE);
        verify(commandLine).hasOption(GDriveCmdRunner.OPTION_TRASH);
        verify(commandLine).getOptionValue(GDriveCmdRunner.OPTION_PROPERTIES);
        verify(commandLine).getOptionValue(GDriveCmdRunner.OPTION_AUTHORIZATION);
        verify(propertiesGDriveCredential).setPropertyFile(propertiesValue);
        verify(authorizationService).authorize(authorizationValue);
        verify(codeWaiter).getCode();

        verifyNoMoreInteractions(commandLineParser);
        verifyNoMoreInteractions(helpFormatter);
        verifyNoMoreInteractions(gDriveSetting);
        verifyNoMoreInteractions(commandLine);
        verifyNoMoreInteractions(propertiesGDriveCredential);
        verifyNoMoreInteractions(codeWaiter);
        verifyNoMoreInteractions(authorizationService);

        verifyNoInteractions(options);
        verifyNoInteractions(fileService);
        verifyNoInteractions(trashService);
    }

    @Test
    public void testRunWhereCommandLineHasPropertiesAndFileOptionsAndNoDirectory() throws ParseException {
        String propertiesValue = "test-properties-value";
        String optionFile = "test-file-value";
        List<String> optionFiles = List.of(optionFile);
        String[] args = {"arg1", "arg2"};
        Option[] optionList = {mock(Option.class), mock(Option.class)};
        when(commandLineParser.parse(any(Options.class), any(String[].class))).thenReturn(commandLine);
        when(commandLine.getOptions()).thenReturn(optionList);
        when(commandLine.hasOption(GDriveCmdRunner.OPTION_PROPERTIES)).thenReturn(true);
        when(commandLine.hasOption(GDriveCmdRunner.OPTION_VERSION)).thenReturn(false);
        when(commandLine.hasOption(GDriveCmdRunner.OPTION_HELP)).thenReturn(false);
        when(commandLine.hasOption(GDriveCmdRunner.OPTION_LINK)).thenReturn(false);
        when(commandLine.hasOption(GDriveCmdRunner.OPTION_AUTHORIZATION)).thenReturn(false);
        when(commandLine.hasOption(GDriveCmdRunner.OPTION_FILE)).thenReturn(true);
        when(commandLine.hasOption(GDriveCmdRunner.OPTION_DIRECTORY)).thenReturn(false);
        when(commandLine.hasOption(GDriveCmdRunner.OPTION_TRASH)).thenReturn(false);
        when(commandLine.getOptionValue(GDriveCmdRunner.OPTION_PROPERTIES)).thenReturn(propertiesValue);
        when(commandLine.getOptionValues(GDriveCmdRunner.OPTION_FILE)).thenReturn(new String[]{optionFile});

        runner.run(args);

        verify(commandLineParser).parse(options, args);
        verify(commandLine).getOptions();
        verify(commandLine).hasOption(GDriveCmdRunner.OPTION_PROPERTIES);
        verify(commandLine).hasOption(GDriveCmdRunner.OPTION_VERSION);
        verify(commandLine).hasOption(GDriveCmdRunner.OPTION_HELP);
        verify(commandLine).hasOption(GDriveCmdRunner.OPTION_LINK);
        verify(commandLine).hasOption(GDriveCmdRunner.OPTION_AUTHORIZATION);
        verify(commandLine).hasOption(GDriveCmdRunner.OPTION_FILE);
        verify(commandLine).hasOption(GDriveCmdRunner.OPTION_DIRECTORY);
        verify(commandLine).hasOption(GDriveCmdRunner.OPTION_TRASH);
        verify(commandLine).getOptionValue(GDriveCmdRunner.OPTION_PROPERTIES);
        verify(commandLine).getOptionValues(GDriveCmdRunner.OPTION_FILE);
        verify(propertiesGDriveCredential).setPropertyFile(propertiesValue);
        verify(fileService).uploadFiles(optionFiles, (String) null);

        verifyNoMoreInteractions(commandLineParser);
        verifyNoMoreInteractions(helpFormatter);
        verifyNoMoreInteractions(gDriveSetting);
        verifyNoMoreInteractions(commandLine);
        verifyNoMoreInteractions(propertiesGDriveCredential);
        verifyNoMoreInteractions(fileService);

        verifyNoInteractions(options);
        verifyNoInteractions(authorizationService);
        verifyNoInteractions(authorizationService);
        verifyNoInteractions(trashService);
        verifyNoInteractions(codeWaiter);
    }

    @Test
    public void testRunWhereCommandLineHasPropertiesAndFileOptionsAndDirectory() throws ParseException {
        String propertiesValue = "test-properties-value";
        String optionFile = "test-file-value";
        String optionDirectory = "test-directory";
        List<String> optionFiles = List.of(optionFile);
        String[] args = {"arg1", "arg2"};
        Option[] optionList = {mock(Option.class), mock(Option.class)};
        File file1 = mock(File.class);
        File file2 = mock(File.class);
        List<File> files = Arrays.asList(file1, file2);
        when(commandLineParser.parse(any(Options.class), any(String[].class))).thenReturn(commandLine);
        when(commandLine.getOptions()).thenReturn(optionList);
        when(commandLine.hasOption(GDriveCmdRunner.OPTION_PROPERTIES)).thenReturn(true);
        when(commandLine.hasOption(GDriveCmdRunner.OPTION_VERSION)).thenReturn(false);
        when(commandLine.hasOption(GDriveCmdRunner.OPTION_HELP)).thenReturn(false);
        when(commandLine.hasOption(GDriveCmdRunner.OPTION_LINK)).thenReturn(false);
        when(commandLine.hasOption(GDriveCmdRunner.OPTION_AUTHORIZATION)).thenReturn(false);
        when(commandLine.hasOption(GDriveCmdRunner.OPTION_FILE)).thenReturn(true);
        when(commandLine.hasOption(GDriveCmdRunner.OPTION_DIRECTORY)).thenReturn(true);
        when(commandLine.hasOption(GDriveCmdRunner.OPTION_TRASH)).thenReturn(false);
        when(commandLine.getOptionValue(GDriveCmdRunner.OPTION_PROPERTIES)).thenReturn(propertiesValue);
        when(commandLine.getOptionValues(GDriveCmdRunner.OPTION_FILE)).thenReturn(new String[]{optionFile});
        when(commandLine.getOptionValue(GDriveCmdRunner.OPTION_DIRECTORY)).thenReturn(optionDirectory);
        when(fileService.uploadFiles(anyList(), anyString())).thenReturn(files);

        runner.run(args);

        verify(commandLineParser).parse(options, args);
        verify(commandLine).getOptions();
        verify(commandLine).hasOption(GDriveCmdRunner.OPTION_PROPERTIES);
        verify(commandLine).hasOption(GDriveCmdRunner.OPTION_VERSION);
        verify(commandLine).hasOption(GDriveCmdRunner.OPTION_HELP);
        verify(commandLine).hasOption(GDriveCmdRunner.OPTION_LINK);
        verify(commandLine).hasOption(GDriveCmdRunner.OPTION_AUTHORIZATION);
        verify(commandLine).hasOption(GDriveCmdRunner.OPTION_FILE);
        verify(commandLine).hasOption(GDriveCmdRunner.OPTION_DIRECTORY);
        verify(commandLine).hasOption(GDriveCmdRunner.OPTION_TRASH);
        verify(commandLine).getOptionValue(GDriveCmdRunner.OPTION_PROPERTIES);
        verify(commandLine).getOptionValues(GDriveCmdRunner.OPTION_FILE);
        verify(commandLine).getOptionValue(GDriveCmdRunner.OPTION_DIRECTORY);
        verify(propertiesGDriveCredential).setPropertyFile(propertiesValue);
        verify(fileService).uploadFiles(optionFiles, optionDirectory);
        verify(file1).getId();
        verify(file1).getName();
        verify(file2).getId();
        verify(file2).getName();

        verifyNoMoreInteractions(commandLineParser);
        verifyNoMoreInteractions(helpFormatter);
        verifyNoMoreInteractions(gDriveSetting);
        verifyNoMoreInteractions(commandLine);
        verifyNoMoreInteractions(propertiesGDriveCredential);
        verifyNoMoreInteractions(file1, file2);
        verifyNoMoreInteractions(fileService);

        verifyNoInteractions(options);
        verifyNoInteractions(authorizationService);
        verifyNoInteractions(authorizationService);
        verifyNoInteractions(trashService);
        verifyNoInteractions(codeWaiter);
    }

    @Test
    public void testRunWhereCommandLineHasPropertiesAndTrashOptions() throws ParseException {
        String appName = "application-name";
        String propertiesValue = "test-properties-value";
        String[] args = {"arg1", "arg2"};
        Option[] optionList = {mock(Option.class), mock(Option.class)};
        when(commandLineParser.parse(any(Options.class), any(String[].class))).thenReturn(commandLine);
        when(commandLine.getOptions()).thenReturn(optionList);
        when(commandLine.hasOption(GDriveCmdRunner.OPTION_PROPERTIES)).thenReturn(true);
        when(commandLine.hasOption(GDriveCmdRunner.OPTION_VERSION)).thenReturn(false);
        when(commandLine.hasOption(GDriveCmdRunner.OPTION_HELP)).thenReturn(false);
        when(commandLine.hasOption(GDriveCmdRunner.OPTION_LINK)).thenReturn(false);
        when(commandLine.hasOption(GDriveCmdRunner.OPTION_AUTHORIZATION)).thenReturn(false);
        when(commandLine.hasOption(GDriveCmdRunner.OPTION_FILE)).thenReturn(false);
        when(commandLine.hasOption(GDriveCmdRunner.OPTION_TRASH)).thenReturn(true);
        when(commandLine.getOptionValue(GDriveCmdRunner.OPTION_PROPERTIES)).thenReturn(propertiesValue);
        when(gDriveSetting.getApplicationName()).thenReturn(appName);

        runner.run(args);

        verify(commandLineParser).parse(options, args);
        verify(commandLine).getOptions();
        verify(commandLine).hasOption(GDriveCmdRunner.OPTION_PROPERTIES);
        verify(commandLine).hasOption(GDriveCmdRunner.OPTION_VERSION);
        verify(commandLine).hasOption(GDriveCmdRunner.OPTION_HELP);
        verify(commandLine).hasOption(GDriveCmdRunner.OPTION_LINK);
        verify(commandLine).hasOption(GDriveCmdRunner.OPTION_AUTHORIZATION);
        verify(commandLine).hasOption(GDriveCmdRunner.OPTION_FILE);
        verify(commandLine).hasOption(GDriveCmdRunner.OPTION_TRASH);
        verify(commandLine).getOptionValue(GDriveCmdRunner.OPTION_PROPERTIES);
        verify(propertiesGDriveCredential).setPropertyFile(propertiesValue);
        verify(trashService).empty();

        verifyNoMoreInteractions(trashService);
        verifyNoMoreInteractions(commandLineParser);
        verifyNoMoreInteractions(gDriveSetting);
        verifyNoMoreInteractions(commandLine);
        verifyNoMoreInteractions(propertiesGDriveCredential);

        verifyNoInteractions(helpFormatter);
        verifyNoInteractions(options);
        verifyNoInteractions(authorizationService);
        verifyNoInteractions(fileService);
        verifyNoInteractions(codeWaiter);
    }

}
