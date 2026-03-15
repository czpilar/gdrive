package net.czpilar.gdrive.cmd.runner.impl;

import net.czpilar.gdrive.core.setting.GDriveSetting;
import org.junit.jupiter.api.Test;

import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthorizationCodeWaiterTest {

    private static final AtomicInteger PORT_COUNTER = new AtomicInteger(19880);
    private static final String TEST_CONTEXT = "/gdrive-test";

    private AuthorizationCodeWaiter createWaiter(int port) {
        GDriveSetting setting = mock(GDriveSetting.class);
        when(setting.getRedirectUriPort()).thenReturn(port);
        when(setting.getRedirectUriContext()).thenReturn(TEST_CONTEXT);

        AuthorizationCodeWaiter waiter = new AuthorizationCodeWaiter();
        waiter.setGDriveSetting(setting);
        return waiter;
    }

    private void waitForServer(int port) throws Exception {
        for (int i = 0; i < 30; i++) {
            try {
                URI uri = URI.create("http://127.0.0.1:" + port + TEST_CONTEXT + "?probe=true");
                HttpURLConnection conn = (HttpURLConnection) uri.toURL().openConnection();
                conn.setConnectTimeout(200);
                conn.setReadTimeout(200);
                conn.getResponseCode();
                conn.disconnect();
                return;
            } catch (ConnectException | java.net.SocketTimeoutException e) {
                Thread.sleep(200);
            }
        }
        fail("Server did not start on port " + port);
    }

    @Test
    void testGetCodeWithValidAuthorizationCode() throws Exception {
        int port = PORT_COUNTER.getAndIncrement();
        String authCode = "test-auth-code-12345";
        AuthorizationCodeWaiter waiter = createWaiter(port);

        CompletableFuture<Optional<String>> codeFuture = CompletableFuture.supplyAsync(waiter::getCode);

        waitForServer(port);

        URI uri = URI.create("http://127.0.0.1:" + port + TEST_CONTEXT + "?code=" + authCode);
        HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();

        assertEquals(200, responseCode);

        String response = new String(connection.getInputStream().readAllBytes());
        assertTrue(response.contains("Authorization Successful"));

        connection.disconnect();

        Optional<String> code = codeFuture.get(10, TimeUnit.SECONDS);
        assertTrue(code.isPresent());
        assertEquals(authCode, code.get());
    }

    @Test
    void testGetCodeWithMultipleQueryParams() throws Exception {
        int port = PORT_COUNTER.getAndIncrement();
        String authCode = "multi-param-code";
        AuthorizationCodeWaiter waiter = createWaiter(port);

        CompletableFuture<Optional<String>> codeFuture = CompletableFuture.supplyAsync(waiter::getCode);

        waitForServer(port);

        URI uri = URI.create("http://127.0.0.1:" + port + TEST_CONTEXT + "?session_state=abc123&code=" + authCode);
        HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();

        assertEquals(200, responseCode);

        String response = new String(connection.getInputStream().readAllBytes());
        assertTrue(response.contains("Authorization Successful"));

        connection.disconnect();

        Optional<String> code = codeFuture.get(10, TimeUnit.SECONDS);
        assertTrue(code.isPresent());
        assertEquals(authCode, code.get());
    }

    @Test
    void testGetCodeResponseContainsHtmlContent() throws Exception {
        int port = PORT_COUNTER.getAndIncrement();
        AuthorizationCodeWaiter waiter = createWaiter(port);

        CompletableFuture<Optional<String>> codeFuture = CompletableFuture.supplyAsync(waiter::getCode);

        waitForServer(port);

        URI uri = URI.create("http://127.0.0.1:" + port + TEST_CONTEXT + "?code=html-test-code");
        HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();
        connection.setRequestMethod("GET");

        assertEquals(200, connection.getResponseCode());
        assertEquals("text/html; charset=UTF-8", connection.getHeaderField("Content-Type"));

        String response = new String(connection.getInputStream().readAllBytes());
        assertTrue(response.contains("<html>"));
        assertTrue(response.contains("gDrive application has been authorized"));

        connection.disconnect();
        codeFuture.get(10, TimeUnit.SECONDS);
    }
}
