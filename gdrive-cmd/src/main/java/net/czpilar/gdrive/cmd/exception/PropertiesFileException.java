package net.czpilar.gdrive.cmd.exception;

import net.czpilar.gdrive.core.exception.GDriveException;

/**
 * Exception used for error during work with properties file.
 *
 * @author David Pilar (david@czpilar.net)
 */
public class PropertiesFileException extends GDriveException {

	public PropertiesFileException(String message) {
		super(message);
	}

	public PropertiesFileException(String message, Throwable cause) {
		super(message, cause);
	}

	public PropertiesFileException(Throwable cause) {
		super(cause);
	}
}
