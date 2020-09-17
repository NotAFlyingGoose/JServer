package com.flyinggoose.jserver.client;

import com.flyinggoose.jserver.client.protocol.JClientProtocol;
import com.flyinggoose.jserver.client.protocol.JClientProtocolProvider;
import com.flyinggoose.jserver.server.JServerClientThread;
import com.flyinggoose.jserver.util.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class JClient {
    protected JClientProtocolProvider provider;

    public JClient(JClientProtocolProvider provider) {
        this.provider = provider;
    }

    public JClientServerThread createConnection(String host, int port) {
        return new JClientServerThread(this.provider, host, port);
    }
}
