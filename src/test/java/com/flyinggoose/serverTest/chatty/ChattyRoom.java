package com.flyinggoose.serverTest.chatty;

import com.flyinggoose.jserver.client.JClient;
import com.flyinggoose.jserver.client.JClientServerThread;
import com.flyinggoose.jserver.client.protocol.JClientProtocol;
import com.flyinggoose.jserver.http.HttpHeader;
import com.flyinggoose.jserver.server.JServer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class ChattyRoom {
    public static final float VERSION = 1.0f;
    final String name;
    final int id;
    final int port;
    final JServer server;

    public ChattyRoom(String name, int port, String mainHost, int mainPort) {
        this.name = name;
        this.port = port;
        List<Object> data = new ArrayList<>();
        JClient mainConnection = new JClient((server, serverThread) -> new GetDataFromMain(serverThread, data));
        mainConnection.createConnection(mainHost, mainPort).start();
        while (data.size() == 0) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.id = (int) data.get(0);
        this.server = new JServer(port, (client, clientThread) -> new ChattyServerProtocol(clientThread, this));
        try {
            this.server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getPort() { return port; }

    public int getID() {
        return id;
    }

    public class GetDataFromMain extends JClientProtocol {
        List<Object> data;
        public GetDataFromMain(JClientServerThread serverThread, List<Object> data) {
            super(serverThread, -1);
            this.data = data;

            HttpHeader header = new HttpHeader();
            header.put("Sender", "Chatty/"+VERSION);
            header.put("Title", "gen_room");
            try {
                List<String> data2 = new ArrayList<>();
                data2.add(InetAddress.getLocalHost().toString().split("/", 2)[2] + ":" + port);
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
}
