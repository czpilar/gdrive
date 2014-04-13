package net.czpilar.gdrive.core.exception;

/**
 * Exception used when no credential found.
 *
 * @author David Pilař (david@czpilar.net)
 */
public class NoCredentialFoundException extends GDriveException {

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
