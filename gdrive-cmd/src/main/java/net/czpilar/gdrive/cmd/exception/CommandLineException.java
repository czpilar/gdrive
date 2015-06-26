package net.czpilar.gdrive.cmd.exception;

import net.czpilar.gdrive.core.exception.GDriveException;

/**
 * Exception used for error during work with command line.
 *
 * @author David Pilar (david@czpilar.net)
 */
public class CommandLineException extends GDriveException {

    public CommandLineException(String message) {
        super(message);
    }

    public CommandLineException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommandLineException(Throwable cause) {
        super(cause);
    }

}
