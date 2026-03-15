package net.czpilar.gdrive.core.credential;

/**
 * gDrive credential interface.
 *
 * @author David Pilar (david@czpilar.net)
 */
public interface IGDriveCredential {

    /**
     * Returns refresh token.
     *
     * @return refresh token
     */
    String getRefreshToken();

    /**
     * Saves refresh token.
     *
     * @param refreshToken refresh token
     */
    void saveRefreshToken(String refreshToken);

    /**
     * Returns upload dir.
     *
     * @return upload directory
     */
    String getUploadDir();

}
