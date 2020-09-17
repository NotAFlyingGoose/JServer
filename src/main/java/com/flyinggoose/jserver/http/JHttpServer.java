package com.flyinggoose.jserver.http;

import com.flyinggoose.jserver.server.JServer;
import com.flyinggoose.jserver.server.JServerClientThread;
import com.flyinggoose.jserver.server.protocol.JServerProtocol;
import com.flyinggoose.jserver.server.protocol.JServerProtocolProvider;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class JHttpServer extends JServer {
    List<JHttpHandler> handlers = new ArrayList<>();

    public JHttpServer(int port) {
        super(port, null);
    }

    public void addUrlHandler(JHttpHandler handler) {
        handlers.add(handler);
    }

    @Override
    public void start() throws IOException {
        this.provider = new JServerProtocolProvider() {
            @Override
            public JServerProtocol getProtocolFor(Socket client, JServerClientThread clientThread) {
                return new HttpServerProtocol(clientThread, JHttpServer.this);
            }
        };
        super.start();
    }

    public List<JHttpHandler> getUrlHandlers() {
        return handlers;
    }
}