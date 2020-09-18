package com.flyinggoose.serverTest;

import com.flyinggoose.serverTest.chatty.ChattyClient;
import com.flyinggoose.serverTest.chatty.ChattyUser;
import com.flyinggoose.serverTest.chatty.RoomInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MyClient {
    static BufferedReader stdIn =
            new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws IOException {
        String user;
        String room;
        String key;
        do {
            System.out.print("What is your username: ");
            user = stdIn.readLine();
            System.out.print("What room do you want to join: ");
            room = stdIn.readLine();
            System.out.print("Please provide a key for this room: ");
            key = stdIn.readLine();

            System.out.println("Username: " + user);
            System.out.println("Room: " + room);
            System.out.println("Key: " + key);
            System.out.print("Do you like this information (y/n): ");
        } while (!stdIn.readLine().equals("y"));
        ChattyUser cuser = new ChattyUser(user);
        cuser.createRoomKey(RoomInfo.getRoomFromName("localhost", 8080, room).getId(), key);
        ChattyClient client = new ChattyClient(cuser, "localhost", 8080);
        client.connectToRoom(room);

    }
}
