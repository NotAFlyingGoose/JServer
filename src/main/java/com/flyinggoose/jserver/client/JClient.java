package com.flyinggoose.jserver.client;

import com.flyinggoose.jserver.client.protocol.JClientProtocolProvider;

public class JClient {
    protected JClientProtocolProvider provider;

    public JClient(JClientProtocolProvider provider) {
        this.provider = provider;
    }

    public JClientServerThread createConnection(String host, int port) {
        return new JClientServerThread(this.provider, host, port);
    }

    public JClientServerThread createConnection(String host, int port, boolean log) {
        return new JClientServerThread(this.provider, host, port, log);
    }
}
