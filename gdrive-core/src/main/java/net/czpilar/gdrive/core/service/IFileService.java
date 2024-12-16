package net.czpilar.gdrive.core.service;

import com.google.api.services.drive.model.File;

import java.util.List;

/**
 * File service interface.
 *
 * @author David Pilar (david@czpilar.net)
 */
public interface IFileService {

    /**
     * Upload file to root directory.
     *
     * @param filename filename
     * @return uploaded file
     */
    File uploadFile(String filename);

    /**
     * Upload file to directory specified by pathname starting on root level.
     * If no directory is found the whole directory path is created.
     *
     * @param pathToFile path to file
     * @param pathname   path name
     * @return uploaded file
     */
    File uploadFile(String pathToFile, String pathname);

    /**
     * Upload file to directory specified by given filename.
     * Inserts new file if remote file does not exist or updates remote file if content
     * was changed or do nothing if remote file has the same content as local file.
     *
     * @param filename  filename
     * @param parentDir parent directory
     * @return uploaded file
     */
    File uploadFile(String filename, File parentDir);

    /**
     * Upload files to root directory.
     *
     * @param filenames filenames
     * @return uploaded files
     */
    List<File> uploadFiles(List<String> filenames);

    /**
     * Upload files to directory specified by pathname starting on root level.
     * If no directory is found the whole directory path is created.
     *
     * @param filenames filenames
     * @param pathname  path name
     * @return uploaded files
     */
    List<File> uploadFiles(List<String> filenames, String pathname);

    /**
     * Upload files to directory.
     *
     * @param filenames filenames
     * @param parentDir parent directory
     * @return uploaded files
     */
    List<File> uploadFiles(List<String> filenames, File parentDir);
}
