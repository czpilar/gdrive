package net.czpilar.gdrive.exception;

/**
 * @author David Pilař (david@czpilar.net)
 */
public class AuthorizationFailedException extends GDriveException {

	public AuthorizationFailedException() {
	}

	public AuthorizationFailedException(String message) {
		super(message);
	}

	public AuthorizationFailedException(String message, Throwable cause) {
		super(message, cause);
	}

	public AuthorizationFailedException(Throwable cause) {
		super(cause);
	}

}
