package net.czpilar.gdrive.core.setting;

import com.google.api.services.drive.DriveScopes;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author David Pilar (david@czpilar.net)
 */
public class GDriveSettingTest {

    @Test
    public void testGDriveSetting() {
        String applicationVersion = "test-application-version";
        String clientId = "test-client-id";
        String clientSecret = "test-client-secret";
        String redirectUri = "http://127.0.0.1:8783/gdrive";
        int redirectUriPort = 8783;
        String redirectUriContext = "/gdrive";
        GDriveSetting setting = new GDriveSetting(applicationVersion, clientId, clientSecret, redirectUri, redirectUriPort, redirectUriContext);

        assertEquals(GDriveSetting.APPLICATION_NAME, setting.getApplicationName());
        assertEquals(applicationVersion, setting.getApplicationVersion());
        assertEquals(clientId, setting.getClientId());
        assertEquals(clientSecret, setting.getClientSecret());
        assertEquals(redirectUriPort, setting.getRedirectUriPort());
        assertEquals(redirectUriContext, setting.getRedirectUriContext());
        assertEquals(redirectUri, setting.getRedirectUri());
        assertEquals(List.of(DriveScopes.DRIVE), setting.getScopes());
    }
}
