package com.earthsway.game.net;

import com.earthsway.Utilities;
import com.earthsway.game.Game;
import com.earthsway.game.entities.PlayerMP;
import com.earthsway.game.net.packets.Packet;
import com.earthsway.game.net.packets.Packet00Login;
import com.earthsway.game.net.packets.Packet01Disconnect;
import com.earthsway.game.net.packets.Packet02Move;

import java.io.IOException;
import java.net.*;

public class GameClient extends Thread{
    private InetAddress ipAddress;
    private DatagramSocket socket;
    private Game main;

    public GameClient(Game main, String ipAddress){
        this.main = main;
        try {
            this.socket = new DatagramSocket();
            this.ipAddress = InetAddress.getByName(ipAddress);
        } catch (SocketException | UnknownHostException e) {
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
            System.out.println("SERVER > " + msg);*/
        }
    }

    private void parsePacket(byte[] data, InetAddress address, int port) {
        String message = new String(data).trim();
        Packet.PacketTypes type = Packet.lookupPacket(message.substring(0, 2));
        Packet packet = null;
        switch (type) {
            default:
            case INVALID:
                break;
            case LOGIN:
                packet = new Packet00Login(data);
                handleLogin((Packet00Login) packet, address, port);
                break;
            case DISCONNECT:
                packet = new Packet01Disconnect(data);
                System.out.println("[" + address.getHostAddress() + ":" + port + "]" + ((Packet01Disconnect) packet).getUsername() + " has left the game...");
                main.level.removePlayerMP(((Packet01Disconnect) packet).getUsername());
            case MOVE:
                packet = new Packet02Move(data);
                handleMove((Packet02Move) packet);
        }
    }

    public void sendData(byte[] data){
        DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, 8080);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
            Utilities.errorReport(e, getClass());
        }
    }

    private void handleLogin(Packet00Login packet, InetAddress address, int port){
        System.out.println("[" + address.getHostAddress() + ":" + port + "]" + packet.getUsername() + " has joined the game...");
        PlayerMP player = new PlayerMP(main.level, packet.getX(), packet.getY(), packet.getUsername(), address, port);
        main.level.addEntity(player);
    }
    private void handleMove(Packet02Move packet) {
        this.main.level.movePlayer(packet.getUsername(), packet.getX(), packet.getY(), packet.getNumSteps(), packet.isMoving(), packet.getMovingDir());
    }
}
