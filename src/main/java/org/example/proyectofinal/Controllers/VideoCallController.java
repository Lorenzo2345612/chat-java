package org.example.proyectofinal.Controllers;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.example.proyectofinal.Services.VideoCallService.VideoCallService;

public class VideoCallController {

    private Stage stage;
    @FXML
    private ImageView consumerImageView;
    @FXML
    private ImageView senderImageView;

    private VideoCallService videoCallService;

    //TODO
    public void initialize() {
        consumerImageView = new ImageView();
        consumerImageView.setImage(new Image("C:\\Users\\loren\\OneDrive\\Escritorio\\Courses\\redes\\frames\\image.jpg"));
        senderImageView = new ImageView();
        senderImageView.setImage(new Image("C:\\Users\\loren\\OneDrive\\Escritorio\\Courses\\redes\\frames\\image.jpg"));

        this.stage.setOnCloseRequest(event -> {
            //videoCallService.stop();
        });
    }
}
