package com.flyinggoose.other.web;

import com.flyinggoose.other.web.handlers.EchoGetHandler;
import com.flyinggoose.other.web.handlers.EchoHeaderHandler;
import com.flyinggoose.other.web.handlers.EchoPostHandler;
import com.flyinggoose.other.web.handlers.StaticFileHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class WebServer {
    public static void main(String[] args) throws IOException {
        new WebServer();
    }

    public WebServer() throws IOException {
        int port = 9000;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        System.out.println("server started at " + port);
        server.createContext("/", new StaticFileHandler("/", "C:\\Users\\julia\\Desktop\\Server Tests\\src\\main\\resources", "/"));
        server.createContext("/echoHeader", new EchoHeaderHandler());
        server.createContext("/echoGet", new EchoGetHandler());
        server.createContext("/echoPost", new EchoPostHandler());
        server.setExecutor(null);
        server.start();
    }
}
