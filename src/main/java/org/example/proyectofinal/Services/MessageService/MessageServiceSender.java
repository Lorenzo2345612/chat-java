package org.example.proyectofinal.Services.MessageService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class MessageServiceSender {
    protected final int SERVER_PORT;
    protected final String SERVER;
    protected DatagramSocket socket;

    public MessageServiceSender(DatagramSocket socket, String server, int serverPort) {
        this.socket = socket;
        SERVER = server;
        SERVER_PORT = serverPort;
    }


    public void sendMessage(String message) throws Exception {
        byte[] messageBytes;
        DatagramPacket packet;

        InetAddress serverAddress = InetAddress.getByName(SERVER);
        messageBytes = message.getBytes();
        packet = new DatagramPacket(messageBytes, message.length(), serverAddress, SERVER_PORT);
        socket.send(packet);
        socket.close();
    }
}