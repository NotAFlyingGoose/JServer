package com.flyinggoose.serverTest.chatty.client;

import com.flyinggoose.jserver.client.JClient;
import com.flyinggoose.jserver.client.JClientServerThread;
import com.flyinggoose.jserver.client.protocol.JClientProtocol;
import com.flyinggoose.jserver.http.HttpHeader;
import com.flyinggoose.serverTest.chatty.main.ChattyMainServer;
import com.flyinggoose.serverTest.chatty.room.RoomInfo;

import java.util.ArrayList;
import java.util.List;

public class UserInfo {
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getId() {
        return id;
    }

    final String username;
    final String password;
    final int id;

    private UserInfo(String username, String password, int id) {
        this.username = username;
        this.password = password;
        this.id = id;
    }

    public static UserInfo getUserFromName(String centralIP, int centralPort, String username, String password) {
        List<Object> data = new ArrayList<>();
        JClient mainConnection = new JClient((serverThread) -> new UserInfo.GetDataFromMain(serverThread, data, username, password));
        mainConnection.createConnection(centralIP, centralPort).start();
        while (data.size() == 0) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (data.get(0).toString().equals("null")) {
            throw new ChattyClientException("Bad user request");
        }

        List<String> roomData = (List<String>) data.get(0);

        return new UserInfo(roomData.get(0), roomData.get(1), Integer.parseInt(roomData.get(2)));
    }

    private static class GetDataFromMain extends JClientProtocol {
        List<Object> data;

        public GetDataFromMain(JClientServerThread serverThread, List<Object> data, String username, String password) {
            super(serverThread, -1);
            this.data = data;

            HttpHeader header = new HttpHeader();
            header.put("Sender", "Chatty/" + ChattyMainServer.VERSION);
            header.put("Title", "user_info");
            List<String> data2 = new ArrayList<>();
            data2.add(username);
            data2.add(password);
            header.put("Data", data2);
            serverThread.send(header.toHeaderString());
        }

        @Override
        public void process(String input) {
            HttpHeader header = new HttpHeader(input);

            if (header.get("Title").equals("user_info")) {
                data.add(header.get("Data"));
            }
            serverThread.closeConnection();
        }
    }

}
