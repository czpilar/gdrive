package net.czpilar.gdrive.service;

import com.google.api.client.auth.oauth2.Credential;

/**
 * @author David Pilař (david@czpilar.net)
 */
public interface IGDriveAuthorizationService {

	/**
	 * Returns authorization URL to authorize application.
	 *
	 * @return
	 */
	String getAuthorizationURL();

	/**
	 * Authorize application and returns credential.
	 *
	 * @param authorizationCode
	 * @return
	 */
	Credential authorize(String authorizationCode);

}
