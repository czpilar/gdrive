package net.czpilar.gdrive.service.impl;

import static net.czpilar.gdrive.constant.GDriveConstants.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import net.czpilar.gdrive.exception.PropertiesFileException;

/**
 * @author David Pilař (david@czpilar.net)
 */
public abstract class AbstractGDriveService {

	private final HttpTransport httpTransport = new NetHttpTransport();

	private final JsonFactory jsonFactory = new JacksonFactory();

	private final String propertyFile;

	private final Properties properties;

	private GoogleAuthorizationCodeFlow flow;

	private Credential credential;

	protected AbstractGDriveService(String propertyFile) {
		this.propertyFile = propertyFile;
		properties = new Properties();
		try {
			loadProperties();
		} catch (PropertiesFileException e) {
			saveProperties();
		}

		if (!checkProperties()) {
			saveProperties();
		}
	}

	private boolean checkProperties() {
		return checkProperty(KEY_UPLOAD_DIR, UPLOAD_DIR);
	}

	private boolean checkProperty(String propertyName, String defaultValue) {
		boolean hasProps = properties.getProperty(propertyName) != null;
		if (!hasProps) {
			properties.setProperty(propertyName, defaultValue);
		}
		return hasProps;
	}

	private void saveProperties() {
		try {
			properties.store(new FileOutputStream(propertyFile), "gdrive properties file");
		} catch (IOException e) {
			throw new PropertiesFileException("Cannot save properties file.", e);
		}
	}

	private void loadProperties() {
		try {
			properties.load(new FileInputStream(propertyFile));
		} catch (IOException e) {
			throw new PropertiesFileException("Cannot load properties file.", e);
		}
	}

	protected GoogleAuthorizationCodeFlow getFlow() {
		if (flow == null) {
			flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, jsonFactory,
					CLIENT_ID, CLIENT_SECRET, Arrays.asList(DriveScopes.DRIVE)).build();
		}
		return flow;
	}

	protected Drive getDrive() {
		Drive.Builder builder = new Drive.Builder(httpTransport, jsonFactory, getCredential());
		builder.setApplicationName(APP_NAME);
		return builder.build();
	}

	protected void storeCredential(Credential credential) {
		properties.setProperty(KEY_ACCESS_TOKEN, credential.getAccessToken());
		properties.setProperty(KEY_REFRESH_TOKEN, credential.getRefreshToken());
		saveProperties();
		this.credential = credential;
	}

	private Credential buildCredential(String accessToken, String refershToken) {
		GoogleCredential credential = new GoogleCredential.Builder()
				.setJsonFactory(jsonFactory).setTransport(httpTransport)
				.setClientSecrets(CLIENT_ID, CLIENT_SECRET).build();
		return credential.setAccessToken(accessToken).setRefreshToken(refershToken);
	}

	protected Credential getCredential() {
		if (credential == null) {
			credential = buildCredential(properties.getProperty(KEY_ACCESS_TOKEN),
					properties.getProperty(KEY_REFRESH_TOKEN));
		}
		return credential;
	}

	protected String getUploadDir(String uploadDirName) {
		if (uploadDirName == null) {
			uploadDirName = properties.getProperty(KEY_UPLOAD_DIR);
		}
		return uploadDirName;
	}
}
