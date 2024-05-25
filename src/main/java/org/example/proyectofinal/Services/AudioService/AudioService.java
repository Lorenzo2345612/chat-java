package org.example.proyectofinal.Services.AudioService;

public class AudioService {
    private AudioConsumerService audioConsumerService;
    private AudioSenderService audioSenderService;
    public AudioService(String host) {
        audioConsumerService = new AudioConsumerService(host);
        audioSenderService = new AudioSenderService();
    }

    public void start() {
        audioConsumerService.start();
        audioSenderService.start();
    }

    public void stop() {
        audioConsumerService.interrupt();
        audioSenderService.interrupt();
    }
}
