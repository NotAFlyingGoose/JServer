package com.flyinggoose.serverTest.chatty;

import com.flyinggoose.jserver.http.HttpHeader;
import com.flyinggoose.jserver.server.JServer;
import com.flyinggoose.jserver.server.JServerClientThread;
import com.flyinggoose.jserver.server.protocol.JServerProtocol;
import com.flyinggoose.jserver.server.protocol.JServerProtocolProvider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.URL;
import java.util.*;

public class ChattyMainServer extends JServer {
    public static final float VERSION = 1.0f;
    //  -id----  -port&name--
    Map<Integer, List<String>> rooms = new HashMap<>();
    public ChattyMainServer(int port) {
        super(port, null);
    }

    @Override
    public void start() throws IOException {
        this.provider = (client, clientThread) -> new ChattyMainServerProtocol(clientThread);
        super.start();
    }

    public class ChattyMainServerProtocol extends JServerProtocol {

        public ChattyMainServerProtocol(JServerClientThread clientThread) {
            super(clientThread, -1);
        }

        @Override
        public void process(String input) {
            HttpHeader in = new HttpHeader(input);

            System.out.println(in.toHeaderString());
            switch (in.get("Title").toString()) {
                case "gen_room" -> {
                    List<String> inData = (List<String>) in.get("Data");
                    String address = inData.get(0);
                    String name = inData.get(1);

                    Random r = new Random(Integer.parseInt(address.split(":")[2]));
                    int id;

                    do id = r.nextInt();
                    while (rooms.containsKey(id));


                    rooms.put(id, inData);

                    HttpHeader out = new HttpHeader();
                    out.put("Sender", "Chatty/"+VERSION);
                    out.put("Title", "gen_room");
                    out.put("Data", id);
                    System.out.println("creating room " + id);
                    clientThread.send(out.toHeaderString());
                }
                case "room_info" -> {
                    String reqName = in.get("Data").toString();
                    HttpHeader out = new HttpHeader();
                    out.put("Sender", "Chatty/"+VERSION);
                    out.put("Title", "room_info");
                    for (Integer id : rooms.keySet()) {
                        if (isNumber(reqName)) {
                            if (rooms.get(id).get(0).split(":", 2)[1].equals(reqName)) {
                                List<String> outData = new ArrayList<>();
                                outData.add(rooms.get(id).get(0));
                                outData.add(String.valueOf(id));
                                out.put("Data", outData);
                                break;
                            }
                        } else {
                            if (rooms.get(id).get(1).equals(reqName)) {
                                List<String> outData = new ArrayList<>();
                                outData.add(rooms.get(id).get(0));
                                outData.add(String.valueOf(id));
                                out.put("Data", outData);
                                break;
                            }
                        }
                    }
                    if (!out.containsKey("Data")) {
                        out.put("Data", "null");
                    }
                    clientThread.send(out.toHeaderString());
                }
            }
        }
    }

    public boolean isNumber(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getCurrentIP() throws IOException {
        URL whatismyip = new URL("http://checkip.amazonaws.com");
        BufferedReader in = new BufferedReader(new InputStreamReader(
                whatismyip.openStream()));

        return in.readLine(); //you get the IP as a String
    }
}