package net.czpilar.gdrive.core.service.impl;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.drive.Drive;
import net.czpilar.gdrive.core.credential.IGDriveCredential;
import net.czpilar.gdrive.core.setting.GDriveSetting;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;

/**
 * @author David Pilar (david@czpilar.net)
 */
public class AbstractServiceTest {

    private AbstractService service;

    @Mock
    private IGDriveCredential gDriveCredential;

    @Mock
    private GDriveSetting gDriveSetting;

    @Mock
    private Credential credential;

    @Mock
    private ApplicationContext applicationContext;

    @Mock
    private Drive drive;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        service = new AbstractService() {
        };
        service.setApplicationContext(applicationContext);

        when(applicationContext.getBean(Drive.class)).thenReturn(drive);
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

    @Test
    public void testGetDrive() {
        Drive result = service.getDrive();

        assertNotNull(result);
        assertEquals(drive, result);

        verify(applicationContext).getBean(Drive.class);

        verifyNoMoreInteractions(applicationContext);
    }
}
