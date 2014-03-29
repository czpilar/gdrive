package net.czpilar.gdrive.exception;

/**
 * @author David Pilař (david@czpilar.net)
 */
public class CommandLineException extends GDriveException {

	public CommandLineException() {
	}

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
