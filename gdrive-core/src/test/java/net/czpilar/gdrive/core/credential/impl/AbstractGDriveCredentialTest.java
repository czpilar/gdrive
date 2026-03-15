package net.czpilar.gdrive.core.credential.impl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author David Pilar (david@czpilar.net)
 */
public class AbstractGDriveCredentialTest {

    @Test
    public void testCanBeSubclassed() {
        AbstractGDriveCredential credential = new AbstractGDriveCredential() {
            @Override
            public String getRefreshToken() {
                return null;
            }

            @Override
            public void saveRefreshToken(String refreshToken) {
            }

            @Override
            public String getUploadDir() {
                return null;
            }
        };

        assertNotNull(credential);
    }
}
