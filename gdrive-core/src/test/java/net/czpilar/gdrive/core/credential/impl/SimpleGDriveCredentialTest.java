package net.czpilar.gdrive.core.credential.impl;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author David Pilar (david@czpilar.net)
 */
public class SimpleGDriveCredentialTest {

    private SimpleGDriveCredential credential;

    @Mock
    private GoogleCredential.Builder credentialBuilder;

    @Before
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
