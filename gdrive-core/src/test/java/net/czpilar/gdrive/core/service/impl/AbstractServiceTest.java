package net.czpilar.gdrive.core.service.impl;

import static org.junit.Assert.*;

import com.google.api.client.auth.oauth2.Credential;
import net.czpilar.gdrive.core.credential.IGDriveCredential;
import net.czpilar.gdrive.core.setting.GDriveSetting;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * @author David Pilař (david@czpilar.net)
 */
public class AbstractServiceTest {

	private AbstractService service;

	@Mock
	private IGDriveCredential gDriveCredential;

	@Mock
	private GDriveSetting gDriveSetting;

	@Mock
	private Credential credential;

	@Before
	public void before() {
		MockitoAnnotations.initMocks(this);
		service = new AbstractService() {
		};
	}

	@Test
	public void testSetAndGetGDriveSetting() {
		assertNull(service.getGDriveSetting());

		service.setGDriveSetting(gDriveSetting);

		assertEquals(gDriveSetting, service.getGDriveSetting());
	}

	@Test
	public void testSetAndGetGDriveCredential() {
		assertNull(service.getGDriveCredential());

		service.setGDriveCredential(gDriveCredential);

		assertEquals(gDriveCredential, service.getGDriveCredential());
	}
}
