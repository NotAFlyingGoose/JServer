package com.flyinggoose.ServerTests.server;

import com.flyinggoose.ServerTests.protocol.IProtocol;
import com.flyinggoose.ServerTests.protocol.KnockKnockServerProtocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

public class ServerClientThread extends Thread implements Runnable {
    private final Socket client;
    private final PrintWriter out;
    private final BufferedReader in;
    private final ServerProtocolProvider provider;

    public ServerClientThread(Socket client, ServerProtocolProvider provider) throws IOException {
        this.provider = provider;

        this.client = client;
        this.out = new PrintWriter(client.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(client.getInputStream()));
    }

    @Override
    public void run() {
        try {
            String inputLine, outputLine;

            // Initiate conversation with client
            IProtocol protocol = this.provider.getProtocolFor(this.client);
            outputLine = protocol.process(null);
            this.out.println(outputLine);

            while ((inputLine = this.in.readLine()) != null) {
                outputLine = protocol.process(inputLine);
                this.out.println(outputLine);
                System.out.println(inputLine);
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
