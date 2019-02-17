package net.czpilar.gdrive.core.exception;

/**
 * Exception used when trash handling fails.
 *
 * @author David Pilar (david@czpilar.net)
 */
public class TrashHandleException extends GDriveException {

    public TrashHandleException(String message, Throwable cause) {
        super(message, cause);
    }
}
