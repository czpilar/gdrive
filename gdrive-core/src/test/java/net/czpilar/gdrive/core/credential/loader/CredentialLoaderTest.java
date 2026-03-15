package net.czpilar.gdrive.core.credential.loader;

import net.czpilar.gdrive.core.credential.IGDriveCredential;
import net.czpilar.gdrive.core.exception.NoCredentialFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author David Pilar (david@czpilar.net)
 */
public class CredentialLoaderTest {

    private CredentialLoader loader;

    @Mock
    private IGDriveCredential gDriveCredential;

    private AutoCloseable autoCloseable;

    @BeforeEach
    public void before() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        loader = new CredentialLoader(gDriveCredential);
    }

    @AfterEach
    public void after() throws Exception {
        autoCloseable.close();
    }

    @Test
    public void testGetRefreshTokenWhereNoCredentialLoaded() {
        assertThrows(NoCredentialFoundException.class, () -> new CredentialLoader(null).getRefreshToken());
    }

    @Test
    public void testGetRefreshToken() {
        when(gDriveCredential.getRefreshToken()).thenReturn("test-refresh-token");

        String result = loader.getRefreshToken();

        assertNotNull(result);
        assertEquals("test-refresh-token", result);

        verify(gDriveCredential).getRefreshToken();

        verifyNoMoreInteractions(gDriveCredential);
    }

    @Test
    public void testGetRefreshTokenReturnsNull() {
        when(gDriveCredential.getRefreshToken()).thenReturn(null);

        String result = loader.getRefreshToken();

        assertNull(result);

        verify(gDriveCredential).getRefreshToken();

        verifyNoMoreInteractions(gDriveCredential);
    }
}
