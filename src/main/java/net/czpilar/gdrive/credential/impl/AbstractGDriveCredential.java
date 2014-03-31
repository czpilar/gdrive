package net.czpilar.gdrive.credential.impl;

import static net.czpilar.gdrive.constant.GDriveConstants.*;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import net.czpilar.gdrive.credential.IGDriveCredential;

/**
 * @author David Pilař (david@czpilar.net)
 */
public abstract class AbstractGDriveCredential implements IGDriveCredential {

	private Credential credential;

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
		if (credential == null) {
			credential = new GoogleCredential.Builder()
					.setJsonFactory(new JacksonFactory())
					.setTransport(new NetHttpTransport())
					.setClientSecrets(CLIENT_ID, CLIENT_SECRET)
					.build();
			credential.setAccessToken(getAccessToken())
					.setRefreshToken(getRefreshToken());
		}
		return credential;
	}

	@Override
	public void saveCredential(Credential credential) {
		saveTokens(credential.getAccessToken(), credential.getRefreshToken());
		this.credential = credential;
	}
}
