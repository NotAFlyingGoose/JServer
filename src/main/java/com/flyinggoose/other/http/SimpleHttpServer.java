package com.flyinggoose.other.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class SimpleHttpServer {
    final List<RequestHandler> handlers = new ArrayList<>();
    final ServerSocket server;
    final int port;

    public SimpleHttpServer(int port) throws IOException {
        this.port = port;
        this.server = new ServerSocket(this.port);
    }

    public void addHandler(RequestHandler handler) {
        handlers.add(handler);
    }

    public void start() throws Exception {
        System.out.println("Listening for connection on port 8080 ....");
        while (true) {
            final Socket client = server.accept();
            // 1. Read HTTP request from the client socket
            String GET_REQUEST = "";
            float HTTP_VERSION = 1.1f;
            InputStreamReader isr =  new InputStreamReader(client.getInputStream());
            BufferedReader reader = new BufferedReader(isr);
            String line = reader.readLine();
            while (!line.isEmpty()) {
                if (line.startsWith("GET")) {
                    String[] words = line.split(" ");
                    GET_REQUEST = words[1];
                    HTTP_VERSION = Float.parseFloat(words[2].split("/")[1]);
                    System.out.println("Getting " + GET_REQUEST);
                }
                line = reader.readLine();
            }
            // 2. Prepare an HTTP response

            String httpResponse = null;
            for (RequestHandler handler : handlers) {
                if (handler.urlPattern.matcher(GET_REQUEST).matches()) {
                    httpResponse = handler.handle(HttpRequest.GET, GET_REQUEST);
                    break;
                }
            }
            if (httpResponse == null) httpResponse = RequestHandler.createErrorResponse(404, "Not Found");
            // 3. Send HTTP response to the client
            client.getOutputStream().write(httpResponse.getBytes(StandardCharsets.UTF_8));
            // 4. Close the socket
            client.close();
        }
    }
}
