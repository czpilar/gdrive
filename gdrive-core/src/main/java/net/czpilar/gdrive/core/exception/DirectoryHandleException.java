package net.czpilar.gdrive.core.exception;

/**
 * Exception used when directory handling fails.
 *
 * @author David Pilar (david@czpilar.net)
 */
public class DirectoryHandleException extends GDriveException {

    public DirectoryHandleException(String message) {
        super(message);
    }

    public DirectoryHandleException(String message, Throwable cause) {
        super(message, cause);
    }

    public DirectoryHandleException(Throwable cause) {
        super(cause);
    }
}
