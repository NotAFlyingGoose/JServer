package com.flyinggoose.jserver.server.protocol;

import com.flyinggoose.jserver.server.JServerClientThread;
import com.flyinggoose.jserver.server.http.HttpHeader;
import com.flyinggoose.jserver.server.http.JHttpHandler;
import com.flyinggoose.jserver.server.http.JHttpMethodType;
import com.flyinggoose.jserver.server.http.JHttpServer;

public class HttpServerProtocol extends JServerProtocol {
    JHttpServer server;

    public HttpServerProtocol(JServerClientThread clientThread, JHttpServer server) {
        super(clientThread, -1);
        this.server = server;
    }

    @Override
    public void process(String input) {
        //get header info
        String[] lines = input.split("\n");
        final String REQUEST_METHOD = lines[0].split(" ")[0];
        final JHttpMethodType HTTP_METHOD = JHttpMethodType.valueOf(REQUEST_METHOD);
        final String REQUEST_URL = lines[0].split(" ")[1];
        final String HTTP_VERSION = lines[0].split(" ")[2];
        HttpHeader headers = new HttpHeader(input);

        // loop through the url handlers
        // and handle the url
        boolean handled = false;
        for (JHttpHandler handler : this.server.getUrlHandlers()) {
            if (handler.getPattern().matcher(REQUEST_URL).matches()) {
                handler.handle(HTTP_METHOD, REQUEST_URL, this.clientThread);
                handled = true;
                break;
            }
        }

        if (!handled) {
            clientThread.send(JHttpHandler.createErrorResponse(500, "Internal Server Error."));
        }

        clientThread.closeConnection();
    }
}
