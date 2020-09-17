package com.flyinggoose.jserver.server;

import com.flyinggoose.jserver.server.http.JHttpHandler;
import com.flyinggoose.jserver.server.http.JHttpMethodType;
import com.flyinggoose.jserver.server.http.JHttpServer;
import com.flyinggoose.jserver.server.protocol.KnockKnockServerProtocol;
import com.flyinggoose.jserver.util.LocalFileLocator;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) throws IOException {
        createSimpleHttpServer(4444).start();
    }

    public static JServer createSimpleHttpServer(int port) throws IOException {
        JHttpServer server = new JHttpServer(port);
        LocalFileLocator files = new LocalFileLocator(new File("src/main/resources/public/"));
        server.addUrlHandler(new JHttpHandler(Pattern.compile(".*")) {
            @Override
            public void handle(JHttpMethodType type, String path, JServerClientThread clientThread) {
                clientThread.send(files.generateFileResponseForUrl(path));
            }
        });

        return server;
    }

    public static JServer createKnockKnockServer(int port) throws IOException {
        JServer server = new JServer(port, ((client, clientThread) -> new KnockKnockServerProtocol(clientThread)));


        return server;
    }
}
