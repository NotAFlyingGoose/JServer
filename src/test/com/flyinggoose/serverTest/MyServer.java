package com.flyinggoose.serverTest;

import com.flyinggoose.serverTest.chatty.ChattyRoom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MyServer {
    static BufferedReader stdIn =
            new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws IOException {
        int port;
        String roomName;
        String key;
        int minutes;
        do {
            System.out.print("What port do you want your Room located on: ");
            port = Integer.parseInt(stdIn.readLine());
            System.out.print("What is the name of your room: ");
            roomName = stdIn.readLine();
            System.out.print("Create a key for your room: ");
            key = stdIn.readLine();
            System.out.print("How long (in minutes) do you want your key to last: ");
            minutes = Integer.parseInt(stdIn.readLine());

            System.out.println("Room Server Port: " + port);
            System.out.println("Room Name: " + roomName);
            System.out.println("Key: " + key);
            System.out.println("Key Length: " + minutes + " minutes");
            System.out.print("Do you like this information (y/n): ");
        } while (!stdIn.readLine().equals("y"));
        ChattyRoom room = new ChattyRoom(roomName, port, "localhost", 8080);
        room.createKey(key, minutes);
    }
}
