package net.czpilar.gdrive.core.util;

import com.google.api.client.util.DateTime;
import com.google.api.services.drive.model.File;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({EqualUtils.class, File.class})
@PowerMockIgnore("jdk.internal.reflect.*")
public class EqualUtilsTest {

    private java.io.File testFile;

    @Before
    public void before() throws IOException {
        String tempDir = System.getProperty("java.io.tmpdir");
        testFile = new java.io.File(tempDir + "some-test-file-for-md5-" + System.currentTimeMillis() + ".properties");
        FileUtils.writeStringToFile(testFile, "Some test file data to store.", Charset.defaultCharset(), false);
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
    public void testEqualsWhereFileAndPathIsNotEqualWhereLengthIsEqualAndRemoteLastModifiedIsLower() {
        Path path = Paths.get(testFile.getPath());
        File file = mock(File.class);
        when(file.getModifiedTime()).thenReturn(new DateTime(path.toFile().lastModified() - 1000));
        when(file.getSize()).thenReturn(path.toFile().length());

        boolean result = EqualUtils.equals(file, path);

        assertFalse(result);
    }

    @Test
    public void testEqualsWhereFileAndPathIsNotEqualWhereLengthIsNotEqual() {
        Path path = Paths.get(testFile.getPath());
        File file = mock(File.class);
        when(file.getModifiedTime()).thenReturn(new DateTime(path.toFile().lastModified()));
        when(file.getSize()).thenReturn(path.toFile().length() + 1);

        boolean result = EqualUtils.equals(file, path);

        assertFalse(result);
    }

    @Test
    public void testEqualsWhereFileAndPathIsEqualWhereLengthIsEqualAndLastModifiedIsEqual() {
        Path path = Paths.get(testFile.getPath());
        File file = mock(File.class);
        when(file.getModifiedTime()).thenReturn(new DateTime(path.toFile().lastModified()));
        when(file.getSize()).thenReturn(path.toFile().length());

        boolean result = EqualUtils.equals(file, path);

        assertTrue(result);
    }

    @Test
    public void testEqualsWhereFileAndPathIsEqualWhereLengthIsEqualAndRemoteLastModifiedIsGreater() {
        Path path = Paths.get(testFile.getPath());
        File file = mock(File.class);
        when(file.getModifiedTime()).thenReturn(new DateTime(path.toFile().lastModified() + 1000));
        when(file.getSize()).thenReturn(path.toFile().length());

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