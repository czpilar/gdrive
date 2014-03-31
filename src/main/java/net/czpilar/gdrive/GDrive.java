package net.czpilar.gdrive;

import java.util.Arrays;

import com.google.api.client.auth.oauth2.Credential;
import net.czpilar.gdrive.constant.GDriveConstants;
import net.czpilar.gdrive.credential.impl.GDrivePropertiesCredential;
import net.czpilar.gdrive.exception.CommandLineException;
import net.czpilar.gdrive.service.IGDriveAuthorizationService;
import net.czpilar.gdrive.service.IGDriveFileService;
import net.czpilar.gdrive.service.impl.GDriveService;
import org.apache.commons.cli.*;

/**
 * Main class for running GDrive.
 *
 * @author David Pilař (david@czpilar.net)
 */
public class GDrive {

	private static final Options OPTIONS = new Options();

	private static final String FILE = "f";
	private static final String LINK = "l";
	private static final String AUTHORIZATION = "a";
	private static final String DIRECTORY = "d";
	private static final String PROPERTIES = "p";
	private static final String HELP = "h";

	private final GDriveService service;

	static {
		OPTIONS.addOption(HELP, false, "show this help");
		OPTIONS.addOption(LINK, false, "display authorization link");

		Option authorization = new Option(AUTHORIZATION, true, "process authorization");
		authorization.setArgName("code");
		OPTIONS.addOption(authorization);

		Option file = new Option(FILE, true, "upload files");
		file.setArgs(Option.UNLIMITED_VALUES);
		file.setArgName("file");
		OPTIONS.addOption(file);

		Option dir = new Option(DIRECTORY, true, "directory for upload; creates new one if no directory exists; default is gdrive-uploads");
		dir.setArgName("dir");
		OPTIONS.addOption(dir);

		Option props = new Option(PROPERTIES, true, "path to gdrive properties file");
		props.setArgName("props");
		props.setRequired(true);
		OPTIONS.addOption(props);
	}

	public GDrive(String propertyFile) {
		this.service = new GDriveService(new GDrivePropertiesCredential(propertyFile));
	}

	public IGDriveAuthorizationService getAuthorizationService() {
		return service;
	}

	public IGDriveFileService getFileService() {
		return service;
	}

	public static CommandLine parseCommandLine(String[] args) {
		try {
			CommandLine commandLine = new BasicParser().parse(OPTIONS, args);
			int length = commandLine.getOptions().length;
			if (length == 0 || length == 1 && commandLine.hasOption(PROPERTIES)) {
				throw new CommandLineException("No arguments passed!");
			}
			return commandLine;
		} catch (ParseException e) {
			throw new CommandLineException("Invalid arguments passed!", e);
		}
	}

	public static void printCommandLine() {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp(GDriveConstants.APP_NAME, OPTIONS, true);
	}

	public static void main(String[] args) {
		try {
			CommandLine cli = parseCommandLine(args);
			GDrive gDrive = new GDrive(cli.getOptionValue(PROPERTIES));
			if (cli.hasOption(HELP)) {
				printCommandLine();
			}
			if (cli.hasOption(LINK)) {
				System.out.println("Please authorize gDrive application with following link:\n");
				System.out.println(gDrive.getAuthorizationService().getAuthorizationURL());
			}
			if (cli.hasOption(AUTHORIZATION)) {
				Credential credential = gDrive.getAuthorizationService().authorize(cli.getOptionValue(AUTHORIZATION));
				if (credential != null) {
					System.out.println("Authorization was successful.");
				}
			}
			if (cli.hasOption(FILE)) {
				String dir = cli.hasOption(DIRECTORY) ? cli.getOptionValue(DIRECTORY) : null;
				gDrive.getFileService().uploadFiles(Arrays.asList(cli.getOptionValues(FILE)), dir);
			}
		} catch (CommandLineException e) {
			System.out.println(e.getMessage() + "\n");
			printCommandLine();
		}
	}
}
