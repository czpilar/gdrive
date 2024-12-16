package net.czpilar.gdrive.core.listener;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import net.czpilar.gdrive.core.credential.IGDriveCredential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Listener used for saving credential.
 *
 * @author David Pilar (david@czpilar.net)
 */
@Component
public class CredentialSaverListener implements AuthorizationCodeFlow.CredentialCreatedListener {

    private final IGDriveCredential gDriveCredential;

    @Autowired
    public CredentialSaverListener(IGDriveCredential gDriveCredential) {
        this.gDriveCredential = gDriveCredential;
    }

    @Override
    public void onCredentialCreated(Credential credential, TokenResponse tokenResponse) {
        gDriveCredential.saveCredential(credential);
    }
}
