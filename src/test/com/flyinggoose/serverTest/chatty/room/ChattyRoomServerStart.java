package com.flyinggoose.serverTest.chatty.room;

import com.flyinggoose.jserver.client.JClient;
import com.flyinggoose.jserver.http.HttpHeader;
import com.flyinggoose.jserver.server.JServer;
import com.flyinggoose.serverTest.chatty.client.ChattyClientException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChattyRoomServerStart {
    public static List<ServerCommand> roomCommands = new ArrayList<>();
    static {
        roomCommands.add(new ServerCommand("all") {
            @Override
            public void execute(String[] args, ChattyRoom room) {
                List<List<String>> rows = new ArrayList<>();
                rows.add(Arrays.asList("Message ID", "Author", "Time", "Content"));
                for (String msg : room.getMessages()) {
                    String[] msgInfo = msg.replaceAll("(?<!\\%)\\%(?!\\%)2C", ",").split(";", 4);
                    rows.add(Arrays.asList(msgInfo));
                }
                System.out.println(formatAsTable(rows));
            }
        });
        roomCommands.add(new ServerCommand("msg") {
            @Override
            public void execute(String[] args, ChattyRoom room) {
                String msg = room.getMessage(Integer.parseInt(args[0]));
                String[] msgInfo = msg.replaceAll("(?<!\\%)\\%(?!\\%)2C", ",").split(";", 4);
                HttpHeader header = new HttpHeader();
                header.put("Author", msgInfo[1]);
                header.put("Created", msgInfo[2]);
                header.put("Content", msgInfo[2]);
                System.out.println(header.toHeaderString());
            }
        });
        roomCommands.add(new ServerCommand("info") {
            @Override
            public void execute(String[] args, ChattyRoom room) {
                System.out.println("Room Name: " + room.getInfo().getRoomName());
                System.out.println("Room ID: " + room.info.getId());
                System.out.println("Room Keys: " + room.getKeysAmt());
                System.out.println("Messages: " + room.getMessages().size());
                System.out.println("Contributors: " + room.getContributors().size());
            }
        });
        roomCommands.add(new ServerCommand("key") {
            @Override
            public void execute(String[] args, ChattyRoom room) {
                if (args.length < 2) {
                    System.out.println("Usage: \"key create [name] [minutes]\"\n" +
                            "or \"key valid [name]\"\n" +
                            "or \"key delete [name]\"");
                    return;
                }

                if (args[0].equals("create")) {
                    if (args.length < 3) {
                        System.out.println("Usage: \"key create [name] [minutes]\"\n");
                        return;
                    }

                    room.createKey(args[1], Integer.parseInt(args[2]));
                }
                else if (args[0].equals("valid")) {
                    if (room.isValidKey(args[1])) {
                        System.out.println("Valid Key :)");
                    } else {
                        System.out.println("Invalid Key :(");
                    }
                }
            }
        });
        roomCommands.add(new ServerCommand("stop") {
            @Override
            public void execute(String[] args, ChattyRoom room) {
                room.getServer().stop();
            }
        });
    }
    static BufferedReader stdIn =
            new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws IOException {
        JClient.logConnections = false;
        JServer.logConnections = false;
        int port;
        String roomName;
        String exist;
        do {
            System.out.print("What port do you want your Room located on: ");
            port = Integer.parseInt(stdIn.readLine());
            System.out.print("What is the name of your room: ");
            roomName = stdIn.readLine();
            System.out.print("does this room exist (y/n): ");
            exist = stdIn.readLine();
            if (!exist.equals("n") && !exist.equals("y")) {
                throw new ChattyClientException("Answer must be 'y' or 'n'");
            }

            System.out.println("Room Server Port: " + port);
            System.out.println("Room Name: " + roomName);
            System.out.println(exist.equals("y")?"This room exists":"This room will be created");
            System.out.print("Do you like this information (y/n): ");
        } while (!stdIn.readLine().equals("y"));
        ChattyRoom room = new ChattyRoom(roomName, port, exist.equals("y"), "localhost", 8080);
    }

    public static String formatAsTable(List<List<String>> rows)
    {
        int[] maxLengths = new int[rows.get(0).size()];
        for (List<String> row : rows)
        {
            for (int i = 0; i < row.size(); i++)
            {
                maxLengths[i] = Math.max(maxLengths[i], row.get(i).length());
            }
        }

        StringBuilder formatBuilder = new StringBuilder();
        for (int maxLength : maxLengths)
        {
            formatBuilder.append("%-").append(maxLength + 2).append("s");
        }
        String format = formatBuilder.toString();

        StringBuilder result = new StringBuilder();
        for (List<String> row : rows)
        {
            result.append(String.format(format, row.toArray(new String[0]))).append("\n");
        }
        return result.toString();
    }
}
