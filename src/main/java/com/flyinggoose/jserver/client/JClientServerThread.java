package com.flyinggoose.jserver.client;

import com.flyinggoose.jserver.NetworkCommunicator;
import com.flyinggoose.jserver.client.protocol.JClientProtocol;
import com.flyinggoose.jserver.client.protocol.JClientProtocolProvider;
import com.flyinggoose.jserver.util.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class JClientServerThread extends Thread implements NetworkCommunicator {
    JClientProtocolProvider provider;
    String host;
    int port;
    PrintWriter out;
    BufferedReader in;
    private boolean open = false;

    public JClientServerThread(JClientProtocolProvider provider, String host, int port) {
        this.provider = provider;
        this.host = host;
        this.port = port;
    }

    @Override
    public void send(String data) {
        out.println(data);
    }

    @Override
    public void closeConnection() {
        open = false;
        Logger.log("Client", "Client closing connection to host " + this.port + ".");
    }

    @Override
    public void run() {
        open = true;
        try (
                Socket serverSocket = new Socket(host, port);
        ) {
            out = new PrintWriter(serverSocket.getOutputStream(), true);
            in = new BufferedReader(
                    new InputStreamReader(serverSocket.getInputStream()));

            while (open) {
                JClientProtocol protocol = this.provider.getProtocolFor(serverSocket, this);

                //get data from server
                StringBuilder sent = new StringBuilder();
                int reqs = 1;
                String line = in.readLine();
                sent.append(line).append("\n");
                while (line != null && !line.isEmpty() && protocol.goodReqsAmt(reqs) && open) {
                    line = in.readLine();
                    sent.append(line).append("\n");
                    reqs++;
                }

                //process data
                protocol.process(sent.toString().trim());
            }

            out.close();
            in.close();
            serverSocket.close();
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + host);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                    host);
            System.exit(1);
        }
    }
}
