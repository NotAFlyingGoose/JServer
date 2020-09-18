package com.flyinggoose.serverTest.chatty;

import com.flyinggoose.jserver.client.JClient;

public class ChattyClient extends JClient {
    public static final float VERSION = 1.0f;
    public final String mainHost;
    public final int mainPort;

    public ChattyClient(ChattyUser user, String mainHost, int mainPort) {
        super((serverThread) -> new ChattyClientProtocol(serverThread, user, RoomInfo.getRoomFromName(mainHost, mainPort, String.valueOf(serverThread.getServerSocket().getPort()))));
        this.mainHost = mainHost;
        this.mainPort = mainPort;
    }

    public void connectToRoom(String room) {
        RoomInfo info = RoomInfo.getRoomFromName(mainHost, mainPort, room);
        createConnection(info.getHost(), info.getPort(), false).start();
    }

}
