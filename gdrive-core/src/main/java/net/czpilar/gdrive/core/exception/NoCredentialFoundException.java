package net.czpilar.gdrive.core.exception;

/**
 * Exception used when no credential found.
 *
 * @author David Pilar (david@czpilar.net)
 */
public class NoCredentialFoundException extends GDriveException {

    public NoCredentialFoundException(String message) {
        super(message);
    }
}
