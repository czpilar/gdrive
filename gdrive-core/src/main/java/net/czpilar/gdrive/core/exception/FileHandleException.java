package net.czpilar.gdrive.core.exception;

/**
 * Exception used when file handling fails.
 *
 * @author David Pilar (david@czpilar.net)
 */
public class FileHandleException extends GDriveException {

    public FileHandleException(String message) {
        super(message);
    }

    public FileHandleException(String message, Throwable cause) {
        super(message, cause);
    }
}
