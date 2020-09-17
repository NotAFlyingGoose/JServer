package com.flyinggoose.serverTest;

import com.flyinggoose.jserver.client.JClientServerThread;
import com.flyinggoose.jserver.client.protocol.JClientProtocol;
import com.flyinggoose.jserver.http.HttpHeader;

import java.util.ArrayList;
import java.util.List;

public class ChattyClientProtocol extends JClientProtocol {
    public static final float VERSION = 1.0f;
    final String username;
    final int roomId;
    public ChattyClientProtocol(JClientServerThread serverThread, String username, int roomId) {
        super(serverThread);
        this.username = username;
        this.roomId = roomId;
    }

    @Override
    public void process(String input) {
        HttpHeader in = new HttpHeader(input);

        List<String> inRequesting = (List<String>) in.get("Requesting");

        HttpHeader out = new HttpHeader();
        out.put("Sender", "Chatty/"+VERSION);
        List<String> outRequesting = new ArrayList<>();
        outRequesting.add("Username");
        outRequesting.add("RoomID");
        out.put("Request", inRequesting);
        out.put("Reply", outRequesting);
        out.put("RoomID", roomId);
        serverThread.send(out.toHeaderString());
    }
}
