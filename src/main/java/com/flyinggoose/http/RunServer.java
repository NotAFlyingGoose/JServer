package com.flyinggoose.http;

import de.neuland.jade4j.Jade4J;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class RunServer {
    public static void main(String[] args) throws Exception {
        SimpleHttpServer server = new SimpleHttpServer(8080);
        LocalFileLocator lfl = new LocalFileLocator(new File("src/main/resources/public/"));
        server.addHandler(new RequestHandler(Pattern.compile(".*")) {
            @Override
            public String handle(HttpRequest type, String path) {
                String html404 = "<!DOCTYPE HTML PUBLIC \"-//IETF//DTD HTML 2.0//EN\">\n" +
                        "<html>\n" +
                        "<head>\n" +
                        "   <title>404 Not Found</title>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "   <h1>Not Found</h1>\n" +
                        "   <p>The requested URL /t.html was not found on this server.</p>\n" +
                        "</body>\n" +
                        "</html>";
                File f = lfl.getFile(path);
                if (f.isDirectory()) {
                    f = lfl.folderToFile(f, "index");
                }
                if (f == null || !f.exists()) {
                    return createResponse("404 Not Found", "text/html", html404);
                }
                String fileExt = "." + f.getName().split("\\.")[f.getName().split("\\.").length - 1];

                String content = getFileContent(f);
                if (fileExt.equals(".jade")) {
                    try {
                        List<Book> books = new ArrayList<Book>();
                        books.add(new Book("The Hitchhiker's Guide to the Galaxy", 5.70, true));
                        books.add(new Book("Life, the Universe and Everything", 5.60, false));
                        books.add(new Book("The Restaurant at the End of the Universe", 5.40, true));

                        Map<String, Object> model = new HashMap<String, Object>();
                        model.put("books", books);
                        model.put("pageName", "My Bookshelf");
                        content = Jade4J.render(f.getAbsolutePath(), model);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(content);
                return createResponse("200 OK", LocalFileLocator.getMimeType(fileExt), content);
            }
        });
        server.addHandler(new RequestHandler(Pattern.compile(".*")) {
            @Override
            public String handle(HttpRequest type, String path) {
                String file = "<!DOCTYPE HTML PUBLIC \"-//IETF//DTD HTML 2.0//EN\">\n" +
                        "<html>\n" +
                        "<head>\n" +
                        "   <title>404 Not Found</title>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "   <h1>Not Found</h1>\n" +
                        "   <p>The requested URL /t.html was not found on this server.</p>\n" +
                        "</body>\n" +
                        "</html>";
                return createResponse("404 Not Found", "text/html", file);
            }
        });
        server.start();
    }

    public static String getFileContent(File file) {
        try {
            return Files.readString(Paths.get(file.getAbsolutePath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
