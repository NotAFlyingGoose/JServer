package com.flyinggoose.serverTest;

import com.flyinggoose.jserver.client.JClient;

public class MyClient {
    public static void main(String[] args) {
        JClient client = new JClient((server, serverThread) -> new ChattyClientProtocol(serverThread, "Julian Gramajo", 4444));

        client.createConnection("localhost", 4444).start();
    }
}
