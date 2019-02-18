package net.czpilar.gdrive.core.service.impl;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import net.czpilar.gdrive.core.exception.AuthorizationFailedException;
import net.czpilar.gdrive.core.service.IAuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Authorization service with methods for authorization to Google Drive.
 *
 * @author David Pilar (david@czpilar.net)
 */
@Service
public class AuthorizationService extends AbstractService implements IAuthorizationService {

    private GoogleAuthorizationCodeFlow authorizationCodeFlow;

    @Autowired
    public void setAuthorizationCodeFlow(GoogleAuthorizationCodeFlow authorizationCodeFlow) {
        this.authorizationCodeFlow = authorizationCodeFlow;
    }

    @Override
    public String getAuthorizationURL() {
        return authorizationCodeFlow.newAuthorizationUrl()
                .setRedirectUri(getGDriveSetting().getRedirectUri()).build();
    }

    @Override
    public Credential authorize(String authorizationCode) {
        try {
            GoogleTokenResponse response = authorizationCodeFlow
                    .newTokenRequest(authorizationCode)
                    .setRedirectUri(getGDriveSetting().getRedirectUri()).execute();
            return authorizationCodeFlow.createAndStoreCredential(response, null);
        } catch (IOException e) {
            throw new AuthorizationFailedException("Error occurs during authorization process.", e);
        }
    }

}
