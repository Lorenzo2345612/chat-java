package org.example.proyectofinal.Services.AudioService;

import com.almasb.fxgl.audio.AudioPlayer;
import com.almasb.fxgl.audio.Music;
import org.example.proyectofinal.Constants.DataConstants;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class AudioConsumerService extends Thread {
    private final String host;
    public AudioConsumerService(String host) {
        this.host = host;
    }
    public void run() {
        try (Socket socket = new Socket(host, DataConstants.AUDIO_CALL_PORT)) {

            receiveAudio(socket);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private void receiveAudio(Socket socket) {
        try {
            InputStream inputStream = socket.getInputStream();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

                AudioFormat audioFormat = new AudioFormat(8000.0f, 16, 2, true, true);
                DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);
                SourceDataLine sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);

                sourceDataLine.open(audioFormat);
                sourceDataLine.start();

                byte[] buffer = new byte[DataConstants.MAX_PACKET_SIZE];
                int bytesRead;

                while ((bytesRead = bufferedInputStream.read(buffer, 0, buffer.length)) != -1) {
                    sourceDataLine.write(buffer, 0, bytesRead);
                }

                sourceDataLine.drain();
                sourceDataLine.close();
                socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
