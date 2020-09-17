package com.flyinggoose.serverTest;

import com.flyinggoose.jserver.client.JClient;
import com.flyinggoose.serverTest.chatty.ChattyClient;
import com.flyinggoose.serverTest.chatty.ChattyClientProtocol;
import com.flyinggoose.serverTest.chatty.ChattyRoom;

import java.util.ArrayList;
import java.util.List;

public class MyClient {
    public static void main(String[] args) {
        ChattyClient client = new ChattyClient("Julian Gramajo", "Julian's Room", "localhost", 8080);
        client.connectToRoom();
    }
}
