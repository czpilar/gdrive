package net.czpilar.gdrive.cmd;

import net.czpilar.gdrive.cmd.context.GDriveCmdContext;
import net.czpilar.gdrive.cmd.runner.IGDriveCmdRunner;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Main class for running gDrive from command line.
 *
 * @author David Pilar (david@czpilar.net)
 */
public class GDrive {

    public static void main(String[] args) {
        new AnnotationConfigApplicationContext(GDriveCmdContext.class)
                .getBean(IGDriveCmdRunner.class)
                .run(args);
    }
}
