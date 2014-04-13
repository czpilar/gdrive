package net.czpilar.gdrive.cmd.runner.impl;

import java.util.Arrays;

import com.google.api.client.auth.oauth2.Credential;
import net.czpilar.gdrive.cmd.credential.PropertiesGDriveCredential;
import net.czpilar.gdrive.cmd.exception.CommandLineException;
import net.czpilar.gdrive.cmd.runner.IGDriveCmdRunner;
import net.czpilar.gdrive.core.service.IAuthorizationService;
import net.czpilar.gdrive.core.service.IFileService;
import net.czpilar.gdrive.core.setting.GDriveSetting;
import org.apache.commons.cli.*;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Command line runner implementation.
 *
 * @author David Pilař (david@czpilar.net)
 */
public class GDriveCmdRunner implements IGDriveCmdRunner {

	public static final String OPTION_FILE = "f";
	public static final String OPTION_LINK = "l";
	public static final String OPTION_AUTHORIZATION = "a";
	public static final String OPTION_DIRECTORY = "d";
	public static final String OPTION_PROPERTIES = "p";
	public static final String OPTION_HELP = "h";

	private CommandLineParser commandLineParser;

	private Options options;

	private HelpFormatter helpFormatter;

	private IAuthorizationService authorizationService;

	private IFileService fileService;

	private GDriveSetting gDriveSetting;

	private PropertiesGDriveCredential propertiesGDriveCredential;

	@Autowired
	public void setCommandLineParser(CommandLineParser commandLineParser) {
		this.commandLineParser = commandLineParser;
	}

	@Autowired
	public void setOptions(Options options) {
		this.options = options;
	}

	@Autowired
	public void setHelpFormatter(HelpFormatter helpFormatter) {
		this.helpFormatter = helpFormatter;
	}

	@Autowired
	public void setAuthorizationService(IAuthorizationService authorizationService) {
		this.authorizationService = authorizationService;
	}

	@Autowired
	public void setFileService(IFileService fileService) {
		this.fileService = fileService;
	}

	@Autowired
	public void setGDriveSetting(GDriveSetting gDriveSetting) {
		this.gDriveSetting = gDriveSetting;
	}

	@Autowired
	public void setPropertiesGDriveCredential(PropertiesGDriveCredential propertiesGDriveCredential) {
		this.propertiesGDriveCredential = propertiesGDriveCredential;
	}

	private void doPropertiesOption(CommandLine cmd) {
		propertiesGDriveCredential.setPropertyFile(cmd.getOptionValue(OPTION_PROPERTIES));
	}

	private void doHelpOption(CommandLine cmd) {
		if (cmd.hasOption(OPTION_HELP)) {
			printCommandLine();
		}
	}

	private void doLinkOption(CommandLine cmd) {
		if (cmd.hasOption(OPTION_LINK)) {
			System.out.println("Please authorize gDrive application with following link:\n");
			System.out.println(authorizationService.getAuthorizationURL());
		}
	}

	private void doAuthrizationOption(CommandLine cmd) {
		if (cmd.hasOption(OPTION_AUTHORIZATION)) {
			Credential credential = authorizationService.authorize(cmd.getOptionValue(OPTION_AUTHORIZATION));
			if (credential != null) {
				System.out.println("Authorization was successful.");
			}
		}
	}

	private void doFileOption(CommandLine cmd) {
		if (cmd.hasOption(OPTION_FILE)) {
			String dir = cmd.hasOption(OPTION_DIRECTORY) ? cmd.getOptionValue(OPTION_DIRECTORY) : null;
			fileService.uploadFiles(Arrays.asList(cmd.getOptionValues(OPTION_FILE)), dir);
		}
	}

	private CommandLine parseCommandLine(String[] args) {
		try {
			CommandLine commandLine = commandLineParser.parse(options, args);
			int length = commandLine.getOptions().length;
			if (length == 0 || length == 1 && commandLine.hasOption(OPTION_PROPERTIES)) {
				throw new CommandLineException("No arguments passed!");
			}
			return commandLine;
		} catch (ParseException e) {
			throw new CommandLineException("Invalid arguments passed!", e);
		}
	}

	private void printCommandLine() {
		helpFormatter.printHelp(gDriveSetting.getApplicationName(), options, true);
	}

	@Override
	public void run(String[] args) {
		try {
			CommandLine cmd = parseCommandLine(args);
			doPropertiesOption(cmd);
			doHelpOption(cmd);
			doLinkOption(cmd);
			doAuthrizationOption(cmd);
			doFileOption(cmd);
		} catch (CommandLineException e) {
			System.out.println(e.getMessage() + "\n");
			printCommandLine();
		}
	}

}