package com.earthsway.game.net;

import com.earthsway.Utilities;
import com.earthsway.game.Main;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;

public class GameClient extends Thread{
    private InetAddress ipAddress;
    private DatagramSocket socket;
    private Main main;

    public GameClient(Main main, String ipAddress){
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
            String msg = new String(packet.getData());
            System.out.println("SERVER > " + msg);
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

}
