package com.flyinggoose.other.http;

import com.flyinggoose.jserver.util.LocalFileLocator;

import java.io.File;
import java.util.regex.Pattern;

public class RunServer {
    public static void main(String[] args) throws Exception {
        SimpleHttpServer server = new SimpleHttpServer(8080);
        LocalFileLocator lfl = new LocalFileLocator(new File("src/main/resources/public/"));
        server.addHandler(new RequestHandler(Pattern.compile(".*")) {
            @Override
            public String handle(HttpRequest type, String path) {
                return lfl.generateFileResponseForUrl(path);
            }
        });
        server.start();
    }
}
