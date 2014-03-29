package net.czpilar.gdrive.exception;

/**
 * @author David Pilař (david@czpilar.net)
 */
public class FileSaveException extends GDriveException {

	public FileSaveException() {
	}

	public FileSaveException(String message) {
		super(message);
	}

	public FileSaveException(String message, Throwable cause) {
		super(message, cause);
	}

	public FileSaveException(Throwable cause) {
		super(cause);
	}

}
