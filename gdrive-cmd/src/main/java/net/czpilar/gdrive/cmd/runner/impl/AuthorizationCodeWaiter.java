package net.czpilar.gdrive.cmd.runner.impl;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import net.czpilar.gdrive.cmd.exception.CommandLineException;
import net.czpilar.gdrive.core.setting.GDriveSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.Optional;

@Service
public class AuthorizationCodeWaiter {

    private static final int WAIT_TIME = 5; // minutes

    private GDriveSetting gDriveSetting;

    @Autowired
    public void setGDriveSetting(GDriveSetting gDriveSetting) {
        this.gDriveSetting = gDriveSetting;
    }

    public Optional<String> getCode() {
        Optional<String> code = Optional.empty();
        try {
            System.out.println("Waiting " + WAIT_TIME + " minutes for receiving authorization code to authorize the gDrive application...");
            code = doWait();
        } catch (IOException | InterruptedException e) {
            throw new CommandLineException(e);
        } finally {
            if (code.isEmpty()) {
                System.out.println("gDrive application was not authorized due to expired waiting time.");
            }
        }
        return code;
    }

    private Optional<String> doWait() throws IOException, InterruptedException {
        HttpServer server = HttpServer.create(new InetSocketAddress(gDriveSetting.getRedirectUriPort()), 0);
        AuthorizationCodeWaiterHandler handler = new AuthorizationCodeWaiterHandler();
        server.createContext(gDriveSetting.getRedirectUriContext(), handler);
        server.start();
        long waitTime = System.currentTimeMillis() + WAIT_TIME * 60 * 1000;
        while (handler.code.isEmpty() && System.currentTimeMillis() < waitTime) {
            Thread.sleep(100);
        }
        server.stop(0);
        return handler.code;
    }

    private static class AuthorizationCodeWaiterHandler implements HttpHandler {

        private Optional<String> code = Optional.empty();

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (exchange.getRequestMethod().equalsIgnoreCase("GET")) {
                code = Arrays.stream(exchange.getRequestURI().getQuery().split("&"))
                        .filter(s -> s.startsWith("code="))
                        .map(s -> s.replace("code=", ""))
                        .filter(s -> !s.isBlank())
                        .findFirst();

                String consoleMessage = code.map(s -> "Your authorization code is: " + s).orElse("Authorization code not found in redirect.");
                System.out.println(consoleMessage);

                String html = code.map(_ -> """
                        <html><body style="font-family:sans-serif;text-align:center;padding:50px;">
                        <h1 style="color:green;">&#10004; Authorization Successful</h1>
                        <p>gDrive application has been authorized. You can close this window.</p>
                        </body></html>"""
                ).orElse("""
                        <html><body style="font-family:sans-serif;text-align:center;padding:50px;">
                        <h1 style="color:red;">&#10008; Authorization Failed</h1>
                        <p>Authorization code was not found. Please try again.</p>
                        </body></html>""");

                byte[] response = html.getBytes();
                exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
                exchange.sendResponseHeaders(200, response.length);

                OutputStream responseBody = exchange.getResponseBody();
                responseBody.write(response);
                responseBody.close();
            }
        }
    }
}
