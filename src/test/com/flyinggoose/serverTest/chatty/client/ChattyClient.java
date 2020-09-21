package com.flyinggoose.serverTest.chatty.client;

import com.flyinggoose.jserver.client.JClient;
import com.flyinggoose.jserver.client.JClientServerThread;
import com.flyinggoose.jserver.server.JServerClientThread;
import com.flyinggoose.serverTest.chatty.room.RoomInfo;

public class ChattyClient extends JClient {
    public static final float VERSION = 1.0f;
    public final String mainHost;
    public final int mainPort;

    public ChattyClient(ChattyUser user, String mainHost, int mainPort) {
        super((serverThread) -> new ChattyClientProtocol(serverThread, user, RoomInfo.getRoomFromName(mainHost, mainPort, String.valueOf(serverThread.getServerSocket().getPort()))));
        this.mainHost = mainHost;
        this.mainPort = mainPort;
    }

    public JClientServerThread connectToRoom(String room) {
        RoomInfo info = RoomInfo.getRoomFromName(mainHost, mainPort, room);
        JClientServerThread serverThread = createConnection(info.getHost(), info.getPort());
        serverThread.start();
        return serverThread;
    }

}
