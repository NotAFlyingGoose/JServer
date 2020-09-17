package com.flyinggoose.jserver.client;

import com.flyinggoose.jserver.client.protocol.JClientProtocol;
import com.flyinggoose.jserver.client.protocol.JClientProtocolProvider;
import com.flyinggoose.jserver.util.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class JClient {
    final JClientProtocolProvider provider;
    private boolean open = false;
    private String currentHost = null;
    private int currentPort = 0;

    public JClient(JClientProtocolProvider provider) {
        this.provider = provider;
    }

    public void connectToServer(String host, int port) {
        this.currentHost = host;
        this.currentPort = port;
        open = true;
        try (
                Socket serverSocket = new Socket(host, port);
                PrintWriter out = new PrintWriter(serverSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(serverSocket.getInputStream()))
        ) {

            while (open) {
                JClientProtocol protocol = this.provider.getProtocolFor(serverSocket, this);

                //get data from client
                StringBuilder sent = new StringBuilder();
                int reqs = 1;
                String line = in.readLine();
                sent.append(line).append("\n");
                while (line != null && !line.isEmpty() && protocol.goodReqsAmt(reqs) && open) {
                    line = in.readLine();
                    sent.append(line).append("\n");
                    reqs++;
                }
            }
            BufferedReader stdIn =
                    new BufferedReader(new InputStreamReader(System.in));
            String fromServer;
            String fromUser;

            while ((fromServer = in.readLine()) != null) {
                Logger.log("Server", fromServer);
                if (fromServer.equals("Bye."))
                    break;

                fromUser = stdIn.readLine();
                if (fromUser != null) {
                    Logger.log("Client", fromUser);
                    out.println(fromUser);
                }
            }
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + host);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                    host);
            System.exit(1);
        }
    }

    public void closeConnection() {
        if (currentHost == null) {
            Logger.log("Client", "Cannot close connection");
            return;
        }
        open = false;
        Logger.log("Client", "Client closing connection to host " + currentPort + ".");
    }
}
