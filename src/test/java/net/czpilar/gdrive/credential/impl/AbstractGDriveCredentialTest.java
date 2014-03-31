package net.czpilar.gdrive.credential.impl;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * @author David Pilař (david@czpilar.net)
 */
public class AbstractGDriveCredentialTest {

	@Mock
	private AbstractGDriveCredential gdriveCredential;

	@Before
	public void before() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testGetCredential() {
		when(gdriveCredential.getCredential()).thenCallRealMethod();
		when(gdriveCredential.getAccessToken()).thenReturn("access-token");
		when(gdriveCredential.getRefreshToken()).thenReturn("refresh-token");

		Credential result = gdriveCredential.getCredential();

		assertNotNull(result);
		assertEquals("access-token", result.getAccessToken());
		assertEquals("refresh-token", result.getRefreshToken());

		verify(gdriveCredential).getCredential();
		verify(gdriveCredential).getAccessToken();
		verify(gdriveCredential).getRefreshToken();

		verifyNoMoreInteractions(gdriveCredential);
	}

	@Test
	public void testSaveCredential() {
		doCallRealMethod().when(gdriveCredential).saveCredential(any(Credential.class));
		doNothing().when(gdriveCredential).saveTokens(anyString(), anyString());

		Credential credential = new GoogleCredential.Builder()
				.setJsonFactory(new JacksonFactory())
				.setTransport(new NetHttpTransport())
				.setClientSecrets("client-id", "client-secret")
				.build();
		credential.setAccessToken("access-token");
		credential.setRefreshToken("refresh-token");

		gdriveCredential.saveCredential(credential);

		verify(gdriveCredential).saveCredential(any(Credential.class));
		verify(gdriveCredential).saveTokens("access-token", "refresh-token");

		verifyNoMoreInteractions(gdriveCredential);
	}
}
