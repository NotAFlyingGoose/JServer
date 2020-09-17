package com.flyinggoose.jserver.client.protocol;

import com.flyinggoose.jserver.client.JClient;

import java.net.Socket;

public interface JClientProtocolProvider {
    JClientProtocol getProtocolFor(Socket server, JClient client);
}
