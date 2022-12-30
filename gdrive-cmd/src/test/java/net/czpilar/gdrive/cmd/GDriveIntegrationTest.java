package net.czpilar.gdrive.cmd;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * @author David Pilar (david@czpilar.net)
 */
public class GDriveIntegrationTest {

    private static final String PROPERTIES = "gdrive.properties";

    @Test
    public void testNoArgs() {
        GDrive.main(new String[]{});
    }

    @Test
    public void testHelp() {
        GDrive.main(new String[]{"-h"});
    }

    @Test
    public void testVersion() {
        GDrive.main(new String[]{"-v"});
    }

    @Test
    public void testProperties() {
        GDrive.main(new String[]{"-p", PROPERTIES});
    }

    @Test
    public void testShowAuthLink() {
        GDrive.main(new String[]{"-l", "-p", PROPERTIES});
    }

    @Test
    public void testAuthorize() {
        GDrive.main(new String[]{"-a", "auth_code", "-p", PROPERTIES});
    }

    @Test
    public void testShowAuthLinkAndAuthorize() {
        GDrive.main(new String[]{"-l", "-a", "-p", PROPERTIES});
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
        GDrive.main(new String[]{"-f", filename1, filename2, filename3, "-d", "gdrive-test-backup", "-p", PROPERTIES});
    }

    @Test
    public void testUploadFileToSubdirectory() throws IOException {
        String filename = "test1.txt";
        createFileIfNotExist(filename);
        GDrive.main(new String[]{"-f", filename, "-d", "gdrive-test-backup/gdrive-subdir/gdrive-lastdir", "-p", PROPERTIES});
    }

    @Test
    public void testEmtpyTrash() {
        GDrive.main(new String[]{"-t", "-p", PROPERTIES});
    }
}
