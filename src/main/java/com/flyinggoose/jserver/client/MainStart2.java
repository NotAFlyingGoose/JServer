package com.flyinggoose.jserver.client;

import com.flyinggoose.jserver.client.protocol.JClientProtocol;
import com.flyinggoose.jserver.client.protocol.JClientProtocolProvider;

import java.net.Socket;

public class MainStart2 {
    public static void main(String[] args) {
        new JClient(new JClientProtocolProvider() {
            @Override
            public JClientProtocol getProtocolFor(Socket server, JClient client) {
                return null;
            }
        });
    }
}
