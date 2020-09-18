package com.flyinggoose.jserver;

public interface NetworkCommunicator {
    void send(String data);
    void closeConnection();

    boolean isOpen();
}
