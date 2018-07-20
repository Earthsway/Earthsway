package com.earthsway.game.net;

import com.earthsway.Utilities;
import com.earthsway.game.Main;
import com.earthsway.game.entities.PlayerMP;
import com.earthsway.game.net.packets.Packet;
import com.earthsway.game.net.packets.Packet00Login;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameServer extends Thread{
    private DatagramSocket socket;
    private Main main;
    private List<PlayerMP> connectedPlayers = new ArrayList<PlayerMP>();

    public GameServer(Main main){
        this.main = main;
        try {
            this.socket = new DatagramSocket(8080);
        } catch (SocketException e) {
            e.printStackTrace();
            Utilities.errorReport(e, getClass());
        }
    }

    public void run(){
        while (true){
            byte[] data = new byte[1024];
            DatagramPacket packet = new DatagramPacket(data, data.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
                Utilities.errorReport(e, getClass());
            }

            parsePacket(packet.getData(), packet.getAddress(), packet.getPort());

            /*String msg = new String(packet.getData());
            System.out.println("CLIENT [" + packet.getAddress().getHostAddress() + ":" + packet.getPort() + "]> " + msg);
            if(msg.trim().equalsIgnoreCase("ping")) sendData("pong".getBytes(), packet.getAddress(), packet.getPort());*/
        }
    }

    private void parsePacket(byte[] data, InetAddress address, int port) {
        String message = new String(data).trim();
        Packet.PacketTypes type = Packet.lookupPacket(message.substring(0, 2));
        switch (type){
            default:
            case INVALID:
                break;
            case LOGIN:
                Packet00Login packet = new Packet00Login(data);
                System.out.println("[" + address.getHostAddress() + ":" + port + "]" + packet.getUsername() + " has connected...");
                PlayerMP player = null;
                if(address.getHostAddress().equalsIgnoreCase("127.0.0.1")){
                player = new PlayerMP(main.level, 100, 100, main.input, packet.getUsername(), address, port);
                }
                else{
                    player = new PlayerMP(main.level, 100, 100, packet.getUsername(), address, port);
                }
                this.connectedPlayers.add(player);
                main.level.addEntity(player);
                main.player = player;
                break;
            case DISCONNECT:
                break;
        }
    }

    public void sendData(byte[] data, InetAddress ipAddress, int port){
        DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);
        try {
            this.socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
            Utilities.errorReport(e, getClass());
        }
    }

    public void sendDataToAllClients(byte[] data) {
        for(PlayerMP p : connectedPlayers){
            sendData(data, p.ipAddress, p.port);
        }
    }
}
