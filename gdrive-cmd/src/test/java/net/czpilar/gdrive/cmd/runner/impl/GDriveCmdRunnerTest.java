package net.czpilar.gdrive.cmd.runner.impl;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import net.czpilar.gdrive.cmd.credential.PropertiesGDriveCredential;
import net.czpilar.gdrive.core.service.IAuthorizationService;
import net.czpilar.gdrive.core.service.IFileService;
import net.czpilar.gdrive.core.setting.GDriveSetting;
import org.apache.commons.cli.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * @author David Pilař (david@czpilar.net)
 */
public class GDriveCmdRunnerTest {

	private GDriveCmdRunner runner = new GDriveCmdRunner();
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
	private GDriveSetting gDriveSetting;
	@Mock
	private PropertiesGDriveCredential propertiesGDriveCredential;
	@Mock
	private CommandLine commandLine;

	@Before
	public void before() {
		MockitoAnnotations.initMocks(this);
		runner.setCommandLineParser(commandLineParser);
		runner.setOptions(options);
		runner.setHelpFormatter(helpFormatter);
		runner.setAuthorizationService(authorizationService);
		runner.setFileService(fileService);
		runner.setGDriveSetting(gDriveSetting);
		runner.setPropertiesGDriveCredential(propertiesGDriveCredential);
	}

	@Test
	public void testRunWhereCommandLineParsingFails() throws ParseException {
		String appName = "application-name";
		String[] args = { "arg1", "arg2" };
		when(commandLineParser.parse(any(Options.class), any(String[].class))).thenThrow(ParseException.class);
		when(gDriveSetting.getApplicationName()).thenReturn(appName);

		runner.run(args);

		verify(commandLineParser).parse(options, args);
		verify(helpFormatter).printHelp(appName, options, true);
		verify(gDriveSetting).getApplicationName();

		verifyNoMoreInteractions(commandLineParser);
		verifyNoMoreInteractions(helpFormatter);
		verifyNoMoreInteractions(gDriveSetting);

		verifyZeroInteractions(options);
		verifyZeroInteractions(authorizationService);
		verifyZeroInteractions(fileService);
		verifyZeroInteractions(propertiesGDriveCredential);
		verifyZeroInteractions(commandLine);
	}

	@Test
	public void testRunWhereCommandLineHasEmptyOptions() throws ParseException {
		String appName = "application-name";
		String[] args = { "arg1", "arg2" };
		Option[] optionList = { };
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

		verifyZeroInteractions(options);
		verifyZeroInteractions(authorizationService);
		verifyZeroInteractions(fileService);
		verifyZeroInteractions(propertiesGDriveCredential);
	}

	@Test
	public void testRunWhereCommandLineHasOnlyPropertiesOption() throws ParseException {
		String appName = "application-name";
		String[] args = { "arg1", "arg2" };
		Option[] optionList = { mock(Option.class) };
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

		verifyZeroInteractions(options);
		verifyZeroInteractions(authorizationService);
		verifyZeroInteractions(fileService);
		verifyZeroInteractions(propertiesGDriveCredential);
	}

	@Test
	public void testRunWhereCommandLineHasPropertiesAndHelpOptions() throws ParseException {
		String appName = "application-name";
		String propertiesValue = "test-properties-value";
		String[] args = { "arg1", "arg2" };
		Option[] optionList = { mock(Option.class), mock(Option.class) };
		when(commandLineParser.parse(any(Options.class), any(String[].class))).thenReturn(commandLine);
		when(commandLine.getOptions()).thenReturn(optionList);
		when(commandLine.hasOption(GDriveCmdRunner.OPTION_PROPERTIES)).thenReturn(true);
		when(commandLine.hasOption(GDriveCmdRunner.OPTION_HELP)).thenReturn(true);
		when(commandLine.hasOption(GDriveCmdRunner.OPTION_LINK)).thenReturn(false);
		when(commandLine.hasOption(GDriveCmdRunner.OPTION_AUTHORIZATION)).thenReturn(false);
		when(commandLine.hasOption(GDriveCmdRunner.OPTION_FILE)).thenReturn(false);
		when(commandLine.getOptionValue(GDriveCmdRunner.OPTION_PROPERTIES)).thenReturn(propertiesValue);
		when(gDriveSetting.getApplicationName()).thenReturn(appName);

		runner.run(args);

		verify(commandLineParser).parse(options, args);
		verify(helpFormatter).printHelp(appName, options, true);
		verify(commandLine).getOptions();
		verify(commandLine).hasOption(GDriveCmdRunner.OPTION_HELP);
		verify(commandLine).hasOption(GDriveCmdRunner.OPTION_LINK);
		verify(commandLine).hasOption(GDriveCmdRunner.OPTION_AUTHORIZATION);
		verify(commandLine).hasOption(GDriveCmdRunner.OPTION_FILE);
		verify(commandLine).getOptionValue(GDriveCmdRunner.OPTION_PROPERTIES);
		verify(gDriveSetting).getApplicationName();
		verify(propertiesGDriveCredential).setPropertyFile(propertiesValue);

		verifyNoMoreInteractions(commandLineParser);
		verifyNoMoreInteractions(helpFormatter);
		verifyNoMoreInteractions(gDriveSetting);
		verifyNoMoreInteractions(commandLine);
		verifyNoMoreInteractions(propertiesGDriveCredential);

		verifyZeroInteractions(options);
		verifyZeroInteractions(authorizationService);
		verifyZeroInteractions(fileService);
	}

	@Test
	public void testRunWhereCommandLineHasPropertiesAndLinkOptions() throws ParseException {
		String propertiesValue = "test-properties-value";
		String[] args = { "arg1", "arg2" };
		Option[] optionList = { mock(Option.class), mock(Option.class) };
		when(commandLineParser.parse(any(Options.class), any(String[].class))).thenReturn(commandLine);
		when(commandLine.getOptions()).thenReturn(optionList);
		when(commandLine.hasOption(GDriveCmdRunner.OPTION_PROPERTIES)).thenReturn(true);
		when(commandLine.hasOption(GDriveCmdRunner.OPTION_HELP)).thenReturn(false);
		when(commandLine.hasOption(GDriveCmdRunner.OPTION_LINK)).thenReturn(true);
		when(commandLine.hasOption(GDriveCmdRunner.OPTION_AUTHORIZATION)).thenReturn(false);
		when(commandLine.hasOption(GDriveCmdRunner.OPTION_FILE)).thenReturn(false);
		when(commandLine.getOptionValue(GDriveCmdRunner.OPTION_PROPERTIES)).thenReturn(propertiesValue);
		when(authorizationService.getAuthorizationURL()).thenReturn("test-authorization-url");

		runner.run(args);

		verify(commandLineParser).parse(options, args);
		verify(commandLine).getOptions();
		verify(commandLine).hasOption(GDriveCmdRunner.OPTION_HELP);
		verify(commandLine).hasOption(GDriveCmdRunner.OPTION_LINK);
		verify(commandLine).hasOption(GDriveCmdRunner.OPTION_AUTHORIZATION);
		verify(commandLine).hasOption(GDriveCmdRunner.OPTION_FILE);
		verify(commandLine).getOptionValue(GDriveCmdRunner.OPTION_PROPERTIES);
		verify(propertiesGDriveCredential).setPropertyFile(propertiesValue);
		verify(authorizationService).getAuthorizationURL();

		verifyNoMoreInteractions(commandLineParser);
		verifyNoMoreInteractions(helpFormatter);
		verifyNoMoreInteractions(gDriveSetting);
		verifyNoMoreInteractions(commandLine);
		verifyNoMoreInteractions(propertiesGDriveCredential);

		verifyZeroInteractions(options);
		verifyZeroInteractions(authorizationService);
		verifyZeroInteractions(fileService);
	}

	@Test
	public void testRunWhereCommandLineHasPropertiesAndAuthorizationOptionsAndReturnNullCredential() throws ParseException {
		String propertiesValue = "test-properties-value";
		String authorizationValue = "test-athorization-value";
		String[] args = { "arg1", "arg2" };
		Option[] optionList = { mock(Option.class), mock(Option.class) };
		when(commandLineParser.parse(any(Options.class), any(String[].class))).thenReturn(commandLine);
		when(commandLine.getOptions()).thenReturn(optionList);
		when(commandLine.hasOption(GDriveCmdRunner.OPTION_PROPERTIES)).thenReturn(true);
		when(commandLine.hasOption(GDriveCmdRunner.OPTION_HELP)).thenReturn(false);
		when(commandLine.hasOption(GDriveCmdRunner.OPTION_LINK)).thenReturn(false);
		when(commandLine.hasOption(GDriveCmdRunner.OPTION_AUTHORIZATION)).thenReturn(true);
		when(commandLine.hasOption(GDriveCmdRunner.OPTION_FILE)).thenReturn(false);
		when(commandLine.getOptionValue(GDriveCmdRunner.OPTION_PROPERTIES)).thenReturn(propertiesValue);
		when(commandLine.getOptionValue(GDriveCmdRunner.OPTION_AUTHORIZATION)).thenReturn(authorizationValue);
		when(authorizationService.authorize(authorizationValue)).thenReturn(null);

		runner.run(args);

		verify(commandLineParser).parse(options, args);
		verify(commandLine).getOptions();
		verify(commandLine).hasOption(GDriveCmdRunner.OPTION_HELP);
		verify(commandLine).hasOption(GDriveCmdRunner.OPTION_LINK);
		verify(commandLine).hasOption(GDriveCmdRunner.OPTION_AUTHORIZATION);
		verify(commandLine).hasOption(GDriveCmdRunner.OPTION_FILE);
		verify(commandLine).getOptionValue(GDriveCmdRunner.OPTION_PROPERTIES);
		verify(commandLine).getOptionValue(GDriveCmdRunner.OPTION_AUTHORIZATION);
		verify(propertiesGDriveCredential).setPropertyFile(propertiesValue);
		verify(authorizationService).authorize(authorizationValue);

		verifyNoMoreInteractions(commandLineParser);
		verifyNoMoreInteractions(helpFormatter);
		verifyNoMoreInteractions(gDriveSetting);
		verifyNoMoreInteractions(commandLine);
		verifyNoMoreInteractions(propertiesGDriveCredential);

		verifyZeroInteractions(options);
		verifyZeroInteractions(authorizationService);
		verifyZeroInteractions(fileService);
	}

	@Test
	public void testRunWhereCommandLineHasPropertiesAndAuthorizationOptionsAndReturnCredential() throws ParseException {
		String propertiesValue = "test-properties-value";
		String authorizationValue = "test-athorization-value";
		String[] args = { "arg1", "arg2" };
		Option[] optionList = { mock(Option.class), mock(Option.class) };
		when(commandLineParser.parse(any(Options.class), any(String[].class))).thenReturn(commandLine);
		when(commandLine.getOptions()).thenReturn(optionList);
		when(commandLine.hasOption(GDriveCmdRunner.OPTION_PROPERTIES)).thenReturn(true);
		when(commandLine.hasOption(GDriveCmdRunner.OPTION_HELP)).thenReturn(false);
		when(commandLine.hasOption(GDriveCmdRunner.OPTION_LINK)).thenReturn(false);
		when(commandLine.hasOption(GDriveCmdRunner.OPTION_AUTHORIZATION)).thenReturn(true);
		when(commandLine.hasOption(GDriveCmdRunner.OPTION_FILE)).thenReturn(false);
		when(commandLine.getOptionValue(GDriveCmdRunner.OPTION_PROPERTIES)).thenReturn(propertiesValue);
		when(commandLine.getOptionValue(GDriveCmdRunner.OPTION_AUTHORIZATION)).thenReturn(authorizationValue);
		when(authorizationService.authorize(authorizationValue)).thenReturn(mock(Credential.class));

		runner.run(args);

		verify(commandLineParser).parse(options, args);
		verify(commandLine).getOptions();
		verify(commandLine).hasOption(GDriveCmdRunner.OPTION_HELP);
		verify(commandLine).hasOption(GDriveCmdRunner.OPTION_LINK);
		verify(commandLine).hasOption(GDriveCmdRunner.OPTION_AUTHORIZATION);
		verify(commandLine).hasOption(GDriveCmdRunner.OPTION_FILE);
		verify(commandLine).getOptionValue(GDriveCmdRunner.OPTION_PROPERTIES);
		verify(commandLine).getOptionValue(GDriveCmdRunner.OPTION_AUTHORIZATION);
		verify(propertiesGDriveCredential).setPropertyFile(propertiesValue);
		verify(authorizationService).authorize(authorizationValue);

		verifyNoMoreInteractions(commandLineParser);
		verifyNoMoreInteractions(helpFormatter);
		verifyNoMoreInteractions(gDriveSetting);
		verifyNoMoreInteractions(commandLine);
		verifyNoMoreInteractions(propertiesGDriveCredential);

		verifyZeroInteractions(options);
		verifyZeroInteractions(authorizationService);
		verifyZeroInteractions(fileService);
	}

	@Test
	public void testRunWhereCommandLineHasPropertiesAndFileOptionsAndNoDirectory() throws ParseException {
		String propertiesValue = "test-properties-value";
		String optionFile = "test-file-value";
		List<String> optionFiles = Arrays.asList(optionFile);
		String[] args = { "arg1", "arg2" };
		Option[] optionList = { mock(Option.class), mock(Option.class) };
		when(commandLineParser.parse(any(Options.class), any(String[].class))).thenReturn(commandLine);
		when(commandLine.getOptions()).thenReturn(optionList);
		when(commandLine.hasOption(GDriveCmdRunner.OPTION_PROPERTIES)).thenReturn(true);
		when(commandLine.hasOption(GDriveCmdRunner.OPTION_HELP)).thenReturn(false);
		when(commandLine.hasOption(GDriveCmdRunner.OPTION_LINK)).thenReturn(false);
		when(commandLine.hasOption(GDriveCmdRunner.OPTION_AUTHORIZATION)).thenReturn(false);
		when(commandLine.hasOption(GDriveCmdRunner.OPTION_FILE)).thenReturn(true);
		when(commandLine.hasOption(GDriveCmdRunner.OPTION_DIRECTORY)).thenReturn(false);
		when(commandLine.getOptionValue(GDriveCmdRunner.OPTION_PROPERTIES)).thenReturn(propertiesValue);
		when(commandLine.getOptionValues(GDriveCmdRunner.OPTION_FILE)).thenReturn(new String[] { optionFile });

		runner.run(args);

		verify(commandLineParser).parse(options, args);
		verify(commandLine).getOptions();
		verify(commandLine).hasOption(GDriveCmdRunner.OPTION_HELP);
		verify(commandLine).hasOption(GDriveCmdRunner.OPTION_LINK);
		verify(commandLine).hasOption(GDriveCmdRunner.OPTION_AUTHORIZATION);
		verify(commandLine).hasOption(GDriveCmdRunner.OPTION_FILE);
		verify(commandLine).hasOption(GDriveCmdRunner.OPTION_DIRECTORY);
		verify(commandLine).getOptionValue(GDriveCmdRunner.OPTION_PROPERTIES);
		verify(commandLine).getOptionValues(GDriveCmdRunner.OPTION_FILE);
		verify(propertiesGDriveCredential).setPropertyFile(propertiesValue);
		verify(fileService).uploadFiles(optionFiles, (String) null);

		verifyNoMoreInteractions(commandLineParser);
		verifyNoMoreInteractions(helpFormatter);
		verifyNoMoreInteractions(gDriveSetting);
		verifyNoMoreInteractions(commandLine);
		verifyNoMoreInteractions(propertiesGDriveCredential);

		verifyZeroInteractions(options);
		verifyZeroInteractions(authorizationService);
		verifyZeroInteractions(fileService);
		verifyZeroInteractions(authorizationService);
	}

	@Test
	public void testRunWhereCommandLineHasPropertiesAndFileOptionsAndDirectory() throws ParseException {
		String propertiesValue = "test-properties-value";
		String optionFile = "test-file-value";
		String optionDirectory = "test-directory";
		List<String> optionFiles = Arrays.asList(optionFile);
		String[] args = { "arg1", "arg2" };
		Option[] optionList = { mock(Option.class), mock(Option.class) };
		when(commandLineParser.parse(any(Options.class), any(String[].class))).thenReturn(commandLine);
		when(commandLine.getOptions()).thenReturn(optionList);
		when(commandLine.hasOption(GDriveCmdRunner.OPTION_PROPERTIES)).thenReturn(true);
		when(commandLine.hasOption(GDriveCmdRunner.OPTION_HELP)).thenReturn(false);
		when(commandLine.hasOption(GDriveCmdRunner.OPTION_LINK)).thenReturn(false);
		when(commandLine.hasOption(GDriveCmdRunner.OPTION_AUTHORIZATION)).thenReturn(false);
		when(commandLine.hasOption(GDriveCmdRunner.OPTION_FILE)).thenReturn(true);
		when(commandLine.hasOption(GDriveCmdRunner.OPTION_DIRECTORY)).thenReturn(true);
		when(commandLine.getOptionValue(GDriveCmdRunner.OPTION_PROPERTIES)).thenReturn(propertiesValue);
		when(commandLine.getOptionValues(GDriveCmdRunner.OPTION_FILE)).thenReturn(new String[] { optionFile });
		when(commandLine.getOptionValue(GDriveCmdRunner.OPTION_DIRECTORY)).thenReturn(optionDirectory);

		runner.run(args);

		verify(commandLineParser).parse(options, args);
		verify(commandLine).getOptions();
		verify(commandLine).hasOption(GDriveCmdRunner.OPTION_HELP);
		verify(commandLine).hasOption(GDriveCmdRunner.OPTION_LINK);
		verify(commandLine).hasOption(GDriveCmdRunner.OPTION_AUTHORIZATION);
		verify(commandLine).hasOption(GDriveCmdRunner.OPTION_FILE);
		verify(commandLine).hasOption(GDriveCmdRunner.OPTION_DIRECTORY);
		verify(commandLine).getOptionValue(GDriveCmdRunner.OPTION_PROPERTIES);
		verify(commandLine).getOptionValues(GDriveCmdRunner.OPTION_FILE);
		verify(commandLine).getOptionValue(GDriveCmdRunner.OPTION_DIRECTORY);
		verify(propertiesGDriveCredential).setPropertyFile(propertiesValue);
		verify(fileService).uploadFiles(optionFiles, optionDirectory);

		verifyNoMoreInteractions(commandLineParser);
		verifyNoMoreInteractions(helpFormatter);
		verifyNoMoreInteractions(gDriveSetting);
		verifyNoMoreInteractions(commandLine);
		verifyNoMoreInteractions(propertiesGDriveCredential);

		verifyZeroInteractions(options);
		verifyZeroInteractions(authorizationService);
		verifyZeroInteractions(fileService);
		verifyZeroInteractions(authorizationService);
	}
}
