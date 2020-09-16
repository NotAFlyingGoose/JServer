package com.flyinggoose.ServerTests.server;

import com.flyinggoose.ServerTests.protocol.IProtocol;

import java.net.Socket;

public interface ServerProtocolProvider {
    IProtocol getProtocolFor(Socket client);
}
