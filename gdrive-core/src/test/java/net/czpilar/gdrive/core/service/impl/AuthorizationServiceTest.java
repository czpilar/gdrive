package net.czpilar.gdrive.core.service.impl;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import net.czpilar.gdrive.core.exception.AuthorizationFailedException;
import net.czpilar.gdrive.core.setting.GDriveSetting;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author David Pilar (david@czpilar.net)
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(GoogleAuthorizationCodeRequestUrl.class)
public class AuthorizationServiceTest {

	private AuthorizationService service = new AuthorizationService();

	@Mock
	private GoogleAuthorizationCodeFlow authorizationCodeFlow;

	private GoogleAuthorizationCodeRequestUrl authorizationCodeRequestUrl;

	@Mock
	private GDriveSetting gDriveSetting;

	@Before
	public void before() {
		MockitoAnnotations.initMocks(this);
		authorizationCodeRequestUrl = PowerMockito.mock(GoogleAuthorizationCodeRequestUrl.class);
		service.setAuthorizationCodeFlow(authorizationCodeFlow);
		service.setGDriveSetting(gDriveSetting);
	}

	@Test
	public void testGetAuthorizationURL() {
		when(authorizationCodeFlow.newAuthorizationUrl()).thenReturn(authorizationCodeRequestUrl);
		when(authorizationCodeRequestUrl.setRedirectUri(anyString())).thenReturn(authorizationCodeRequestUrl);
		when(authorizationCodeRequestUrl.build()).thenReturn("authorization-request-url");
		when(gDriveSetting.getRedirectUri()).thenReturn("test-redirect-uri");

		String result = service.getAuthorizationURL();

		assertEquals("authorization-request-url", result);

		verify(authorizationCodeFlow).newAuthorizationUrl();
		verify(authorizationCodeRequestUrl).setRedirectUri("test-redirect-uri");
		verify(authorizationCodeRequestUrl).build();
		verify(gDriveSetting).getRedirectUri();

		verifyNoMoreInteractions(authorizationCodeFlow);
		verifyNoMoreInteractions(authorizationCodeRequestUrl);
		verifyNoMoreInteractions(gDriveSetting);
	}

	@Test
	public void testAuthorize() throws IOException {
		String authorizationCode = "test-authorization-code";
		String redirectURI = "test-redirect-uri";

		GoogleAuthorizationCodeTokenRequest authorizationCodeTokenRequest = mock(GoogleAuthorizationCodeTokenRequest.class);
		GoogleTokenResponse tokenResponse = mock(GoogleTokenResponse.class);
		Credential credential = mock(Credential.class);

		when(authorizationCodeFlow.newTokenRequest(authorizationCode)).thenReturn(authorizationCodeTokenRequest);
		when(authorizationCodeTokenRequest.setRedirectUri(redirectURI)).thenReturn(authorizationCodeTokenRequest);
		when(authorizationCodeTokenRequest.execute()).thenReturn(tokenResponse);
		when(gDriveSetting.getRedirectUri()).thenReturn(redirectURI);
		when(authorizationCodeFlow.createAndStoreCredential(tokenResponse, null)).thenReturn(credential);

		Credential result = service.authorize(authorizationCode);

		assertNotNull(result);
		assertEquals(credential, result);

		verify(authorizationCodeFlow).newTokenRequest(authorizationCode);
		verify(authorizationCodeTokenRequest).setRedirectUri(redirectURI);
		verify(authorizationCodeTokenRequest).execute();
		verify(gDriveSetting).getRedirectUri();
		verify(authorizationCodeFlow).createAndStoreCredential(tokenResponse, null);

		verifyNoMoreInteractions(authorizationCodeFlow);
		verifyNoMoreInteractions(authorizationCodeTokenRequest);
		verifyNoMoreInteractions(gDriveSetting);
	}

	@Test(expected = AuthorizationFailedException.class)
	public void testAuthorizeWithExceptionDuringStoringCredential() throws IOException {
		String authorizationCode = "test-authorization-code";
		String redirectURI = "test-redirect-uri";

		GoogleAuthorizationCodeTokenRequest authorizationCodeTokenRequest = mock(GoogleAuthorizationCodeTokenRequest.class);
		GoogleTokenResponse tokenResponse = mock(GoogleTokenResponse.class);

		when(authorizationCodeFlow.newTokenRequest(authorizationCode)).thenReturn(authorizationCodeTokenRequest);
		when(authorizationCodeTokenRequest.setRedirectUri(redirectURI)).thenReturn(authorizationCodeTokenRequest);
		when(authorizationCodeTokenRequest.execute()).thenReturn(tokenResponse);
		when(gDriveSetting.getRedirectUri()).thenReturn(redirectURI);
		when(authorizationCodeFlow.createAndStoreCredential(tokenResponse, null)).thenThrow(IOException.class);

		try {
			service.authorize(authorizationCode);
		} catch (AuthorizationFailedException e) {
			verify(authorizationCodeFlow).newTokenRequest(authorizationCode);
			verify(authorizationCodeTokenRequest).setRedirectUri(redirectURI);
			verify(authorizationCodeTokenRequest).execute();
			verify(gDriveSetting).getRedirectUri();
			verify(authorizationCodeFlow).createAndStoreCredential(tokenResponse, null);

			verifyNoMoreInteractions(authorizationCodeFlow);
			verifyNoMoreInteractions(authorizationCodeTokenRequest);
			verifyNoMoreInteractions(gDriveSetting);

			throw e;
		}
	}
}
