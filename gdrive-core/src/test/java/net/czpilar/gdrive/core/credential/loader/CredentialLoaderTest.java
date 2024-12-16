package net.czpilar.gdrive.core.credential.loader;

import com.google.api.client.auth.oauth2.Credential;
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

    @Mock
    private Credential credential;

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
    public void testGetCredentialWhereNoCredentialLoaded() {
        assertThrows(NoCredentialFoundException.class, () -> new CredentialLoader(null).getCredential());
    }

    @Test
    public void testGetCredential() {
        when(gDriveCredential.getCredential()).thenReturn(credential);

        Credential result = loader.getCredential();

        assertNotNull(result);
        assertEquals(credential, result);

        verify(gDriveCredential).getCredential();

        verifyNoMoreInteractions(gDriveCredential);
        verifyNoInteractions(credential);
    }
}
