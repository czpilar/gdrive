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
class CredentialLoaderTest {

    private CredentialLoader loader;

    @Mock
    private IGDriveCredential gDriveCredential;

    private AutoCloseable autoCloseable;

    @BeforeEach
    void before() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        loader = new CredentialLoader(gDriveCredential);
    }

    @AfterEach
    void after() throws Exception {
        autoCloseable.close();
    }

    @Test
    void testGetRefreshTokenWhereNoCredentialLoaded() {
        assertThrows(NoCredentialFoundException.class, () -> new CredentialLoader(null));
    }

    @Test
    void testGetRefreshToken() {
        when(gDriveCredential.getRefreshToken()).thenReturn("test-refresh-token");

        String result = loader.getRefreshToken();

        assertNotNull(result);
        assertEquals("test-refresh-token", result);

        verify(gDriveCredential).getRefreshToken();

        verifyNoMoreInteractions(gDriveCredential);
    }

    @Test
    void testGetRefreshTokenReturnsNull() {
        when(gDriveCredential.getRefreshToken()).thenReturn(null);

        String result = loader.getRefreshToken();

        assertNull(result);

        verify(gDriveCredential).getRefreshToken();

        verifyNoMoreInteractions(gDriveCredential);
    }
}
