package com.flyinggoose.serverTest;

import com.flyinggoose.serverTest.chatty.ChattyMainServer;
import com.flyinggoose.serverTest.chatty.ChattyRoom;

import java.io.IOException;

public class MyServer {
    public static void main(String[] args) throws IOException {
        ChattyRoom room = new ChattyRoom("Julian's Room", 4444, "localhost", 8080);
    }
}
