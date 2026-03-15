package net.czpilar.gdrive.core.service.impl;

import com.google.api.services.drive.Drive;
import net.czpilar.gdrive.core.credential.IGDriveCredential;
import net.czpilar.gdrive.core.setting.GDriveSetting;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

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
    private Drive drive;

    private AutoCloseable autoCloseable;

    @BeforeEach
    public void before() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        service = new AbstractService() {
        };
    }

    @AfterEach
    public void after() throws Exception {
        autoCloseable.close();
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
    public void testSetAndGetDrive() {
        assertNull(service.getDrive());

        service.setDrive(drive);

        assertEquals(drive, service.getDrive());
    }
}
