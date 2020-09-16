package com.flyinggoose.multicast.server;

import java.io.IOException;

public class MulticastServer {
    public static void main(String[] args) throws IOException {
        new MulticastServerThread().start();
    }
}
