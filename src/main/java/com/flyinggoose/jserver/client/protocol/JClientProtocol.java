package com.flyinggoose.jserver.client.protocol;

import com.flyinggoose.jserver.client.JClient;
import com.flyinggoose.jserver.client.JClientServerThread;

public abstract class JClientProtocol {
    public final int reqs;
    public final JClientServerThread serverThread;

    public JClientProtocol(JClientServerThread serverThread, int reqs) {
        this.serverThread = serverThread;
        this.reqs = reqs;
    }

    public JClientProtocol(JClientServerThread serverThread) {
        this.serverThread = serverThread;
        this.reqs = 1;
    }

    public abstract void process(String input);

    public boolean goodReqsAmt(int reqs) {
        if (this.reqs < 0) return true;
        return reqs < this.reqs;
    }
}
