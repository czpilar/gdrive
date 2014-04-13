package net.czpilar.gdrive.core.credential.impl;

/**
 * Simple implementation of {@link net.czpilar.gdrive.core.credential.IGDriveCredential} interface
 * for gDrive credential.
 *
 * @author David Pilař (david@czpilar.net)
 */
public class SimpleGDriveCredential extends AbstractGDriveCredential {

	private String accessToken;
	private String refreshToken;
	private String uploadDir;

	@Override
	protected String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	@Override
	protected String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	@Override
	protected void saveTokens(String accessToken, String refreshToken) {
		setAccessToken(accessToken);
		setRefreshToken(refreshToken);
	}

	@Override
	public String getUploadDir() {
		return uploadDir;
	}

	public void setUploadDir(String uploadDir) {
		this.uploadDir = uploadDir;
	}
}
