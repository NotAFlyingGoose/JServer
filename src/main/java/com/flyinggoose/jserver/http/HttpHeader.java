package com.flyinggoose.jserver.http;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class HttpHeader extends HashMap<String, Object> {
    public HttpHeader() {

    }

    public HttpHeader(String input) {
        String[] lines = input.split("\n");

        for (String line : lines) {
            if (!line.contains(": ")) continue;
            String key = line.split(": ", 2)[0];
            String rawValue = line.split(": ", 2)[1];

            Pattern comma = Pattern.compile("(\\s*),(\\s*)");
            if (comma.matcher(rawValue).find()) {
                List<String> list = new ArrayList<>(Arrays.asList(rawValue.split(comma.pattern())));
                put(key, list);
            } else {
                put(key, rawValue);
            }
        }
    }

    public String toHeaderString() {
        StringBuilder res = new StringBuilder();
        for (String key : keySet()) {
            if (get(key) instanceof List) {
                res.append(key).append(": ").append(get(key).toString().substring(1, get(key).toString().length()-1)).append("\n");
            } else
                res.append(key).append(": ").append(get(key)).append("\n");
        }
        return res.toString();
    }
}
