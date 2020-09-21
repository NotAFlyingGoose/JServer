package com.flyinggoose.serverTest.chatty.client;

import com.flyinggoose.jserver.client.JClient;
import com.flyinggoose.jserver.client.JClientServerThread;
import com.flyinggoose.jserver.server.JServer;
import com.flyinggoose.serverTest.Console;
import com.flyinggoose.serverTest.TextPrompt;
import com.flyinggoose.serverTest.chatty.room.RoomInfo;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ChattyClientStart {
    public final static String mainHost = "localhost";
    public final static int mainPort = 8080;

    public static void main(String[] args) {
        new ChattyClientStart();
    }

    public ChattyClientStart() {
        start();
    }

    public void start() {
        JClient.logConnections = false;
        JServer.logConnections = false;

        JFrame frame = new JFrame("Chatty");

        // page setup
        JPanel loginPage = new JPanel(new GridBagLayout());
        JPanel signUpPage = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        // login page details
        JLabel loginTitle = new JLabel("Welcome to Chatty! Log In Here");
        loginTitle.setFont(new Font(loginTitle.getFont().getName(), loginTitle.getFont().getStyle(), 20));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 0;
        loginPage.add(loginTitle, c);

        TextPrompt loginUsernameInput = new TextPrompt("Username");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 1;
        loginPage.add(loginUsernameInput, c);

        TextPrompt loginPasswordInput = new TextPrompt("Password");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 2;
        loginPage.add(loginPasswordInput, c);

        JButton loginSwitchButton = new JButton("Don't have an Account?");
        loginSwitchButton.addActionListener(e -> {
            frame.getContentPane().removeAll();
            frame.add(signUpPage);
            frame.pack();
        });
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 3;
        loginPage.add(loginSwitchButton, c);

        JButton loginEnterButton = new JButton("Sign In");
        loginEnterButton.addActionListener(e -> {
            try {
                run(new ChattyUser(loginUsernameInput.getText(), loginPasswordInput.getText(), true, mainHost, mainPort));
                frame.dispose();
            } catch (ChattyClientException ce) {
                JOptionPane.showMessageDialog(frame, "There was an error while trying to sign in (Incorrect Username or Password)", "Sign In", JOptionPane.ERROR_MESSAGE);
            }
        });
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 1;
        c.gridx = 1;
        c.gridy = 3;
        loginPage.add(loginEnterButton, c);

        loginPage.setBorder(new TitledBorder("Login Page"));

        // sign up page details
        JLabel signUpTitle = new JLabel("Welcome to Chatty! Sign Up Here");
        signUpTitle.setFont(new Font(loginTitle.getFont().getName(), loginTitle.getFont().getStyle(), 20));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 0;
        signUpPage.add(signUpTitle, c);

        TextPrompt signUpUsernameInput = new TextPrompt("Username");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 1;
        signUpPage.add(signUpUsernameInput, c);

        TextPrompt signUpPasswordInput = new TextPrompt("Password");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 2;
        signUpPage.add(signUpPasswordInput, c);

        JButton signUpSwitchButton = new JButton("Already have an Account?");
        signUpSwitchButton.addActionListener(e -> {
            frame.getContentPane().removeAll();
            frame.add(loginPage);
            frame.pack();
        });
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 3;
        signUpPage.add(signUpSwitchButton, c);

        JButton signUpEnterButton = new JButton("Sign Up");
        signUpEnterButton.addActionListener(e -> {
            try {
                run(new ChattyUser(loginUsernameInput.getText(), loginPasswordInput.getText(), false, mainHost, mainPort));
                frame.dispose();
            } catch (ChattyClientException ce) {
                JOptionPane.showMessageDialog(frame, "There was an error while trying to sign up your account", "Sign Up", JOptionPane.ERROR_MESSAGE);
            }
        });
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 1;
        c.gridx = 1;
        c.gridy = 3;
        signUpPage.add(signUpEnterButton, c);

        signUpPage.setBorder(new TitledBorder("Sign Up Page"));

        // finish

        frame.getContentPane().removeAll();
        frame.add(loginPage);
        frame.pack();

        frame.setResizable(false);
        frame.setVisible(true);

        /*String user;
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
        client.connectToRoom(room);*/
    }

    private void run(ChattyUser user) {
        ChattyClient client = new ChattyClient(user, "localhost", 8080);

        JFrame frame = new JFrame("Join Room");

        JPanel joinPage = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        // join page details
        JLabel joinTitle = new JLabel("Join Chatty Room");
        joinTitle.setFont(new Font(joinTitle.getFont().getName(), joinTitle.getFont().getStyle(), 20));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 0;
        joinPage.add(joinTitle, c);

        TextPrompt joinRoomName = new TextPrompt("Room Name");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 1;
        joinPage.add(joinRoomName, c);

        TextPrompt joinRoomKey = new TextPrompt("Key");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 2;
        joinPage.add(joinRoomKey, c);

        JButton signUpSwitchButton = new JButton("Join");
        signUpSwitchButton.addActionListener(e -> {
            createRoomConnection(user, client, joinRoomName.getText(), joinRoomKey.getText());
        });
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 3;
        joinPage.add(signUpSwitchButton, c);

        joinPage.setBorder(new TitledBorder("Join a Chatty Room"));

        frame.getContentPane().removeAll();
        frame.add(joinPage);
        frame.pack();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setResizable(false);
        frame.setVisible(true);
    }

    public void createRoomConnection(ChattyUser user, ChattyClient client, String name, String key) {
        new Thread(() -> {
            final boolean[] running = {true};
                RoomInfo roomInfo = RoomInfo.getRoomFromName(mainHost, mainPort, name);
                user.createRoomKey(RoomInfo.getRoomFromName(mainHost, mainPort, name).getId(), key);
                JClientServerThread serverThread = client.connectToRoom(name);
                Console console = new Console();
                console.show();

                console.getWindow().addWindowListener(new WindowAdapter()
                {
                    @Override
                    public void windowClosing(WindowEvent e)
                    {
                        serverThread.closeConnection();
                        running[0] = false;
                        //serverThread.interrupt();
                        e.getWindow().dispose();
                    }
                });

                while (running[0]) {
                    try {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            running[0] = false;
                        }
                        synchronized (console) {
                            console.clear();
                            for (String msg : user.getMessages(roomInfo.getId())) {
                                String[] parts = msg.split(";", 4);
                                console.print(parts[2] + ", ", Color.GREEN, null);
                                console.print(parts[1] + " said : ", Color.RED, null);
                                console.print(parts[3], null, null);
                                console.println();
                            }

                            if (!console.getInput().isEmpty()) {
                                System.out.println(console.getLastEntered());
                                user.queueMessage(roomInfo.getId(), console.getLastEntered());
                            }
                        }
                    } catch (ChattyClientException e) {
                        running[0] = false;
                        JOptionPane.showMessageDialog(null, "The Connection was refused (Bad Key)", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }

            System.out.println("Connection Ended");
        }).start();
    }
}
