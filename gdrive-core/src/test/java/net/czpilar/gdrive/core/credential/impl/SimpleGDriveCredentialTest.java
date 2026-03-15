package net.czpilar.gdrive.core.credential.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * @author David Pilar (david@czpilar.net)
 */
class SimpleGDriveCredentialTest {

    private SimpleGDriveCredential credential;

    @BeforeEach
    void before() {
        credential = new SimpleGDriveCredential();
    }

    @Test
    void testSetAndGetRefreshToken() {
        assertNull(credential.getRefreshToken());

        String refreshToken = "test-refresh-token";
        credential.setRefreshToken(refreshToken);

        assertEquals(refreshToken, credential.getRefreshToken());
    }

    @Test
    void testSaveRefreshToken() {
        assertNull(credential.getRefreshToken());

        String refreshToken = "test-refresh-token";
        credential.saveRefreshToken(refreshToken);

        assertEquals(refreshToken, credential.getRefreshToken());
    }

    @Test
    void testSetAndGetUploadDir() {
        assertNull(credential.getUploadDir());

        String uploadDir = "test-upload-dir";
        credential.setUploadDir(uploadDir);

        assertEquals(uploadDir, credential.getUploadDir());
    }

}
