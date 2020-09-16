package com.flyinggoose.ServerTests.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Written by Martin Ombura Jr. <@martinomburajr>
 */
public class Server {
    private final ServerProtocolProvider provider;
    private final int port;
    private boolean running = false;

    public Server(int port, ServerProtocolProvider provider) {
        this.port = port;
        this.provider = provider;
    }

    public void start() throws IOException {
        running = true;
        log("Server", "creating socket...");
        ServerSocket serverSocket = new ServerSocket(port);
        log("Server", "Server is ready for connections at "+serverSocket.getLocalPort()+"!");
        while (running) {
            Socket clientSocket = serverSocket.accept();

            log("Connections Manager", "Connecting to "+clientSocket.getInetAddress().getCanonicalHostName() + ":" + clientSocket.getPort());

            new ServerClientThread(clientSocket, this.provider).start();
        }
    }

    public static void log(String author, Object text) {
        System.out.println("[" + author + "] : " + text);
    }
}