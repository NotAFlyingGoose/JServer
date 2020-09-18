package com.flyinggoose.serverTest.knockknock;

import com.flyinggoose.jserver.server.JServer;
import com.flyinggoose.jserver.server.JServerClientThread;
import com.flyinggoose.jserver.server.protocol.JServerProtocol;
import com.flyinggoose.jserver.server.protocol.JServerProtocolProvider;

import java.io.IOException;
import java.net.Socket;

public class KnockKnockServerStart {
    public static void main(String[] args) throws IOException {
        JServer server = new JServer(4444, KnockKnockServerProtocol::new);
        server.start();

    }
}
