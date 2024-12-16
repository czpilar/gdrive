package net.czpilar.gdrive.core.context;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import net.czpilar.gdrive.core.credential.loader.CredentialLoader;
import net.czpilar.gdrive.core.setting.GDriveSetting;
import org.springframework.context.annotation.*;

@Configuration
@ComponentScan(basePackages = "net.czpilar.gdrive.core")
@PropertySource("classpath:gdrive.properties")
public class GDriveCoreContext {

    private final NetHttpTransport netHttpTransport = new NetHttpTransport();
    private final GsonFactory gsonFactory = GsonFactory.getDefaultInstance();

    private final GDriveSetting gDriveSetting;
    private final CredentialLoader credentialLoader;
    private final AuthorizationCodeFlow.CredentialCreatedListener credentialSaverListener;

    public GDriveCoreContext(GDriveSetting gDriveSetting, CredentialLoader credentialLoader, AuthorizationCodeFlow.CredentialCreatedListener credentialSaverListener) {
        this.gDriveSetting = gDriveSetting;
        this.credentialLoader = credentialLoader;
        this.credentialSaverListener = credentialSaverListener;
    }

    @Bean
    public GoogleAuthorizationCodeFlow googleAuthorizationCodeFlow() {
        return new GoogleAuthorizationCodeFlow.Builder(netHttpTransport, gsonFactory, gDriveSetting.getClientId(),
                gDriveSetting.getClientSecret(), gDriveSetting.getScopes())
                .setCredentialCreatedListener(credentialSaverListener)
                .build();
    }

    @Bean
    public Credential.Builder googleCredentialBuilder() {
        return new Credential.Builder(BearerToken.authorizationHeaderAccessMethod())
                .setTransport(netHttpTransport)
                .setJsonFactory(gsonFactory)
                .setClientAuthentication(new ClientParametersAuthentication(gDriveSetting.getClientId(), gDriveSetting.getClientSecret()))
                .setTokenServerEncodedUrl(GDriveSetting.TOKEN_URL);
    }

    @Bean
    @Scope("prototype")
    public Drive drive() {
        return new Drive.Builder(netHttpTransport, gsonFactory, credentialLoader.getCredential())
                .setApplicationName(gDriveSetting.getApplicationName())
                .build();
    }
}
