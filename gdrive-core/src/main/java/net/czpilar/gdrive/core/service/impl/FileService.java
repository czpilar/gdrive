package net.czpilar.gdrive.core.service.impl;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveRequest;
import com.google.api.services.drive.model.File;
import net.czpilar.gdrive.core.exception.FileHandleException;
import net.czpilar.gdrive.core.listener.FileUploadProgressListener;
import net.czpilar.gdrive.core.service.IDirectoryService;
import net.czpilar.gdrive.core.service.IFileService;
import net.czpilar.gdrive.core.util.EqualUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Service with methods for handling files in Google Drive.
 *
 * @author David Pilar (david@czpilar.net)
 */
@Service
public class FileService extends AbstractFileService implements IFileService {

    private static final Logger LOG = LoggerFactory.getLogger(FileService.class);

    private final int retries;

    private IDirectoryService directoryService;

    public FileService(@Value("${gdrive.file.upload.retries}") int retries) {
        this.retries = retries;
    }

    public int getRetries() {
        return retries;
    }

    @Autowired
    public void setDirectoryService(IDirectoryService directoryService) {
        this.directoryService = directoryService;
    }

    protected IDirectoryService getDirectoryService() {
        return directoryService;
    }

    protected String getUploadDir(String uploadDirname) {
        if (uploadDirname == null) {
            uploadDirname = getGDriveCredential().getUploadDir();
        }
        return uploadDirname;
    }

    @Override
    public File uploadFile(String filename, String pathname) {
        File parentDir = getDirectoryService().findOrCreateDirectory(getUploadDir(pathname));
        return uploadFile(filename, parentDir);
    }

    private File insertFile(Path pathToFile, File parentDir) throws IOException {
        String filename = pathToFile.getFileName().toString();

        LOG.info("Uploading new file {}", filename);

        File file = new File()
                .setName(filename)
                .setMimeType(Files.probeContentType(pathToFile));
        if (parentDir != null) {
            file.setParents(Arrays.asList(parentDir.getId()));
        }

        Drive.Files.Create insert = getDrive().files().create(file,
                new FileContent(file.getMimeType(), pathToFile.toFile()));
        insert.getMediaHttpUploader().setDirectUploadEnabled(false)
                .setProgressListener(new FileUploadProgressListener(filename));

        return execute(insert);
    }

    private File updateFile(File currentFile, Path pathToFile) throws IOException {
        String filename = pathToFile.getFileName().toString();

        LOG.info("Uploading updated file {}", filename);

        File uploadFile = new File()
                .setName(currentFile.getName())
                .setMimeType(currentFile.getMimeType());

        Drive.Files.Update update = getDrive().files().update(currentFile.getId(), uploadFile,
                new FileContent(currentFile.getMimeType(), pathToFile.toFile()));
        update.getMediaHttpUploader().setDirectUploadEnabled(false)
                .setProgressListener(new FileUploadProgressListener(filename));
        return execute(update);
    }

    private File execute(DriveRequest<File> driveRequest) throws IOException {
        int retry = 0;
        while (true) {
            try {
                return driveRequest.execute();
            } catch (GoogleJsonResponseException e) {
                retry++;
                if (retry > getRetries()) {
                    throw e;
                }
                LOG.warn("Error during executing drive request, retrying for {} time(s), status message: {}, error response: {}", retry, e.getStatusMessage(), e.getDetails());
            }
        }
    }

    @Override
    public File uploadFile(String filename, File parentDir) {
        try {
            Path pathToFile = Paths.get(filename);
            File currentFile = findFile(pathToFile.getFileName().toString(), parentDir, false);

            if (currentFile == null) {
                currentFile = insertFile(pathToFile, parentDir);
            } else if (EqualUtils.notEquals(currentFile, pathToFile)) {
                currentFile = updateFile(currentFile, pathToFile);
            } else {
                LOG.info("There is nothing to upload.");
            }
            LOG.info("Finished uploading file {} - remote file ID is {}", filename, currentFile.getId());
            return currentFile;
        } catch (IOException e) {
            LOG.error("Unable to upload file {}.", filename);
            throw new FileHandleException("Unable to upload file.", e);
        }
    }

    @Override
    public File uploadFile(String filename) {
        return uploadFile(filename, (File) null);
    }

    @Override
    public List<File> uploadFiles(List<String> filenames) {
        return uploadFiles(filenames, (File) null);
    }

    @Override
    public List<File> uploadFiles(List<String> filenames, String pathname) {
        File parentDir = getDirectoryService().findOrCreateDirectory(getUploadDir(pathname));
        return uploadFiles(filenames, parentDir);
    }

    @Override
    public List<File> uploadFiles(List<String> filenames, File parentDir) {
        List<File> files = new ArrayList<>();
        if (filenames != null) {
            for (String filename : filenames) {
                try {
                    files.add(uploadFile(filename, parentDir));
                } catch (FileHandleException e) {
                    LOG.error("Error during uploading file.", e);
                }
            }
        }
        return files;
    }
}
