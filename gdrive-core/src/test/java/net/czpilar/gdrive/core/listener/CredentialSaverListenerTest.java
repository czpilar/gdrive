package net.czpilar.gdrive.core.listener;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import net.czpilar.gdrive.core.credential.IGDriveCredential;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

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

    private AutoCloseable autoCloseable;

    @BeforeEach
    public void before() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        listener = new CredentialSaverListener(gDriveCredential);
    }

    @AfterEach
    public void after() throws Exception {
        autoCloseable.close();
    }

    @Test
    public void testOnCredentialCreated() throws IOException {
        listener.onCredentialCreated(credential, tokenResponse);

        verify(gDriveCredential).saveCredential(credential);

        verifyNoMoreInteractions(gDriveCredential);
    }
}
