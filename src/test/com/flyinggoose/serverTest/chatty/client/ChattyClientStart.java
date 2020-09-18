package com.flyinggoose.serverTest.chatty.client;

import com.flyinggoose.jserver.client.JClient;
import com.flyinggoose.jserver.server.JServer;
import com.flyinggoose.serverTest.chatty.room.RoomInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ChattyClientStart {
    static BufferedReader stdIn =
            new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws IOException {
        JClient.logConnections = false;
        JServer.logConnections = false;
        String user;
        String password;
        String exist;
        String room;
        String key;
        do {
            System.out.print("What is your username: ");
            user = stdIn.readLine();
            System.out.print("What is your password: ");
            password = stdIn.readLine();
            System.out.print("does this account exist (y/n): ");
            exist = stdIn.readLine();
            if (!exist.equals("n") && !exist.equals("y")) {
                throw new ChattyClientException("Answer must be 'y' or 'n'");
            }
            System.out.print("What room do you want to join: ");
            room = stdIn.readLine();
            System.out.print("Please provide a key for this room: ");
            key = stdIn.readLine();

            System.out.println("Username: " + user);
            System.out.println("Password: " + password);
            System.out.println(exist.equals("y")?"This account exists":"This account will be created");
            System.out.println("Room: " + room);
            System.out.println("Key: " + key);
            System.out.print("Do you like this information (y/n): ");
        } while (!stdIn.readLine().equals("y"));
        ChattyUser cuser = new ChattyUser(user, password, exist.equals("y"), "localhost", 8080);
        cuser.createRoomKey(RoomInfo.getRoomFromName("localhost", 8080, room).getId(), key);
        ChattyClient client = new ChattyClient(cuser, "localhost", 8080);
        client.connectToRoom(room);

    }
}
