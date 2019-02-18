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

    @Autowired
    private GDriveSetting gDriveSetting;

    private NetHttpTransport netHttpTransport;

    private JacksonFactory jacksonFactory;

    @Autowired
    private CredentialLoader credentialLoader;

    @Autowired
    private AuthorizationCodeFlow.CredentialCreatedListener credentialSaverListener;

    @Bean
    public NetHttpTransport netHttpTransport() {
        if (netHttpTransport == null) {
            netHttpTransport = new NetHttpTransport();
        }
        return netHttpTransport;
    }

    @Bean
    public JacksonFactory jacksonFactory() {
        if (jacksonFactory == null) {
            jacksonFactory = new JacksonFactory();
        }
        return jacksonFactory;
    }

    @Bean
    public GoogleAuthorizationCodeFlow googleAuthorizationCodeFlow() {
        // TODO
//        GoogleAuthorizationCodeFlow.Builder builder = new GoogleAuthorizationCodeFlow.Builder(netHttpTransport(),
//                jacksonFactory(), gDriveSetting().getClientId(),
//                gDriveSetting().getClientSecret(), gDriveSetting().getScopes());
//        builder.setCredentialCreatedListener(credentialSaverListener);
//        return new GoogleAuthorizationCodeFlow(builder);
        return new GoogleAuthorizationCodeFlow(netHttpTransport(),
                jacksonFactory(), gDriveSetting.getClientId(),
                gDriveSetting.getClientSecret(), gDriveSetting.getScopes());
    }

    @Bean
    public GoogleCredential.Builder googleCredentialBuilder() {
        return new GoogleCredential.Builder()
                .setTransport(netHttpTransport())
                .setJsonFactory(jacksonFactory())
                .setClientAuthentication(new ClientParametersAuthentication(gDriveSetting.getClientId(), gDriveSetting.getClientSecret()));
    }

    @Bean
    @Scope("prototype")
    public Drive drive() {
        // TODO
//        Drive.Builder builder = new Drive.Builder(netHttpTransport(), jacksonFactory(), credentialLoader.getCredential());
//        builder.setApplicationName(gDriveSetting().getApplicationName());
//        return new Drive(builder);
        return new Drive(netHttpTransport(), jacksonFactory(), credentialLoader.getCredential());
    }

}
