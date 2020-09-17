package com.flyinggoose.jserver.client.protocol;

import com.flyinggoose.jserver.client.JClient;

public abstract class JClientProtocol {
    public final int reqs;
    public final JClient client;

    public JClientProtocol(JClient client, int reqs) {
        this.client = client;
        this.reqs = reqs;
    }

    public JClientProtocol(JClient client) {
        this.client = client;
        this.reqs = 1;
    }

    public abstract void process(String input);

    public boolean goodReqsAmt(int reqs) {
        if (this.reqs < 0) return true;
        return reqs < this.reqs;
    }
}
