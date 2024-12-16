package net.czpilar.gdrive.core.service.impl;

import com.google.api.services.drive.Drive;
import net.czpilar.gdrive.core.credential.IGDriveCredential;
import net.czpilar.gdrive.core.exception.TrashHandleException;
import net.czpilar.gdrive.core.util.EqualUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * @author David Pilar (david@czpilar.net)
 */
public class TrashServiceTest {

    @Mock
    private TrashService serviceMock;

    @Mock
    private ApplicationContext applicationContext;

    @Mock
    private Drive drive;

    @Mock
    private IGDriveCredential gDriveCredential;

    private AutoCloseable autoCloseable;

    private MockedStatic<EqualUtils> equalUtilsMockedStatic;

    @BeforeEach
    public void before() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        when(applicationContext.getBean(Drive.class)).thenReturn(drive);
        equalUtilsMockedStatic = mockStatic(EqualUtils.class);
    }

    @AfterEach
    public void after() throws Exception {
        autoCloseable.close();
        equalUtilsMockedStatic.close();
    }

    @Test
    public void testEmpty() throws IOException {
        Drive.Files files = mock(Drive.Files.class);
        Drive.Files.EmptyTrash emptyTrash = mock(Drive.Files.EmptyTrash.class);

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

    @Test
    public void testEmptyWithException() {
        doCallRealMethod().when(serviceMock).empty();
        when(serviceMock.getDrive()).thenReturn(drive);
        when(drive.files()).thenAnswer(invocationOnMock -> {
            throw new IOException("there is something wrong");
        });

        assertThrows(TrashHandleException.class, () -> serviceMock.empty());

        verify(serviceMock).empty();
        verify(serviceMock).getDrive();
        verify(drive).files();

        verifyNoMoreInteractions(serviceMock);
        verifyNoMoreInteractions(drive);
    }

}