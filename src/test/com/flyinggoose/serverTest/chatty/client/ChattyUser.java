package com.flyinggoose.serverTest.chatty.client;

import com.flyinggoose.jserver.client.JClient;
import com.flyinggoose.jserver.client.JClientServerThread;
import com.flyinggoose.jserver.client.protocol.JClientProtocol;
import com.flyinggoose.jserver.http.HttpHeader;
import com.flyinggoose.serverTest.chatty.main.ChattyMainServer;
import com.flyinggoose.serverTest.chatty.room.RoomInfo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChattyUser {
    static BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
    public final Map<Integer, Boolean> accepted = new ConcurrentHashMap<>();
    private final UserInfo info;
    private final Map<Integer, String> keys = new ConcurrentHashMap<>();
    public Map<Integer, List<String>> messages = new ConcurrentHashMap<>();
    private Map<Integer, String> content = new ConcurrentHashMap<>();

    public ChattyUser(String username, String password, boolean exists, String mainHost, int mainPort) {
        if (!exists) {
            List<Object> data = new ArrayList<>();
            JClient mainConnection = new JClient((serverThread) -> new GetDataFromMain(serverThread, data, username, password));
            mainConnection.createConnection(mainHost, mainPort).start();
            while (data.size() == 0) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        this.info = UserInfo.getUserFromName(mainHost, mainPort, username, password);
    }

    public UserInfo getInfo() {
        return info;
    }

    public void createRoomKey(int id, String key) {
        keys.put(id, key);
    }

    public String getRoomKey(int id) {
        return keys.get(id);
    }

    public void updateMessages(RoomInfo room, List<String> messages, JClientServerThread serverThread) {
        this.messages.put(room.getId(), messages);

        if (content.get(room.getId()) != null && !content.get(room.getId()).isEmpty()) {
            HttpHeader out = new HttpHeader();
            out.put("Sender", "Chatty/" + ChattyMainServer.VERSION);
            out.put("Title", "message_post");
            List<String> data = new ArrayList<>();
            data.add(info.getUsername());
            data.add(content.get(room.getId()).replaceAll(",", "%2C"));
            out.put("Data", data);
            out.put("RoomID", room.getId());
            serverThread.send(out.toHeaderString());
            content.remove(room.getId());
        }
    }

    public void queueMessage(int roomId, String content) {
        this.content.put(roomId, content);
    }

    public void queueUpdate(int roomId) {
        queueMessage(roomId, "");
    }

    public List<String> getMessages(int roomId) {
        return messages.get(roomId);
    }

    public class GetDataFromMain extends JClientProtocol {
        List<Object> data;

        public GetDataFromMain(JClientServerThread serverThread, List<Object> data, String username, String password) {
            super(serverThread, -1);
            this.data = data;

            HttpHeader header = new HttpHeader();
            header.put("Sender", "Chatty/" + ChattyMainServer.VERSION);
            header.put("Title", "gen_user");
            List<String> data2 = new ArrayList<>();
            data2.add(username);
            data2.add(password);
            header.put("Data", data2);
            serverThread.send(header.toHeaderString());
        }

        @Override
        public void process(String input) {
            HttpHeader header = new HttpHeader(input);

            if (header.get("Title").equals("gen_user")) {
                int inRoom = Integer.parseInt(header.get("Data").toString());
                data.add(inRoom);
            }
            serverThread.closeConnection();
        }
    }
}
