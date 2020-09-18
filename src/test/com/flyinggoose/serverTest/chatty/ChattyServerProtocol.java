package com.flyinggoose.serverTest.chatty;

import com.flyinggoose.jserver.http.HttpHeader;
import com.flyinggoose.jserver.server.JServerClientThread;
import com.flyinggoose.jserver.server.protocol.JServerProtocol;

import java.util.ArrayList;
import java.util.List;

public class ChattyServerProtocol extends JServerProtocol {
    ChattyRoom room;
    public ChattyServerProtocol(JServerClientThread clientThread, ChattyRoom room) {
        super(clientThread, -1);
        HttpHeader header = new HttpHeader();
        header.put("Sender", "Chatty/" + ChattyMainServer.VERSION);
        header.put("Title", "client_info");
        header.put("RoomID", room.getInfo().getId());
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
                String user = inData.get(0);
                String key = inData.get(1);
                int inRoom = Integer.parseInt(in.get("RoomID").toString());
                if (inRoom != room.getInfo().getId()) {
                    clientThread.closeConnection();
                    throw new ChattyClientException("Incorrect Room Number");
                }

                HttpHeader out = new HttpHeader();
                out.put("Sender", "Chatty/" + ChattyMainServer.VERSION);
                out.put("Title", "client_accept");
                out.put("Data", room.isValidKey(key));
                out.put("RoomID", room.getInfo().getId());
                System.out.println("giving " + out.toHeaderString());
                clientThread.send(out.toHeaderString());
            }
            case "message_post" -> {
                List<String> inData = (List<String>) in.get("Data");
                String user = inData.get(0);
                String content = inData.get(1).replaceAll("(?<!\\%)\\%(?!\\%)2C", ",");
                int inRoom = Integer.parseInt(in.get("RoomID").toString());
                if (inRoom != room.getInfo().getId()) {
                    clientThread.closeConnection();
                    throw new ChattyClientException("Incorrect Room Number");
                }

                HttpHeader out = new HttpHeader();
                out.put("Sender", "Chatty/" + ChattyMainServer.VERSION);
                out.put("Title", "message_post");
                out.put("Data", room.postMessage(user, content));
                out.put("RoomID", room.getInfo().getId());
                System.out.println("giving " + out.toHeaderString());
                clientThread.send(out.toHeaderString());
            }
            case "message_get" -> {
                int inRoom = Integer.parseInt(in.get("RoomID").toString());
                if (inRoom != room.getInfo().getId()) {
                    clientThread.closeConnection();
                    throw new ChattyClientException("Incorrect Room Number");
                }

                HttpHeader out = new HttpHeader();
                out.put("Sender", "Chatty/" + ChattyMainServer.VERSION);
                out.put("Title", "message_get");
                List<String> data = new ArrayList<>();
                for (String msg : room.getMessages()) {
                    data.add(msg.replaceAll(",", "%2C"));
                }
                out.put("Data", data);
                out.put("RoomID", room.getInfo().getId());
                System.out.println("giving " + out.toHeaderString());
                clientThread.send(out.toHeaderString());
            }
        }
    }
}
