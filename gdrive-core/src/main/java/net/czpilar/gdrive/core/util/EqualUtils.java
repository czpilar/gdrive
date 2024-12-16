package net.czpilar.gdrive.core.util;

import com.google.api.services.drive.model.File;

import java.nio.file.Path;

/**
 * Equal utility class.
 *
 * @author David Pilar (david@czpilar.net)
 */
public class EqualUtils {

    /**
     * Returns true if lengths are equal and remote modified time is greater or equal to local file, otherwise returns false.
     *
     * @param file       file
     * @param pathToFile path to file
     * @return true if equals
     */
    public static boolean equals(File file, Path pathToFile) {
        boolean result = false;
        if (file != null && pathToFile != null) {
            java.io.File localFile = pathToFile.toFile();
            if (localFile.exists()) {
                result = file.getSize() == localFile.length()
                        && toSeconds(file.getModifiedTime().getValue()) >= toSeconds(localFile.lastModified());
            }
        }
        return result;
    }

    /**
     * This method strips milliseconds and returns seconds.
     *
     * @param milliseconds milliseconds
     * @return seconds
     */
    private static long toSeconds(long milliseconds) {
        return milliseconds / 1000;
    }

    /**
     * Returns true if md5 checksum of content of given inputs is not equal, otherwise returns false.
     *
     * @param file       file
     * @param pathToFile path to file
     * @return true if equals
     */
    public static boolean notEquals(File file, Path pathToFile) {
        return !equals(file, pathToFile);
    }

}
