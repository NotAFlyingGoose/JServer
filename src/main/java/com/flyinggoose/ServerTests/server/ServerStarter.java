package com.flyinggoose.ServerTests.server;

import com.flyinggoose.ServerTests.protocol.IProtocol;
import com.flyinggoose.ServerTests.protocol.KnockKnockServerProtocol;

import java.io.IOException;
import java.net.Socket;

public class ServerStarter {
    public static void main(String[] args) {
        try {
            new Server(4444, client -> new KnockKnockServerProtocol()).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
