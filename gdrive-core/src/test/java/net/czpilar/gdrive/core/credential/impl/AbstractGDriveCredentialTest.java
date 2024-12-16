package net.czpilar.gdrive.core.credential.impl;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import net.czpilar.gdrive.core.setting.GDriveSetting;
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
        Credential.Builder googleCredentialBuilder = mock(Credential.Builder.class);
        when(gDriveCredential.getCredentialBuilder()).thenReturn(googleCredentialBuilder);
        Credential googleCredential = mock(Credential.class);
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

        Credential credential = new Credential.Builder(BearerToken.authorizationHeaderAccessMethod())
                .setJsonFactory(GsonFactory.getDefaultInstance())
                .setTransport(new NetHttpTransport())
                .setClientAuthentication(new ClientParametersAuthentication("client-id", "client-secret"))
                .setTokenServerEncodedUrl(GDriveSetting.TOKEN_URL)
                .build();
        credential.setAccessToken("access-token");
        credential.setRefreshToken("refresh-token");

        gDriveCredential.saveCredential(credential);

        verify(gDriveCredential).saveCredential(any(Credential.class));
        verify(gDriveCredential).saveTokens("access-token", "refresh-token");

        verifyNoMoreInteractions(gDriveCredential);
    }
}
