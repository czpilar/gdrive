package net.czpilar.gdrive.core.credential.impl;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

/**
 * @author David Pilar (david@czpilar.net)
 */
public class AbstractGDriveCredentialTest {

    @Mock
    private AbstractGDriveCredential gDriveCredential;

    private AutoCloseable autoCloseable;

    @BeforeEach
    public void before() {
        autoCloseable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void after() throws Exception {
        autoCloseable.close();
    }

    @Test
    public void testGetCredential() {
        when(gDriveCredential.getCredential()).thenCallRealMethod();
        when(gDriveCredential.getAccessToken()).thenReturn("access-token");
        when(gDriveCredential.getRefreshToken()).thenReturn("refresh-token");
        GoogleCredential.Builder googleCredentialBuilder = mock(GoogleCredential.Builder.class);
        when(gDriveCredential.getCredentialBuilder()).thenReturn(googleCredentialBuilder);
        GoogleCredential googleCredential = mock(GoogleCredential.class);
        when(googleCredentialBuilder.build()).thenReturn(googleCredential);
        when(googleCredential.setAccessToken(anyString())).thenReturn(googleCredential);
        when(googleCredential.setRefreshToken(anyString())).thenReturn(googleCredential);

        Credential result = gDriveCredential.getCredential();

        assertNotNull(result);
        assertEquals(googleCredential, result);

        verify(gDriveCredential).getCredential();
        verify(gDriveCredential).getAccessToken();
        verify(gDriveCredential).getRefreshToken();
        verify(gDriveCredential).getCredentialBuilder();
        verify(googleCredentialBuilder).build();
        verify(googleCredential).setAccessToken(anyString());
        verify(googleCredential).setRefreshToken(anyString());

        verifyNoMoreInteractions(gDriveCredential);
        verifyNoMoreInteractions(googleCredentialBuilder);
        verifyNoMoreInteractions(googleCredential);
    }

    @Test
    public void testSaveCredential() {
        doCallRealMethod().when(gDriveCredential).saveCredential(any(Credential.class));
        doNothing().when(gDriveCredential).saveTokens(anyString(), anyString());

        Credential credential = new GoogleCredential.Builder()
                .setJsonFactory(new JacksonFactory())
                .setTransport(new NetHttpTransport())
                .setClientSecrets("client-id", "client-secret")
                .build();
        credential.setAccessToken("access-token");
        credential.setRefreshToken("refresh-token");

        gDriveCredential.saveCredential(credential);

        verify(gDriveCredential).saveCredential(any(Credential.class));
        verify(gDriveCredential).saveTokens("access-token", "refresh-token");

        verifyNoMoreInteractions(gDriveCredential);
    }
}
