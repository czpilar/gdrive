package net.czpilar.gdrive.core.credential;

import com.google.api.client.auth.oauth2.Credential;

/**
 * gDrive credential interface.
 *
 * @author David Pilař (david@czpilar.net)
 */
public interface IGDriveCredential {

	/**
	 * Returns credential.
	 *
	 * @return
	 */
	Credential getCredential();

	/**
	 * Saves credential.
	 *
	 * @param credential
	 */
	void saveCredential(Credential credential);

	/**
	 * Returns upload dir.
	 *
	 * @return
	 */
	String getUploadDir();

}
