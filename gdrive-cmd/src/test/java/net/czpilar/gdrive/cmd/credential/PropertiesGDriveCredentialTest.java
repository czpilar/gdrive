package net.czpilar.gdrive.cmd.credential;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author David Pilar (david@czpilar.net)
 */
public class PropertiesGDriveCredentialTest {

    private static final String UPLOAD_DIR_PROPERTY_KEY = "gdrive.uploadDir";
    private static final String ACCESS_TOKEN_PROPERTY_KEY = "accessTokenPropertyKey";
    private static final String REFRESH_TOKEN_PROPERTY_KEY = "gdrive.refreshToken";
    private static final String DEFAULT_UPLOAD_DIR = "gdrive-uploads";

    private PropertiesGDriveCredential gDrivePropertiesNotExist;
    private PropertiesGDriveCredential gDrivePropertiesExist;

    private File propertiesNotExist;
    private File propertiesExist;
    private Properties properties;

    @Before
    public void before() throws IOException {
        String tempDir = System.getProperty("java.io.tmpdir");
        propertiesNotExist = new File(tempDir + "test-properties-not-exist-file-" + System.currentTimeMillis() + ".properties");
        propertiesExist = new File(tempDir + "test-properties-exist-file-" + System.currentTimeMillis() + ".properties");
        deleteIfExist(propertiesNotExist);
        deleteIfExist(propertiesExist);

        properties = new Properties();
        properties.setProperty(UPLOAD_DIR_PROPERTY_KEY, "test-upload-dir");
        properties.setProperty(ACCESS_TOKEN_PROPERTY_KEY, "test-access-token");
        properties.setProperty(REFRESH_TOKEN_PROPERTY_KEY, "test-refresh-token");
        properties.store(new FileOutputStream(propertiesExist), "properties created in test");

        gDrivePropertiesNotExist = createGDriveCredential(propertiesNotExist.getPath());
        gDrivePropertiesExist = createGDriveCredential(propertiesExist.getPath());
    }

    @After
    public void after() {
        deleteIfExist(propertiesNotExist);
        deleteIfExist(propertiesExist);
    }

    private PropertiesGDriveCredential createGDriveCredential(String propertyFile) {
        PropertiesGDriveCredential gDriveCredential = new PropertiesGDriveCredential();
        gDriveCredential.setUploadDirPropertyKey(UPLOAD_DIR_PROPERTY_KEY);
        gDriveCredential.setAccessTokenPropertyKey(ACCESS_TOKEN_PROPERTY_KEY);
        gDriveCredential.setRefreshTokenPropertyKey(REFRESH_TOKEN_PROPERTY_KEY);
        gDriveCredential.setDefaultUploadDir(DEFAULT_UPLOAD_DIR);
        gDriveCredential.setPropertyFile(propertyFile);
        return gDriveCredential;
    }

    private void deleteIfExist(File file) {
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    public void testGetAccessTokenWherePropertiesNotExist() {
        assertNull(gDrivePropertiesNotExist.getAccessToken());
    }

    @Test
    public void testGetAccessTokenWherePropertiesExist() {
        assertEquals("test-access-token", gDrivePropertiesExist.getAccessToken());
    }

    @Test
    public void testSetAccessToken() {
        gDrivePropertiesNotExist.setAccessToken("new-access-token");
        assertEquals("new-access-token", gDrivePropertiesNotExist.getAccessToken());
    }

    @Test
    public void testGetRefreshTokenWherePropertiesNotExist() {
        assertNull(gDrivePropertiesNotExist.getRefreshToken());
    }

    @Test
    public void testGetRefreshTokenWherePropertiesExist() {
        assertEquals("test-refresh-token", gDrivePropertiesExist.getRefreshToken());
    }

    @Test
    public void testSetRefreshToken() {
        gDrivePropertiesNotExist.setRefreshToken("new-refresh-token");
        assertEquals("new-refresh-token", gDrivePropertiesNotExist.getRefreshToken());
    }

    @Test
    public void testSaveTokens() {
        gDrivePropertiesNotExist.saveTokens("new-access-token-to-save", "new-refresh-token-to-save");

        PropertiesGDriveCredential gDrivePropertiesInTest = createGDriveCredential(propertiesNotExist.getPath());

        assertEquals("new-access-token-to-save", gDrivePropertiesInTest.getAccessToken());
        assertEquals("new-refresh-token-to-save", gDrivePropertiesInTest.getRefreshToken());
    }

    @Test
    public void testGetUploadDirWherePropertiesNotExist() {
        assertEquals(DEFAULT_UPLOAD_DIR, gDrivePropertiesNotExist.getUploadDir());
    }

    @Test
    public void testGetUploadDirWherePropertiesExist() {
        assertEquals("test-upload-dir", gDrivePropertiesExist.getUploadDir());
    }

    @Test
    public void testSetUploadDir() {
        gDrivePropertiesNotExist.setUploadDir("new-upload-dir");
        assertEquals("new-upload-dir", gDrivePropertiesNotExist.getUploadDir());
    }
}
