package com.earthsway.game.net;

import com.earthsway.Utilities;
import com.earthsway.game.Main;
import com.earthsway.game.entities.PlayerMP;
import com.earthsway.game.net.packets.Packet;
import com.earthsway.game.net.packets.Packet00Login;
import com.earthsway.game.net.packets.Packet01Disconnect;
import com.earthsway.game.net.packets.Packet02Move;

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

            this.parsePacket(packet.getData(), packet.getAddress(), packet.getPort());

            /*String msg = new String(packet.getData());
            System.out.println("CLIENT [" + packet.getAddress().getHostAddress() + ":" + packet.getPort() + "]> " + msg);
            if(msg.trim().equalsIgnoreCase("ping")) sendData("pong".getBytes(), packet.getAddress(), packet.getPort());*/
        }
    }

    private void parsePacket(byte[] data, InetAddress address, int port) {
        String message = new String(data).trim();
        Packet.PacketTypes type = Packet.lookupPacket(message.substring(0, 2));
        Packet packet = null;

        switch (type){
            default:
            case INVALID:
                break;
            case LOGIN:
                packet = new Packet00Login(data);
                System.out.println("[" + address.getHostAddress() + ":" + port + "]" + ((Packet00Login) packet).getUsername() + " has connected...");
                PlayerMP player = new PlayerMP(main.level, 100, 100, ((Packet00Login) packet).getUsername(), address, port);
                this.addConnection(player, (Packet00Login) packet);
                break;
            case DISCONNECT:
                packet = new Packet01Disconnect(data);
                System.out.println("[" + address.getHostAddress() + ":" + port + "]" + ((Packet01Disconnect) packet).getUsername() + " has disconnected...");
                this.removeConnection((Packet01Disconnect) packet);
                break;
            case MOVE:
                packet = new Packet02Move(data);
                //System.out.println(((Packet02Move) packet). getUsername() + " has moved to " + ((Packet02Move) packet).getX() + ", " + ((Packet02Move) packet).getY());
                this.handleMove((Packet02Move) packet);
        }
    }

    public void addConnection(PlayerMP player, Packet00Login packet) {
        boolean alreadyConnected = false;
        for(PlayerMP p : this.connectedPlayers){
            if(player.getUsername().equalsIgnoreCase(p.getUsername())){
                if(p.ipAddress == null)p.ipAddress = player.ipAddress;
                if(p.port == -1) p.port = player.port;
                alreadyConnected = true;
            }
            else{
                sendData(packet.getData(), p.ipAddress, p.port);
                packet = new Packet00Login(p.getUsername(), p.x, p.y);
                sendData(packet.getData(), player.ipAddress, player.port);
            }
        }
        if(!alreadyConnected){
            this.connectedPlayers.add(player);
        }
    }

    public void removeConnection(Packet01Disconnect packet) {
        this.connectedPlayers.remove(getPlayerMPIndex(packet.getUsername()));
        packet.writeData(this);
    }

    public PlayerMP getPlayerMP(String username) {
        for(PlayerMP p : this.connectedPlayers){
            if(p.getUsername().equals(username)) return p;
        }
        return null;
    }

    public int getPlayerMPIndex(String username) {
        int index = 0;
        for(PlayerMP p : this.connectedPlayers){
            if(p.getUsername().equals(username)) break;
            index++;
        }
        return index;
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

    private void handleMove(Packet02Move packet) {
        if(getPlayerMP(packet.getUsername()) != null){
            int index = getPlayerMPIndex(packet.getUsername());
            PlayerMP player = this.connectedPlayers.get(index);
            player.x = packet.getX();
            player.y = packet.getY();
            player.setMoving(packet.isMoving());
            player.setMovingDir(packet.getMovingDir());
            player.setNumSteps(packet.getNumSteps());
            packet.writeData(this);
        }
    }
}
