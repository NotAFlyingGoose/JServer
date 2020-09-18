package com.flyinggoose.serverTest.chatty.client;

import com.flyinggoose.jserver.client.JClientServerThread;
import com.flyinggoose.jserver.client.protocol.JClientProtocol;
import com.flyinggoose.jserver.http.HttpHeader;
import com.flyinggoose.serverTest.chatty.main.ChattyMainServer;
import com.flyinggoose.serverTest.chatty.room.RoomInfo;

import java.util.ArrayList;
import java.util.List;

public class ChattyClientProtocol extends JClientProtocol {
    final ChattyUser user;
    final RoomInfo room;

    public ChattyClientProtocol(JClientServerThread serverThread, ChattyUser user, RoomInfo room) {
        super(serverThread, -1);
        this.user = user;
        this.room = room;
        user.accepted.put(room.getId(), true);
    }

    @Override
    public void process(String input) {
        HttpHeader in = new HttpHeader(input);

        switch (in.get("Title").toString()) {
            case "client_info" -> {
                int inRoom = Integer.parseInt(in.get("RoomID").toString());
                if (inRoom != room.getId()) {
                    serverThread.closeConnection();
                    throw new ChattyClientException("Incorrect Room Number");
                }

                HttpHeader out = new HttpHeader();
                out.put("Sender", "Chatty/" + ChattyMainServer.VERSION);
                List<String> outData = new ArrayList<>();
                outData.add(user.getInfo().getUsername());
                outData.add(user.getRoomKey(room.getId()));
                out.put("Title", "client_info");
                out.put("Data", outData);
                out.put("RoomID", room.getId());
                serverThread.send(out.toHeaderString());
            }
            case "client_accept" -> {
                String inData = (String) in.get("Data");
                int inRoom = Integer.parseInt(in.get("RoomID").toString());
                if (inRoom != room.getId()) {
                    serverThread.closeConnection();
                    throw new ChattyClientException("Incorrect Room Number");
                }
                if (!inData.equals("true")) {
                    serverThread.closeConnection();
                    throw new ChattyClientException("Connection Refused.");
                } else {
                    user.accepted.replace(room.getId(), true);
                    // send request for messages
                    HttpHeader out = new HttpHeader();
                    out.put("Sender", "Chatty/" + ChattyMainServer.VERSION);
                    String content = "Hello, World";
                    out.put("Title", "message_get");
                    out.put("RoomID", room.getId());
                    serverThread.send(out.toHeaderString());
                }
            }
            case "message_post" -> {
                String inData = (String) in.get("Data");
                int inRoom = Integer.parseInt(in.get("RoomID").toString());
                if (inRoom != room.getId()) {
                    serverThread.closeConnection();
                    throw new ChattyClientException("Incorrect Room Number");
                }

                if (!inData.equals("true")) {
                    System.err.println("The message was refused");
                }
            }
            case "message_get" -> {
                int inRoom = Integer.parseInt(in.get("RoomID").toString());
                if (inRoom != room.getId()) {
                    serverThread.closeConnection();
                    throw new ChattyClientException("Incorrect Room Number");
                }
                Object data = in.get("Data");
                if (data instanceof List) {
                    this.user.updateMessages(room, (List<String>) data, serverThread);
                } else if (data instanceof String) {
                    List<String> newData = new ArrayList<>();
                    newData.add((String) data);
                    this.user.updateMessages(room, newData, serverThread);
                }

                // get new messages
                HttpHeader out = new HttpHeader();
                out.put("Sender", "Chatty/" + ChattyMainServer.VERSION);
                out.put("Title", "message_get");
                out.put("RoomID", room.getId());
                serverThread.send(out.toHeaderString());
            }
        }
    }
}
