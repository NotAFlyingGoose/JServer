package com.flyinggoose.jserver.server;

import com.flyinggoose.jserver.NetworkCommunicator;
import com.flyinggoose.jserver.server.protocol.JServerProtocol;
import com.flyinggoose.jserver.server.protocol.JServerProtocolProvider;
import com.flyinggoose.jserver.util.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

public class JServerClientThread extends Thread implements NetworkCommunicator {
    private final Socket client;
    private final PrintWriter out;
    private final BufferedReader in;
    private final JServerProtocolProvider provider;
    private boolean open = false;
    private boolean log = true;

    public JServerClientThread(Socket client, JServerProtocolProvider provider) throws IOException {
        this.provider = provider;

        this.client = client;
        this.out = new PrintWriter(client.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(client.getInputStream()));
    }

    public JServerClientThread(Socket client, JServerProtocolProvider provider, boolean log) throws IOException {
        this(client, provider);
        this.log = log;
    }

    public Socket getClientSocket() {
        return client;
    }

    @Override
    public void send(String data) {
        out.println(data);
    }

    @Override
    public void run() {
        open = true;
        try {
            // create new protocol for this client
            JServerProtocol protocol = this.provider.getProtocolFor(this.client, this);

            while (open) {
                //get data from client
                StringBuilder sent = new StringBuilder();
                int reqs = 1;
                String line;
                do {
                    line = in.readLine();
                    if (line == null) closeConnection();
                    sent.append(line).append("\n");
                    reqs++;
                } while (line != null && !line.isEmpty() && protocol.goodReqsAmt(reqs) && open);

                //process data
                if (open) protocol.process(sent.toString().trim());
            }
        } catch (SocketException e) {
            if (log) Logger.log("Client Thread", this.client.getPort() + " was disconnected :(");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void closeConnection() {
        open = false;
        if (log) Logger.log("Client Thread", "Server closing connection to client " + this.client.getPort() + ".");
    }
}
