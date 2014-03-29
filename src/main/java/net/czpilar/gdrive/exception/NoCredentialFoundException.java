package net.czpilar.gdrive.exception;

/**
 * @author David Pilař (david@czpilar.net)
 */
public class NoCredentialFoundException extends GDriveException {

	public NoCredentialFoundException() {
	}

	public NoCredentialFoundException(String message) {
		super(message);
	}

	public NoCredentialFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoCredentialFoundException(Throwable cause) {
		super(cause);
	}

}
