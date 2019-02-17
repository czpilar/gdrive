package net.czpilar.gdrive.core.listener;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import net.czpilar.gdrive.core.credential.IGDriveCredential;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Listener used for saving credential.
 *
 * @author David Pilar (david@czpilar.net)
 */
public class CredentialSaverListener implements AuthorizationCodeFlow.CredentialCreatedListener {

    private IGDriveCredential gDriveCredential;

    @Autowired
    public void setGDriveCredential(IGDriveCredential gDriveCredential) {
        this.gDriveCredential = gDriveCredential;
    }

    @Override
    public void onCredentialCreated(Credential credential, TokenResponse tokenResponse) {
        gDriveCredential.saveCredential(credential);
    }
}
