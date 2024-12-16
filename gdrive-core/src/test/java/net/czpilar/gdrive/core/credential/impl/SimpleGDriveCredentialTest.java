package net.czpilar.gdrive.core.credential.impl;

import com.google.api.client.auth.oauth2.Credential;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * @author David Pilar (david@czpilar.net)
 */
public class SimpleGDriveCredentialTest {

    private SimpleGDriveCredential credential;

    @Mock
    private Credential.Builder credentialBuilder;

    @BeforeEach
    public void before() {
        credential = new SimpleGDriveCredential();
        credential.setCredentialBuilder(credentialBuilder);
    }

    @Test
    public void testGetCredentialBuilder() {
        assertEquals(credentialBuilder, credential.getCredentialBuilder());
    }

    @Test
    public void testSetAndGetAccessToken() {
        assertNull(credential.getAccessToken());

        String accessToken = "test-access-token";
        credential.setAccessToken(accessToken);

        assertEquals(accessToken, credential.getAccessToken());
    }

    @Test
    public void testSetAndGetRefreshToken() {
        assertNull(credential.getRefreshToken());

        String refreshToken = "test-refresh-token";
        credential.setRefreshToken(refreshToken);

        assertEquals(refreshToken, credential.getRefreshToken());
    }

    @Test
    public void testSaveTokens() {
        assertNull(credential.getAccessToken());
        assertNull(credential.getRefreshToken());

        String accessToken = "test-access-token";
        String refreshToken = "test-refresh-token";

        credential.saveTokens(accessToken, refreshToken);

        assertEquals(accessToken, credential.getAccessToken());
        assertEquals(refreshToken, credential.getRefreshToken());
    }

    @Test
    public void testSetAndGetUploadDir() {
        assertNull(credential.getUploadDir());

        String uploadDir = "test-upload-dir";
        credential.setUploadDir(uploadDir);

        assertEquals(uploadDir, credential.getUploadDir());
    }

}
