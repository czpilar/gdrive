package net.czpilar.gdrive.core.context;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import net.czpilar.gdrive.core.credential.loader.CredentialLoader;
import net.czpilar.gdrive.core.setting.GDriveSetting;
import org.springframework.context.annotation.*;

@Configuration
@ComponentScan(basePackages = "net.czpilar.gdrive.core")
@PropertySource("classpath:gdrive.properties")
public class GDriveCoreContext {

    private final NetHttpTransport netHttpTransport;
    private final JacksonFactory jacksonFactory;

    private final GDriveSetting gDriveSetting;
    private final CredentialLoader credentialLoader;
    private final AuthorizationCodeFlow.CredentialCreatedListener credentialSaverListener;

    public GDriveCoreContext(GDriveSetting gDriveSetting, CredentialLoader credentialLoader, AuthorizationCodeFlow.CredentialCreatedListener credentialSaverListener) {
        this.gDriveSetting = gDriveSetting;
        this.credentialLoader = credentialLoader;
        this.credentialSaverListener = credentialSaverListener;
        netHttpTransport = new NetHttpTransport();
        jacksonFactory = new JacksonFactory();
    }

    @Bean
    public GoogleAuthorizationCodeFlow googleAuthorizationCodeFlow() {
        return new GoogleAuthorizationCodeFlow.Builder(netHttpTransport, jacksonFactory, gDriveSetting.getClientId(),
                gDriveSetting.getClientSecret(), gDriveSetting.getScopes())
                .setCredentialCreatedListener(credentialSaverListener)
                .build();
    }

    @Bean
    public GoogleCredential.Builder googleCredentialBuilder() {
        return new GoogleCredential.Builder()
                .setTransport(netHttpTransport)
                .setJsonFactory(jacksonFactory)
                .setClientAuthentication(new ClientParametersAuthentication(gDriveSetting.getClientId(), gDriveSetting.getClientSecret()));
    }

    @Bean
    @Scope("prototype")
    public Drive drive() {
        return new Drive.Builder(netHttpTransport, jacksonFactory, credentialLoader.getCredential())
                .setApplicationName(gDriveSetting.getApplicationName())
                .build();
    }
}
