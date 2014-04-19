package net.czpilar.gdrive.core.service.impl;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import net.czpilar.gdrive.core.exception.DirectoryHandleException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author David Pilar (david@czpilar.net)
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ File.class, FileList.class })
public class DirectoryServiceTest {

	private DirectoryService service = new DirectoryService();

	@Mock
	private DirectoryService serviceMock;

	@Mock
	private Drive drive;

	@Before
	public void before() {
		MockitoAnnotations.initMocks(this);

		when(serviceMock.getDrive()).thenReturn(drive);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testBuildQueryWhereDirnameIsNullAndParentIsNull() {
		service.buildQuery(null, null);
	}

	@Test
	public void testBuildQueryWhereParentIsNull() {
		String result = service.buildQuery("test-dirname", null);

		assertNotNull(result);
		assertEquals("title='test-dirname' and 'root' in parents and trashed = false and mimeType = 'application/vnd.google-apps.folder'", result);
	}

	@Test
	public void testBuildQueryWhereParentExists() {
		File parentDir = mock(File.class);
		when(parentDir.getId()).thenReturn("test-id-parent-dir");

		String result = service.buildQuery("test-dirname", parentDir);

		assertNotNull(result);
		assertEquals("title='test-dirname' and 'test-id-parent-dir' in parents and trashed = false and mimeType = 'application/vnd.google-apps.folder'", result);

		verify(parentDir).getId();
		verifyNoMoreInteractions(parentDir);
	}

	@Test
	public void testBuildQueryWhereDirnameNeedsEscaping() {
		String result = service.buildQuery("test ' test \\' test \\\\' test \\\\\\' test \\\\\\\\' test \\\\\\\\\\' test", null);

		assertNotNull(result);
		assertEquals("title='test \\' test \\' test \\\\\\' test \\\\\\' test \\\\\\\\\\' test \\\\\\\\\\' test' and 'root' in parents and trashed = false and mimeType = 'application/vnd.google-apps.folder'", result);
	}

	@Test
	public void testBuildQueryWhereDirnameDontNeedEscaping() {
		String result = service.buildQuery("test \" test \" test", null);

		assertNotNull(result);
		assertEquals("title='test \" test \" test' and 'root' in parents and trashed = false and mimeType = 'application/vnd.google-apps.folder'", result);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFindOneDirectoryWhereDirnameIsNull() {
		File parentDir = mock(File.class);
		service.findOneDirectory(null, parentDir);
	}

	@Test
	public void testFindOneDirectoryWhereNoItemsFound() throws IOException {
		File parentDir = mock(File.class);
		Drive.Files files = mock(Drive.Files.class);
		Drive.Files.List list = mock(Drive.Files.List.class);
		FileList fileList = mock(FileList.class);
		String query = "test-query-string";
		String dirname = "test-dirname";

		when(serviceMock.findOneDirectory(anyString(), any(File.class))).thenCallRealMethod();
		when(serviceMock.buildQuery(anyString(), any(File.class))).thenReturn(query);
		when(drive.files()).thenReturn(files);
		when(files.list()).thenReturn(list);
		when(list.setQ(anyString())).thenReturn(list);
		when(list.execute()).thenReturn(fileList);
		when(fileList.getItems()).thenReturn(null);

		File result = serviceMock.findOneDirectory(dirname, parentDir);

		assertNull(result);

		verify(serviceMock).findOneDirectory(dirname, parentDir);
		verify(serviceMock).getDrive();
		verify(serviceMock).buildQuery(dirname, parentDir);
		verify(drive).files();
		verify(files).list();
		verify(list).setQ(query);
		verify(list).execute();
		verify(fileList).getItems();

		verifyNoMoreInteractions(serviceMock);
		verifyNoMoreInteractions(drive);
		verifyNoMoreInteractions(drive);
		verifyNoMoreInteractions(files);
		verifyNoMoreInteractions(list);
		verifyNoMoreInteractions(fileList);
		verifyZeroInteractions(parentDir);
	}

	@Test
	public void testFindOneDirectoryWhereEmptyItemsFound() throws IOException {
		File parentDir = mock(File.class);
		Drive.Files files = mock(Drive.Files.class);
		Drive.Files.List list = mock(Drive.Files.List.class);
		FileList fileList = mock(FileList.class);
		String query = "test-query-string";
		String dirname = "test-dirname";

		when(serviceMock.findOneDirectory(anyString(), any(File.class))).thenCallRealMethod();
		when(serviceMock.buildQuery(anyString(), any(File.class))).thenReturn(query);
		when(drive.files()).thenReturn(files);
		when(files.list()).thenReturn(list);
		when(list.setQ(anyString())).thenReturn(list);
		when(list.execute()).thenReturn(fileList);
		when(fileList.getItems()).thenReturn(new ArrayList<File>());

		File result = serviceMock.findOneDirectory(dirname, parentDir);

		assertNull(result);

		verify(serviceMock).findOneDirectory(dirname, parentDir);
		verify(serviceMock).getDrive();
		verify(serviceMock).buildQuery(dirname, parentDir);
		verify(drive).files();
		verify(files).list();
		verify(list).setQ(query);
		verify(list).execute();
		verify(fileList).getItems();

		verifyNoMoreInteractions(serviceMock);
		verifyNoMoreInteractions(drive);
		verifyNoMoreInteractions(drive);
		verifyNoMoreInteractions(files);
		verifyNoMoreInteractions(list);
		verifyNoMoreInteractions(fileList);
		verifyZeroInteractions(parentDir);
	}

	@Test(expected = DirectoryHandleException.class)
	public void testFindOneDirectoryWhereMoreThanOneItemsFound() throws IOException {
		File parentDir = mock(File.class);
		Drive.Files files = mock(Drive.Files.class);
		Drive.Files.List list = mock(Drive.Files.List.class);
		FileList fileList = mock(FileList.class);
		String query = "test-query-string";
		String dirname = "test-dirname";
		File dir1 = mock(File.class);
		File dir2 = mock(File.class);

		when(serviceMock.findOneDirectory(anyString(), any(File.class))).thenCallRealMethod();
		when(serviceMock.buildQuery(anyString(), any(File.class))).thenReturn(query);
		when(drive.files()).thenReturn(files);
		when(files.list()).thenReturn(list);
		when(list.setQ(anyString())).thenReturn(list);
		when(list.execute()).thenReturn(fileList);
		when(fileList.getItems()).thenReturn(Arrays.asList(dir1, dir2));

		try {
			serviceMock.findOneDirectory(dirname, parentDir);
		} catch (DirectoryHandleException e) {
			verify(serviceMock).findOneDirectory(dirname, parentDir);
			verify(serviceMock).getDrive();
			verify(serviceMock).buildQuery(dirname, parentDir);
			verify(drive).files();
			verify(files).list();
			verify(list).setQ(query);
			verify(list).execute();
			verify(fileList).getItems();

			verifyNoMoreInteractions(serviceMock);
			verifyNoMoreInteractions(drive);
			verifyNoMoreInteractions(drive);
			verifyNoMoreInteractions(files);
			verifyNoMoreInteractions(list);
			verifyNoMoreInteractions(fileList);
			verifyZeroInteractions(parentDir);
			verifyZeroInteractions(dir1);
			verifyZeroInteractions(dir2);

			throw e;
		}
	}

	@Test(expected = DirectoryHandleException.class)
	public void testFindOneDirectoryWhereIOExceptionWasThrown() throws IOException {
		File parentDir = mock(File.class);
		Drive.Files files = mock(Drive.Files.class);
		Drive.Files.List list = mock(Drive.Files.List.class);
		String query = "test-query-string";
		String dirname = "test-dirname";

		when(serviceMock.findOneDirectory(anyString(), any(File.class))).thenCallRealMethod();
		when(serviceMock.buildQuery(anyString(), any(File.class))).thenReturn(query);
		when(drive.files()).thenReturn(files);
		when(files.list()).thenReturn(list);
		when(list.setQ(anyString())).thenReturn(list);
		when(list.execute()).thenThrow(IOException.class);

		try {
			serviceMock.findOneDirectory(dirname, parentDir);
		} catch (DirectoryHandleException e) {
			verify(serviceMock).findOneDirectory(dirname, parentDir);
			verify(serviceMock).getDrive();
			verify(serviceMock).buildQuery(dirname, parentDir);
			verify(drive).files();
			verify(files).list();
			verify(list).setQ(query);
			verify(list).execute();

			verifyNoMoreInteractions(serviceMock);
			verifyNoMoreInteractions(drive);
			verifyNoMoreInteractions(drive);
			verifyNoMoreInteractions(files);
			verifyNoMoreInteractions(list);
			verifyZeroInteractions(parentDir);

			throw e;
		}
	}

	@Test
	public void testFindOneDirectory() throws IOException {
		File parentDir = mock(File.class);
		Drive.Files files = mock(Drive.Files.class);
		Drive.Files.List list = mock(Drive.Files.List.class);
		FileList fileList = mock(FileList.class);
		File directory = mock(File.class);
		String query = "test-query-string";
		String dirname = "test-dirname";

		when(serviceMock.findOneDirectory(anyString(), any(File.class))).thenCallRealMethod();
		when(serviceMock.buildQuery(anyString(), any(File.class))).thenReturn(query);
		when(drive.files()).thenReturn(files);
		when(files.list()).thenReturn(list);
		when(list.setQ(anyString())).thenReturn(list);
		when(list.execute()).thenReturn(fileList);
		when(fileList.getItems()).thenReturn(Arrays.asList(directory));

		File result = serviceMock.findOneDirectory(dirname, parentDir);

		assertNotNull(result);
		assertEquals(directory, result);

		verify(serviceMock).findOneDirectory(dirname, parentDir);
		verify(serviceMock).getDrive();
		verify(serviceMock).buildQuery(dirname, parentDir);
		verify(drive).files();
		verify(files).list();
		verify(list).setQ(query);
		verify(list).execute();
		verify(fileList).getItems();

		verifyNoMoreInteractions(serviceMock);
		verifyNoMoreInteractions(drive);
		verifyNoMoreInteractions(drive);
		verifyNoMoreInteractions(files);
		verifyNoMoreInteractions(list);
		verifyNoMoreInteractions(fileList);
		verifyZeroInteractions(parentDir);
		verifyZeroInteractions(directory);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateOneDirectoryWhereDirnameIsNullAndParentDirIsNull() {
		service.createOneDirectory(null, null);
	}

	@Test
	public void testCreateOneDirectoryWhereParentDirIsNull() throws IOException {
		String dirname = "test-dirname";
		File directory = mock(File.class);
		Drive.Files files = mock(Drive.Files.class);
		Drive.Files.Insert insert = mock(Drive.Files.Insert.class);

		when(serviceMock.createOneDirectory(anyString(), any(File.class))).thenCallRealMethod();
		when(drive.files()).thenReturn(files);
		when(files.insert(any(File.class))).thenReturn(insert);
		when(insert.execute()).thenReturn(directory);

		File result = serviceMock.createOneDirectory(dirname, null);

		assertNotNull(result);
		assertEquals(directory, result);

		verify(serviceMock).createOneDirectory(dirname, null);
		verify(serviceMock).getDrive();
		verify(drive).files();
		verify(files).insert(any(File.class));
		verify(insert).execute();

		verifyNoMoreInteractions(serviceMock);
		verifyNoMoreInteractions(drive);
		verifyNoMoreInteractions(files);
		verifyNoMoreInteractions(insert);
		verifyZeroInteractions(directory);
	}

	@Test
	public void testCreateOneDirectoryWhereParentDirExists() throws IOException {
		String dirname = "test-dirname";
		File parentDir = mock(File.class);
		File directory = mock(File.class);
		Drive.Files files = mock(Drive.Files.class);
		Drive.Files.Insert insert = mock(Drive.Files.Insert.class);

		when(serviceMock.createOneDirectory(anyString(), any(File.class))).thenCallRealMethod();
		when(drive.files()).thenReturn(files);
		when(files.insert(any(File.class))).thenReturn(insert);
		when(insert.execute()).thenReturn(directory);
		when(parentDir.getId()).thenReturn("test-parent-dir-id");

		File result = serviceMock.createOneDirectory(dirname, parentDir);

		assertNotNull(result);
		assertEquals(directory, result);

		verify(serviceMock).createOneDirectory(dirname, parentDir);
		verify(serviceMock).getDrive();
		verify(drive).files();
		verify(files).insert(any(File.class));
		verify(insert).execute();
		verify(parentDir).getId();

		verifyNoMoreInteractions(serviceMock);
		verifyNoMoreInteractions(drive);
		verifyNoMoreInteractions(files);
		verifyNoMoreInteractions(insert);
		verifyNoMoreInteractions(parentDir);
		verifyZeroInteractions(directory);
	}

	@Test(expected = DirectoryHandleException.class)
	public void testCreateOneDirectoryWhereIOExceptionWatThrown() throws IOException {
		String dirname = "test-dirname";
		File directory = mock(File.class);
		Drive.Files files = mock(Drive.Files.class);
		Drive.Files.Insert insert = mock(Drive.Files.Insert.class);

		when(serviceMock.createOneDirectory(anyString(), any(File.class))).thenCallRealMethod();
		when(drive.files()).thenReturn(files);
		when(files.insert(any(File.class))).thenReturn(insert);
		when(insert.execute()).thenThrow(IOException.class);

		try {
			serviceMock.createOneDirectory(dirname, null);
		} catch (DirectoryHandleException e) {
			verify(serviceMock).createOneDirectory(dirname, null);
			verify(serviceMock).getDrive();
			verify(drive).files();
			verify(files).insert(any(File.class));
			verify(insert).execute();

			verifyNoMoreInteractions(serviceMock);
			verifyNoMoreInteractions(drive);
			verifyNoMoreInteractions(files);
			verifyNoMoreInteractions(insert);
			verifyZeroInteractions(directory);

			throw e;
		}
	}

	@Test
	public void testFindOrCreateOneDirectoryWhereDirectoryIsFound() {
		File parentDir = mock(File.class);
		File directory = mock(File.class);
		String dirname = "test-dirname";

		when(serviceMock.findOrCreateOneDirectory(anyString(), any(File.class))).thenCallRealMethod();
		when(serviceMock.findOneDirectory(anyString(), any(File.class))).thenReturn(directory);

		File result = serviceMock.findOrCreateOneDirectory(dirname, parentDir);

		assertNotNull(result);
		assertEquals(directory, result);

		verify(serviceMock).findOrCreateOneDirectory(dirname, parentDir);
		verify(serviceMock).findOneDirectory(dirname, parentDir);

		verifyNoMoreInteractions(serviceMock);

		verifyZeroInteractions(parentDir);
		verifyZeroInteractions(directory);
	}

	@Test
	public void testFindOrCreateOneDirectoryWhereDirectoryIsCreated() {
		File parentDir = mock(File.class);
		File directory = mock(File.class);
		String dirname = "test-dirname";

		when(serviceMock.findOrCreateOneDirectory(anyString(), any(File.class))).thenCallRealMethod();
		when(serviceMock.findOneDirectory(anyString(), any(File.class))).thenReturn(null);
		when(serviceMock.createOneDirectory(anyString(), any(File.class))).thenReturn(directory);

		File result = serviceMock.findOrCreateOneDirectory(dirname, parentDir);

		assertNotNull(result);
		assertEquals(directory, result);

		verify(serviceMock).findOrCreateOneDirectory(dirname, parentDir);
		verify(serviceMock).findOneDirectory(dirname, parentDir);
		verify(serviceMock).createOneDirectory(dirname, parentDir);

		verifyNoMoreInteractions(serviceMock);

		verifyZeroInteractions(parentDir);
		verifyZeroInteractions(directory);
	}

	@Test
	public void testFindDirectoryWithPathname() {
		String pathname = "test-pathname";
		File directory = mock(File.class);

		when(serviceMock.findDirectory(anyString())).thenCallRealMethod();
		when(serviceMock.findDirectory(anyString(), any(File.class))).thenReturn(directory);

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
		when(serviceMock.findOneDirectory(anyString(), any(File.class))).thenReturn(null);

		File result = serviceMock.findDirectory(pathname, parentDir);

		assertNull(result);

		verify(serviceMock).findDirectory(pathname, parentDir);
		verify(serviceMock).findOneDirectory(dirname1, parentDir);

		verifyNoMoreInteractions(serviceMock);

		verifyZeroInteractions(parentDir);
	}

	@Test
	public void testFindDirectoryWithPathnameAndParentWherePathnameHasOneDir() {
		String pathname = "test-dirname";
		File parentDir = mock(File.class);
		File directory = mock(File.class);

		when(serviceMock.findDirectory(anyString(), any(File.class))).thenCallRealMethod();
		when(serviceMock.findOneDirectory(anyString(), any(File.class))).thenReturn(directory);

		File result = serviceMock.findDirectory(pathname, parentDir);

		assertNotNull(result);
		assertEquals(directory, result);

		verify(serviceMock).findDirectory(pathname, parentDir);
		verify(serviceMock).findOneDirectory(pathname, parentDir);

		verifyNoMoreInteractions(serviceMock);

		verifyZeroInteractions(parentDir);
		verifyZeroInteractions(directory);
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
		when(serviceMock.findOneDirectory(anyString(), any(File.class))).thenReturn(directory1, directory2, directory3);

		File result = serviceMock.findDirectory(pathname, parentDir);

		assertNotNull(result);
		assertEquals(directory3, result);

		verify(serviceMock).findDirectory(pathname, parentDir);
		verify(serviceMock).findDirectory(dirname2 + "/" + dirname3, directory1);
		verify(serviceMock).findDirectory(dirname3, directory2);
		verify(serviceMock).findOneDirectory(dirname1, parentDir);
		verify(serviceMock).findOneDirectory(dirname2, directory1);
		verify(serviceMock).findOneDirectory(dirname3, directory2);

		verifyNoMoreInteractions(serviceMock);

		verifyZeroInteractions(directory1);
		verifyZeroInteractions(directory2);
		verifyZeroInteractions(directory3);
		verifyZeroInteractions(parentDir);
	}

	@Test
	public void testFindOrCreateDirectoryWithPathname() {
		String pathname = "test-pathname";
		File directory = mock(File.class);

		when(serviceMock.findOrCreateDirectory(anyString())).thenCallRealMethod();
		when(serviceMock.findOrCreateDirectory(anyString(), any(File.class))).thenReturn(directory);

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

		verifyZeroInteractions(directory);
		verifyZeroInteractions(parentDir);
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

		verifyZeroInteractions(directory1);
		verifyZeroInteractions(directory2);
		verifyZeroInteractions(directory3);
		verifyZeroInteractions(parentDir);
	}
}
