package com.flyinggoose.ServerTests.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Written by Martin Ombura Jr. <@martinomburajr>
 */
public class Server {
    boolean running = false;

    public static void main(String[] args) throws IOException {
        new Server();
    }

    public Server() {
        try {
            int port = 4444;
            start(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start(int port) throws IOException {
        running = true;
        log("Server", "creating socket...");
        ServerSocket serverSocket = new ServerSocket(port);
        log("Server", "Server is ready for connections at "+serverSocket.getLocalPort()+"!");
        while (true) {
            Socket clientSocket = serverSocket.accept();

            log("Connections Manager", "Connecting to "+clientSocket.getInetAddress().getCanonicalHostName() + ":" + clientSocket.getPort());

            new ServerClientThread(clientSocket).start();
        }
    }

    public static void log(String author, Object text) {
        System.out.println("[" + author + "] : " + text);
    }
}