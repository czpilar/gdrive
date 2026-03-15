package net.czpilar.gdrive.cmd.context;

import net.czpilar.gdrive.cmd.credential.PropertiesGDriveCredential;
import net.czpilar.gdrive.core.context.GDriveCoreContext;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.help.HelpFormatter;
import org.apache.commons.cli.help.OptionFormatter;
import org.springframework.context.annotation.*;

import static net.czpilar.gdrive.cmd.runner.impl.GDriveCmdRunner.*;

@Configuration
@ComponentScan(basePackages = "net.czpilar.gdrive.cmd")
@Import(GDriveCoreContext.class)
@PropertySource("classpath:gdrive-core.properties")
public class GDriveCmdContext {

    public static final String UPLOAD_DIR_PROPERTY_KEY = "gdrive.uploadDir";
    public static final String REFRESH_TOKEN_PROPERTY_KEY = "gdrive.refreshToken";
    public static final String DEFAULT_UPLOAD_DIR = "gdrive-uploads";

    @Bean
    public PropertiesGDriveCredential propertiesGDriveCredential() {
        return new PropertiesGDriveCredential(GDriveCmdContext.UPLOAD_DIR_PROPERTY_KEY,
                GDriveCmdContext.REFRESH_TOKEN_PROPERTY_KEY, GDriveCmdContext.DEFAULT_UPLOAD_DIR);
    }

    @Bean
    public HelpFormatter helpFormatter() {
        return HelpFormatter.builder()
                .setShowSince(false)
                .setOptionFormatBuilder(OptionFormatter.builder().setArgumentNameDelimiters("", ""))
                .get();
    }

    @Bean
    public DefaultParser defaultParser() {
        return new DefaultParser();
    }

    @Bean
    public Options options() {
        return new Options()
                .addOption(toOption(OPTION_VERSION, "show gDrive version"))
                .addOption(toOption(OPTION_HELP, "show this help"))
                .addOption(toOption(OPTION_LINK, "display authorization link"))
                .addOption(toOptionalOption(OPTION_AUTHORIZATION, "process authorization; waits for code if not provided", "[code]"))
                .addOption(toUnlimitedOption(toOption(OPTION_FILE, "upload file(s)", "<file>")))
                .addOption(toOption(OPTION_DIRECTORY, "directory for upload; creates new one if no directory exists; default is gdrive-uploads", "<dir>"))
                .addOption(toOption(OPTION_PROPERTIES, "path to gDrive properties file", "<props>"))
                .addOption(toOption(OPTION_TRASH, "empty trash"));
    }

    private Option toOption(String opt, String description) {
        return new Option(opt, description);
    }

    private Option toOption(String opt, String description, String argName) {
        Option option = new Option(opt, true, description);
        option.setArgName(argName);
        return option;
    }

    private Option toOptionalOption(String opt, String description, String argName) {
        Option option = new Option(opt, true, description);
        option.setArgName(argName);
        option.setOptionalArg(true);
        return option;
    }

    private Option toUnlimitedOption(Option option) {
        option.setArgs(Option.UNLIMITED_VALUES);
        return option;
    }
}
