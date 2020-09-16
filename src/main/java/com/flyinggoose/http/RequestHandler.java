package com.flyinggoose.http;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Pattern;

public abstract class RequestHandler {
    final Pattern urlPattern;
    public RequestHandler(Pattern urlPattern) {
        this.urlPattern = urlPattern;
    }

    public abstract String handle(HttpRequest type, String path);

    public static String createErrorResponse(int code, String message) {
        return createResponse(code + message, "text/plain", "Error "+code + ": "+message);
    }

    public static String createResponse(String responseType, String contentType, String content) {
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'GMT'HH:mm:ss");
        isoFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        String date = isoFormat.format(new Date());

        String httpResponse = "HTTP/1.1 "+responseType+"\r\n";
        httpResponse += "Date:" + date + "\r\n";
        if (content != null) {
            httpResponse += "Content-Length:" + content.length() + "\r\n";
            if (contentType != null) httpResponse += "Content-Type:" + contentType + "\r\n";
            else httpResponse += "Content-Type: text/plain\r\n";
        }
        httpResponse += "Connection: Closed" + "\r\n";
        if (contentType != null) httpResponse += "\r\n" + content;

        return httpResponse;
    }
}
