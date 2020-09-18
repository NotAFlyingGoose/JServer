package com.flyinggoose.serverTest.chatty;

import com.flyinggoose.jserver.client.JClient;
import com.flyinggoose.jserver.client.JClientServerThread;
import com.flyinggoose.jserver.client.protocol.JClientProtocol;
import com.flyinggoose.jserver.http.HttpHeader;
import com.flyinggoose.jserver.server.JServer;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class ChattyRoom {
    final RoomInfo info;
    private final List<String> messages = new LinkedList<>();
    final String name;
    private final Map<String, TimeData> keys = new HashMap<>();
    final JServer server;

    public ChattyRoom(String name, int port, String mainHost, int mainPort) {
        this.name = name;
        List<Object> data = new ArrayList<>();
        JClient mainConnection = new JClient((serverThread) -> new GetDataFromMain(serverThread, data, port));
        mainConnection.createConnection(mainHost, mainPort).start();
        while (data.size() == 0) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.info = RoomInfo.getRoomFromName(mainHost, mainPort, name);
        this.server = new JServer(port, (client, clientThread) -> new ChattyServerProtocol(clientThread, this));
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    server.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        postMessage("Server", "Welcome to the start of the " + info.getRoomName() + " Room");
    }

    public RoomInfo getInfo() {
        return info;
    }

    public void updateKeys() {
        for (String key : keys.keySet()) {
            Instant then = keys.get(key).getInstant();
            if (then == null) continue;
            Instant now = Instant.now();
            Duration timeElapsed = Duration.between(then, now);
            if (timeElapsed.toMinutes() > keys.get(key).getMinutes()) keys.remove(key);
        }
    }

    public String createRandomKey(int minutes) {
        // create new key
        SecretKey secretKey = null;
        try {
            secretKey = KeyGenerator.getInstance("AES").generateKey();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        // get base64 encoded version of the key
        String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        return createKey(encodedKey, minutes);
    }

    public String createKey(String str, int minutes) {
        TimeData data;
        if (minutes > 0) data = new TimeData(Instant.now(), minutes);
        else data = null;
        keys.put(str, data);
        return str;
    }

    public Object isValidKey(String key) {
        updateKeys();
        return keys.containsKey(key);
    }

    public boolean postMessage(String username, String content) {
        messages.add(username + ";" + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()) + ";" + content);
        return true;
    }

    public List<String> getMessages() {
        return messages;
    }

    public class GetDataFromMain extends JClientProtocol {
        List<Object> data;

        public GetDataFromMain(JClientServerThread serverThread, List<Object> data, int port) {
            super(serverThread, -1);
            this.data = data;

            HttpHeader header = new HttpHeader();
            header.put("Sender", "Chatty/" + ChattyMainServer.VERSION);
            header.put("Title", "gen_room");
            try {
                List<String> data2 = new ArrayList<>();
                data2.add(InetAddress.getLocalHost().toString().split("/", 2)[1] + ":" + port);
                data2.add(name);
                header.put("Data", data2);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            serverThread.send(header.toHeaderString());
        }

        @Override
        public void process(String input) {
            HttpHeader header = new HttpHeader(input);

            if (header.get("Title").equals("gen_room")) {
                int inRoom = Integer.parseInt(header.get("Data").toString());
                data.add(inRoom);
            }
            serverThread.closeConnection();
        }
    }

    private class TimeData {
        Instant now;
        int minutes;

        public TimeData(Instant now, int minutes) {
            this.now = now;
            this.minutes = minutes;
        }

        public Instant getInstant() {
            return now;
        }

        public int getMinutes() {
            return minutes;
        }
    }
}
