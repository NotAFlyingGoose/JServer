package com.flyinggoose.serverTest.chatty;

import com.flyinggoose.jserver.client.JClient;
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
        super(serverThread, -1);
        this.username = username;
        this.roomId = roomId;
    }

    @Override
    public void process(String input) {
        HttpHeader in = new HttpHeader(input);
        System.out.println("reciving " + in.toHeaderString());

        switch (in.get("Title").toString()) {
            case "client_info" -> {
                int inRoom = Integer.parseInt(in.get("RoomID").toString());
                if (inRoom != roomId) {
                    serverThread.closeConnection();
                    throw new ChattyClientException("Incorrect Room Number");
                }

                HttpHeader out = new HttpHeader();
                out.put("Sender", "Chatty/"+VERSION);
                List<String> outData = new ArrayList<>();
                outData.add(this.username);
                outData.add(this.roomId + "");
                out.put("Title", "client_info");
                out.put("Data", outData);
                out.put("RoomID", roomId);
                System.out.println("giving " + out.toHeaderString());
                serverThread.send(out.toHeaderString());
            }
            case "room_accept" -> {
                String inData = (String) in.get("Data");
                int inRoom = Integer.parseInt(in.get("RoomID").toString());
                if (inRoom != roomId) {
                    serverThread.closeConnection();
                    throw new ChattyClientException("Incorrect Room Number");
                }
                if (!inData.equals("true")) {
                    serverThread.closeConnection();
                    throw new ChattyClientException("Connection Refused");
                }
            }
        }
    }
}
