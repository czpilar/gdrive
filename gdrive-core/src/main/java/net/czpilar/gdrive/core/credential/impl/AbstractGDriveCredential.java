package net.czpilar.gdrive.core.credential.impl;

import com.google.api.client.auth.oauth2.Credential;
import net.czpilar.gdrive.core.credential.IGDriveCredential;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Template implementation of {@link net.czpilar.gdrive.core.credential.IGDriveCredential} interface
 * for gDrive credential.
 *
 * @author David Pilar (david@czpilar.net)
 */
public abstract class AbstractGDriveCredential implements IGDriveCredential {

    private Credential.Builder credentialBuilder;

    protected Credential.Builder getCredentialBuilder() {
        return credentialBuilder;
    }

    @Autowired
    public void setCredentialBuilder(Credential.Builder credentialBuilder) {
        this.credentialBuilder = credentialBuilder;
    }

    /**
     * Returns access token.
     *
     * @return access token
     */
    protected abstract String getAccessToken();

    /**
     * Returns refresh token.
     *
     * @return refresh token
     */
    protected abstract String getRefreshToken();

    /**
     * Saves access and refresh tokens.
     *
     * @param accessToken  access token
     * @param refreshToken refresh token
     */
    protected abstract void saveTokens(String accessToken, String refreshToken);

    @Override
    public Credential getCredential() {
        return getCredentialBuilder().build()
                .setAccessToken(getAccessToken())
                .setRefreshToken(getRefreshToken());
    }

    @Override
    public void saveCredential(Credential credential) {
        saveTokens(credential.getAccessToken(), credential.getRefreshToken());
    }
}
