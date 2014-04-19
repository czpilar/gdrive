package net.czpilar.gdrive.core.setting;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author David Pilar (david@czpilar.net)
 */
public class GDriveSettingTest {

	@Test
	public void testGDriveSetting() {
		String applicationName = "test-application-name";
		String clientId = "test-client-id";
		String clientSecret = "test-client-secret";
		String redirectUri = "test-redirect-uri";
		List<String> scopes = Arrays.asList("test-scope-1", "test-scope-2");
		GDriveSetting setting = new GDriveSetting(applicationName, clientId, clientSecret, redirectUri, scopes);

		Assert.assertEquals(applicationName, setting.getApplicationName());
		Assert.assertEquals(clientId, setting.getClientId());
		Assert.assertEquals(clientSecret, setting.getClientSecret());
		Assert.assertEquals(redirectUri, setting.getRedirectUri());
		Assert.assertEquals(scopes, setting.getScopes());
	}
}
