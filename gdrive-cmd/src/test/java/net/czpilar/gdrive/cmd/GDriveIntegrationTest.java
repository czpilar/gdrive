package net.czpilar.gdrive.cmd;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

/**
 * @author David Pilar (david@czpilar.net)
 */
public class GDriveIntegrationTest {

	@Test
	public void testNoArgs() {
		GDrive.main(new String[] { });
	}

	@Test
	public void testHelp() {
		GDrive.main(new String[] { "-p", "gdrive.properties", "-h" });
	}

	@Test
	public void testProperties() {
		GDrive.main(new String[] { "-p", "gdrive.properties" });
	}

	@Test
	public void testShowAuthLink() {
		GDrive.main(new String[] { "-l", "-p", "gdrive.properties" });
	}

	@Test
	public void testAuthorize() {
		GDrive.main(new String[] { "-a", "4/vAgd4EX2rQ6D64tMKsjsq1ffd8LG.QuX1tlk-VdgcOl05ti8ZT3YRrZljigI", "-p", "gdrive.properties" });
	}

	private void createFileIfNotExist(String filename) throws IOException {
		File file = new File(filename);
		if (!file.exists()) {
			FileUtils.writeStringToFile(file, "This is a testing file with filename: " + filename);
		}
	}

	@Test
	public void testUploadFiles() throws IOException {
		String filename1 = "test1.txt";
		String filename2 = "test2.txt";
		String filename3 = "test3.txt";
		createFileIfNotExist(filename1);
		createFileIfNotExist(filename2);
		createFileIfNotExist(filename3);
		GDrive.main(new String[] { "-f", filename1, filename2, filename3, "-d", "gdrive-test-backup", "-p", "gdrive.properties" });
	}

	@Test
	public void testUploadFileToSubdirectory() throws IOException {
		String filename = "test1.txt";
		createFileIfNotExist(filename);
		GDrive.main(new String[] { "-f", filename, "-d", "gdrive-test-backup/gdrive-subdir/gdrive-lastdir", "-p", "gdrive.properties" });
	}
}
