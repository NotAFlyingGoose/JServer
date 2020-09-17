package com.flyinggoose.other.multicast.server;

import com.flyinggoose.other.datagrams.server.QuoteServerThread;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Date;

public class MulticastServerThread extends QuoteServerThread {
    public MulticastServerThread() throws IOException {
    }

    public MulticastServerThread(String name) throws IOException {
        super(name);
    }

    public void run() {
        while (moreQuotes) {
            try {
                byte[] buf = new byte[256];
                // don't wait for request...just send a quote

                String dString = null;
                if (in == null)
                    dString = new Date().toString();
                else
                    dString = getNextQuote();
                buf = dString.getBytes();

                InetAddress group = InetAddress.getByName("203.0.113.0");
                DatagramPacket packet;
                packet = new DatagramPacket(buf, buf.length, group, 4446);
                socket.send(packet);

                try {
                    sleep((long)Math.random() * 5000);
                }
                catch (InterruptedException e) { }
            }
            catch (IOException e) {
                e.printStackTrace();
                moreQuotes = false;
            }
        }
        socket.close();
    }
}
