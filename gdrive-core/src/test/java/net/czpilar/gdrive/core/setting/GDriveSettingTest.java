package net.czpilar.gdrive.core.setting;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * @author David Pilar (david@czpilar.net)
 */
public class GDriveSettingTest {

    @Test
    public void testGDriveSetting() {
        String applicationVersion = "test-application-version";
        String clientId = "test-client-id";
        String clientSecret = "test-client-secret";
        String redirectUri = "test-redirect-uri";
        GDriveSetting setting = new GDriveSetting(applicationVersion, clientId, clientSecret, redirectUri);

        assertEquals(GDriveSetting.APPLICATION_NAME, setting.getApplicationName());
        assertEquals(applicationVersion, setting.getApplicationVersion());
        assertEquals(clientId, setting.getClientId());
        assertEquals(clientSecret, setting.getClientSecret());
        assertEquals(redirectUri, setting.getRedirectUri());
        assertEquals(Arrays.asList(GDriveSetting.GOOGLE_SCOPE), setting.getScopes());
    }
}
