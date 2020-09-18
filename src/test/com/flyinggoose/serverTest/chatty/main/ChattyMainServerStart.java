package com.flyinggoose.serverTest.chatty.main;

import com.flyinggoose.jserver.client.JClient;
import com.flyinggoose.jserver.server.JServer;

import java.io.IOException;

public class ChattyMainServerStart {
    public static void main(String[] args) throws IOException {
        JClient.logConnections = false;
        JServer.logConnections = false;
        ChattyMainServer main = new ChattyMainServer(8080);
        main.start();
    }
}
