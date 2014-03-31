package net.czpilar.gdrive.service.impl;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.api.client.auth.oauth2.Credential;
import net.czpilar.gdrive.credential.IGDriveCredential;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * @author David Pilař (david@czpilar.net)
 */
public class AbstractGDriveServiceTest {

	private AbstractGDriveService service;

	@Mock
	private IGDriveCredential gdriveCredential;

	@Mock
	private Credential credential;

	@Before
	public void before() {
		MockitoAnnotations.initMocks(this);
		when(gdriveCredential.getCredential()).thenReturn(credential);

		service = new AbstractGDriveService(gdriveCredential) {
		};
	}

	@Test
	public void testGetFlow() {
		assertNotNull(service.getFlow());

		verifyZeroInteractions(gdriveCredential);
		verifyZeroInteractions(credential);
	}

	@Test
	public void testGetDrive() {
		assertNotNull(service.getDrive());

		verify(gdriveCredential).getCredential();

		verifyNoMoreInteractions(gdriveCredential);
		verifyZeroInteractions(credential);
	}

	@Test
	public void testGetGdriveCredential() {
		assertEquals(gdriveCredential, service.getGdriveCredential());

		verifyZeroInteractions(gdriveCredential);
		verifyZeroInteractions(credential);
	}

	@Test
	public void testGetCredential() {
		assertEquals(credential, service.getCredential());

		verify(gdriveCredential).getCredential();

		verifyNoMoreInteractions(gdriveCredential);
		verifyZeroInteractions(credential);
	}

	@Test
	public void testGetUploadDirForDefaultUploadDir() {
		String expected = "test-default-upload-dir";

		when(gdriveCredential.getUploadDir()).thenReturn(expected);

		assertEquals(expected, service.getUploadDir(null));

		verify(gdriveCredential).getUploadDir();

		verifyNoMoreInteractions(gdriveCredential);
		verifyZeroInteractions(credential);
	}

	@Test
	public void testGetUploadDir() {
		String expected = "sent-upload-dir";

		assertEquals(expected, service.getUploadDir(expected));

		verifyZeroInteractions(gdriveCredential);
		verifyZeroInteractions(credential);
	}
}
