package net.czpilar.gdrive.core.service.impl;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import net.czpilar.gdrive.core.exception.DirectoryHandleException;
import net.czpilar.gdrive.core.exception.FileHandleException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ File.class, Files.class, FileList.class })
public class AbstractFileServiceTest {

    @Mock
    private AbstractFileService service;

    @Mock
    private Drive drive;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);

        when(service.getDrive()).thenReturn(drive);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildQueryWhereFilenameIsNullAndParentIsNullAndIsFile() {
        when(service.buildQuery(anyString(), any(File.class), anyBoolean())).thenCallRealMethod();

        try {
            service.buildQuery(null, null, false);
        } catch (IllegalArgumentException e) {

            verify(service).buildQuery(anyString(), any(File.class), anyBoolean());
            verifyNoMoreInteractions(service);

            throw e;
        }
    }

    @Test
    public void testBuildQueryWhereParentIsNullAndMimeTypeAndIsFile() {
        String filename = "test-filename";
        when(service.buildQuery(anyString(), any(File.class), anyBoolean())).thenCallRealMethod();

        String result = service.buildQuery(filename, null, false);

        assertNotNull(result);
        assertEquals("title='" + filename + "' and 'root' in parents and trashed = false and mimeType != 'application/vnd.google-apps.folder'", result);

        verify(service).buildQuery(filename, null, false);
        verifyNoMoreInteractions(service);
    }

    @Test
    public void testBuildQueryWhereParentIsNullAndIsDir() {
        String filename = "test-filename";
        when(service.buildQuery(anyString(), any(File.class), anyBoolean())).thenCallRealMethod();

        String result = service.buildQuery(filename, null, true);

        assertNotNull(result);
        assertEquals("title='" + filename + "' and 'root' in parents and trashed = false and mimeType = 'application/vnd.google-apps.folder'", result);

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
        assertEquals("title='" + filename + "' and '" + parentId + "' in parents and trashed = false and mimeType != 'application/vnd.google-apps.folder'", result);

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
        assertEquals("title='" + filename + "' and '" + parentId + "' in parents and trashed = false and mimeType = 'application/vnd.google-apps.folder'", result);

        verify(parent).getId();
        verify(service).buildQuery(filename, parent, true);

        verifyNoMoreInteractions(service);
        verifyNoMoreInteractions(parent);
    }

    @Test
    public void testBuildQueryWhereFilenameNeedsEscaping() {
        when(service.buildQuery(anyString(), any(File.class), anyBoolean())).thenCallRealMethod();

        String filename = "test ' test \\' test \\\\' test \\\\\\' test \\\\\\\\' test \\\\\\\\\\' test";
        String result = service.buildQuery(filename, null, false);

        assertNotNull(result);
        assertEquals("title='test \\' test \\' test \\\\\\' test \\\\\\' test \\\\\\\\\\' test \\\\\\\\\\' test' and 'root' in parents and trashed = false and mimeType != 'application/vnd.google-apps.folder'", result);

        verify(service).buildQuery(filename, null, false);

        verifyNoMoreInteractions(service);
    }

    @Test
    public void testBuildQueryWhereFilenameDontNeedEscaping() {
        when(service.buildQuery(anyString(), any(File.class), anyBoolean())).thenCallRealMethod();
        String filename = "test \" test \" test";

        String result = service.buildQuery(filename, null, false);

        assertNotNull(result);
        assertEquals("title='test \" test \" test' and 'root' in parents and trashed = false and mimeType != 'application/vnd.google-apps.folder'", result);

        verify(service).buildQuery(filename, null, false);

        verifyNoMoreInteractions(service);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindFileWhereDirnameIsNull() {
        File parent = mock(File.class);

        when(service.findFile(anyString(), any(File.class), anyBoolean())).thenCallRealMethod();

        try {
            service.findFile(null, parent, true);
        } catch (IllegalArgumentException e) {
            verify(service).findFile(null, parent, true);
            verifyNoMoreInteractions(service);

            throw e;
        }
    }

    @Test
    public void testFindFileWhereNoItemsFound() throws IOException {
        File parent = mock(File.class);
        Drive.Files files = mock(Drive.Files.class);
        Drive.Files.List list = mock(Drive.Files.List.class);
        FileList fileList = mock(FileList.class);
        String query = "test-query-string";
        String filename = "test-filename";

        when(service.findFile(anyString(), any(File.class), anyBoolean())).thenCallRealMethod();
        when(service.buildQuery(anyString(), any(File.class), anyBoolean())).thenReturn(query);
        when(drive.files()).thenReturn(files);
        when(files.list()).thenReturn(list);
        when(list.setQ(anyString())).thenReturn(list);
        when(list.execute()).thenReturn(fileList);
        when(fileList.getItems()).thenReturn(null);

        File result = service.findFile(filename, parent, false);

        assertNull(result);

        verify(service).findFile(filename, parent, false);
        verify(service).getDrive();
        verify(service).buildQuery(filename, parent, false);
        verify(drive).files();
        verify(files).list();
        verify(list).setQ(query);
        verify(list).execute();
        verify(fileList).getItems();

        verifyNoMoreInteractions(service);
        verifyNoMoreInteractions(drive);
        verifyNoMoreInteractions(drive);
        verifyNoMoreInteractions(files);
        verifyNoMoreInteractions(list);
        verifyNoMoreInteractions(fileList);
        verifyZeroInteractions(parent);
    }

    @Test
    public void testFindFileWhereEmptyItemsFound() throws IOException {
        File parent = mock(File.class);
        Drive.Files files = mock(Drive.Files.class);
        Drive.Files.List list = mock(Drive.Files.List.class);
        FileList fileList = mock(FileList.class);
        String query = "test-query-string";
        String filename = "test-filename";

        when(service.findFile(anyString(), any(File.class), anyBoolean())).thenCallRealMethod();
        when(service.buildQuery(anyString(), any(File.class), anyBoolean())).thenReturn(query);
        when(drive.files()).thenReturn(files);
        when(files.list()).thenReturn(list);
        when(list.setQ(anyString())).thenReturn(list);
        when(list.execute()).thenReturn(fileList);
        when(fileList.getItems()).thenReturn(new ArrayList<File>());

        File result = service.findFile(filename, parent, false);

        assertNull(result);

        verify(service).findFile(filename, parent, false);
        verify(service).getDrive();
        verify(service).buildQuery(filename, parent, false);
        verify(drive).files();
        verify(files).list();
        verify(list).setQ(query);
        verify(list).execute();
        verify(fileList).getItems();

        verifyNoMoreInteractions(service);
        verifyNoMoreInteractions(drive);
        verifyNoMoreInteractions(drive);
        verifyNoMoreInteractions(files);
        verifyNoMoreInteractions(list);
        verifyNoMoreInteractions(fileList);
        verifyZeroInteractions(parent);
    }

    @Test(expected = FileHandleException.class)
    public void testFindFileWhereMoreThanOneItemsFound() throws IOException {
        File parent = mock(File.class);
        Drive.Files files = mock(Drive.Files.class);
        Drive.Files.List list = mock(Drive.Files.List.class);
        FileList fileList = mock(FileList.class);
        String query = "test-query-string";
        String filename = "test-filename";
        File dir1 = mock(File.class);
        File dir2 = mock(File.class);

        when(service.findFile(anyString(), any(File.class), anyBoolean())).thenCallRealMethod();
        when(service.buildQuery(anyString(), any(File.class), anyBoolean())).thenReturn(query);
        when(drive.files()).thenReturn(files);
        when(files.list()).thenReturn(list);
        when(list.setQ(anyString())).thenReturn(list);
        when(list.execute()).thenReturn(fileList);
        when(fileList.getItems()).thenReturn(Arrays.asList(dir1, dir2));

        try {
            service.findFile(filename, parent, false);
        } catch (DirectoryHandleException e) {
            verify(service).findFile(filename, parent, false);
            verify(service).getDrive();
            verify(service).buildQuery(filename, parent, false);
            verify(drive).files();
            verify(files).list();
            verify(list).setQ(query);
            verify(list).execute();
            verify(fileList).getItems();

            verifyNoMoreInteractions(service);
            verifyNoMoreInteractions(drive);
            verifyNoMoreInteractions(drive);
            verifyNoMoreInteractions(files);
            verifyNoMoreInteractions(list);
            verifyNoMoreInteractions(fileList);
            verifyZeroInteractions(parent);
            verifyZeroInteractions(dir1);
            verifyZeroInteractions(dir2);

            throw e;
        }
    }

    @Test(expected = FileHandleException.class)
    public void testFindFileWhereIOExceptionWasThrown() throws IOException {
        File parent = mock(File.class);
        Drive.Files files = mock(Drive.Files.class);
        Drive.Files.List list = mock(Drive.Files.List.class);
        String query = "test-query-string";
        String filename = "test-filename";

        when(service.findFile(anyString(), any(File.class), anyBoolean())).thenCallRealMethod();
        when(service.buildQuery(anyString(), any(File.class), anyBoolean())).thenReturn(query);
        when(drive.files()).thenReturn(files);
        when(files.list()).thenReturn(list);
        when(list.setQ(anyString())).thenReturn(list);
        when(list.execute()).thenThrow(IOException.class);

        try {
            service.findFile(filename, parent, false);
        } catch (DirectoryHandleException e) {
            verify(service).findFile(filename, parent, false);
            verify(service).getDrive();
            verify(service).buildQuery(filename, parent, false);
            verify(drive).files();
            verify(files).list();
            verify(list).setQ(query);
            verify(list).execute();

            verifyNoMoreInteractions(service);
            verifyNoMoreInteractions(drive);
            verifyNoMoreInteractions(drive);
            verifyNoMoreInteractions(files);
            verifyNoMoreInteractions(list);
            verifyZeroInteractions(parent);

            throw e;
        }
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

        when(service.findFile(anyString(), any(File.class), anyBoolean())).thenCallRealMethod();
        when(service.buildQuery(anyString(), any(File.class), anyBoolean())).thenReturn(query);
        when(drive.files()).thenReturn(files);
        when(files.list()).thenReturn(list);
        when(list.setQ(anyString())).thenReturn(list);
        when(list.execute()).thenReturn(fileList);
        when(fileList.getItems()).thenReturn(Arrays.asList(directory));

        File result = service.findFile(filename, parent, false);

        assertNotNull(result);
        assertEquals(directory, result);

        verify(service).findFile(filename, parent, false);
        verify(service).getDrive();
        verify(service).buildQuery(filename, parent, false);
        verify(drive).files();
        verify(files).list();
        verify(list).setQ(query);
        verify(list).execute();
        verify(fileList).getItems();

        verifyNoMoreInteractions(service);
        verifyNoMoreInteractions(drive);
        verifyNoMoreInteractions(drive);
        verifyNoMoreInteractions(files);
        verifyNoMoreInteractions(list);
        verifyNoMoreInteractions(fileList);
        verifyZeroInteractions(parent);
        verifyZeroInteractions(directory);
    }

}