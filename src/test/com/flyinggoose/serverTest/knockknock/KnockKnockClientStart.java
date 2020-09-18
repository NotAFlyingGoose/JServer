package com.flyinggoose.serverTest.knockknock;

import com.flyinggoose.jserver.client.JClient;
import com.flyinggoose.jserver.client.JClientServerThread;
import com.flyinggoose.jserver.client.protocol.JClientProtocol;
import com.flyinggoose.jserver.client.protocol.JClientProtocolProvider;

public class KnockKnockClientStart {
    public static void main(String[] args) {
        JClient client = new JClient(KnockKnockClientProtocol::new);
        client.createConnection("localhost", 4444);

    }
}
