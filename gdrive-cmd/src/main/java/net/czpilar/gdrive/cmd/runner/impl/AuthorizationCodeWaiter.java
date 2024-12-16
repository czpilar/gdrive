package net.czpilar.gdrive.cmd.runner.impl;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import net.czpilar.gdrive.cmd.exception.CommandLineException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.Optional;

@Service
public class AuthorizationCodeWaiter {

    private static final int WAIT_TIME = 5; // minutes

    @Value("${gdrive.core.drive.redirectUri.port}")
    private int port;

    @Value("${gdrive.core.drive.redirectUri.context}")
    private String context;

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
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        AuthorizationCodeWaiterHandler handler = new AuthorizationCodeWaiterHandler();
        server.createContext(context, handler);
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
                        .filter(StringUtils::isNotBlank)
                        .findFirst();

                String message = code.map(s -> "Your authorization code is: " + s).orElse("");
                System.out.println(message);

                exchange.sendResponseHeaders(200, message.getBytes().length);

                OutputStream responseBody = exchange.getResponseBody();
                responseBody.write(message.getBytes());
                responseBody.close();
            }
        }
    }
}
