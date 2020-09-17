package com.flyinggoose.serverTest;

import com.flyinggoose.jserver.http.HttpHeader;
import com.flyinggoose.jserver.server.JServerClientThread;
import com.flyinggoose.jserver.server.protocol.JServerProtocol;

import java.util.ArrayList;
import java.util.List;

public class ChattyServerProtocol extends JServerProtocol {
    public static final float VERSION = 1.0f;
    public ChattyServerProtocol(JServerClientThread clientThread, ChattyRoom room) {
        super(clientThread);
        HttpHeader header = new HttpHeader();
        header.put("Sender", "Chatty/"+VERSION);
        List<String> requesting = new ArrayList<>();
        requesting.add("Username");
        requesting.add("RoomID");
        header.put("Requesting", requesting);
        header.put("RoomID", room.getID());
        clientThread.send(header.toHeaderString());
    }

    @Override
    public void process(String input) {
        HttpHeader header = new HttpHeader(input);
        System.out.println(header.toHeaderString());
    }
}
