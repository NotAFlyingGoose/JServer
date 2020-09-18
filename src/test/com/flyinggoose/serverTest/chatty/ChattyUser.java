package com.flyinggoose.serverTest.chatty;

import com.flyinggoose.jserver.client.JClientServerThread;
import com.flyinggoose.jserver.http.HttpHeader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChattyUser {
    static BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
    public final Map<Integer, Boolean> accepted = new HashMap<>();
    private final String username;
    private final Map<Integer, String> keys = new HashMap<>();
    public Map<Integer, List<String>> messages = new HashMap<>();

    public ChattyUser(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void createRoomKey(int id, String key) {
        keys.put(id, key);
    }

    public String getRoomKey(int id) {
        return keys.get(id);
    }

    public void updateMessages(RoomInfo room, List<String> messages, JClientServerThread serverThread) {
        System.out.println(room.getRoomName());
        System.out.println("============================");
        System.out.println("==========MESSAGES==========");
        System.out.println("============================");
        for (String msg : messages) {
            String[] msgInfo = msg.replaceAll("(?<!\\%)\\%(?!\\%)2C", ",").split(";", 3);
            try {
                System.out.println(msgInfo[0] + " sent at " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(msgInfo[1]) + " : " + msgInfo[2]);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        this.messages.put(room.getId(), messages);

        String content = "";
        try {
            System.out.print(username + "> ");
            content = stdIn.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!content.isEmpty()) {
            HttpHeader out = new HttpHeader();
            out.put("Sender", "Chatty/" + ChattyMainServer.VERSION);
            out.put("Title", "message_post");
            List<String> data = new ArrayList<>();
            data.add(getUsername());
            data.add(content.replaceAll(",", "%2C"));
            out.put("Data", data);
            out.put("RoomID", room.getId());
            serverThread.send(out.toHeaderString());
        }
    }
}
