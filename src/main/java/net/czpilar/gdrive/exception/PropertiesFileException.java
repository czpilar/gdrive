package net.czpilar.gdrive.exception;

/**
 * @author David Pilař (david@czpilar.net)
 */
public class PropertiesFileException extends GDriveException {

	public PropertiesFileException() {
	}

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
