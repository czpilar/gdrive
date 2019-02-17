package net.czpilar.gdrive.core.setting;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author David Pilar (david@czpilar.net)
 */
public class GDriveSettingTest {

    @Test
    public void testGDriveSetting() {
        String applicationName = "test-application-name";
        String applicationVersion = "test-application-version";
        String clientId = "test-client-id";
        String clientSecret = "test-client-secret";
        String redirectUri = "test-redirect-uri";
        List<String> scopes = Arrays.asList("test-scope-1", "test-scope-2");
        GDriveSetting setting = new GDriveSetting(applicationName, applicationVersion, clientId, clientSecret, redirectUri, scopes);

        assertEquals(applicationName, setting.getApplicationName());
        assertEquals(applicationVersion, setting.getApplicationVersion());
        assertEquals(clientId, setting.getClientId());
        assertEquals(clientSecret, setting.getClientSecret());
        assertEquals(redirectUri, setting.getRedirectUri());
        assertEquals(scopes, setting.getScopes());
    }
}
