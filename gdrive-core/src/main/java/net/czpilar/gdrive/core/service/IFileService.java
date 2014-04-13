package net.czpilar.gdrive.core.service;

import java.util.List;

import com.google.api.services.drive.model.File;

/**
 * File service interface.
 *
 * @author David Pilař (david@czpilar.net)
 */
public interface IFileService {

	/**
	 * Upload file to root directory.
	 *
	 * @param filename
	 * @return
	 */
	File uploadFile(String filename);

	/**
	 * Upload file to directory specified by name.
	 * Directory has to be child of root directory.
	 * If no directory is found new one is created.
	 *
	 * @param filename
	 * @param parentDir
	 * @return
	 */
	File uploadFile(String filename, String parentDir);

	/**
	 * Upload file to directory.
	 *
	 * @param filename
	 * @param parent
	 * @return
	 */
	File uploadFile(String filename, File parent);

	/**
	 * Upload files to root directory.
	 *
	 * @param filenames
	 */
	void uploadFiles(List<String> filenames);

	/**
	 * Upload files to directory specified by name.
	 * Directory has to be child of root directory.
	 * If no directory is found new one is created.
	 *
	 * @param filenames
	 * @param parentDir
	 * @return
	 */
	void uploadFiles(List<String> filenames, String parentDir);

	/**
	 * Upload files to directory.
	 *
	 * @param filenames
	 * @param parent
	 * @return
	 */
	void uploadFiles(List<String> filenames, File parent);
}
