package net.czpilar.gdrive.core.service;

import java.util.List;

import com.google.api.services.drive.model.File;

/**
 * File service interface.
 *
 * @author David Pilar (david@czpilar.net)
 */
public interface IFileService {

	/**
	 * Upload file to root directory.
	 *
	 * @param filename
	 * @return uploaded file
	 */
	File uploadFile(String filename);

	/**
	 * Upload file to directory specified by pathname starting on root level.
	 * If no directory is found the whole directory path is created.
	 *
	 * @param filename
	 * @param pathname
	 * @return uploaded file
	 */
	File uploadFile(String filename, String pathname);

	/**
	 * Upload file to directory.
	 *
	 * @param filename
	 * @param parentDir
	 * @return uploaded file
	 */
	File uploadFile(String filename, File parentDir);

	/**
	 * Upload files to root directory.
	 *
	 * @param filenames
	 * @return uploaded files
	 */
	List<File> uploadFiles(List<String> filenames);

	/**
	 * Upload files to directory specified by pathname starting on root level.
	 * If no directory is found the whole directory path is created.
	 *
	 * @param filenames
	 * @param pathname
	 * @return uploaded files
	 */
	List<File> uploadFiles(List<String> filenames, String pathname);

	/**
	 * Upload files to directory.
	 *
	 * @param filenames
	 * @param parentDir
	 * @return uploaded files
	 */
	List<File> uploadFiles(List<String> filenames, File parentDir);
}
