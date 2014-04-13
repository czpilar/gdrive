package net.czpilar.gdrive.core.exception;

/**
 * Exception used when uploading file fails.
 *
 * @author David Pilař (david@czpilar.net)
 */
public class FileUploadException extends GDriveException {

	public FileUploadException(String message) {
		super(message);
	}

	public FileUploadException(String message, Throwable cause) {
		super(message, cause);
	}

	public FileUploadException(Throwable cause) {
		super(cause);
	}

}
