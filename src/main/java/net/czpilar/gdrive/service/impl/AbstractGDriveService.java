package net.czpilar.gdrive.service.impl;

import static net.czpilar.gdrive.constant.GDriveConstants.*;

import java.util.Arrays;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import net.czpilar.gdrive.credential.IGDriveCredential;

/**
 * @author David Pilař (david@czpilar.net)
 */
public abstract class AbstractGDriveService {

	private final IGDriveCredential gdriveCredential;

	private GoogleAuthorizationCodeFlow flow;

	protected AbstractGDriveService(IGDriveCredential gdriveCredential) {
		this.gdriveCredential = gdriveCredential;
	}

	protected GoogleAuthorizationCodeFlow getFlow() {
		if (flow == null) {
			flow = new GoogleAuthorizationCodeFlow.Builder(new NetHttpTransport(), new JacksonFactory(),
					CLIENT_ID, CLIENT_SECRET, Arrays.asList(DriveScopes.DRIVE)).build();
		}
		return flow;
	}

	protected Drive getDrive() {
		Drive.Builder builder = new Drive.Builder(new NetHttpTransport(), new JacksonFactory(), getCredential());
		builder.setApplicationName(APP_NAME);
		return builder.build();
	}

	protected IGDriveCredential getGdriveCredential() {
		return gdriveCredential;
	}

	protected Credential getCredential() {
		return gdriveCredential.getCredential();
	}

	protected String getUploadDir(String uploadDirName) {
		if (uploadDirName == null) {
			uploadDirName = gdriveCredential.getUploadDir();
		}
		return uploadDirName;
	}
}
