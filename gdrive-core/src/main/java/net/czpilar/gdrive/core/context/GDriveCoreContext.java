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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;

@Configuration
@ComponentScan(basePackages = "net.czpilar.gdrive.core")
@PropertySource("gdrive.properties")
public class GDriveCoreContext {

    private final NetHttpTransport netHttpTransport;
    private final JacksonFactory jacksonFactory;

    @Autowired
    private GDriveSetting gDriveSetting;

    @Autowired
    private CredentialLoader credentialLoader;

    @Autowired
    private AuthorizationCodeFlow.CredentialCreatedListener credentialSaverListener;

    public GDriveCoreContext() {
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
