package net.czpilar.gdrive.core.setting;

import java.util.List;

/**
 * Holder for gDrive secrets, scopes and other info.
 *
 * @author David Pilar (david@czpilar.net)
 */
public class GDriveSetting {

    private final String applicationName;
    private final String applicationVersion;
    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;
    private final List<String> scopes;

    public GDriveSetting(String applicationName, String applicationVersion, String clientId, String clientSecret, String redirectUri, List<String> scopes) {
        this.applicationName = applicationName;
        this.applicationVersion = applicationVersion;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        this.scopes = scopes;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public String getApplicationVersion() {
        return applicationVersion;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public List<String> getScopes() {
        return scopes;
    }

}
