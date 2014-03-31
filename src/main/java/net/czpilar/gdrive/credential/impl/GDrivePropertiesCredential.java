package net.czpilar.gdrive.credential.impl;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import net.czpilar.gdrive.exception.PropertiesFileException;

/**
 * @author David Pilař (david@czpilar.net)
 */
public class GDrivePropertiesCredential extends AbstractGDriveCredential {

	public static final String KEY_UPLOAD_DIR = "gdrive.uploadDir";
	public static final String KEY_ACCESS_TOKEN = "gdrive.accessToken";
	public static final String KEY_REFRESH_TOKEN = "gdrive.refreshToken";
	public static final String DEFAULT_UPLOAD_DIR = "gdrive-uploads";

	private final String propertyFile;
	private final Properties properties;

	public GDrivePropertiesCredential(String propertyFile) {
		this.propertyFile = propertyFile;
		properties = new Properties();
		try {
			loadProperties();
		} catch (PropertiesFileException e) {
			saveProperties();
		}

		if (getUploadDir() == null) {
			setUploadDir(DEFAULT_UPLOAD_DIR);
			saveProperties();
		}
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

	@Override
	public String getAccessToken() {
		return properties.getProperty(KEY_ACCESS_TOKEN);
	}

	public void setAccessToken(String accessToken) {
		properties.setProperty(KEY_ACCESS_TOKEN, accessToken);
	}

	@Override
	public String getRefreshToken() {
		return properties.getProperty(KEY_REFRESH_TOKEN);
	}

	public void setRefreshToken(String refreshToken) {
		properties.setProperty(KEY_REFRESH_TOKEN, refreshToken);
	}

	@Override
	public void saveTokens(String accessToken, String refreshToken) {
		setAccessToken(accessToken);
		setRefreshToken(refreshToken);
		saveProperties();
	}

	@Override
	public String getUploadDir() {
		return properties.getProperty(KEY_UPLOAD_DIR);
	}

	public void setUploadDir(String uploadDir) {
		properties.setProperty(KEY_UPLOAD_DIR, uploadDir);
	}
}
