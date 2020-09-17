package com.flyinggoose.serverTest.chatty;

import com.flyinggoose.jserver.client.JClient;
import com.flyinggoose.jserver.client.JClientServerThread;
import com.flyinggoose.jserver.client.protocol.JClientProtocol;
import com.flyinggoose.jserver.client.protocol.JClientProtocolProvider;
import com.flyinggoose.jserver.http.HttpHeader;

import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class ChattyClient extends JClient {
    public static final float VERSION = 1.0f;
    public ChattyClient(String user, String room, String mainHost, int mainPort) {
        super(null);

        provider = (server, serverThread) -> {
            List<Object> data = new ArrayList<>();
            JClient mainConnection = new JClient((server2, serverThread2) -> new GetDataFromMain(serverThread2, data));
            mainConnection.createConnection(mainHost, mainPort).start();
            while (data.size() == 0) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("data " + data.get(0));
            return new ChattyClientProtocol(serverThread, user, (int) data.get(0));
        };
    }

    public void connectToRoom() {

    }

    public class GetDataFromMain extends JClientProtocol {
        List<Object> data;
        public GetDataFromMain(JClientServerThread serverThread, List<Object> data) {
            super(serverThread, -1);
            this.data = data;

            HttpHeader header = new HttpHeader();
            header.put("Sender", "Chatty/"+VERSION);
            header.put("Title", "room_info");
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
