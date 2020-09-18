package com.flyinggoose.serverTest.chatty.room;

abstract class ServerCommand {
    private String name;

    public String getName() {
        return name;
    }

    public ServerCommand(String name) {
        this.name = name;
    }

    public abstract void execute(String[] args, ChattyRoom room);
}
