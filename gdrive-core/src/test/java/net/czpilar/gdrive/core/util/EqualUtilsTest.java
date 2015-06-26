package net.czpilar.gdrive.core.util;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.api.services.drive.model.File;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ EqualUtils.class, File.class })
public class EqualUtilsTest {

    private java.io.File testFile;

    @Before
    public void before() throws IOException {
        String tempDir = System.getProperty("java.io.tmpdir");
        testFile = new java.io.File(tempDir + "some-test-file-for-md5-" + System.currentTimeMillis() + ".properties");
        FileUtils.writeStringToFile(testFile, "Some test file data to store.");
    }

    @After
    public void after() {
        if (testFile.exists()) {
            testFile.delete();
        }
    }

    @Test
    public void testEqualsWhereBothParametersAreNull() {
        File file = null;
        Path path = null;
        boolean result = EqualUtils.equals(file, path);

        assertFalse(result);
    }

    @Test
    public void testEqualsWhereFileIsNull() {
        File file = null;
        Path path = mock(Path.class);
        boolean result = EqualUtils.equals(file, path);

        assertFalse(result);
    }

    @Test
    public void testEqualsWherePathIsNull() {
        File file = mock(File.class);
        Path path = null;
        boolean result = EqualUtils.equals(file, path);

        assertFalse(result);
    }

    @Test
    public void testEqualsWhereFileNotExists() {
        File file = mock(File.class);
        Path path = mock(Path.class);
        java.io.File ioFile = new java.io.File("invalid-file-to-test.qwerty");
        when(path.toFile()).thenReturn(ioFile);

        boolean result = EqualUtils.equals(file, path);

        assertFalse(result);
    }

    @Test
    public void testEqualsWhereFileAndPathIsNotEqual() {
        File file = mock(File.class);
        Path path = Paths.get(testFile.getPath());
        when(file.getMd5Checksum()).thenReturn("some-md5-checksum");

        boolean result = EqualUtils.equals(file, path);

        assertFalse(result);
    }

    @Test
    public void testEqualsWhereFileAndPathIsEqual() {
        File file = mock(File.class);
        Path path = Paths.get(testFile.getPath());
        when(file.getMd5Checksum()).thenReturn("d2f949bc807fe828ee442d03c2fc1497");

        boolean result = EqualUtils.equals(file, path);

        assertTrue(result);
    }

    @Test
    public void testNotEquals1() {
        PowerMockito.mockStatic(EqualUtils.class);
        when(EqualUtils.notEquals(any(File.class), any(Path.class))).thenCallRealMethod();
        when(EqualUtils.equals(any(File.class), any(Path.class))).thenReturn(true);

        boolean result = EqualUtils.notEquals(mock(File.class), mock(Path.class));

        assertFalse(result);
    }

    @Test
    public void testNotEquals2() {
        PowerMockito.mockStatic(EqualUtils.class);
        when(EqualUtils.notEquals(any(File.class), any(Path.class))).thenCallRealMethod();
        when(EqualUtils.equals(any(File.class), any(Path.class))).thenReturn(false);

        boolean result = EqualUtils.notEquals(mock(File.class), mock(Path.class));

        assertTrue(result);
    }
}