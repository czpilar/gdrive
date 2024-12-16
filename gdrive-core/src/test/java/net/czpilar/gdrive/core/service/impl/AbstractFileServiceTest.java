package net.czpilar.gdrive.core.service.impl;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import net.czpilar.gdrive.core.exception.FileHandleException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AbstractFileServiceTest {

    @Mock
    private AbstractFileService service;

    @Mock
    private Drive drive;

    private AutoCloseable autoCloseable;

    @BeforeEach
    public void before() {
        autoCloseable = MockitoAnnotations.openMocks(this);

        when(service.getDrive()).thenReturn(drive);
    }

    @AfterEach
    public void after() throws Exception {
        autoCloseable.close();
    }

    @Test
    public void testBuildQueryWhereFilenameIsNullAndParentIsNullAndIsFile() {
        when(service.buildQuery(any(), any(), anyBoolean())).thenCallRealMethod();

        assertThrows(IllegalArgumentException.class, () -> service.buildQuery(null, null, false));

        verify(service).buildQuery(any(), any(), anyBoolean());
        verifyNoMoreInteractions(service);
    }

    @Test
    public void testBuildQueryWhereParentIsNullAndMimeTypeAndIsFile() {
        String filename = "test-filename";
        when(service.buildQuery(anyString(), any(), anyBoolean())).thenCallRealMethod();

        String result = service.buildQuery(filename, null, false);

        assertNotNull(result);
        assertEquals("name = '" + filename + "' and 'root' in parents and trashed = false and mimeType != 'application/vnd.google-apps.folder'", result);

        verify(service).buildQuery(filename, null, false);
        verifyNoMoreInteractions(service);
    }

    @Test
    public void testBuildQueryWhereParentIsNullAndIsDir() {
        String filename = "test-filename";
        when(service.buildQuery(anyString(), any(), anyBoolean())).thenCallRealMethod();

        String result = service.buildQuery(filename, null, true);

        assertNotNull(result);
        assertEquals("name = '" + filename + "' and 'root' in parents and trashed = false and mimeType = 'application/vnd.google-apps.folder'", result);

        verify(service).buildQuery(filename, null, true);
        verifyNoMoreInteractions(service);
    }

    @Test
    public void testBuildQueryWhereParentExistsAndIsFile() {
        File parent = mock(File.class);
        String parentId = "test-id-parent-dir";
        String filename = "test-filename";
        when(service.buildQuery(anyString(), any(File.class), anyBoolean())).thenCallRealMethod();
        when(parent.getId()).thenReturn(parentId);

        String result = service.buildQuery(filename, parent, false);

        assertNotNull(result);
        assertEquals("name = '" + filename + "' and '" + parentId + "' in parents and trashed = false and mimeType != 'application/vnd.google-apps.folder'", result);

        verify(parent).getId();
        verify(service).buildQuery(filename, parent, false);

        verifyNoMoreInteractions(service);
        verifyNoMoreInteractions(parent);
    }

    @Test
    public void testBuildQueryWhereParentExistsAndIsDir() {
        File parent = mock(File.class);
        String parentId = "test-id-parent-dir";
        String filename = "test-filename";
        when(service.buildQuery(anyString(), any(File.class), anyBoolean())).thenCallRealMethod();
        when(parent.getId()).thenReturn(parentId);

        String result = service.buildQuery(filename, parent, true);

        assertNotNull(result);
        assertEquals("name = '" + filename + "' and '" + parentId + "' in parents and trashed = false and mimeType = 'application/vnd.google-apps.folder'", result);

        verify(parent).getId();
        verify(service).buildQuery(filename, parent, true);

        verifyNoMoreInteractions(service);
        verifyNoMoreInteractions(parent);
    }

    @Test
    public void testBuildQueryWhereFilenameNeedsEscaping() {
        when(service.buildQuery(anyString(), any(), anyBoolean())).thenCallRealMethod();

        String filename = "test ' test \\' test \\\\' test \\\\\\' test \\\\\\\\' test \\\\\\\\\\' test";
        String result = service.buildQuery(filename, null, false);

        assertNotNull(result);
        assertEquals("name = 'test \\' test \\' test \\\\\\' test \\\\\\' test \\\\\\\\\\' test \\\\\\\\\\' test' and 'root' in parents and trashed = false and mimeType != 'application/vnd.google-apps.folder'", result);

        verify(service).buildQuery(filename, null, false);

        verifyNoMoreInteractions(service);
    }

    @Test
    public void testBuildQueryWhereFilenameDontNeedEscaping() {
        when(service.buildQuery(anyString(), any(), anyBoolean())).thenCallRealMethod();
        String filename = "test \" test \" test";

        String result = service.buildQuery(filename, null, false);

        assertNotNull(result);
        assertEquals("name = 'test \" test \" test' and 'root' in parents and trashed = false and mimeType != 'application/vnd.google-apps.folder'", result);

        verify(service).buildQuery(filename, null, false);

        verifyNoMoreInteractions(service);
    }

    @Test
    public void testFindFileWhereDirnameIsNull() {
        File parent = mock(File.class);

        when(service.findFile(any(), any(File.class), anyBoolean())).thenCallRealMethod();

        assertThrows(IllegalArgumentException.class, () -> service.findFile(null, parent, true));

        verify(service).findFile(null, parent, true);
        verifyNoMoreInteractions(service);
    }

    @Test
    public void testFindFileWhereNoItemsFound() throws IOException {
        File parent = mock(File.class);
        Drive.Files files = mock(Drive.Files.class);
        Drive.Files.List list = mock(Drive.Files.List.class);
        FileList fileList = mock(FileList.class);
        String query = "test-query-string";
        String filename = "test-filename";
        String fields = "files(*)";

        when(service.findFile(anyString(), any(File.class), anyBoolean())).thenCallRealMethod();
        when(service.buildQuery(anyString(), any(File.class), anyBoolean())).thenReturn(query);
        when(drive.files()).thenReturn(files);
        when(files.list()).thenReturn(list);
        when(list.setQ(anyString())).thenReturn(list);
        when(list.setFields(fields)).thenReturn(list);
        when(list.execute()).thenReturn(fileList);
        when(fileList.getFiles()).thenReturn(null);

        File result = service.findFile(filename, parent, false);

        assertNull(result);

        verify(service).findFile(filename, parent, false);
        verify(service).getDrive();
        verify(service).buildQuery(filename, parent, false);
        verify(drive).files();
        verify(files).list();
        verify(list).setQ(query);
        verify(list).setFields(fields);
        verify(list).execute();
        verify(fileList).getFiles();

        verifyNoMoreInteractions(service);
        verifyNoMoreInteractions(drive);
        verifyNoMoreInteractions(drive);
        verifyNoMoreInteractions(files);
        verifyNoMoreInteractions(list);
        verifyNoMoreInteractions(fileList);
        verifyNoInteractions(parent);
    }

    @Test
    public void testFindFileWhereEmptyItemsFound() throws IOException {
        File parent = mock(File.class);
        Drive.Files files = mock(Drive.Files.class);
        Drive.Files.List list = mock(Drive.Files.List.class);
        FileList fileList = mock(FileList.class);
        String query = "test-query-string";
        String filename = "test-filename";
        String fields = "files(*)";

        when(service.findFile(anyString(), any(File.class), anyBoolean())).thenCallRealMethod();
        when(service.buildQuery(anyString(), any(File.class), anyBoolean())).thenReturn(query);
        when(drive.files()).thenReturn(files);
        when(files.list()).thenReturn(list);
        when(list.setQ(anyString())).thenReturn(list);
        when(list.setFields(fields)).thenReturn(list);
        when(list.execute()).thenReturn(fileList);
        when(fileList.getFiles()).thenReturn(new ArrayList<>());

        File result = service.findFile(filename, parent, false);

        assertNull(result);

        verify(service).findFile(filename, parent, false);
        verify(service).getDrive();
        verify(service).buildQuery(filename, parent, false);
        verify(drive).files();
        verify(files).list();
        verify(list).setQ(query);
        verify(list).setFields(fields);
        verify(list).execute();
        verify(fileList).getFiles();

        verifyNoMoreInteractions(service);
        verifyNoMoreInteractions(drive);
        verifyNoMoreInteractions(drive);
        verifyNoMoreInteractions(files);
        verifyNoMoreInteractions(list);
        verifyNoMoreInteractions(fileList);
        verifyNoInteractions(parent);
    }

    @Test
    public void testFindFileWhereMoreThanOneItemsFound() throws IOException {
        File parent = mock(File.class);
        Drive.Files files = mock(Drive.Files.class);
        Drive.Files.List list = mock(Drive.Files.List.class);
        FileList fileList = mock(FileList.class);
        String query = "test-query-string";
        String filename = "test-filename";
        String fields = "files(*)";
        File dir1 = mock(File.class);
        File dir2 = mock(File.class);

        when(service.findFile(anyString(), any(File.class), anyBoolean())).thenCallRealMethod();
        when(service.buildQuery(anyString(), any(File.class), anyBoolean())).thenReturn(query);
        when(drive.files()).thenReturn(files);
        when(files.list()).thenReturn(list);
        when(list.setQ(anyString())).thenReturn(list);
        when(list.setFields(fields)).thenReturn(list);
        when(list.execute()).thenReturn(fileList);
        when(fileList.getFiles()).thenReturn(Arrays.asList(dir1, dir2));

        assertThrows(FileHandleException.class, () -> service.findFile(filename, parent, false));

        verify(service).findFile(filename, parent, false);
        verify(service).getDrive();
        verify(service).buildQuery(filename, parent, false);
        verify(drive).files();
        verify(files).list();
        verify(list).setQ(query);
        verify(list).setFields(fields);
        verify(list).execute();
        verify(fileList).getFiles();

        verifyNoMoreInteractions(service);
        verifyNoMoreInteractions(drive);
        verifyNoMoreInteractions(drive);
        verifyNoMoreInteractions(files);
        verifyNoMoreInteractions(list);
        verifyNoMoreInteractions(fileList);
        verifyNoInteractions(parent);
        verifyNoInteractions(dir1);
        verifyNoInteractions(dir2);
    }

    @Test
    public void testFindFileWhereIOExceptionWasThrown() throws IOException {
        File parent = mock(File.class);
        Drive.Files files = mock(Drive.Files.class);
        Drive.Files.List list = mock(Drive.Files.List.class);
        String query = "test-query-string";
        String filename = "test-filename";
        String fields = "files(*)";

        when(service.findFile(anyString(), any(File.class), anyBoolean())).thenCallRealMethod();
        when(service.buildQuery(anyString(), any(File.class), anyBoolean())).thenReturn(query);
        when(drive.files()).thenReturn(files);
        when(files.list()).thenReturn(list);
        when(list.setQ(anyString())).thenReturn(list);
        when(list.setFields(fields)).thenReturn(list);
        when(list.execute()).thenThrow(IOException.class);

        assertThrows(FileHandleException.class, () -> service.findFile(filename, parent, false));

        verify(service).findFile(filename, parent, false);
        verify(service).getDrive();
        verify(service).buildQuery(filename, parent, false);
        verify(drive).files();
        verify(files).list();
        verify(list).setQ(query);
        verify(list).setFields(fields);
        verify(list).execute();

        verifyNoMoreInteractions(service);
        verifyNoMoreInteractions(drive);
        verifyNoMoreInteractions(drive);
        verifyNoMoreInteractions(files);
        verifyNoMoreInteractions(list);
        verifyNoInteractions(parent);
    }

    @Test
    public void testFindFile() throws IOException {
        File parent = mock(File.class);
        Drive.Files files = mock(Drive.Files.class);
        Drive.Files.List list = mock(Drive.Files.List.class);
        FileList fileList = mock(FileList.class);
        File directory = mock(File.class);
        String query = "test-query-string";
        String filename = "test-filename";
        String fields = "files(*)";

        when(service.findFile(anyString(), any(File.class), anyBoolean())).thenCallRealMethod();
        when(service.buildQuery(anyString(), any(File.class), anyBoolean())).thenReturn(query);
        when(drive.files()).thenReturn(files);
        when(files.list()).thenReturn(list);
        when(list.setQ(anyString())).thenReturn(list);
        when(list.setFields(fields)).thenReturn(list);
        when(list.execute()).thenReturn(fileList);
        when(fileList.getFiles()).thenReturn(List.of(directory));

        File result = service.findFile(filename, parent, false);

        assertNotNull(result);
        assertEquals(directory, result);

        verify(service).findFile(filename, parent, false);
        verify(service).getDrive();
        verify(service).buildQuery(filename, parent, false);
        verify(drive).files();
        verify(files).list();
        verify(list).setQ(query);
        verify(list).setFields(fields);
        verify(list).execute();
        verify(fileList).getFiles();

        verifyNoMoreInteractions(service);
        verifyNoMoreInteractions(drive);
        verifyNoMoreInteractions(drive);
        verifyNoMoreInteractions(files);
        verifyNoMoreInteractions(list);
        verifyNoMoreInteractions(fileList);
        verifyNoInteractions(parent);
        verifyNoInteractions(directory);
    }

}