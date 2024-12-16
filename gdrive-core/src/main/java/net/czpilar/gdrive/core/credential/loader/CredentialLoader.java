package net.czpilar.gdrive.core.credential.loader;

import com.google.api.client.auth.oauth2.Credential;
import net.czpilar.gdrive.core.credential.IGDriveCredential;
import net.czpilar.gdrive.core.exception.NoCredentialFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Autoloader of gDrive credential from Spring context.
 *
 * @author David Pilar (david@czpilar.net)
 */
@Component
public class CredentialLoader {

    private final IGDriveCredential gDriveCredential;

    @Autowired
    public CredentialLoader(IGDriveCredential gDriveCredential) {
        this.gDriveCredential = gDriveCredential;
    }

    public Credential getCredential() {
        if (gDriveCredential == null) {
            throw new NoCredentialFoundException("No credential found.");
        }
        return gDriveCredential.getCredential();
    }
}
