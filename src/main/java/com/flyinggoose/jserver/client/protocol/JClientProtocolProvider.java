package com.flyinggoose.jserver.client.protocol;

import com.flyinggoose.jserver.client.JClient;
import com.flyinggoose.jserver.client.JClientServerThread;

import java.net.Socket;

public interface JClientProtocolProvider {
    JClientProtocol getProtocolFor(Socket server, JClientServerThread serverThread);
}
