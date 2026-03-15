package net.czpilar.gdrive.cmd;

import com.google.api.client.googleapis.media.MediaHttpUploader;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;

/**
 * @author David Pilar (david@czpilar.net)
 */
class GDriveIntegrationTest {

    private static final String PROPERTIES = "gdrive.properties";

    @Test
    void testNoArgs() {
        GDrive.main(new String[]{});
    }

    @Test
    void testHelp() {
        GDrive.main(new String[]{"-h"});
    }

    @Test
    void testVersion() {
        GDrive.main(new String[]{"-v"});
    }

    @Test
    void testProperties() {
        GDrive.main(new String[]{"-p", PROPERTIES});
    }

    @Test
    void testShowAuthLink() {
        GDrive.main(new String[]{"-l", "-p", PROPERTIES});
    }

    @Test
    void testAuthorize() {
        GDrive.main(new String[]{"-a", "auth_code", "-p", PROPERTIES});
    }

    @Test
    void testShowAuthLinkAndAuthorize() {
        GDrive.main(new String[]{"-l", "-a", "-p", PROPERTIES});
    }

    private void createFileIfNotExist(String filename) throws IOException {
        File file = new File(filename);
        if (!file.exists()) {
            FileUtils.writeStringToFile(file, "This is a testing file with filename: " + filename, Charset.defaultCharset());
        }
    }

    private void createLargeFileIfNotExist(String filename, long size) throws IOException {
        File file = new File(filename);
        if (!file.exists()) {
            try (RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
                raf.setLength(size);
            }
        }
    }

    @Test
    void testUploadFiles() throws IOException {
        String filename1 = "target/test1.txt";
        String filename2 = "target/test2.txt";
        String filename3 = "target/test3.txt";
        createFileIfNotExist(filename1);
        createFileIfNotExist(filename2);
        createFileIfNotExist(filename3);
        GDrive.main(new String[]{"-f", filename1, filename2, filename3, "-d", "gdrive-test-backup", "-p", PROPERTIES});
    }

    @Test
    void testUploadFileToSubdirectory() throws IOException {
        String filename = "target/test1.txt";
        createFileIfNotExist(filename);
        GDrive.main(new String[]{"-f", filename, "-d", "gdrive-test-backup/gdrive-subdir/gdrive-last-dir", "-p", PROPERTIES});
    }

    @Test
    void testUploadLargeFile() throws IOException {
        String filename = "target/test-large-file.bin";
        createLargeFileIfNotExist(filename, MediaHttpUploader.DEFAULT_CHUNK_SIZE + 1);
        GDrive.main(new String[]{"-f", filename, "-d", "gdrive-test-backup", "-p", PROPERTIES});
    }

    @Test
    void testUploadLargeFileMultipleChunks() throws IOException {
        String filename = "target/test-large-file-multi-chunk.bin";
        createLargeFileIfNotExist(filename, MediaHttpUploader.DEFAULT_CHUNK_SIZE * 3L + 1);
        GDrive.main(new String[]{"-f", filename, "-d", "gdrive-test-backup", "-p", PROPERTIES});
    }

    @Test
    void testUploadSmallAndLargeFiles() throws IOException {
        String smallFilename = "target/test-small.txt";
        String largeFilename = "target/test-large-file-mixed.bin";
        createFileIfNotExist(smallFilename);
        createLargeFileIfNotExist(largeFilename, MediaHttpUploader.DEFAULT_CHUNK_SIZE + 1);
        GDrive.main(new String[]{"-f", smallFilename, largeFilename, "-d", "gdrive-test-backup", "-p", PROPERTIES});
    }

    @Test
    void testEmptyTrash() {
        GDrive.main(new String[]{"-t", "-p", PROPERTIES});
    }
}
