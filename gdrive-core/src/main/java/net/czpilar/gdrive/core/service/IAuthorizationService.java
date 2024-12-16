package net.czpilar.gdrive.core.service;

import com.google.api.client.auth.oauth2.Credential;

/**
 * Authorization service interface.
 *
 * @author David Pilar (david@czpilar.net)
 */
public interface IAuthorizationService {

    /**
     * Returns authorization URL to authorize application.
     *
     * @return authorization url
     */
    String getAuthorizationURL();

    /**
     * Authorize application and returns credential.
     *
     * @param authorizationCode authorization code
     * @return credential
     */
    Credential authorize(String authorizationCode);

}
