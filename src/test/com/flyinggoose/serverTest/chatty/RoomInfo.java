package com.flyinggoose.serverTest.chatty;

import com.flyinggoose.jserver.client.JClient;
import com.flyinggoose.jserver.client.JClientServerThread;
import com.flyinggoose.jserver.client.protocol.JClientProtocol;
import com.flyinggoose.jserver.http.HttpHeader;

import java.util.ArrayList;
import java.util.List;

public class RoomInfo {

    final String roomName;
    final String address;
    final String host;
    final int port;
    final int id;

    private RoomInfo(String roomName, String address, int id) {
        this.roomName = roomName;
        this.address = address;
        this.host = address.split(":")[0];
        this.port = Integer.parseInt(address.split(":")[1]);
        this.id = id;
    }

    public static RoomInfo getRoomFromName(String centralIP, int centralPort, String name) {
        List<Object> data = new ArrayList<>();
        JClient mainConnection = new JClient((serverThread) -> new GetDataFromMain(serverThread, data, name));
        mainConnection.createConnection(centralIP, centralPort).start();
        while (data.size() == 0) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (data.get(0).toString().equals("null")) {
            throw new com.flyinggoose.serverTest.chatty.ChattyClientException("Room does not exist");
        }
        List<String> roomData = (List<String>) data.get(0);

        return new RoomInfo(roomData.get(2), roomData.get(0), Integer.parseInt(roomData.get(1)));
    }

    public String getRoomName() {
        return roomName;
    }

    public String getAddress() {
        return address;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public int getId() {
        return id;
    }

    private static class GetDataFromMain extends JClientProtocol {
        List<Object> data;

        public GetDataFromMain(JClientServerThread serverThread, List<Object> data, String room) {
            super(serverThread, -1);
            this.data = data;

            HttpHeader header = new HttpHeader();
            header.put("Sender", "Chatty/" + ChattyMainServer.VERSION);
            header.put("Title", "room_info");
            header.put("Data", room);
            serverThread.send(header.toHeaderString());
        }

        @Override
        public void process(String input) {
            HttpHeader header = new HttpHeader(input);

            if (header.get("Title").equals("room_info")) {
                if (header.get("Data") == null) data.add("null");
                else data.add(header.get("Data"));
            }
            serverThread.closeConnection();
        }
    }
}
