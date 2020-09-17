package com.flyinggoose.serverTest.chatty;

import com.flyinggoose.jserver.http.HttpHeader;
import com.flyinggoose.jserver.server.JServerClientThread;
import com.flyinggoose.jserver.server.protocol.JServerProtocol;

import java.util.List;

public class ChattyServerProtocol extends JServerProtocol {
    ChattyRoom room;
    public ChattyServerProtocol(JServerClientThread clientThread, ChattyRoom room) {
        super(clientThread, -1);
        HttpHeader header = new HttpHeader();
        header.put("Sender", "Chatty/"+room.VERSION);
        header.put("Title", "client_info");
        header.put("RoomID", room.getID());
        System.out.println("sending message to client " + header.toHeaderString());
        clientThread.send(header.toHeaderString());
        this.room = room;
    }

    @Override
    public void process(String input) {
        HttpHeader in = new HttpHeader(input);

        switch (in.get("Title").toString()) {
            case "client_info" -> {
                List<String> inData = (List<String>) in.get("Data");
                int inRoom = Integer.parseInt(in.get("RoomID").toString());
                if (inRoom != room.getID()) {
                    clientThread.closeConnection();
                    throw new ChattyClientException("Incorrect Room Number");
                }

                HttpHeader out = new HttpHeader();
                out.put("Sender", "Chatty/"+room.VERSION);
                out.put("Title", "room_accept");
                out.put("Data", true);
                out.put("RoomID", room.getID());
                System.out.println("giving " + out.toHeaderString());
                clientThread.send(out.toHeaderString());
            }
        }
    }
}
