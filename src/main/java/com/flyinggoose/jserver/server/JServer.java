package com.flyinggoose.jserver.server;

import com.flyinggoose.jserver.server.protocol.JServerProtocolProvider;
import com.flyinggoose.jserver.util.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Written by Martin Ombura Jr. <@martinomburajr>
 */
public class JServer {
    protected final int port;
    protected JServerProtocolProvider provider;
    private boolean running = false;

    public JServer(int port, JServerProtocolProvider provider) {
        this.port = port;
        this.provider = provider;
    }

    public void start() throws IOException {
        running = true;
        Logger.log("Server", "creating socket...");
        ServerSocket serverSocket = new ServerSocket(port);
        Logger.log("Server", "Server is ready for connections at " + serverSocket.getLocalPort() + "!");
        while (running) {
            Socket clientSocket = serverSocket.accept();

            Logger.log("Connections Manager", "Connecting to client " + clientSocket.getPort());

            new JServerClientThread(clientSocket, this.provider).start();
        }
    }

}