package com.flyinggoose.serverTest.chatty;

import java.io.IOException;

public class MyMainServer {
    public static void main(String[] args) throws IOException {
        ChattyMainServer main = new ChattyMainServer(8080);
        main.start();
    }
}
