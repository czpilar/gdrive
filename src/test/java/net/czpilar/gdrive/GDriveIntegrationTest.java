package net.czpilar.gdrive;

import org.junit.Test;

/**
 * @author David Pilař (david@czpilar.net)
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
		GDrive.main(new String[] { "-a", "4/D6K_PhjXs2ZAhkLRWyZwNnanZKh0.4mDoY-XrQ08YOl05ti8ZT3bffTsQigI", "-p", "gdrive.properties" });
	}

	@Test
	public void testUploadFile() {
		GDrive.main(new String[] { "-f", "c:/test.txt", "c:/test.txt", "c:/test.txt", "-d", "gdrive-backup", "-p", "gdrive.properties" });
	}
}
