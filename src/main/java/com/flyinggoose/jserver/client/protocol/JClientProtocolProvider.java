package com.flyinggoose.jserver.client.protocol;

import com.flyinggoose.jserver.client.JClientServerThread;

public interface JClientProtocolProvider {
    JClientProtocol getProtocolFor(JClientServerThread serverThread);
}
