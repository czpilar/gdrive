package net.czpilar.gdrive.core.service.impl;

import java.io.IOException;
import java.util.Arrays;

import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.ParentReference;
import net.czpilar.gdrive.core.exception.DirectoryHandleException;
import net.czpilar.gdrive.core.service.IDirectoryService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * Service with methods for handling directories in Google Drive.
 *
 * @author David Pilar (david@czpilar.net)
 */
public class DirectoryService extends AbstractFileService implements IDirectoryService {

    private static final String DIRECTORY_SEPARATOR = "/";

    private static final Logger LOG = LoggerFactory.getLogger(DirectoryService.class);

    private static String normalizePathname(String pathname) {
        return StringUtils.replace(pathname, "\\", DIRECTORY_SEPARATOR);
    }

    private static String getCurrentDirname(String pathname) {
        return StringUtils.trimToNull(StringUtils.substringBefore(pathname, DIRECTORY_SEPARATOR));
    }

    private static String getNextPathname(String pathname) {
        return StringUtils.trimToNull(StringUtils.substringAfter(pathname, DIRECTORY_SEPARATOR));
    }

    protected File createOneDirectory(String dirname, File parentDir) {
        Assert.notNull(dirname);

        File directory = new File();
        directory.setTitle(dirname);
        directory.setMimeType(DIRECTORY_MIME_TYPE);
        if (parentDir != null) {
            directory.setParents(Arrays.asList(new ParentReference().setId(parentDir.getId())));
        }
        try {
            return getDrive().files().insert(directory).execute();
        } catch (IOException e) {
            LOG.error("Unable to create directory {}.", dirname);
            throw new DirectoryHandleException("Unable to create directory.", e);
        }
    }

    protected File findOrCreateOneDirectory(String dirname, File parentDir) {
        File dir = findFile(dirname, parentDir, true);
        if (dir == null) {
            dir = createOneDirectory(dirname, parentDir);
        }
        return dir;
    }

    @Override
    public File findDirectory(String pathname) {
        return findDirectory(pathname, null);
    }

    @Override
    public File findDirectory(String pathname, File parentDir) {
        pathname = normalizePathname(pathname);
        String dirname = getCurrentDirname(pathname);
        File currentDir = parentDir;
        if (dirname != null) {
            currentDir = findFile(dirname, parentDir, true);
        }
        String nextPathname = getNextPathname(pathname);
        if (currentDir != null && nextPathname != null) {
            currentDir = findDirectory(nextPathname, currentDir);
        }
        return currentDir;
    }

    @Override
    public File findOrCreateDirectory(String pathname) {
        return findOrCreateDirectory(pathname, null);
    }

    @Override
    public File findOrCreateDirectory(String pathname, File parentDir) {
        pathname = normalizePathname(pathname);
        String dirname = getCurrentDirname(pathname);
        File currentDir = parentDir;
        if (dirname != null) {
            currentDir = findOrCreateOneDirectory(dirname, parentDir);
        }
        String nextPathname = getNextPathname(pathname);
        if (nextPathname != null) {
            currentDir = findOrCreateDirectory(nextPathname, currentDir);
        }
        return currentDir;
    }
}
