package net.czpilar.gdrive.core.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.ParentReference;
import net.czpilar.gdrive.core.exception.FileUploadException;
import net.czpilar.gdrive.core.listener.FileUploadProgressListener;
import net.czpilar.gdrive.core.service.IFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 * File service with methods for uploading file(s) to Google Drive.
 *
 * @author David Pilař (david@czpilar.net)
 */
public class FileService extends AbstractService implements IFileService {

	private static final Logger LOG = LoggerFactory.getLogger(FileService.class);

	private ApplicationContext applicationContext;

	@Autowired
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	protected Drive getDrive() {
		return applicationContext.getBean(Drive.class);
	}

	protected String getUploadDir(String uploadDirName) {
		if (uploadDirName == null) {
			uploadDirName = getGDriveCredential().getUploadDir();
		}
		return uploadDirName;
	}

	protected File findParent(String parentName, Drive drive) throws IOException {
		if (parentName == null) {
			return null;
		}
		List<File> items = drive.files().list().setQ("title='" + parentName + "' and mimeType = 'application/vnd.google-apps.folder'").execute().getItems();
		if (items == null) {
			return null;
		}
		for (File item : items) {
			if (!Boolean.TRUE.equals(item.getExplicitlyTrashed())) {
				List<ParentReference> parents = item.getParents();
				if (parents == null) {
					continue;
				}
				for (ParentReference reference : parents) {
					if (Boolean.TRUE.equals(reference.getIsRoot())) {
						return item;
					}
				}
			}
		}
		return null;
	}

	protected File findOrCreateParent(String parentDir, Drive drive) {
		if (parentDir == null) {
			return null;
		}
		try {
			File parent = findParent(parentDir, drive);
			if (parent == null) {
				File body = new File();
				body.setTitle(parentDir);
				body.setMimeType("application/vnd.google-apps.folder");
				parent = drive.files().insert(body).execute();
			}
			return parent;
		} catch (IOException e) {
			LOG.warn("Cannot find or create parent dir. Using root dir.", e);
			return null;
		}
	}

	protected File uploadFile(String filename, File parent, Drive drive) {
		LOG.info("Uploading file {}", filename);
		try {
			Path path = Paths.get(filename);

			File body = new File();
			body.setTitle(path.getFileName().toString());
			body.setMimeType(Files.probeContentType(path));
			if (parent != null) {
				body.setParents(Arrays.asList(new ParentReference().setId(parent.getId())));
			}

			Drive.Files.Insert insert = drive.files().insert(body, new FileContent(body.getMimeType(), path.toFile()));
			insert.getMediaHttpUploader().setDirectUploadEnabled(false);
			insert.getMediaHttpUploader().setProgressListener(new FileUploadProgressListener(filename));
			File file = insert.execute();
			LOG.info("Finished uploading file {} - remote file ID is {}", filename, file.getId());
			return file;
		} catch (IOException e) {
			LOG.error("File {} was not uploaded.", filename);
			throw new FileUploadException("File was not uploaded.", e);
		}
	}

	protected void uploadFiles(List<String> filenames, File parent, Drive drive) {
		if (filenames != null) {
			for (String filename : filenames) {
				try {
					uploadFile(filename, parent, drive);
				} catch (FileUploadException e) {
					LOG.error("Error during uploading file.", e);
				}
			}
		}
	}

	@Override
	public File uploadFile(String filename, String parentDir) {
		Drive drive = getDrive();
		return uploadFile(filename, findOrCreateParent(getUploadDir(parentDir), drive), drive);
	}

	@Override
	public File uploadFile(String filename, File parent) {
		return uploadFile(filename, parent, getDrive());
	}

	@Override
	public File uploadFile(String filename) {
		return uploadFile(filename, (File) null);
	}

	@Override
	public void uploadFiles(List<String> filenames) {
		uploadFiles(filenames, (File) null);
	}

	@Override
	public void uploadFiles(List<String> filenames, String parentDir) {
		Drive drive = getDrive();
		uploadFiles(filenames, findOrCreateParent(getUploadDir(parentDir), drive), drive);
	}

	@Override
	public void uploadFiles(List<String> filenames, File parent) {
		uploadFiles(filenames, parent, getDrive());
	}
}
