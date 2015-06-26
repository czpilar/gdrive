package net.czpilar.gdrive.core.service.impl;

import java.io.IOException;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import net.czpilar.gdrive.core.credential.IGDriveCredential;
import net.czpilar.gdrive.core.exception.TrashHandleException;
import net.czpilar.gdrive.core.util.EqualUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.context.ApplicationContext;

import static org.mockito.Mockito.*;

/**
 * @author David Pilar (david@czpilar.net)
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ File.class, Drive.Files.EmptyTrash.class })
public class TrashServiceTest {

    private TrashService service = new TrashService();

    @Mock
    private TrashService serviceMock;

    @Mock
    private ApplicationContext applicationContext;

    @Mock
    private Drive drive;

    @Mock
    private IGDriveCredential gDriveCredential;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        service.setApplicationContext(applicationContext);
        service.setGDriveCredential(gDriveCredential);

        when(applicationContext.getBean(Drive.class)).thenReturn(drive);

        PowerMockito.mockStatic(EqualUtils.class);
    }

    @Test
    public void testEmtpy() throws IOException {
        Drive.Files files = mock(Drive.Files.class);
        Drive.Files.EmptyTrash emptyTrash = PowerMockito.mock(Drive.Files.EmptyTrash.class);

        doCallRealMethod().when(serviceMock).empty();
        when(serviceMock.getDrive()).thenReturn(drive);
        when(drive.files()).thenReturn(files);
        when(files.emptyTrash()).thenReturn(emptyTrash);

        serviceMock.empty();

        verify(serviceMock).empty();
        verify(serviceMock).getDrive();
        verify(drive).files();
        verify(files).emptyTrash();
        verify(emptyTrash).execute();

        verifyNoMoreInteractions(serviceMock);
        verifyNoMoreInteractions(drive);
        verifyNoMoreInteractions(files);
        verifyNoMoreInteractions(emptyTrash);
    }

    @Test(expected = TrashHandleException.class)
    public void testEmtpyWithException() throws IOException {

        doCallRealMethod().when(serviceMock).empty();
        when(serviceMock.getDrive()).thenReturn(drive);
        when(drive.files()).thenThrow(IOException.class);

        try {
            serviceMock.empty();
        } catch (TrashHandleException e) {
            verify(serviceMock).empty();
            verify(serviceMock).getDrive();
            verify(drive).files();

            verifyNoMoreInteractions(serviceMock);
            verifyNoMoreInteractions(drive);

            throw e;
        }
    }

}