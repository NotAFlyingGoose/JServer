package com.flyinggoose.jserver.util;

import com.flyinggoose.jserver.http.JHttpHandler;
import de.neuland.jade4j.Jade4J;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class LocalFileLocator {
    public final static Map<String, String> MIME_all = new HashMap<>();
    public final static Map<String, String> MIME_application = new HashMap<>();
    public final static Map<String, String> MIME_text = new HashMap<>();
    public final static Map<String, String> MIME_audio = new HashMap<>();
    public final static Map<String, String> MIME_video = new HashMap<>();
    public final static Map<String, String> MIME_model = new HashMap<>();
    public final static Map<String, String> MIME_image = new HashMap<>();
    public final static Map<String, String> MIME_chemical = new HashMap<>();
    public final static Map<String, String> MIME_message = new HashMap<>();
    public final static Map<String, String> MIME_x_conference = new HashMap<>();
    static {
        JSONParser parser = new JSONParser();

        try (Reader reader = new FileReader(new File("src/main/resources/mime.json").getAbsolutePath())) {

            JSONObject mimeJSON = (JSONObject) parser.parse(reader);
            for (Object key : mimeJSON.keySet()) {
                String ext = key.toString();
                String mimeType = mimeJSON.get(ext).toString().split("/")[0];
                switch (mimeType) {
                    case "application" -> MIME_application.put(ext, mimeJSON.get(ext).toString());
                    case "text" -> MIME_text.put(ext, mimeJSON.get(ext).toString());
                    case "audio" -> MIME_audio.put(ext, mimeJSON.get(ext).toString());
                    case "video" -> MIME_video.put(ext, mimeJSON.get(ext).toString());
                    case "model" -> MIME_model.put(ext, mimeJSON.get(ext).toString());
                    case "image" -> MIME_image.put(ext, mimeJSON.get(ext).toString());
                    case "chemical" -> MIME_chemical.put(ext, mimeJSON.get(ext).toString());
                    case "message" -> MIME_message.put(ext, mimeJSON.get(ext).toString());
                    case "x-conference" -> MIME_x_conference.put(ext, mimeJSON.get(ext).toString());
                }
            }
            MIME_all.putAll(MIME_application);
            MIME_all.putAll(MIME_text);
            MIME_all.putAll(MIME_audio);
            MIME_all.putAll(MIME_video);
            MIME_all.putAll(MIME_model);
            MIME_all.putAll(MIME_image);
            MIME_all.putAll(MIME_chemical);
            MIME_all.putAll(MIME_message);
            MIME_all.putAll(MIME_x_conference);

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public final File directory;

    public LocalFileLocator(File directory) {
        this.directory = directory;
    }

    public File getFile(String localPath) {
        return new File(directory.getAbsolutePath() + localPath);
    }

    public static File folderToFile(File folder, String targetFileName) {

        File[] files = folder.listFiles();
        List<File> matches = new ArrayList<>();
        for (File file : files) {
            List<String> list = new LinkedList<>(Arrays.asList(file.getName().split("\\.")));
            if (list.size() > 1) list.remove(list.size() - 1);
            String fileName = joinList(list);
            String Ext = file.getName().split("\\.")[list.size()];
            if (!fileName.equalsIgnoreCase(targetFileName)) continue;
            matches.add(file);
        }

        if (matches.isEmpty()) {
            return null;
        }
        matches.sort((d1, d2) -> {
            int im1 = getMimeImportance("." + d2.getName().split("\\.")[d2.getName().split("\\.").length - 1]);
            int im2 = getMimeImportance("." + d1.getName().split("\\.")[d1.getName().split("\\.").length - 1]);
            int res = im1 - im2;
            return res;
        });

        return matches.get(0);
    }

    private static String joinList(List<?> list) {
        StringBuilder sb = new StringBuilder();
        for (Object s : list)
        {
            sb.append(s);
        }
        return sb.toString();
    }

    public static String getMimeType(String ext) {
        String res = MIME_all.get(ext);
        return res != null ? res : MIME_text.get(".txt");
    }

    public static int getMimeImportance(String ext) {
        if (MIME_all.containsKey(ext)) {
            if (MIME_application.containsKey(ext)) return 8;
            if (MIME_text.containsKey(ext)) return 7;
            if (MIME_audio.containsKey(ext)) return 6;
            if (MIME_video.containsKey(ext)) return 5;
            if (MIME_model.containsKey(ext)) return 4;
            if (MIME_image.containsKey(ext)) return 3;
            if (MIME_chemical.containsKey(ext)) return 2;
            if (MIME_message.containsKey(ext)) return 1;
            if (MIME_x_conference.containsKey(ext)) return 0;
        }
        return -1;
    }

    public static String getFileContent(File file) {
        try {
            return Files.readString(Paths.get(file.getAbsolutePath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String generateFileResponseForUrl(String url) {
        System.out.println(url);
        File f = getFile(url);
        if (f.isDirectory()) {
            f = folderToFile(f, "index");
        }
        if (f == null || !f.exists()) {
            return JHttpHandler.createErrorResponse(404, "Not Found");
        }
        String fileExt = "." + f.getName().split("\\.")[f.getName().split("\\.").length - 1];

        String content = getFileContent(f);
        if (fileExt.equals(".jade")) {
            try {
                Map<String, Object> model = new HashMap<String, Object>();
                content = Jade4J.render(f.getAbsolutePath(), model);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return JHttpHandler.createResponse("200 OK", LocalFileLocator.getMimeType(fileExt), content);
    }
}
