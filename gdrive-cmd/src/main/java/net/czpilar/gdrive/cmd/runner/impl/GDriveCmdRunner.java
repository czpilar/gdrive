package net.czpilar.gdrive.cmd.runner.impl;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.drive.model.File;
import net.czpilar.gdrive.cmd.credential.PropertiesGDriveCredential;
import net.czpilar.gdrive.cmd.exception.CommandLineException;
import net.czpilar.gdrive.cmd.runner.IGDriveCmdRunner;
import net.czpilar.gdrive.core.service.IAuthorizationService;
import net.czpilar.gdrive.core.service.IFileService;
import net.czpilar.gdrive.core.service.ITrashService;
import net.czpilar.gdrive.core.setting.GDriveSetting;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.help.HelpFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Command line runner implementation.
 *
 * @author David Pilar (david@czpilar.net)
 */
@Component
public class GDriveCmdRunner implements IGDriveCmdRunner {

    public static final String OPTION_FILE = "f";
    public static final String OPTION_LINK = "l";
    public static final String OPTION_AUTHORIZATION = "a";
    public static final String OPTION_DIRECTORY = "d";
    public static final String OPTION_PROPERTIES = "p";
    public static final String OPTION_HELP = "h";
    public static final String OPTION_VERSION = "v";
    public static final String OPTION_TRASH = "t";

    private CommandLineParser commandLineParser;

    private Options options;

    private HelpFormatter helpFormatter;

    private IAuthorizationService authorizationService;

    private IFileService fileService;

    private ITrashService trashService;

    private GDriveSetting gDriveSetting;

    private PropertiesGDriveCredential propertiesGDriveCredential;

    private AuthorizationCodeWaiter codeWaiter;

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
    public void setTrashService(ITrashService trashService) {
        this.trashService = trashService;
    }

    @Autowired
    public void setGDriveSetting(GDriveSetting gDriveSetting) {
        this.gDriveSetting = gDriveSetting;
    }

    @Autowired
    public void setPropertiesGDriveCredential(PropertiesGDriveCredential propertiesGDriveCredential) {
        this.propertiesGDriveCredential = propertiesGDriveCredential;
    }

    @Autowired
    public void setCodeWaiter(AuthorizationCodeWaiter codeWaiter) {
        this.codeWaiter = codeWaiter;
    }

    private void doPropertiesOption(CommandLine cmd) {
        if (cmd.hasOption(OPTION_PROPERTIES)) {
            propertiesGDriveCredential.setPropertyFile(cmd.getOptionValue(OPTION_PROPERTIES));
        }
    }

    private void doVersionOption(CommandLine cmd) {
        if (cmd.hasOption(OPTION_VERSION)) {
            System.out.println("gDrive version: " + gDriveSetting.getApplicationVersion());
        }
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

    private void doAuthorizationOption(CommandLine cmd) {
        if (cmd.hasOption(OPTION_AUTHORIZATION)) {
            Optional<String> optionValue = Optional.ofNullable(cmd.getOptionValue(OPTION_AUTHORIZATION));
            if (optionValue.isEmpty()) {
                optionValue = codeWaiter.getCode();
            }
            optionValue.ifPresent(code -> {
                Credential credential = authorizationService.authorize(code);
                if (credential != null) {
                    System.out.println("Authorization was successful.");
                }
            });
        }
    }

    private void doFileOption(CommandLine cmd) {
        if (cmd.hasOption(OPTION_FILE)) {
            String dir = cmd.hasOption(OPTION_DIRECTORY) ? cmd.getOptionValue(OPTION_DIRECTORY) : null;
            List<File> files = fileService.uploadFiles(Arrays.asList(cmd.getOptionValues(OPTION_FILE)), dir);
            System.out.println("Uploaded " + files.size() + " file(s)...");
            for (File file : files) {
                System.out.println("- " + file.getName() + " (remote ID: " + file.getId() + ")");
            }
        }
    }

    private void doTrashOption(CommandLine cmd) {
        if (cmd.hasOption(OPTION_TRASH)) {
            trashService.empty();
            System.out.println("Emptied trash...");
        }
    }

    private CommandLine parseCommandLine(String[] args) {
        try {
            CommandLine cmd = commandLineParser.parse(options, args);
            int length = cmd.getOptions().length;
            if (length == 0) {
                throw new CommandLineException("No arguments passed!");
            } else if (length == 1 && cmd.hasOption(OPTION_PROPERTIES)) {
                throw new CommandLineException("Provide at least one more argument!");
            }
            return cmd;
        } catch (ParseException e) {
            throw new CommandLineException("Invalid arguments passed!", e);
        }
    }

    private void printCommandLine() {
        try {
            helpFormatter.printHelp(gDriveSetting.getApplicationName(), null, options, null, true);
        } catch (IOException e) {
            throw new CommandLineException("Unable to print help!", e);
        }
    }

    @Override
    public void run(String[] args) {
        try {
            CommandLine cmd = parseCommandLine(args);
            doPropertiesOption(cmd);
            doVersionOption(cmd);
            doHelpOption(cmd);
            doLinkOption(cmd);
            doAuthorizationOption(cmd);
            doFileOption(cmd);
            doTrashOption(cmd);
        } catch (CommandLineException e) {
            System.out.println(e.getMessage() + "\n");
            printCommandLine();
        }
    }
}
