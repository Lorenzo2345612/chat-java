package org.example.proyectofinal.Services.FileService;

import javafx.application.Platform;
import javafx.scene.text.Text;
import org.example.proyectofinal.Constants.DataConstants;
import org.example.proyectofinal.Utils.TextFormat;
import org.example.proyectofinal.Utils.TransferMetrics;
import org.example.proyectofinal.Utils.UnitsConverter;

import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class FileServiceConsumer extends Thread{
    private final Text text;
    public FileServiceConsumer(Text text) {
        this.text = text;
    }
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

            try (FileOutputStream fileOutputStream = new FileOutputStream(DataConstants.getDownloadPath() + fileName)) {

                byte[] buffer = new byte[4096];
                int bytesRead;
                double startTime = System.currentTimeMillis();
                double realStartTime = startTime;
                long totalBytesRead = 0;
                long lastBytesRead = 0;

                while (totalBytesRead < fileLength && (bytesRead = dataInputStream.read(buffer)) != -1) {
                    // Print buffer content
                    System.out.println(new String(buffer));
                    fileOutputStream.write(buffer, 0, bytesRead);
                    double endTime = System.currentTimeMillis();
                    Thread.sleep(10);
                    if (endTime - startTime > 1000) {
                        double seconds = (endTime - startTime) / 1000;
                        double bps = (totalBytesRead - lastBytesRead) * 8 / seconds;
                        String transferInfo = "Nombre: " + TextFormat.toFixedLengthString(fileName, 8)
                                + " - Tasa Transferencia " + UnitsConverter.convertbpsToHumanReadable(bps)
                                + " - Tiempo Transcurrido "+UnitsConverter.convertSecondsToHumanReadable((endTime - realStartTime) / 1000)
                                + " - Tiempo Restante "+UnitsConverter.convertSecondsToHumanReadable(TransferMetrics.calculateRemainingTime(fileLength - totalBytesRead, bps));
                        Platform.runLater(() -> text.setText(transferInfo));
                        startTime = endTime;
                        lastBytesRead = totalBytesRead;
                    }
                    totalBytesRead += bytesRead;
                }



                Platform.runLater(() -> text.setText("Transferencia de archivo completada"));

            } catch (IOException e) {
                System.err.println("File transfer error: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
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
