package net.czpilar.gdrive.credential.impl;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author David Pilař (david@czpilar.net)
 */
public class GDrivePropertiesCredentialTest {

	private GDrivePropertiesCredential gdrivePropertiesNotExist;
	private GDrivePropertiesCredential gdrivePropertiesExist;

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
		properties.setProperty(GDrivePropertiesCredential.KEY_UPLOAD_DIR, "test-upload-dir");
		properties.setProperty(GDrivePropertiesCredential.KEY_ACCESS_TOKEN, "test-access-token");
		properties.setProperty(GDrivePropertiesCredential.KEY_REFRESH_TOKEN, "test-refresh-token");
		properties.store(new FileOutputStream(propertiesExist), "properties created in test");

		gdrivePropertiesNotExist = new GDrivePropertiesCredential(propertiesNotExist.getPath());
		gdrivePropertiesExist = new GDrivePropertiesCredential(propertiesExist.getPath());
	}

	@After
	public void after() throws IOException {
		deleteIfExist(propertiesNotExist);
		deleteIfExist(propertiesExist);
	}

	private void deleteIfExist(File file) throws IOException {
		if (file.exists()) {
			file.delete();
		}
	}

	@Test
	public void testGetAccessTokenWherePropertiesNotExist() {
		assertNull(gdrivePropertiesNotExist.getAccessToken());
	}

	@Test
	public void testGetAccessTokenWherePropertiesExist() {
		assertEquals("test-access-token", gdrivePropertiesExist.getAccessToken());
	}

	@Test
	public void testSetAccessToken() {
		gdrivePropertiesNotExist.setAccessToken("new-access-token");
		assertEquals("new-access-token", gdrivePropertiesNotExist.getAccessToken());
	}

	@Test
	public void testGetRefreshTokenWherePropertiesNotExist() {
		assertNull(gdrivePropertiesNotExist.getRefreshToken());
	}

	@Test
	public void testGetRefreshTokenWherePropertiesExist() {
		assertEquals("test-refresh-token", gdrivePropertiesExist.getRefreshToken());
	}

	@Test
	public void testSetRefreshToken() {
		gdrivePropertiesNotExist.setRefreshToken("new-refresh-token");
		assertEquals("new-refresh-token", gdrivePropertiesNotExist.getRefreshToken());
	}

	@Test
	public void testSaveTokens() {
		gdrivePropertiesNotExist.saveTokens("new-access-token-to-save", "new-refresh-token-to-save");

		GDrivePropertiesCredential gdrivePropertiesInTest = new GDrivePropertiesCredential(propertiesNotExist.getPath());
		assertEquals("new-access-token-to-save", gdrivePropertiesInTest.getAccessToken());
		assertEquals("new-refresh-token-to-save", gdrivePropertiesInTest.getRefreshToken());
	}

	@Test
	public void testGetUploadDirWherePropertiesNotExist() {
		assertEquals(GDrivePropertiesCredential.DEFAULT_UPLOAD_DIR, gdrivePropertiesNotExist.getUploadDir());
	}

	@Test
	public void testGetUploadDirWherePropertiesExist() {
		assertEquals("test-upload-dir", gdrivePropertiesExist.getUploadDir());
	}

	@Test
	public void testSetUploadDir() {
		gdrivePropertiesNotExist.setUploadDir("new-upload-dir");
		assertEquals("new-upload-dir", gdrivePropertiesNotExist.getUploadDir());
	}
}
