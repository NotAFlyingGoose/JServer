package com.flyinggoose.serverTest;

import com.flyinggoose.jserver.server.JServer;
import com.flyinggoose.jserver.server.JServerClientThread;

import java.io.IOException;

public class ChattyRoom {
    final String name;
    final int id;
    final JServer server;

    public ChattyRoom(String name) {
        this.name = name;
        this.id = 4444;
        this.server = new JServer(this.id, (client, clientThread) -> new ChattyServerProtocol(clientThread, this));
        try {
            this.server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getID() {
        return id;
    }
}
