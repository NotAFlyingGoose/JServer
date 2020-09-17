package com.flyinggoose.serverTest;

import com.flyinggoose.jserver.http.HttpHeader;
import com.flyinggoose.jserver.server.JServerClientThread;
import com.flyinggoose.jserver.server.protocol.JServerProtocol;

import java.util.List;

public class ChattyRootServer extends JServerProtocol {
    public ChattyRootServer(JServerClientThread clientThread) {
        super(clientThread);
    }

    @Override
    public void process(String input) {
        HttpHeader header = new HttpHeader(input);

        List<String> requesting = (List<String>) header.get("Requesting");

        System.out.println(requesting);
    }
}
