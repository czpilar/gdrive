package net.czpilar.gdrive.cmd.credential;

import net.czpilar.gdrive.cmd.exception.PropertiesFileException;
import net.czpilar.gdrive.core.credential.impl.AbstractGDriveCredential;
import org.springframework.util.Assert;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Implementation of gDrive credential using
 * a properties file to store tokens and upload dir name.
 *
 * @author David Pilar (david@czpilar.net)
 */
public class PropertiesGDriveCredential extends AbstractGDriveCredential {

    private final String uploadDirPropertyKey;
    private final String refreshTokenPropertyKey;
    private final String defaultUploadDir;

    private String propertyFile;
    private Properties properties;

    public PropertiesGDriveCredential(String uploadDirPropertyKey, String refreshTokenPropertyKey, String defaultUploadDir) {
        this.uploadDirPropertyKey = uploadDirPropertyKey;
        this.refreshTokenPropertyKey = refreshTokenPropertyKey;
        this.defaultUploadDir = defaultUploadDir;
    }

    public void setPropertyFile(String propertyFile) {
        this.propertyFile = propertyFile;
    }

    protected Properties getProperties() {
        Assert.notNull(propertyFile, "Missing property file name.");
        if (properties == null) {
            properties = new Properties();
            try {
                loadProperties();
            } catch (PropertiesFileException e) {
                saveProperties();
            }

            if (getUploadDir() == null) {
                setUploadDir(defaultUploadDir);
                saveProperties();
            }
        }
        return properties;
    }

    private void saveProperties() {
        try {
            try (FileOutputStream out = new FileOutputStream(propertyFile)) {
                getProperties().store(out, "gdrive properties file");
            }
        } catch (IOException e) {
            throw new PropertiesFileException("Cannot save properties file.", e);
        }
    }

    private void loadProperties() {
        try {
            try (FileInputStream is = new FileInputStream(propertyFile)) {
                getProperties().load(is);
            }
        } catch (IOException e) {
            throw new PropertiesFileException("Cannot load properties file.", e);
        }
    }

    @Override
    public String getRefreshToken() {
        return getProperties().getProperty(refreshTokenPropertyKey);
    }

    public void setRefreshToken(String refreshToken) {
        getProperties().setProperty(refreshTokenPropertyKey, refreshToken);
    }

    @Override
    public void saveRefreshToken(String refreshToken) {
        setRefreshToken(refreshToken);
        saveProperties();
    }

    @Override
    public String getUploadDir() {
        return getProperties().getProperty(uploadDirPropertyKey);
    }

    public void setUploadDir(String uploadDir) {
        getProperties().setProperty(uploadDirPropertyKey, uploadDir);
    }
}
