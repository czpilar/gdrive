package net.czpilar.gdrive.core.credential.loader;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.api.client.auth.oauth2.Credential;
import net.czpilar.gdrive.core.credential.IGDriveCredential;
import net.czpilar.gdrive.core.exception.NoCredentialFoundException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * @author David Pilar (david@czpilar.net)
 */
public class CredentialLoaderTest {

    private CredentialLoader loader;

    @Mock
    private IGDriveCredential gDriveCredential;

    @Mock
    private Credential credential;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        loader = new CredentialLoader();
        loader.setGDriveCredential(gDriveCredential);
    }

    @Test(expected = NoCredentialFoundException.class)
    public void testGetCredentialWhereNoCredentialLoaded() {
        loader.setGDriveCredential(null);
        loader.getCredential();
    }

    @Test
    public void testGetCredential() {
        when(gDriveCredential.getCredential()).thenReturn(credential);

        Credential result = loader.getCredential();

        assertNotNull(result);
        assertEquals(credential, result);

        verify(gDriveCredential).getCredential();

        verifyNoMoreInteractions(gDriveCredential);
        verifyZeroInteractions(credential);
    }
}
