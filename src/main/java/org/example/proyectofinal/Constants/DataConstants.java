package org.example.proyectofinal.Constants;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Path;

public class DataConstants {
    public static final int IN_MESSAGE_PORT = 5020;
    public static final int OUT_MESSAGE_PORT = 5021;
    public static final int IN_FILE_PORT = 5022;
    public static final int VIDEO_CALL_PORT = 5023;
    public static final int AUDIO_CALL_PORT = 5024;
    public static final int AUDIO_CALL_PORT_REQUEST = 5025;
    public static final int MAX_PACKET_SIZE = 1024; // Tamaño máximo del paquete UDP
    public static final int MAX_AUDIO_PACKET_SIZE = 640;
    public static String getDownloadPath(){
        String downloadPath = System.getProperty("user.home") + "/Downloads/";
        Path path = Path.of(downloadPath);
        if (!path.toFile().exists()) {
            return System.getProperty("user.home")+"/Descargas/";
        }
        return downloadPath;
    }
}
