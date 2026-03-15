package net.czpilar.gdrive.core.credential.impl;

/**
 * Simple implementation of {@link net.czpilar.gdrive.core.credential.IGDriveCredential} interface
 * for gDrive credential.
 *
 * @author David Pilar (david@czpilar.net)
 */
public class SimpleGDriveCredential extends AbstractGDriveCredential {

    private String refreshToken;
    private String uploadDir;

    @Override
    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @Override
    public void saveRefreshToken(String refreshToken) {
        setRefreshToken(refreshToken);
    }

    @Override
    public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }
}
