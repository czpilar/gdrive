package net.czpilar.gdrive.service.impl;

import static net.czpilar.gdrive.constant.GDriveConstants.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.ParentReference;
import net.czpilar.gdrive.credential.IGDriveCredential;
import net.czpilar.gdrive.exception.AuthorizationFailedException;
import net.czpilar.gdrive.exception.FileUploadException;
import net.czpilar.gdrive.listener.FileUploadProgressListener;
import net.czpilar.gdrive.service.IGDriveAuthorizationService;
import net.czpilar.gdrive.service.IGDriveFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author David Pilař (david@czpilar.net)
 */
public class GDriveService extends AbstractGDriveService implements IGDriveAuthorizationService, IGDriveFileService {

	private static final Logger LOG = LoggerFactory.getLogger(GDriveService.class);

	public GDriveService(IGDriveCredential gdriveCredential) {
		super(gdriveCredential);
	}

	@Override
	public String getAuthorizationURL() {
		return getFlow().newAuthorizationUrl().setRedirectUri(REDIRECT_URI).build();
	}

	@Override
	public Credential authorize(String authorizationCode) {
		try {
			GoogleTokenResponse response = getFlow().newTokenRequest(authorizationCode).setRedirectUri(REDIRECT_URI).execute();
			Credential credential = getFlow().createAndStoreCredential(response, null);
			getGdriveCredential().saveCredential(credential);
			return credential;
		} catch (IOException e) {
			throw new AuthorizationFailedException("Error occures during authorization process.", e);
		}
	}

	private File findParent(String parentName) throws IOException {
		List<File> items = getDrive().files().list().setQ("title='" + parentName + "' and mimeType = 'application/vnd.google-apps.folder'").execute().getItems();
		for (File item : items) {
			if (!Boolean.TRUE.equals(item.getExplicitlyTrashed())) {
				for (ParentReference reference : item.getParents()) {
					if (reference.getIsRoot()) {
						return item;
					}
				}
			}
		}
		return null;
	}

	private File findOrCreateParent(String parentDir) {
		File parent = null;
		if (parentDir != null) {
			try {
				parent = findParent(parentDir);
				if (parent == null) {
					File body = new File();
					body.setTitle(parentDir);
					body.setMimeType("application/vnd.google-apps.folder");
					parent = getDrive().files().insert(body).execute();
				}
			} catch (IOException e) {
				LOG.warn("Cannot find or create parent dir. Using root dir.", e);
			}
		}
		return parent;
	}

	@Override
	public File uploadFile(String filename, String parentDir) {
		return uploadFile(filename, findOrCreateParent(getUploadDir(parentDir)));
	}

	@Override
	public File uploadFile(String filename, File parent) {
		LOG.info("Uploading file {}", filename);
		try {
			Path path = Paths.get(filename);

			File body = new File();
			body.setTitle(path.getFileName().toString());
			body.setMimeType(Files.probeContentType(path));
			if (parent != null) {
				body.setParents(Arrays.asList(new ParentReference().setId(parent.getId())));
			}

			Drive.Files.Insert insert = getDrive().files().insert(body, new FileContent(body.getMimeType(), path.toFile()));
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
		uploadFiles(filenames, findOrCreateParent(getUploadDir(parentDir)));

	}

	@Override
	public void uploadFiles(List<String> filenames, File parent) {
		for (String filename : filenames) {
			try {
				uploadFile(filename, parent);
			} catch (FileUploadException e) {
				LOG.error("Error during uploading file.", e);
			}
		}
	}
}
