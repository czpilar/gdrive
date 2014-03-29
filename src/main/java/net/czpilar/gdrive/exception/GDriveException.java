package net.czpilar.gdrive.exception;

/**
 * @author David Pilař (david@czpilar.net)
 */
public class GDriveException extends RuntimeException {

	public GDriveException() {
	}

	public GDriveException(String message) {
		super(message);
	}

	public GDriveException(String message, Throwable cause) {
		super(message, cause);
	}

	public GDriveException(Throwable cause) {
		super(cause);
	}

}
