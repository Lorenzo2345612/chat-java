package org.example.proyectofinal.Services.FileService;

import javafx.scene.text.Text;
import org.example.proyectofinal.Constants.DataConstants;

import java.net.Socket;

public class FileService {
    public final FileServiceConsumer fileServiceConsumer;
    public FileService(Text text) {
        fileServiceConsumer = new FileServiceConsumer(text);
        fileServiceConsumer.start();
    }

    public void sendFile(String path, String ip) {
        try {
            Socket socket = new Socket(ip, DataConstants.IN_FILE_PORT);
            FileServiceSender fileServiceSender = new FileServiceSender(path, socket);
            fileServiceSender.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
