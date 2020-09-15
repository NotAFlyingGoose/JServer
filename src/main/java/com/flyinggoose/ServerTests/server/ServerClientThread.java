package com.flyinggoose.ServerTests.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

public class ServerClientThread extends Thread implements Runnable {
    private final Socket client;
    private final PrintWriter outbox;
    private final BufferedReader inbox;

    public ServerClientThread(Socket client) throws IOException {
        this.client = client;
        outbox = new PrintWriter(client.getOutputStream(), true);
        inbox = new BufferedReader(new InputStreamReader(client.getInputStream()));
    }

    @Override
    public void run() {
        try {
            String inputLine, outputLine;

            // Initiate conversation with client
            KnockKnockProtocol kkp = new KnockKnockProtocol();
            outputLine = kkp.processInput(null);
            outbox.println(outputLine);

            while ((inputLine = inbox.readLine()) != null) {
                outputLine = kkp.processInput(inputLine);
                outbox.println(outputLine);
                if (outputLine.equals("Bye."))
                    break;
            }
        } catch (SocketException e) {
            if (e.getMessage().equals("Connection reset")) {
                Server.log("Client Thread", this.client.getInetAddress().getHostAddress()+":"+this.client.getPort()+" was disconnected :(");
            } else {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
