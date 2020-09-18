package com.flyinggoose.jserver.server.protocol;

import com.flyinggoose.jserver.server.JServerClientThread;

import java.net.Socket;

public interface JServerProtocolProvider {
    JServerProtocol getProtocolFor(JServerClientThread clientThread);
}
