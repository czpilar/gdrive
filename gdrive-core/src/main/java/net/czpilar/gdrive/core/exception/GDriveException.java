package net.czpilar.gdrive.core.exception;

/**
 * Base exception for all gDrive exceptions.
 *
 * @author David Pilar (david@czpilar.net)
 */
public class GDriveException extends RuntimeException {

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
