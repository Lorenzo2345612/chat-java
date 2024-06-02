package org.example.proyectofinal;

import javafx.collections.*;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.proyectofinal.Constants.DataConstants;
import org.example.proyectofinal.Controllers.VideoCallController;
import org.example.proyectofinal.Model.ChatModel;
import org.example.proyectofinal.Model.VideoModel;
import org.example.proyectofinal.Services.AudioService.AudioService;
import org.example.proyectofinal.Services.FileService.FileService;
import org.example.proyectofinal.Services.MessageService.MessageServiceConsumer;
import org.example.proyectofinal.Services.MessageService.MessageServiceSender;
import org.example.proyectofinal.Services.VideoCallService.VideoCallService;

import java.io.File;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HelloController {

    @FXML
    private ListView<String> listView;

    private Map<String, ObservableList<String>> messages;

    private ObservableSet<String> chatList;
    @FXML
    private String currentChat;

    @FXML
    private VBox chatBox;
    private ChatModel chatModel;
    @FXML
    private ListView<String> currentChatMessages;

    @FXML
    private TextField messageField;
    @FXML
    private Button sendButton;

    private MessageServiceConsumer messageServiceConsumer;

    // Add Contact
    @FXML
    private TextField addContactTextField;
    @FXML
    private Button addContactButton;

    // File Transfer
    @FXML
    private Button sendFileButton;
    @FXML
    private FileChooser fileChooser;

    private FileService fileService;

    // Video Call
    @FXML
    private VBox videoCallBox;
    private VideoModel videoModel;
    @FXML
    private ImageView senderImageView;
    @FXML
    private ImageView consumerImageView;
    private VideoCallService videoCallService;
    private AudioService audioService;

    @FXML
    private Text fileTransferText;

    public void initialize() {
        setUpChatList();
        messages = new HashMap<>();
        startMessageConsumer();
        chatModel = new ChatModel();
        videoModel = new VideoModel();
        chatBox.visibleProperty().bind(chatModel.isAbleToSeeProperty());
        videoCallBox.visibleProperty().bind(videoModel.isAbleToSeeProperty());
        fileService = new FileService(fileTransferText);
    }

    public void startMessageConsumer() {
        try {
            DatagramSocket socket = new DatagramSocket(DataConstants.IN_MESSAGE_PORT);
            messageServiceConsumer = new MessageServiceConsumer(socket, messages, chatList);
            messageServiceConsumer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startVideoCall() {
        if (currentChat == null) {
            return;
        }
        endVideoCall();
        videoModel.setIsAbleToSee(true);
        videoCallService = new VideoCallService( currentChat, consumerImageView, senderImageView);
        videoCallService.start();
        audioService = new AudioService(currentChat);
        audioService.start();

    }

    private void endVideoCall() {
        videoModel.setIsAbleToSee(false);
        if (videoCallService != null) {
            System.out.println("Stopping video call");
            videoCallService.stop();
            videoCallService = null;
            consumerImageView.setImage(null);
            senderImageView.setImage(null);
        }
        if (audioService != null) {
            System.out.println("Stopping audio call");
            audioService.stop();
            audioService = null;
        }
    }

    public void sendMessage() throws Exception {
        String message = messageField.getText();
        if (message.isEmpty() || currentChat == null) {
            return;
        }
        String localMessage = "You: " + message;
        messages.get(currentChat).add(localMessage);
        messageField.clear();
        DatagramSocket socket = new DatagramSocket();
        MessageServiceSender messageServiceSender = new MessageServiceSender(socket, currentChat, DataConstants.IN_MESSAGE_PORT);
        messageServiceSender.sendMessage(message);

    }

    public void addContact() {
        String contact = addContactTextField.getText();
        if (contact.isEmpty()) {
            return;
        }
        System.out.println("Adding contact: " + contact);
        chatList.add(contact);
        messages.put(contact, FXCollections.observableArrayList());
        addContactTextField.clear();
    }

    protected void setUpFileChooser() {
        fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
    }

    public void sendFile() {
        setUpFileChooser();
        File file = fileChooser.showOpenDialog(null);
        if (file == null || currentChat == null || file.isDirectory()) {
            return;
        }
        fileService.sendFile(file.getAbsolutePath(), currentChat);
    }

    protected void setUpChatList() {
        chatList = FXCollections.observableSet();
        listView.setItems(FXCollections.observableArrayList(chatList));

        chatList.addListener((SetChangeListener<String>) change -> {
            if (change.wasAdded()) {
                listView.getItems().add(change.getElementAdded());
            } else if (change.wasRemoved()) {
                listView.getItems().remove(change.getElementRemoved());
            }
        });

        listView.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            // Detectar si se clickeo en un chat
            String chat = listView.getSelectionModel().getSelectedItem();
            if (chat == null) {
                return;
            }
            if (currentChat != null && currentChat.equals(chat)) {
                return;
            }
            currentChat = chat;
            chatModel.setIsAbleToSee(true);
            videoModel.setIsAbleToSee(false);
            currentChatMessages.setItems(messages.get(chat));
        });
    }
}