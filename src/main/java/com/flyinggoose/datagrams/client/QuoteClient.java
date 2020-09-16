package com.flyinggoose.datagrams.client;

import java.io.Console;
import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class QuoteClient {
    public static void main(String[] args) throws IOException {
        int port;

        DatagramSocket socket = new DatagramSocket();

        while(true) {
            new Scanner(System.in).nextLine();
            // send empty packet (it's just a request)
            byte[] buf = new byte[256];
            InetAddress address = InetAddress.getByName("localhost");
            DatagramPacket packet = new DatagramPacket(buf, buf.length,
                    address, 4445);
            socket.send(packet);

            // display result
            packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);
            String received = new String(packet.getData(), 0, packet.getLength());
            System.out.print("Quote of the Moment: " + received);
        }
    }
}
