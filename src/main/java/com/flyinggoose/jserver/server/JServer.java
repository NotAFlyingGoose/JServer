package com.flyinggoose.jserver.server;

import com.flyinggoose.jserver.server.protocol.JServerProtocolProvider;
import com.flyinggoose.jserver.util.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Written by Martin Ombura Jr. <@martinomburajr>
 */
public class JServer {
    public final List<JServerClientThread> connections = new ArrayList<>();
    public static boolean logConnections = true;
    protected final int port;
    protected JServerProtocolProvider provider;
    private boolean running = false;

    public JServer(int port, JServerProtocolProvider provider) {
        this.port = port;
        this.provider = provider;
    }

    public void start() throws IOException {
        running = true;
        if (logConnections) Logger.log("Server", "creating socket...");
        ServerSocket serverSocket = new ServerSocket(port);
        Logger.log("Server", "Server is ready for connections at " + serverSocket.getLocalPort() + "!");
        while (running) {
            Socket clientSocket = serverSocket.accept();

            if (logConnections) Logger.log("Connections Manager", "Connecting to client " + clientSocket.getPort());

            JServerClientThread thread = new JServerClientThread(clientSocket, this.provider);
            thread.start();
            connections.add(thread);
        }
    }

    public void stop() {
        this.running = false;
        for (JServerClientThread connection : connections) {
            if (connection.isOpen()) {

            }
        }
    }

}