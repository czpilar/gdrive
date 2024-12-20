package net.czpilar.gdrive.core.service.impl;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.googleapis.media.MediaHttpUploader;
import com.google.api.client.googleapis.media.MediaHttpUploaderProgressListener;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import net.czpilar.gdrive.core.credential.IGDriveCredential;
import net.czpilar.gdrive.core.exception.FileHandleException;
import net.czpilar.gdrive.core.service.IDirectoryService;
import net.czpilar.gdrive.core.util.EqualUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author David Pilar (david@czpilar.net)
 */
public class FileServiceTest {

    private final FileService service = new FileService(3);

    @Mock
    private FileService serviceMock;

    @Mock
    private IDirectoryService directoryService;

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
        service.setApplicationContext(applicationContext);
        service.setGDriveCredential(gDriveCredential);
        service.setDirectoryService(directoryService);

        when(serviceMock.getDirectoryService()).thenReturn(directoryService);
        when(applicationContext.getBean(Drive.class)).thenReturn(drive);

        equalUtilsMockedStatic = mockStatic(EqualUtils.class);
    }

    @AfterEach
    public void after() throws Exception {
        autoCloseable.close();
        equalUtilsMockedStatic.close();
    }

    @Test
    public void testGetDirectoryService() {
        IDirectoryService result = service.getDirectoryService();

        assertNotNull(result);
        assertEquals(directoryService, result);
    }

    @Test
    public void testGetUploadDir() {
        String uploadDirName = "test-upload-dir";

        String result = service.getUploadDir(uploadDirName);

        assertNotNull(result);
        assertEquals(uploadDirName, result);

        verifyNoInteractions(gDriveCredential);
    }

    @Test
    public void testGetUploadDirWithNullUploadDir() {
        String uploadDirName = "test-default-upload-dir";

        when(gDriveCredential.getUploadDir()).thenReturn(uploadDirName);

        String result = service.getUploadDir(null);

        assertNotNull(result);
        assertEquals(uploadDirName, result);

        verify(gDriveCredential).getUploadDir();

        verifyNoMoreInteractions(gDriveCredential);
    }

    @Test
    public void testUploadFileWithStringFilenameAndNullParent() throws IOException {
        String filename = "test-filename";
        File file = mock(File.class);
        Drive.Files files = mock(Drive.Files.class);
        Drive.Files.Create insert = mock(Drive.Files.Create.class);
        MediaHttpUploader mediaHttpUploader = mock(MediaHttpUploader.class);

        when(serviceMock.uploadFile(anyString(), (File) any())).thenCallRealMethod();
        when(serviceMock.getDrive()).thenReturn(drive);
        when(serviceMock.findFile(anyString(), any(File.class), anyBoolean())).thenReturn(null);
        when(drive.files()).thenReturn(files);
        when(files.create(any(File.class), any(FileContent.class))).thenReturn(insert);
        when(insert.execute()).thenReturn(file);
        when(insert.getMediaHttpUploader()).thenReturn(mediaHttpUploader);
        when(mediaHttpUploader.setDirectUploadEnabled(anyBoolean())).thenReturn(mediaHttpUploader);
        when(mediaHttpUploader.setProgressListener(any(MediaHttpUploaderProgressListener.class))).thenReturn(mediaHttpUploader);
        when(file.getId()).thenReturn("some-test-file-id");

        File result = serviceMock.uploadFile(filename, (File) null);

        assertNotNull(result);
        assertEquals(file, result);

        verify(serviceMock).uploadFile(filename, (File) null);
        verify(serviceMock).getDrive();
        verify(serviceMock).findFile(filename, null, false);
        verify(drive).files();
        verify(files).create(any(File.class), any(FileContent.class));
        verify(insert).execute();
        verify(insert).getMediaHttpUploader();
        verify(mediaHttpUploader).setDirectUploadEnabled(false);
        verify(mediaHttpUploader).setProgressListener(any(MediaHttpUploaderProgressListener.class));
        verify(file).getId();

        verifyNoMoreInteractions(serviceMock);
        verifyNoMoreInteractions(drive);
        verifyNoMoreInteractions(files);
        verifyNoMoreInteractions(insert);
        verifyNoMoreInteractions(mediaHttpUploader);
        verifyNoMoreInteractions(file);
    }

    @Test
    public void testUploadFileWithStringFilenameAndFileParent() throws IOException {
        String filename = "test-filename";
        File parentDir = mock(File.class);
        File file = mock(File.class);
        Drive.Files files = mock(Drive.Files.class);
        Drive.Files.Create insert = mock(Drive.Files.Create.class);
        MediaHttpUploader mediaHttpUploader = mock(MediaHttpUploader.class);

        when(serviceMock.uploadFile(anyString(), any(File.class))).thenCallRealMethod();
        when(serviceMock.getDrive()).thenReturn(drive);
        when(serviceMock.findFile(anyString(), any(File.class), anyBoolean())).thenReturn(null);
        when(drive.files()).thenReturn(files);
        when(files.create(any(File.class), any(FileContent.class))).thenReturn(insert);
        when(insert.execute()).thenReturn(file);
        when(insert.getMediaHttpUploader()).thenReturn(mediaHttpUploader);
        when(mediaHttpUploader.setDirectUploadEnabled(anyBoolean())).thenReturn(mediaHttpUploader);
        when(mediaHttpUploader.setProgressListener(any(MediaHttpUploaderProgressListener.class))).thenReturn(mediaHttpUploader);
        when(file.getId()).thenReturn("some-test-file-id");
        when(parentDir.getId()).thenReturn("some-test-parent-id");

        File result = serviceMock.uploadFile(filename, parentDir);

        assertNotNull(result);
        assertEquals(file, result);

        verify(serviceMock).uploadFile(filename, parentDir);
        verify(serviceMock).getDrive();
        verify(serviceMock).findFile(filename, parentDir, false);
        verify(drive).files();
        verify(files).create(any(File.class), any(FileContent.class));
        verify(insert).execute();
        verify(insert).getMediaHttpUploader();
        verify(mediaHttpUploader).setDirectUploadEnabled(false);
        verify(mediaHttpUploader).setProgressListener(any(MediaHttpUploaderProgressListener.class));
        verify(file).getId();
        verify(parentDir).getId();

        verifyNoMoreInteractions(serviceMock);
        verifyNoMoreInteractions(drive);
        verifyNoMoreInteractions(files);
        verifyNoMoreInteractions(insert);
        verifyNoMoreInteractions(mediaHttpUploader);
        verifyNoMoreInteractions(file);
        verifyNoMoreInteractions(parentDir);
    }

    @Test
    public void testUploadFileWithStringFilenameAndNullParentAndThrownException() throws IOException {
        String filename = "test-filename";
        Drive.Files files = mock(Drive.Files.class);

        when(serviceMock.uploadFile(anyString(), (File) any())).thenCallRealMethod();
        when(serviceMock.getDrive()).thenReturn(drive);
        when(serviceMock.findFile(anyString(), any(File.class), anyBoolean())).thenReturn(null);
        when(drive.files()).thenReturn(files);
        when(files.create(any(File.class), any(FileContent.class))).thenThrow(IOException.class);

        assertThrows(FileHandleException.class, () -> serviceMock.uploadFile(filename, (File) null));

        verify(serviceMock).uploadFile(filename, (File) null);
        verify(serviceMock).getDrive();
        verify(serviceMock).findFile(filename, null, false);
        verify(drive).files();
        verify(files).create(any(File.class), any(FileContent.class));

        verifyNoMoreInteractions(serviceMock);
        verifyNoMoreInteractions(drive);
        verifyNoMoreInteractions(files);
    }

    @Test
    public void testUploadFileWithStringFilenameAndFileParentWhereUpdateOnlyContent() throws IOException {
        String filename = "test-filename";
        File parentDir = mock(File.class);
        String fileId = "some-test-file-id";
        File file = mock(File.class);
        Drive.Files files = mock(Drive.Files.class);
        Drive.Files.Update update = mock(Drive.Files.Update.class);
        MediaHttpUploader mediaHttpUploader = mock(MediaHttpUploader.class);

        when(serviceMock.uploadFile(anyString(), any(File.class))).thenCallRealMethod();
        when(serviceMock.getDrive()).thenReturn(drive);
        when(serviceMock.findFile(anyString(), any(File.class), anyBoolean())).thenReturn(file);
        when(EqualUtils.notEquals(any(File.class), any(Path.class))).thenReturn(true);
        when(drive.files()).thenReturn(files);
        when(files.update(anyString(), any(File.class), any(FileContent.class))).thenReturn(update);
        when(update.execute()).thenReturn(file);
        when(update.getMediaHttpUploader()).thenReturn(mediaHttpUploader);
        when(mediaHttpUploader.setDirectUploadEnabled(anyBoolean())).thenReturn(mediaHttpUploader);
        when(mediaHttpUploader.setProgressListener(any(MediaHttpUploaderProgressListener.class))).thenReturn(mediaHttpUploader);
        when(file.getId()).thenReturn(fileId);

        File result = serviceMock.uploadFile(filename, parentDir);

        assertNotNull(result);
        assertEquals(file, result);

        verify(serviceMock).uploadFile(filename, parentDir);
        verify(serviceMock).getDrive();
        verify(serviceMock).findFile(filename, parentDir, false);
        verify(drive).files();
        verify(files).update(eq(fileId), any(File.class), any(FileContent.class));
        verify(update).execute();
        verify(update).getMediaHttpUploader();
        verify(mediaHttpUploader).setDirectUploadEnabled(false);
        verify(mediaHttpUploader).setProgressListener(any(MediaHttpUploaderProgressListener.class));
        verify(file, times(2)).getId();
        verify(file, times(2)).getMimeType();
        verify(file).getName();

        verifyNoMoreInteractions(serviceMock);
        verifyNoMoreInteractions(drive);
        verifyNoMoreInteractions(files);
        verifyNoMoreInteractions(update);
        verifyNoMoreInteractions(mediaHttpUploader);
        verifyNoMoreInteractions(file);
        verifyNoMoreInteractions(parentDir);
    }

    @Test
    public void testUploadFileWithStringFilenameAndFileParentWhereNothingToUpdate() {
        String filename = "test-filename";
        String fileId = "some-test-file-id";
        File parentDir = mock(File.class);
        File file = mock(File.class);

        when(serviceMock.uploadFile(anyString(), any(File.class))).thenCallRealMethod();
        when(serviceMock.findFile(anyString(), any(File.class), anyBoolean())).thenReturn(file);
        when(EqualUtils.notEquals(any(File.class), any(Path.class))).thenReturn(false);
        when(file.getId()).thenReturn(fileId);

        File result = serviceMock.uploadFile(filename, parentDir);

        assertNotNull(result);
        assertEquals(file, result);

        verify(serviceMock).uploadFile(filename, parentDir);
        verify(serviceMock).findFile(filename, parentDir, false);
        verify(file).getId();

        verifyNoMoreInteractions(serviceMock);
        verifyNoMoreInteractions(file);
        verifyNoMoreInteractions(parentDir);
    }

    @Test
    public void testUploadFileWithStringFilenameAndFileParentAndRetryApplied() throws IOException {
        String filename = "test-filename";
        File parentDir = mock(File.class);
        final File file = mock(File.class);
        Drive.Files files = mock(Drive.Files.class);
        Drive.Files.Create insert = mock(Drive.Files.Create.class);
        MediaHttpUploader mediaHttpUploader = mock(MediaHttpUploader.class);

        when(serviceMock.uploadFile(anyString(), any(File.class))).thenCallRealMethod();
        when(serviceMock.getDrive()).thenReturn(drive);
        when(serviceMock.findFile(anyString(), any(File.class), anyBoolean())).thenReturn(null);
        when(serviceMock.getRetries()).thenReturn(3);
        when(drive.files()).thenReturn(files);
        when(files.create(any(File.class), any(FileContent.class))).thenReturn(insert);
        Answer<File> answer = new Answer<>() {
            private int retry = 0;

            @Override
            public File answer(InvocationOnMock invocation) throws Throwable {
                if (retry == 0) {
                    retry++;
                    throw mock(GoogleJsonResponseException.class);
                }
                return file;
            }
        };
        when(insert.execute()).thenAnswer(answer);
        when(insert.getMediaHttpUploader()).thenReturn(mediaHttpUploader);
        when(mediaHttpUploader.setDirectUploadEnabled(anyBoolean())).thenReturn(mediaHttpUploader);
        when(mediaHttpUploader.setProgressListener(any(MediaHttpUploaderProgressListener.class))).thenReturn(mediaHttpUploader);
        when(file.getId()).thenReturn("some-test-file-id");
        when(parentDir.getId()).thenReturn("some-test-parent-id");

        File result = serviceMock.uploadFile(filename, parentDir);

        assertNotNull(result);
        assertEquals(file, result);

        verify(serviceMock).uploadFile(filename, parentDir);
        verify(serviceMock).getDrive();
        verify(serviceMock).findFile(filename, parentDir, false);
        verify(serviceMock).getRetries();
        verify(drive).files();
        verify(files).create(any(File.class), any(FileContent.class));
        verify(insert, times(2)).execute();
        verify(insert).getMediaHttpUploader();
        verify(mediaHttpUploader).setDirectUploadEnabled(false);
        verify(mediaHttpUploader).setProgressListener(any(MediaHttpUploaderProgressListener.class));
        verify(file).getId();
        verify(parentDir).getId();

        verifyNoMoreInteractions(serviceMock);
        verifyNoMoreInteractions(drive);
        verifyNoMoreInteractions(files);
        verifyNoMoreInteractions(insert);
        verifyNoMoreInteractions(mediaHttpUploader);
        verifyNoMoreInteractions(file);
        verifyNoMoreInteractions(parentDir);
    }

    @Test
    public void testUploadFileWithStringFilenameAndFileParentAndRetryExceeded() throws IOException {
        String filename = "test-filename";
        File parentDir = mock(File.class);
        final File file = mock(File.class);
        Drive.Files files = mock(Drive.Files.class);
        Drive.Files.Create insert = mock(Drive.Files.Create.class);
        MediaHttpUploader mediaHttpUploader = mock(MediaHttpUploader.class);

        when(serviceMock.uploadFile(anyString(), any(File.class))).thenCallRealMethod();
        when(serviceMock.getDrive()).thenReturn(drive);
        when(serviceMock.findFile(anyString(), any(File.class), anyBoolean())).thenReturn(null);
        when(serviceMock.getRetries()).thenReturn(3);
        when(drive.files()).thenReturn(files);
        when(files.create(any(File.class), any(FileContent.class))).thenReturn(insert);
        when(insert.execute()).thenThrow(mock(GoogleJsonResponseException.class));
        when(insert.getMediaHttpUploader()).thenReturn(mediaHttpUploader);
        when(mediaHttpUploader.setDirectUploadEnabled(anyBoolean())).thenReturn(mediaHttpUploader);
        when(mediaHttpUploader.setProgressListener(any(MediaHttpUploaderProgressListener.class))).thenReturn(mediaHttpUploader);
        when(file.getId()).thenReturn("some-test-file-id");
        when(parentDir.getId()).thenReturn("some-test-parent-id");

        FileHandleException e = assertThrows(FileHandleException.class, () -> serviceMock.uploadFile(filename, parentDir));

        assertInstanceOf(GoogleJsonResponseException.class, e.getCause());

        verify(serviceMock).uploadFile(filename, parentDir);
        verify(serviceMock).getDrive();
        verify(serviceMock).findFile(filename, parentDir, false);
        verify(serviceMock, times(4)).getRetries();
        verify(drive).files();
        verify(files).create(any(File.class), any(FileContent.class));
        verify(insert, times(4)).execute();
        verify(insert).getMediaHttpUploader();
        verify(mediaHttpUploader).setDirectUploadEnabled(false);
        verify(mediaHttpUploader).setProgressListener(any(MediaHttpUploaderProgressListener.class));
        verify(parentDir).getId();

        verifyNoMoreInteractions(serviceMock);
        verifyNoMoreInteractions(drive);
        verifyNoMoreInteractions(files);
        verifyNoMoreInteractions(insert);
        verifyNoMoreInteractions(mediaHttpUploader);
        verifyNoMoreInteractions(file);
        verifyNoMoreInteractions(parentDir);
    }

    @Test
    public void testUploadFilesWithNullFilenames() {
        File parent = mock(File.class);

        List<File> result = service.uploadFiles(null, parent);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testUploadFilesWithEmptyListOfFilenames() {
        File parent = mock(File.class);

        List<File> result = service.uploadFiles(List.of(), parent);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testUploadFilesWhereUploadFileThrowsException() {
        File parent = mock(File.class);

        when(serviceMock.uploadFiles(anyList(), any(File.class))).thenCallRealMethod();
        when(serviceMock.uploadFile(anyString(), any(File.class))).thenThrow(FileHandleException.class);

        List<File> result = serviceMock.uploadFiles(Arrays.asList("filename1", "filename2"), parent);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(serviceMock).uploadFiles(anyList(), any(File.class));
        verify(serviceMock).uploadFile("filename1", parent);
        verify(serviceMock).uploadFile("filename2", parent);

        verifyNoMoreInteractions(serviceMock);
    }

    @Test
    public void testUploadFilesWithListOfFilenames() {
        File parent = mock(File.class);

        when(serviceMock.uploadFiles(anyList(), any(File.class))).thenCallRealMethod();
        File file1 = mock(File.class);
        File file2 = mock(File.class);
        when(serviceMock.uploadFile(anyString(), any(File.class))).thenReturn(file1, file2);

        List<File> result = serviceMock.uploadFiles(Arrays.asList("filename1", "filename2"), parent);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(file1, result.get(0));
        assertEquals(file2, result.get(1));

        verify(serviceMock).uploadFiles(anyList(), any(File.class));
        verify(serviceMock).uploadFile("filename1", parent);
        verify(serviceMock).uploadFile("filename2", parent);

        verifyNoMoreInteractions(serviceMock);
    }

    @Test
    public void testUploadFileWithStringFilenameAndStringParentDir() {
        String pathname = "test-parent-dir";
        String filename = "test-filename";
        File file = mock(File.class);
        File parent = mock(File.class);

        when(serviceMock.uploadFile(anyString(), anyString())).thenCallRealMethod();
        when(serviceMock.uploadFile(anyString(), any(File.class))).thenReturn(file);
        when(serviceMock.getUploadDir(anyString())).thenReturn(pathname);
        when(directoryService.findOrCreateDirectory(anyString())).thenReturn(parent);

        File result = serviceMock.uploadFile(filename, pathname);

        assertNotNull(result);
        assertEquals(file, result);

        verify(serviceMock).uploadFile(filename, pathname);
        verify(serviceMock).uploadFile(filename, parent);
        verify(serviceMock).getUploadDir(pathname);
        verify(serviceMock).getDirectoryService();
        verify(directoryService).findOrCreateDirectory(anyString());

        verifyNoMoreInteractions(serviceMock);
        verifyNoMoreInteractions(directoryService);
        verifyNoInteractions(file);
        verifyNoInteractions(parent);
    }

    @Test
    public void testUploadFileWithStringFilename() {
        String filename = "test-filename";
        File file = mock(File.class);

        when(serviceMock.uploadFile(anyString())).thenCallRealMethod();
        when(serviceMock.uploadFile(anyString(), (File) any())).thenReturn(file);

        File result = serviceMock.uploadFile(filename);

        assertNotNull(result);
        assertEquals(file, result);

        verify(serviceMock).uploadFile(filename);
        verify(serviceMock).uploadFile(filename, (File) null);

        verifyNoMoreInteractions(serviceMock);
        verifyNoInteractions(file);
    }

    @Test
    public void testUploadFilesWithStringFilenames() {
        List<String> filenames = Arrays.asList("test-filename1", "test-filename2");
        File file1 = mock(File.class);
        File file2 = mock(File.class);
        List<File> files = Arrays.asList(file1, file2);

        when(serviceMock.uploadFiles(anyList())).thenCallRealMethod();
        when(serviceMock.uploadFiles(anyList(), (File) any())).thenReturn(files);

        List<File> result = serviceMock.uploadFiles(filenames);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(files, result);

        verify(serviceMock).uploadFiles(filenames);
        verify(serviceMock).uploadFiles(filenames, (File) null);

        verifyNoMoreInteractions(serviceMock);
        verifyNoInteractions(file1);
        verifyNoInteractions(file2);
    }

    @Test
    public void testUploadFilesWithStringFilenamesAndStringParentDir() {
        String parentDir = "test-parent-dir";
        List<String> filenames = Arrays.asList("test-filename1", "test-filename2");
        File file1 = mock(File.class);
        File file2 = mock(File.class);
        List<File> files = Arrays.asList(file1, file2);
        File parent = mock(File.class);

        when(serviceMock.uploadFiles(anyList(), anyString())).thenCallRealMethod();
        when(serviceMock.uploadFiles(anyList(), any(File.class))).thenReturn(files);
        when(serviceMock.getUploadDir(anyString())).thenReturn(parentDir);
        when(directoryService.findOrCreateDirectory(anyString())).thenReturn(parent);

        List<File> result = serviceMock.uploadFiles(filenames, parentDir);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(files, result);

        verify(serviceMock).uploadFiles(filenames, parentDir);
        verify(serviceMock).uploadFiles(filenames, parent);
        verify(serviceMock).getUploadDir(parentDir);
        verify(serviceMock).getDirectoryService();
        verify(directoryService).findOrCreateDirectory(anyString());

        verifyNoMoreInteractions(serviceMock);
        verifyNoMoreInteractions(directoryService);
        verifyNoInteractions(file1);
        verifyNoInteractions(file2);
        verifyNoInteractions(parent);
    }

}
