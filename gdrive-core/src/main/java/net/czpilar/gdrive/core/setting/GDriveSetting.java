package net.czpilar.gdrive.core.setting;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Holder for gDrive secrets, scopes and other info.
 *
 * @author David Pilar (david@czpilar.net)
 */
@Component
public class GDriveSetting {

    public static final String APPLICATION_NAME = "gdrive";
    public static final String GOOGLE_SCOPE = "https://www.googleapis.com/auth/drive";

    private final String applicationVersion;
    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;
    private final List<String> scopes;

    public GDriveSetting(@Value("${gdrive.version}") String applicationVersion,
                         @Value("${gdrive.core.drive.clientId}") String clientId,
                         @Value("${gdrive.core.drive.clientSecret}") String clientSecret,
                         @Value("${gdrive.core.drive.redirectUri}") String redirectUri) {
        this.applicationVersion = applicationVersion;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        this.scopes = List.of(GOOGLE_SCOPE);
    }

    public String getApplicationName() {
        return APPLICATION_NAME;
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
