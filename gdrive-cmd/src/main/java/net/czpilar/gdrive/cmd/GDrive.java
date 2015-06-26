package net.czpilar.gdrive.cmd;

import net.czpilar.gdrive.cmd.runner.IGDriveCmdRunner;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Main class for running gDrive from command line.
 *
 * @author David Pilar (david@czpilar.net)
 */
public class GDrive {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:gdrive-cmd-applicationContext.xml");
        IGDriveCmdRunner runner = context.getBean(IGDriveCmdRunner.class);
        runner.run(args);
    }
}
