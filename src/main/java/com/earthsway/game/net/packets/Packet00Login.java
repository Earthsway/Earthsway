package com.earthsway.game.net.packets;

import com.earthsway.game.net.GameClient;
import com.earthsway.game.net.GameServer;

public class Packet00Login extends Packet{

    private String username;

    public Packet00Login(byte[] data) {
        super(00);
        this.username = readData(data);
    }

    public Packet00Login(String data) {
        super(00);
        this.username = username;
    }

    @Override
    public void writeData(GameClient client) {
        client.sendData(getData());
    }

    @Override
    public void writeData(GameServer server) {
        server.sendDataToAllClients(getData());
    }

    @Override
    public byte[] getData() {
        return ("00" + this.username).getBytes();
    }

    public String getUsername(){
        return username;
    }
}
