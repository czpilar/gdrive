package net.czpilar.gdrive.cmd.credential;

import net.czpilar.gdrive.cmd.context.GDriveCmdContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import static org.junit.Assert.assertEquals;

/**
 * @author David Pilar (david@czpilar.net)
 */
public class PropertiesGDriveCredentialTest {

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
        properties.setProperty(GDriveCmdContext.UPLOAD_DIR_PROPERTY_KEY, "test-upload-dir");
        properties.setProperty(GDriveCmdContext.ACCESS_TOKEN_PROPERTY_KEY, "test-access-token");
        properties.setProperty(GDriveCmdContext.REFRESH_TOKEN_PROPERTY_KEY, "test-refresh-token");
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
        PropertiesGDriveCredential gDriveCredential = new PropertiesGDriveCredential(
                GDriveCmdContext.UPLOAD_DIR_PROPERTY_KEY, GDriveCmdContext.ACCESS_TOKEN_PROPERTY_KEY,
                GDriveCmdContext.REFRESH_TOKEN_PROPERTY_KEY, GDriveCmdContext.DEFAULT_UPLOAD_DIR);
        gDriveCredential.setPropertyFile(propertyFile);
        return gDriveCredential;
    }

    private void deleteIfExist(File file) {
        if (file.exists()) {
            file.delete();
        }
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
        assertEquals(GDriveCmdContext.DEFAULT_UPLOAD_DIR, gDrivePropertiesNotExist.getUploadDir());
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
