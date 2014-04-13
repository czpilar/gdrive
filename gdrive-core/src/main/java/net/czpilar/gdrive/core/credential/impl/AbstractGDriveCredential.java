package net.czpilar.gdrive.core.credential.impl;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import net.czpilar.gdrive.core.credential.IGDriveCredential;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Template implementation of {@link net.czpilar.gdrive.core.credential.IGDriveCredential} interface
 * for gDrive credential.
 *
 * @author David Pilař (david@czpilar.net)
 */
public abstract class AbstractGDriveCredential implements IGDriveCredential {

	private GoogleCredential.Builder credentialBuilder;

	protected GoogleCredential.Builder getCredentialBuilder() {
		return credentialBuilder;
	}

	@Autowired
	public void setCredentialBuilder(GoogleCredential.Builder credentialBuilder) {
		this.credentialBuilder = credentialBuilder;
	}

	/**
	 * Returns access token.
	 *
	 * @return
	 */
	protected abstract String getAccessToken();

	/**
	 * Returns refresh token.
	 *
	 * @return
	 */
	protected abstract String getRefreshToken();

	/**
	 * Saves access and refresh tokens.
	 *
	 * @param accessToken
	 * @param refreshToken
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
