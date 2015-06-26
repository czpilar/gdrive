package net.czpilar.gdrive.core.listener;

import static org.mockito.Mockito.*;

import java.io.IOException;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import net.czpilar.gdrive.core.credential.IGDriveCredential;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * @author David Pilar (david@czpilar.net)
 */
public class CredentialSaverListenerTest {

    private CredentialSaverListener listener;

    @Mock
    private IGDriveCredential gDriveCredential;

    @Mock
    private Credential credential;

    @Mock
    private TokenResponse tokenResponse;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        listener = new CredentialSaverListener();
        listener.setGDriveCredential(gDriveCredential);
    }

    @Test
    public void testOnCredentialCreated() throws IOException {
        listener.onCredentialCreated(credential, tokenResponse);

        verify(gDriveCredential).saveCredential(credential);

        verifyNoMoreInteractions(gDriveCredential);
    }
}
