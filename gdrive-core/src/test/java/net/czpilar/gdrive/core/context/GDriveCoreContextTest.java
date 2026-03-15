package net.czpilar.gdrive.core.context;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.services.drive.Drive;
import net.czpilar.gdrive.core.credential.loader.CredentialLoader;
import net.czpilar.gdrive.core.setting.GDriveSetting;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GDriveCoreContextTest {

    private GDriveCoreContext context;

    @BeforeEach
    public void before() {
        GDriveSetting gDriveSetting = new GDriveSetting("1.0.0", "test-client-id", "test-client-secret", "http://127.0.0.1:8783/gdrive", 8783, "/gdrive");
        CredentialLoader credentialLoader = mock(CredentialLoader.class);
        when(credentialLoader.getRefreshToken()).thenReturn("test-refresh-token");
        AuthorizationCodeFlow.CredentialCreatedListener credentialSaverListener = mock(AuthorizationCodeFlow.CredentialCreatedListener.class);

        context = new GDriveCoreContext(gDriveSetting, credentialLoader, credentialSaverListener);
    }

    @Test
    public void testGoogleAuthorizationCodeFlow() {
        GoogleAuthorizationCodeFlow flow = context.googleAuthorizationCodeFlow();

        assertNotNull(flow);
    }

    @Test
    public void testDrive() {
        Drive drive = context.drive();

        assertNotNull(drive);
    }
}
