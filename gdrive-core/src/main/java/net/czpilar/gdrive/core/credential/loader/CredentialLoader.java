package net.czpilar.gdrive.core.credential.loader;

import com.google.api.client.auth.oauth2.Credential;
import net.czpilar.gdrive.core.credential.IGDriveCredential;
import net.czpilar.gdrive.core.exception.NoCredentialFoundException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Auto loader of gDrive credential from Spring context.
 *
 * @author David Pilař (david@czpilar.net)
 */
public class CredentialLoader {

	private IGDriveCredential gDriveCredential;

	@Autowired
	public void setGDriveCredential(IGDriveCredential gDriveCredential) {
		this.gDriveCredential = gDriveCredential;
	}

	public Credential getCredential() {
		if (gDriveCredential == null) {
			throw new NoCredentialFoundException("No credential found.");
		}
		return gDriveCredential.getCredential();
	}
}
