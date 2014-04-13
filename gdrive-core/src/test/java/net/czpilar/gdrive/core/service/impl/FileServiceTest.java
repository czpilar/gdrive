package net.czpilar.gdrive.core.service.impl;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.api.client.googleapis.media.MediaHttpUploader;
import com.google.api.client.googleapis.media.MediaHttpUploaderProgressListener;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.ParentReference;
import net.czpilar.gdrive.core.credential.IGDriveCredential;
import net.czpilar.gdrive.core.exception.FileUploadException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.context.ApplicationContext;

/**
 * @author David Pilař (david@czpilar.net)
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ FileList.class, File.class, ParentReference.class, MediaHttpUploader.class, Drive.Files.Insert.class })
public class FileServiceTest {

	private FileService service = new FileService();

	@Mock
	private FileService serviceMock;

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

		when(serviceMock.getDrive()).thenReturn(drive);
		when(serviceMock.getGDriveCredential()).thenReturn(gDriveCredential);

		when(applicationContext.getBean(Drive.class)).thenReturn(drive);
	}

	@Test
	public void testGetDrive() {
		Drive result = service.getDrive();

		assertNotNull(result);
		assertEquals(drive, result);

		verify(applicationContext).getBean(Drive.class);

		verifyNoMoreInteractions(applicationContext);
	}

	@Test
	public void testGetUploadDir() {
		String uploadDirName = "test-upload-dir";

		String result = service.getUploadDir(uploadDirName);

		assertNotNull(result);
		assertEquals(uploadDirName, result);

		verifyZeroInteractions(gDriveCredential);
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
	public void testFindParentWhereParentNameIsNull() throws IOException {
		File result = service.findParent(null, drive);

		assertNull(result);

		verifyZeroInteractions(drive);
	}

	@Test
	public void testFindParentWhereNoItemsFound() throws IOException {
		String parentName = "test-parent-name-dir";
		Drive.Files files = mock(Drive.Files.class);
		Drive.Files.List list = mock(Drive.Files.List.class);
		FileList fileList = mock(FileList.class);

		when(drive.files()).thenReturn(files);
		when(files.list()).thenReturn(list);
		when(list.setQ(anyString())).thenReturn(list);
		when(list.execute()).thenReturn(fileList);
		when(fileList.getItems()).thenReturn(null);

		File result = service.findParent(parentName, drive);

		assertNull(result);

		verify(drive).files();
		verify(files).list();
		verify(list).setQ("title='" + parentName + "' and mimeType = 'application/vnd.google-apps.folder'");
		verify(list).execute();
		verify(fileList).getItems();

		verifyNoMoreInteractions(drive);
		verifyNoMoreInteractions(files);
		verifyNoMoreInteractions(list);
		verifyNoMoreInteractions(fileList);
	}

	@Test
	public void testFindParentWhereEmptyItemsFound() throws IOException {
		String parentName = "test-parent-name-dir";
		Drive.Files files = mock(Drive.Files.class);
		Drive.Files.List list = mock(Drive.Files.List.class);
		FileList fileList = mock(FileList.class);

		when(drive.files()).thenReturn(files);
		when(files.list()).thenReturn(list);
		when(list.setQ(anyString())).thenReturn(list);
		when(list.execute()).thenReturn(fileList);
		when(fileList.getItems()).thenReturn(new ArrayList<File>());

		File result = service.findParent(parentName, drive);

		assertNull(result);

		verify(drive).files();
		verify(files).list();
		verify(list).setQ("title='" + parentName + "' and mimeType = 'application/vnd.google-apps.folder'");
		verify(list).execute();
		verify(fileList).getItems();

		verifyNoMoreInteractions(drive);
		verifyNoMoreInteractions(files);
		verifyNoMoreInteractions(list);
		verifyNoMoreInteractions(fileList);
	}

	@Test
	public void testFindParentWhereAreOnlyTrashedItems() throws IOException {
		String parentName = "test-parent-name-dir";
		Drive.Files files = mock(Drive.Files.class);
		Drive.Files.List list = mock(Drive.Files.List.class);
		FileList fileList = mock(FileList.class);
		File file = mock(File.class);

		when(drive.files()).thenReturn(files);
		when(files.list()).thenReturn(list);
		when(list.setQ(anyString())).thenReturn(list);
		when(list.execute()).thenReturn(fileList);
		when(fileList.getItems()).thenReturn(Arrays.asList(file));
		when(file.getExplicitlyTrashed()).thenReturn(true);

		File result = service.findParent(parentName, drive);

		assertNull(result);

		verify(drive).files();
		verify(files).list();
		verify(list).setQ("title='" + parentName + "' and mimeType = 'application/vnd.google-apps.folder'");
		verify(list).execute();
		verify(fileList).getItems();
		verify(file).getExplicitlyTrashed();

		verifyNoMoreInteractions(drive);
		verifyNoMoreInteractions(files);
		verifyNoMoreInteractions(list);
		verifyNoMoreInteractions(fileList);
		verifyNoMoreInteractions(file);
	}

	@Test
	public void testFindParentWhereAreNotTrashedItemsAndHaveNoParents() throws IOException {
		String parentName = "test-parent-name-dir";
		Drive.Files files = mock(Drive.Files.class);
		Drive.Files.List list = mock(Drive.Files.List.class);
		FileList fileList = mock(FileList.class);
		File file1 = mock(File.class);
		File file2 = mock(File.class);

		when(drive.files()).thenReturn(files);
		when(files.list()).thenReturn(list);
		when(list.setQ(anyString())).thenReturn(list);
		when(list.execute()).thenReturn(fileList);
		when(fileList.getItems()).thenReturn(Arrays.asList(file1, file2));
		when(file1.getExplicitlyTrashed()).thenReturn(false);
		when(file2.getExplicitlyTrashed()).thenReturn(null);
		when(file1.getParents()).thenReturn(null);
		when(file2.getParents()).thenReturn(new ArrayList<ParentReference>());

		File result = service.findParent(parentName, drive);

		assertNull(result);

		verify(drive).files();
		verify(files).list();
		verify(list).setQ("title='" + parentName + "' and mimeType = 'application/vnd.google-apps.folder'");
		verify(list).execute();
		verify(fileList).getItems();
		verify(file1).getExplicitlyTrashed();
		verify(file2).getExplicitlyTrashed();
		verify(file1).getParents();
		verify(file2).getParents();

		verifyNoMoreInteractions(drive);
		verifyNoMoreInteractions(files);
		verifyNoMoreInteractions(list);
		verifyNoMoreInteractions(fileList);
		verifyNoMoreInteractions(file1);
		verifyNoMoreInteractions(file2);
	}

	@Test
	public void testFindParentWhereAreNotTrashedItemsAndHaveNonRootParents() throws IOException {
		String parentName = "test-parent-name-dir";
		Drive.Files files = mock(Drive.Files.class);
		Drive.Files.List list = mock(Drive.Files.List.class);
		FileList fileList = mock(FileList.class);
		File file = mock(File.class);
		ParentReference parent1 = mock(ParentReference.class);
		ParentReference parent2 = mock(ParentReference.class);

		when(drive.files()).thenReturn(files);
		when(files.list()).thenReturn(list);
		when(list.setQ(anyString())).thenReturn(list);
		when(list.execute()).thenReturn(fileList);
		when(fileList.getItems()).thenReturn(Arrays.asList(file));
		when(file.getExplicitlyTrashed()).thenReturn(false);
		when(file.getParents()).thenReturn(Arrays.asList(parent1, parent2));
		when(parent1.getIsRoot()).thenReturn(null);
		when(parent2.getIsRoot()).thenReturn(false);

		File result = service.findParent(parentName, drive);

		assertNull(result);

		verify(drive).files();
		verify(files).list();
		verify(list).setQ("title='" + parentName + "' and mimeType = 'application/vnd.google-apps.folder'");
		verify(list).execute();
		verify(fileList).getItems();
		verify(file).getExplicitlyTrashed();
		verify(file).getParents();
		verify(parent1).getIsRoot();
		verify(parent2).getIsRoot();

		verifyNoMoreInteractions(drive);
		verifyNoMoreInteractions(files);
		verifyNoMoreInteractions(list);
		verifyNoMoreInteractions(fileList);
		verifyNoMoreInteractions(file);
		verifyNoMoreInteractions(parent1);
		verifyNoMoreInteractions(parent2);
	}

	@Test
	public void testFindParentWhereAreNotTrashedItemsAndHaveRootParents() throws IOException {
		String parentName = "test-parent-name-dir";
		Drive.Files files = mock(Drive.Files.class);
		Drive.Files.List list = mock(Drive.Files.List.class);
		FileList fileList = mock(FileList.class);
		File file = mock(File.class);
		ParentReference parent = mock(ParentReference.class);

		when(drive.files()).thenReturn(files);
		when(files.list()).thenReturn(list);
		when(list.setQ(anyString())).thenReturn(list);
		when(list.execute()).thenReturn(fileList);
		when(fileList.getItems()).thenReturn(Arrays.asList(file));
		when(file.getExplicitlyTrashed()).thenReturn(false);
		when(file.getParents()).thenReturn(Arrays.asList(parent));
		when(parent.getIsRoot()).thenReturn(true);

		File result = service.findParent(parentName, drive);

		assertNotNull(result);
		assertEquals(file, result);

		verify(drive).files();
		verify(files).list();
		verify(list).setQ("title='" + parentName + "' and mimeType = 'application/vnd.google-apps.folder'");
		verify(list).execute();
		verify(fileList).getItems();
		verify(file).getExplicitlyTrashed();
		verify(file).getParents();
		verify(parent).getIsRoot();

		verifyNoMoreInteractions(drive);
		verifyNoMoreInteractions(files);
		verifyNoMoreInteractions(list);
		verifyNoMoreInteractions(fileList);
		verifyNoMoreInteractions(file);
		verifyNoMoreInteractions(parent);
	}

	@Test
	public void testFindOrCreateParentWhereParentDirIsNull() throws IOException {
		File result = service.findOrCreateParent(null, drive);

		assertNull(result);

		verifyZeroInteractions(drive);
	}

	@Test
	public void testFindOrCreateParentWhereFindParentThrowsException() throws IOException {
		String parentName = "test-parent-name-dir";

		when(serviceMock.findOrCreateParent(anyString(), any(Drive.class))).thenCallRealMethod();
		when(serviceMock.findParent(anyString(), any(Drive.class))).thenThrow(IOException.class);

		File result = serviceMock.findOrCreateParent(parentName, drive);

		assertNull(result);

		verify(serviceMock).findOrCreateParent(anyString(), any(Drive.class));
		verify(serviceMock).findParent(anyString(), any(Drive.class));

		verifyZeroInteractions(drive);
		verifyNoMoreInteractions(serviceMock);
	}

	@Test
	public void testFindOrCreateParentWhereFindParentReturnsParent() throws IOException {
		String parentName = "test-parent-name-dir";
		File parent = mock(File.class);

		when(serviceMock.findOrCreateParent(anyString(), any(Drive.class))).thenCallRealMethod();
		when(serviceMock.findParent(anyString(), any(Drive.class))).thenReturn(parent);

		File result = serviceMock.findOrCreateParent(parentName, drive);

		assertNotNull(result);
		assertEquals(parent, result);

		verify(serviceMock).findOrCreateParent(anyString(), any(Drive.class));
		verify(serviceMock).findParent(anyString(), any(Drive.class));

		verifyZeroInteractions(drive);
		verifyNoMoreInteractions(serviceMock);
		verifyZeroInteractions(parent);
	}

	@Test
	public void testFindOrCreateParentWhereFindParentReturnsNull() throws IOException {
		String parentName = "test-parent-name-dir";
		File parent = mock(File.class);
		Drive.Files files = mock(Drive.Files.class);
		Drive.Files.Insert insert = mock(Drive.Files.Insert.class);

		when(serviceMock.findOrCreateParent(anyString(), any(Drive.class))).thenCallRealMethod();
		when(serviceMock.findParent(anyString(), any(Drive.class))).thenReturn(null);
		when(drive.files()).thenReturn(files);
		when(files.insert(any(File.class))).thenReturn(insert);
		when(insert.execute()).thenReturn(parent);

		File result = serviceMock.findOrCreateParent(parentName, drive);

		assertNotNull(result);
		assertEquals(parent, result);

		verify(serviceMock).findOrCreateParent(anyString(), any(Drive.class));
		verify(serviceMock).findParent(anyString(), any(Drive.class));
		verify(drive).files();
		verify(files).insert(any(File.class));
		verify(insert).execute();

		verifyNoMoreInteractions(drive);
		verifyNoMoreInteractions(serviceMock);
		verifyZeroInteractions(parent);
		verifyNoMoreInteractions(files);
		verifyNoMoreInteractions(insert);
	}

	@Test
	public void testUploadFileWithStringFilenameAndNullParentAndDrive() throws IOException {
		File file = mock(File.class);
		Drive.Files files = mock(Drive.Files.class);
		Drive.Files.Insert insert = PowerMockito.mock(Drive.Files.Insert.class);
		MediaHttpUploader mediaHttpUploader = mock(MediaHttpUploader.class);

		when(drive.files()).thenReturn(files);
		when(files.insert(any(File.class), any(FileContent.class))).thenReturn(insert);
		when(insert.execute()).thenReturn(file);
		when(insert.getMediaHttpUploader()).thenReturn(mediaHttpUploader);
		when(file.getId()).thenReturn("some-test-file-id");

		File result = service.uploadFile("test-filename", null, drive);

		assertNotNull(result);
		assertEquals(file, result);

		verify(drive).files();
		verify(files).insert(any(File.class), any(FileContent.class));
		verify(insert).execute();
		verify(insert, times(2)).getMediaHttpUploader();
		verify(mediaHttpUploader).setDirectUploadEnabled(false);
		verify(mediaHttpUploader).setProgressListener(any(MediaHttpUploaderProgressListener.class));
		verify(file).getId();

		verifyNoMoreInteractions(drive);
		verifyNoMoreInteractions(files);
		verifyNoMoreInteractions(insert);
		verifyNoMoreInteractions(mediaHttpUploader);
		verifyNoMoreInteractions(file);
	}

	@Test
	public void testUploadFileWithStringFilenameAndFileParentAndDrive() throws IOException {
		File parent = mock(File.class);
		File file = mock(File.class);
		Drive.Files files = mock(Drive.Files.class);
		Drive.Files.Insert insert = PowerMockito.mock(Drive.Files.Insert.class);
		MediaHttpUploader mediaHttpUploader = mock(MediaHttpUploader.class);

		when(drive.files()).thenReturn(files);
		when(files.insert(any(File.class), any(FileContent.class))).thenReturn(insert);
		when(insert.execute()).thenReturn(file);
		when(insert.getMediaHttpUploader()).thenReturn(mediaHttpUploader);
		when(file.getId()).thenReturn("some-test-file-id");
		when(parent.getId()).thenReturn("some-test-parent-id");

		File result = service.uploadFile("test-filename", parent, drive);

		assertNotNull(result);
		assertEquals(file, result);

		verify(drive).files();
		verify(files).insert(any(File.class), any(FileContent.class));
		verify(insert).execute();
		verify(insert, times(2)).getMediaHttpUploader();
		verify(mediaHttpUploader).setDirectUploadEnabled(false);
		verify(mediaHttpUploader).setProgressListener(any(MediaHttpUploaderProgressListener.class));
		verify(file).getId();
		verify(parent).getId();

		verifyNoMoreInteractions(drive);
		verifyNoMoreInteractions(files);
		verifyNoMoreInteractions(insert);
		verifyNoMoreInteractions(mediaHttpUploader);
		verifyNoMoreInteractions(file);
		verifyNoMoreInteractions(parent);
	}

	@Test(expected = FileUploadException.class)
	public void testUploadFileWithStringFilenameAndNullParentAndDriveAndThrownException() throws IOException {
		Drive.Files files = mock(Drive.Files.class);

		when(drive.files()).thenReturn(files);
		when(files.insert(any(File.class), any(FileContent.class))).thenThrow(IOException.class);

		try {
			service.uploadFile("test-filename", null, drive);
		} catch (FileUploadException e) {
			verify(drive).files();
			verify(files).insert(any(File.class), any(FileContent.class));

			verifyNoMoreInteractions(drive);
			verifyNoMoreInteractions(files);

			throw e;
		}
	}

	@Test
	public void testUploadFilesWithNullFilenames() {
		File parent = mock(File.class);

		doCallRealMethod().when(serviceMock).uploadFiles(anyListOf(String.class), any(File.class), any(Drive.class));

		serviceMock.uploadFiles(null, parent, drive);

		verify(serviceMock).uploadFiles(anyListOf(String.class), any(File.class), any(Drive.class));

		verifyNoMoreInteractions(serviceMock);
	}

	@Test
	public void testUploadFilesWithEmptyListOfFilenames() {
		File parent = mock(File.class);

		doCallRealMethod().when(serviceMock).uploadFiles(anyListOf(String.class), any(File.class), any(Drive.class));

		serviceMock.uploadFiles(new ArrayList<String>(), parent, drive);

		verify(serviceMock).uploadFiles(anyListOf(String.class), any(File.class), any(Drive.class));

		verifyNoMoreInteractions(serviceMock);
	}

	@Test
	public void testUploadFilesWhereUploadFileThrowsException() {
		File parent = mock(File.class);

		doCallRealMethod().when(serviceMock).uploadFiles(anyListOf(String.class), any(File.class), any(Drive.class));
		when(serviceMock.uploadFile(anyString(), any(File.class), any(Drive.class))).thenThrow(FileUploadException.class);

		serviceMock.uploadFiles(Arrays.asList("filename1", "filename2"), parent, drive);

		verify(serviceMock).uploadFiles(anyListOf(String.class), any(File.class), any(Drive.class));
		verify(serviceMock).uploadFile("filename1", parent, drive);
		verify(serviceMock).uploadFile("filename2", parent, drive);

		verifyNoMoreInteractions(serviceMock);
	}

	@Test
	public void testUploadFilesWithListOfFilenames() {
		File parent = mock(File.class);

		doCallRealMethod().when(serviceMock).uploadFiles(anyListOf(String.class), any(File.class), any(Drive.class));
		when(serviceMock.uploadFile(anyString(), any(File.class), any(Drive.class))).thenReturn(mock(File.class));

		serviceMock.uploadFiles(Arrays.asList("filename1", "filename2"), parent, drive);

		verify(serviceMock).uploadFiles(anyListOf(String.class), any(File.class), any(Drive.class));
		verify(serviceMock).uploadFile("filename1", parent, drive);
		verify(serviceMock).uploadFile("filename2", parent, drive);

		verifyNoMoreInteractions(serviceMock);
	}

	@Test
	public void testUploadFileWithStringFilenameAndStringParentDir() {
		String parentDir = "test-parent-dir";
		String filename = "test-filename";
		File file = mock(File.class);
		File parent = mock(File.class);

		when(serviceMock.uploadFile(anyString(), anyString())).thenCallRealMethod();
		when(serviceMock.uploadFile(anyString(), any(File.class), any(Drive.class))).thenReturn(file);
		when(serviceMock.getUploadDir(anyString())).thenReturn(parentDir);
		when(serviceMock.findOrCreateParent(anyString(), any(Drive.class))).thenReturn(parent);

		File result = serviceMock.uploadFile(filename, parentDir);

		assertNotNull(result);
		assertEquals(file, result);

		verify(serviceMock).uploadFile(filename, parentDir);
		verify(serviceMock).uploadFile(filename, parent, drive);
		verify(serviceMock).getUploadDir(parentDir);
		verify(serviceMock).getDrive();
		verify(serviceMock).findOrCreateParent(parentDir, drive);

		verifyNoMoreInteractions(serviceMock);
		verifyZeroInteractions(file);
		verifyZeroInteractions(parent);
	}

	@Test
	public void testUploadFileWithStringFilenameAndFileParent() {
		String filename = "test-filename";
		File file = mock(File.class);
		File parent = mock(File.class);

		when(serviceMock.uploadFile(anyString(), any(File.class))).thenCallRealMethod();
		when(serviceMock.uploadFile(anyString(), any(File.class), any(Drive.class))).thenReturn(file);

		File result = serviceMock.uploadFile(filename, parent);

		assertNotNull(result);
		assertEquals(file, result);

		verify(serviceMock).uploadFile(filename, parent);
		verify(serviceMock).uploadFile(filename, parent, drive);
		verify(serviceMock).getDrive();

		verifyNoMoreInteractions(serviceMock);
		verifyZeroInteractions(file);
		verifyZeroInteractions(parent);
	}

	@Test
	public void testUploadFileWithStringFilename() {
		String filename = "test-filename";
		File file = mock(File.class);

		when(serviceMock.uploadFile(anyString())).thenCallRealMethod();
		when(serviceMock.uploadFile(anyString(), any(File.class))).thenReturn(file);

		File result = serviceMock.uploadFile(filename);

		assertNotNull(result);
		assertEquals(file, result);

		verify(serviceMock).uploadFile(filename);
		verify(serviceMock).uploadFile(filename, (File) null);

		verifyNoMoreInteractions(serviceMock);
		verifyZeroInteractions(file);
	}

	@Test
	public void testUploadFilesWithStringFilenames() {
		List<String> filenames = Arrays.asList("test-filename1", "test-filename2");
		File file = mock(File.class);

		doCallRealMethod().when(serviceMock).uploadFiles(anyListOf(String.class));
		doNothing().when(serviceMock).uploadFiles(anyListOf(String.class), any(File.class));

		serviceMock.uploadFiles(filenames);

		verify(serviceMock).uploadFiles(filenames);
		verify(serviceMock).uploadFiles(filenames, (File) null);

		verifyNoMoreInteractions(serviceMock);
		verifyZeroInteractions(file);
	}

	@Test
	public void testUploadFilesWithStringFilenamesAndStringParentDir() {
		String parentDir = "test-parent-dir";
		List<String> filenames = Arrays.asList("test-filename1", "test-filename2");
		File file = mock(File.class);
		File parent = mock(File.class);

		doCallRealMethod().when(serviceMock).uploadFiles(anyListOf(String.class), anyString());
		doNothing().when(serviceMock).uploadFiles(anyListOf(String.class), any(File.class), any(Drive.class));
		when(serviceMock.getUploadDir(anyString())).thenReturn(parentDir);
		when(serviceMock.findOrCreateParent(anyString(), any(Drive.class))).thenReturn(parent);

		serviceMock.uploadFiles(filenames, parentDir);

		verify(serviceMock).uploadFiles(filenames, parentDir);
		verify(serviceMock).uploadFiles(filenames, parent, drive);
		verify(serviceMock).getUploadDir(parentDir);
		verify(serviceMock).getDrive();
		verify(serviceMock).findOrCreateParent(parentDir, drive);

		verifyNoMoreInteractions(serviceMock);
		verifyZeroInteractions(file);
		verifyZeroInteractions(parent);
	}

	@Test
	public void testUploadFilesWithStringFilenamesAndFileParent() {
		List<String> filenames = Arrays.asList("test-filename1", "test-filename2");
		File file = mock(File.class);
		File parent = mock(File.class);

		doCallRealMethod().when(serviceMock).uploadFiles(anyListOf(String.class), any(File.class));
		doNothing().when(serviceMock).uploadFiles(anyListOf(String.class), any(File.class), any(Drive.class));

		serviceMock.uploadFiles(filenames, parent);

		verify(serviceMock).uploadFiles(filenames, parent);
		verify(serviceMock).uploadFiles(filenames, parent, drive);
		verify(serviceMock).getDrive();

		verifyNoMoreInteractions(serviceMock);
		verifyZeroInteractions(file);
		verifyZeroInteractions(parent);
	}
}
