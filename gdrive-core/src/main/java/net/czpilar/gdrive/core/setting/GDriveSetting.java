package net.czpilar.gdrive.core.setting;

import com.google.api.services.drive.DriveScopes;
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
    public static final String TOKEN_URL = "https://oauth2.googleapis.com/token";

    private final String applicationVersion;
    private final String clientId;
    private final String clientSecret;
    private final int redirectUriPort;
    private final String redirectUriContext;
    private final List<String> scopes;

    public GDriveSetting(@Value("${gdrive.version}") String applicationVersion,
                         @Value("${gdrive.core.drive.clientId}") String clientId,
                         @Value("${gdrive.core.drive.clientSecret}") String clientSecret,
                         @Value("${gdrive.core.drive.redirectUri.port:8783}") int redirectUriPort,
                         @Value("${gdrive.core.drive.redirectUri.context:/gdrive}") String redirectUriContext) {
        this.applicationVersion = applicationVersion;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUriPort = redirectUriPort;
        this.redirectUriContext = redirectUriContext;
        this.scopes = List.of(DriveScopes.DRIVE);
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

    public int getRedirectUriPort() {
        return redirectUriPort;
    }

    public String getRedirectUriContext() {
        return redirectUriContext;
    }

    public String getRedirectUri() {
        return "http://127.0.0.1:" + redirectUriPort + redirectUriContext;
    }

    public List<String> getScopes() {
        return scopes;
    }

}
