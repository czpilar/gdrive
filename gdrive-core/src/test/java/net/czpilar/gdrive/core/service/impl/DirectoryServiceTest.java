package net.czpilar.gdrive.core.service.impl;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import net.czpilar.gdrive.core.exception.DirectoryHandleException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author David Pilar (david@czpilar.net)
 */
public class DirectoryServiceTest {

    private final DirectoryService service = new DirectoryService();

    @Mock
    private DirectoryService serviceMock;

    @Mock
    private Drive drive;

    private AutoCloseable autoCloseable;

    @BeforeEach
    public void before() {
        autoCloseable = MockitoAnnotations.openMocks(this);

        when(serviceMock.getDrive()).thenReturn(drive);
    }

    @AfterEach
    public void after() throws Exception {
        autoCloseable.close();
    }

    @Test
    public void testCreateOneDirectoryWhereDirnameIsNullAndParentDirIsNull() {
        assertThrows(IllegalArgumentException.class, () -> service.createOneDirectory(null, null));
    }

    @Test
    public void testCreateOneDirectoryWhereParentDirIsNull() throws IOException {
        String dirname = "test-dirname";
        File directory = mock(File.class);
        Drive.Files files = mock(Drive.Files.class);
        Drive.Files.Create insert = mock(Drive.Files.Create.class);

        when(serviceMock.createOneDirectory(anyString(), any())).thenCallRealMethod();
        when(drive.files()).thenReturn(files);
        when(files.create(any(File.class))).thenReturn(insert);
        when(insert.execute()).thenReturn(directory);

        File result = serviceMock.createOneDirectory(dirname, null);

        assertNotNull(result);
        assertEquals(directory, result);

        verify(serviceMock).createOneDirectory(dirname, null);
        verify(serviceMock).getDrive();
        verify(drive).files();
        verify(files).create(any(File.class));
        verify(insert).execute();

        verifyNoMoreInteractions(serviceMock);
        verifyNoMoreInteractions(drive);
        verifyNoMoreInteractions(files);
        verifyNoMoreInteractions(insert);
        verifyNoInteractions(directory);
    }

    @Test
    public void testCreateOneDirectoryWhereParentDirExists() throws IOException {
        String dirname = "test-dirname";
        File parentDir = mock(File.class);
        File directory = mock(File.class);
        Drive.Files files = mock(Drive.Files.class);
        Drive.Files.Create insert = mock(Drive.Files.Create.class);

        when(serviceMock.createOneDirectory(anyString(), any(File.class))).thenCallRealMethod();
        when(drive.files()).thenReturn(files);
        when(files.create(any(File.class))).thenReturn(insert);
        when(insert.execute()).thenReturn(directory);
        when(parentDir.getId()).thenReturn("test-parent-dir-id");

        File result = serviceMock.createOneDirectory(dirname, parentDir);

        assertNotNull(result);
        assertEquals(directory, result);

        verify(serviceMock).createOneDirectory(dirname, parentDir);
        verify(serviceMock).getDrive();
        verify(drive).files();
        verify(files).create(any(File.class));
        verify(insert).execute();
        verify(parentDir).getId();

        verifyNoMoreInteractions(serviceMock);
        verifyNoMoreInteractions(drive);
        verifyNoMoreInteractions(files);
        verifyNoMoreInteractions(insert);
        verifyNoMoreInteractions(parentDir);
        verifyNoInteractions(directory);
    }

    @Test
    public void testCreateOneDirectoryWhereIOExceptionWatThrown() throws IOException {
        String dirname = "test-dirname";
        File directory = mock(File.class);
        Drive.Files files = mock(Drive.Files.class);
        Drive.Files.Create insert = mock(Drive.Files.Create.class);

        when(serviceMock.createOneDirectory(anyString(), any())).thenCallRealMethod();
        when(drive.files()).thenReturn(files);
        when(files.create(any(File.class))).thenReturn(insert);
        when(insert.execute()).thenThrow(IOException.class);

        assertThrows(DirectoryHandleException.class, () -> serviceMock.createOneDirectory(dirname, null));

        verify(serviceMock).createOneDirectory(dirname, null);
        verify(serviceMock).getDrive();
        verify(drive).files();
        verify(files).create(any(File.class));
        verify(insert).execute();

        verifyNoMoreInteractions(serviceMock);
        verifyNoMoreInteractions(drive);
        verifyNoMoreInteractions(files);
        verifyNoMoreInteractions(insert);
        verifyNoInteractions(directory);
    }

    @Test
    public void testFindOrCreateOneDirectoryWhereDirectoryIsFound() {
        File parentDir = mock(File.class);
        File directory = mock(File.class);
        String dirname = "test-dirname";

        when(serviceMock.findOrCreateOneDirectory(anyString(), any(File.class))).thenCallRealMethod();
        when(serviceMock.findFile(anyString(), any(File.class), anyBoolean())).thenReturn(directory);

        File result = serviceMock.findOrCreateOneDirectory(dirname, parentDir);

        assertNotNull(result);
        assertEquals(directory, result);

        verify(serviceMock).findOrCreateOneDirectory(dirname, parentDir);
        verify(serviceMock).findFile(dirname, parentDir, true);

        verifyNoMoreInteractions(serviceMock);

        verifyNoInteractions(parentDir);
        verifyNoInteractions(directory);
    }

    @Test
    public void testFindOrCreateOneDirectoryWhereDirectoryIsCreated() {
        File parentDir = mock(File.class);
        File directory = mock(File.class);
        String dirname = "test-dirname";

        when(serviceMock.findOrCreateOneDirectory(anyString(), any(File.class))).thenCallRealMethod();
        when(serviceMock.findFile(anyString(), any(File.class), anyBoolean())).thenReturn(null);
        when(serviceMock.createOneDirectory(anyString(), any(File.class))).thenReturn(directory);

        File result = serviceMock.findOrCreateOneDirectory(dirname, parentDir);

        assertNotNull(result);
        assertEquals(directory, result);

        verify(serviceMock).findOrCreateOneDirectory(dirname, parentDir);
        verify(serviceMock).findFile(dirname, parentDir, true);
        verify(serviceMock).createOneDirectory(dirname, parentDir);

        verifyNoMoreInteractions(serviceMock);

        verifyNoInteractions(parentDir);
        verifyNoInteractions(directory);
    }

    @Test
    public void testFindDirectoryWithPathname() {
        String pathname = "test-pathname";
        File directory = mock(File.class);

        when(serviceMock.findDirectory(anyString())).thenCallRealMethod();
        when(serviceMock.findDirectory(anyString(), any())).thenReturn(directory);

        File result = serviceMock.findDirectory(pathname);

        assertNotNull(result);
        assertEquals(directory, result);

        verify(serviceMock).findDirectory(pathname);
        verify(serviceMock).findDirectory(pathname, null);

        verifyNoMoreInteractions(serviceMock);
    }

    @Test
    public void testFindDirectoryWithPathnameAndParentWhereDirnameIsNullAndNextPathnameIsNull() {
        when(serviceMock.findDirectory(anyString(), any(File.class))).thenCallRealMethod();

        File result = serviceMock.findDirectory(null, null);

        assertNull(result);

        verify(serviceMock).findDirectory(null, null);

        verifyNoMoreInteractions(serviceMock);
    }

    @Test
    public void testFindDirectoryWithPathnameAndParentWherePathnameHasMoreDirsButCurrentDirIsNull() {
        String dirname1 = "test-dirname1";
        String dirname2 = "test-dirname2";
        String pathname = dirname1 + "/" + dirname2;
        File parentDir = mock(File.class);

        when(serviceMock.findDirectory(anyString(), any(File.class))).thenCallRealMethod();
        when(serviceMock.findFile(anyString(), any(File.class), anyBoolean())).thenReturn(null);

        File result = serviceMock.findDirectory(pathname, parentDir);

        assertNull(result);

        verify(serviceMock).findDirectory(pathname, parentDir);
        verify(serviceMock).findFile(dirname1, parentDir, true);

        verifyNoMoreInteractions(serviceMock);

        verifyNoInteractions(parentDir);
    }

    @Test
    public void testFindDirectoryWithPathnameAndParentWherePathnameHasOneDir() {
        String pathname = "test-dirname";
        File parentDir = mock(File.class);
        File directory = mock(File.class);

        when(serviceMock.findDirectory(anyString(), any(File.class))).thenCallRealMethod();
        when(serviceMock.findFile(anyString(), any(File.class), anyBoolean())).thenReturn(directory);

        File result = serviceMock.findDirectory(pathname, parentDir);

        assertNotNull(result);
        assertEquals(directory, result);

        verify(serviceMock).findDirectory(pathname, parentDir);
        verify(serviceMock).findFile(pathname, parentDir, true);

        verifyNoMoreInteractions(serviceMock);

        verifyNoInteractions(parentDir);
        verifyNoInteractions(directory);
    }

    @Test
    public void testFindDirectoryWithPathnameAndParentWherePathnameHasMoreDirs() {
        String dirname1 = "test-dirname1";
        String dirname2 = "test-dirname2";
        String dirname3 = "test-dirname3";
        String pathname = dirname1 + "/" + dirname2 + "/" + dirname3;
        File parentDir = mock(File.class);
        File directory1 = mock(File.class);
        File directory2 = mock(File.class);
        File directory3 = mock(File.class);

        when(serviceMock.findDirectory(anyString(), any(File.class))).thenCallRealMethod();
        when(serviceMock.findFile(anyString(), any(File.class), anyBoolean())).thenReturn(directory1, directory2, directory3);

        File result = serviceMock.findDirectory(pathname, parentDir);

        assertNotNull(result);
        assertEquals(directory3, result);

        verify(serviceMock).findDirectory(pathname, parentDir);
        verify(serviceMock).findDirectory(dirname2 + "/" + dirname3, directory1);
        verify(serviceMock).findDirectory(dirname3, directory2);
        verify(serviceMock).findFile(dirname1, parentDir, true);
        verify(serviceMock).findFile(dirname2, directory1, true);
        verify(serviceMock).findFile(dirname3, directory2, true);

        verifyNoMoreInteractions(serviceMock);

        verifyNoInteractions(directory1);
        verifyNoInteractions(directory2);
        verifyNoInteractions(directory3);
        verifyNoInteractions(parentDir);
    }

    @Test
    public void testFindOrCreateDirectoryWithPathname() {
        String pathname = "test-pathname";
        File directory = mock(File.class);

        when(serviceMock.findOrCreateDirectory(anyString())).thenCallRealMethod();
        when(serviceMock.findOrCreateDirectory(anyString(), any())).thenReturn(directory);

        File result = serviceMock.findOrCreateDirectory(pathname);

        assertNotNull(result);
        assertEquals(directory, result);

        verify(serviceMock).findOrCreateDirectory(pathname);
        verify(serviceMock).findOrCreateDirectory(pathname, null);

        verifyNoMoreInteractions(serviceMock);
    }

    @Test
    public void testFindOrCreateDirectoryWithPathnameAndParentWhereDirnameIsNullAndNextPathnameIsNull() {
        when(serviceMock.findOrCreateDirectory(anyString(), any(File.class))).thenCallRealMethod();

        File result = serviceMock.findOrCreateDirectory(null, null);

        assertNull(result);

        verify(serviceMock).findOrCreateDirectory(null, null);

        verifyNoMoreInteractions(serviceMock);
    }

    @Test
    public void testFindOrCreateDirectoryWithPathnameAndParentWherePathnameHasOneDir() {
        String pathname = "test-dirname";
        File parentDir = mock(File.class);
        File directory = mock(File.class);

        when(serviceMock.findOrCreateDirectory(anyString(), any(File.class))).thenCallRealMethod();
        when(serviceMock.findOrCreateOneDirectory(anyString(), any(File.class))).thenReturn(directory);

        File result = serviceMock.findOrCreateDirectory(pathname, parentDir);

        assertNotNull(result);
        assertEquals(directory, result);

        verify(serviceMock).findOrCreateDirectory(pathname, parentDir);
        verify(serviceMock).findOrCreateOneDirectory(pathname, parentDir);

        verifyNoMoreInteractions(serviceMock);

        verifyNoInteractions(directory);
        verifyNoInteractions(parentDir);
    }

    @Test
    public void testFindOrCreateDirectoryWithPathnameAndParentWherePathnameHasMoreDirs() {
        String dirname1 = "test-dirname1";
        String dirname2 = "test-dirname2";
        String dirname3 = "test-dirname3";
        String pathname = dirname1 + "/" + dirname2 + "/" + dirname3;
        File parentDir = mock(File.class);
        File directory1 = mock(File.class);
        File directory2 = mock(File.class);
        File directory3 = mock(File.class);

        when(serviceMock.findOrCreateDirectory(anyString(), any(File.class))).thenCallRealMethod();
        when(serviceMock.findOrCreateOneDirectory(anyString(), any(File.class))).thenReturn(directory1, directory2, directory3);

        File result = serviceMock.findOrCreateDirectory(pathname, parentDir);

        assertNotNull(result);
        assertEquals(directory3, result);

        verify(serviceMock).findOrCreateDirectory(pathname, parentDir);
        verify(serviceMock).findOrCreateDirectory(dirname2 + "/" + dirname3, directory1);
        verify(serviceMock).findOrCreateDirectory(dirname3, directory2);
        verify(serviceMock).findOrCreateOneDirectory(dirname1, parentDir);
        verify(serviceMock).findOrCreateOneDirectory(dirname2, directory1);
        verify(serviceMock).findOrCreateOneDirectory(dirname3, directory2);

        verifyNoMoreInteractions(serviceMock);

        verifyNoInteractions(directory1);
        verifyNoInteractions(directory2);
        verifyNoInteractions(directory3);
        verifyNoInteractions(parentDir);
    }
}
