package net.czpilar.gdrive.core.credential;

import com.google.api.client.auth.oauth2.Credential;

/**
 * gDrive credential interface.
 *
 * @author David Pilar (david@czpilar.net)
 */
public interface IGDriveCredential {

    /**
     * Returns credential.
     *
     * @return credential
     */
    Credential getCredential();

    /**
     * Saves credential.
     *
     * @param credential credential
     */
    void saveCredential(Credential credential);

    /**
     * Returns upload dir.
     *
     * @return upload directory
     */
    String getUploadDir();

}
