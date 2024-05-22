package org.example.proyectofinal.Services.MessageService;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import org.example.proyectofinal.Model.Message;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Map;

public class MessageServiceConsumer extends Thread {
    // Create a udp socket
    public DatagramSocket socket;
    protected final int CLIENT_PORT;
    protected Map<String, ObservableList<String>> messages;
    protected ObservableSet<String> chatList;

    public MessageServiceConsumer(DatagramSocket socket, Map<String, ObservableList<String>> messages, ObservableSet<String> chatList) throws Exception {
        // Create the socket
        CLIENT_PORT = socket.getLocalPort();
        this.socket = socket;
        this.messages = messages;
        this.chatList = chatList;
    }

    public void run() {
        try {
            Message messageObj = new Message();
            // Start the loop
            do {
                messageObj = receiveMessage();
                processMessage(messageObj);
            } while (true);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private Message receiveMessage() throws Exception {
        Message messageObj = new Message();
        byte[] receiveServerBytes;
        final int MAX_BUFFER = 1024;
        DatagramPacket serverPacket;

        receiveServerBytes = new byte[MAX_BUFFER];

        serverPacket = new DatagramPacket(receiveServerBytes, MAX_BUFFER);
        socket.receive(serverPacket);
        messageObj.setServerAddress(serverPacket.getAddress());
        messageObj.setServerPort(serverPacket.getPort());

        String message = new String(receiveServerBytes).trim();
        messageObj.setMessage(message);

        System.out.println("Received message: " + message);


        return messageObj;
    }

    private void processMessage(Message message) {
        Platform.runLater(() -> {
            String serverAddress = message.getServerAddress().getHostAddress();
            chatList.add(serverAddress);
            System.out.println(serverAddress);
            if (!messages.containsKey(serverAddress)) {
                messages.put(serverAddress, FXCollections.observableArrayList());
            }
            String messageWithAddress = serverAddress + ": " + message.getMessage();
            messages.get(serverAddress).add(messageWithAddress);
        });
    }

}
