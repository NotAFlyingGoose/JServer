package com.flyinggoose.serverTest.knockknock;

import com.flyinggoose.jserver.client.JClientServerThread;
import com.flyinggoose.jserver.client.protocol.JClientProtocol;
import com.flyinggoose.jserver.util.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class KnockKnockClientProtocol extends JClientProtocol {
    BufferedReader stdIn =
            new BufferedReader(new InputStreamReader(System.in));

    public KnockKnockClientProtocol(JClientServerThread serverThread) {
        super(serverThread);
    }

    @Override
    public void process(String input) {
        Logger.log("Server", input);
        if (input.equals("Bye.")) {
            this.serverThread.closeConnection();
            return;
        }

        String fromUser = null;
        try {
            fromUser = stdIn.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (fromUser != null) {
            if (fromUser.equals("Bye.")) this.serverThread.closeConnection();
            else this.serverThread.send(fromUser);
        }
    }
}
