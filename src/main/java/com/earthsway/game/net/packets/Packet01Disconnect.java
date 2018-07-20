package com.earthsway.game.net.packets;

import com.earthsway.game.net.GameClient;
import com.earthsway.game.net.GameServer;

public class Packet01Disconnect extends Packet{

    private String username;

    public Packet01Disconnect(byte[] data) {
        super(01);
        this.username = readData(data);
    }

    public Packet01Disconnect(String data) {
        super(01);
        this.username = data;
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
        return ("01" + this.username).getBytes();
    }

    public String getUsername(){
        return username;
    }
}
