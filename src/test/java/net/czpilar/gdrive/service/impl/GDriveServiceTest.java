package net.czpilar.gdrive.service.impl;

import static org.junit.Assert.*;

import net.czpilar.gdrive.credential.IGDriveCredential;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * @author David Pilař (david@czpilar.net)
 */
public class GDriveServiceTest {

	private GDriveService service;

	@Mock
	private IGDriveCredential gdriveCredential;

	@Before
	public void before() {
		MockitoAnnotations.initMocks(this);

		service = new GDriveService(gdriveCredential);
	}

	@Test
	public void testGetAuthorizationURL() {
		assertEquals("https://accounts.google.com/o/oauth2/auth?client_id=585450804861-sel1fskbqiavu108osfbkjb53farnnea.apps.googleusercontent.com&redirect_uri=urn:ietf:wg:oauth:2.0:oob&response_type=code&scope=https://www.googleapis.com/auth/drive",
				service.getAuthorizationURL());

	}
}
