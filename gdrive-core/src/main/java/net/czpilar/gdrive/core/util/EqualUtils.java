package net.czpilar.gdrive.core.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;

import com.google.api.services.drive.model.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Equal utility class.
 *
 * @author David Pilar (david@czpilar.net)
 */
public class EqualUtils {

    private static final Logger LOG = LoggerFactory.getLogger(EqualUtils.class);

    /**
     * Returns true if md5 checksum of content of given inputs is equal, otherwise returns false.
     *
     * @param file
     * @param pathToFile
     * @return
     */
    public static boolean equals(File file, Path pathToFile) {
        boolean result = false;
        if (file != null && pathToFile != null) {
            try {
                String localMd5Checksum = org.apache.commons.codec.digest.DigestUtils.md5Hex(new FileInputStream(pathToFile.toFile()));
                result = localMd5Checksum.equals(file.getMd5Checksum());
            } catch (IOException e) {
                LOG.error("Failed to calculating MD5 checksum.", e);
                result = false;
            }
        }
        return result;
    }

    /**
     * Returns true if md5 checksum of content of given inputs is not equal, otherwise returns false.
     *
     * @param file
     * @param pathToFile
     * @return
     */
    public static boolean notEquals(File file, Path pathToFile) {
        return !equals(file, pathToFile);
    }

}
