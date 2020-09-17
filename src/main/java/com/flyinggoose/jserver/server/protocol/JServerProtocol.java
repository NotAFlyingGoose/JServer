package com.flyinggoose.jserver.server.protocol;

import com.flyinggoose.jserver.server.JServerClientThread;

public abstract class JServerProtocol {
    public final int reqs;
    public final JServerClientThread clientThread;

    public JServerProtocol(JServerClientThread clientThread, int reqs) {
        this.clientThread = clientThread;
        this.reqs = reqs;
    }

    public JServerProtocol(JServerClientThread clientThread) {
        this.clientThread = clientThread;
        this.reqs = 1;
    }

    public abstract void process(String input);

    public boolean goodReqsAmt(int reqs) {
        if (this.reqs < 0) return true;
        return reqs < this.reqs;
    }
}
