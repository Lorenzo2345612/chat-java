package org.example.proyectofinal.Services.FileService;

import org.example.proyectofinal.Constants.DataConstants;

import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class FileServiceConsumer extends Thread{
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(DataConstants.IN_FILE_PORT)) {
            while (true) {
                Socket socket = serverSocket.accept();
                receiveFile(socket);
            }
        } catch (IOException e) {
            e.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void receiveFile(Socket socket) {
        try (DataInputStream dataInputStream = new DataInputStream(socket.getInputStream())) {
            String fileName = dataInputStream.readUTF();
            long fileLength = dataInputStream.readLong();

            try (FileOutputStream fileOutputStream = new FileOutputStream(DataConstants.PATH_TO_SAVE + fileName)) {

                byte[] buffer = new byte[4096];
                int bytesRead;
                long totalBytesRead = 0;
                while (totalBytesRead < fileLength && (bytesRead = dataInputStream.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, bytesRead);
                    totalBytesRead += bytesRead;
                }

                System.out.println("File received from client: " + fileName);

            } catch (IOException e) {
                System.err.println("File transfer error: " + e.getMessage());
            }

        } catch (IOException e) {
            System.err.println("Error receiving file: " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("Error closing socket: " + e.getMessage());
            }
        }
    }
}
