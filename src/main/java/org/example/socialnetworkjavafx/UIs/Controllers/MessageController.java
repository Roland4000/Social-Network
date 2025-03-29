package org.example.socialnetworkjavafx.UIs.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.example.socialnetworkjavafx.Entities.Message;

import java.util.List;

public class MessageController {

    String loggedUser, otherUser;
    CliController controller;

    @FXML
    private ListView<String> messageListView;

    @FXML
    private TextField messageInputField;

    private ObservableList<String> messages;

    public void setUsers(String loggedUser, String otherUser, CliController cliController) {
        this.loggedUser = loggedUser;
        this.otherUser = otherUser;
        this.controller = cliController;

        messages = FXCollections.observableArrayList();
        messageListView.setItems(messages);
        loadMessages();
    }

    private void loadMessages() {
        try {
            List<Message> messageList = controller.getAllMessagesOf(loggedUser, otherUser);
            System.out.println("Got " + messageList.size() + " messages");
            messages.clear();
            for (Message message : messageList) {
                System.out.println(message.getFrom() + ": " + message.getContent());

                String formattedMessage = formatMessage(message);
                messages.add(formattedMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String formatMessage(Message message) {
        return message.getFrom() + ": " + message.getContent();
    }

    @FXML
    private void onSendMessage() {
        String messageContent = messageInputField.getText().trim();
        if (!messageContent.isEmpty()) {
            try {
                controller.sendMessage(loggedUser, otherUser, messageContent);
                messages.add(loggedUser + ": " + messageContent);
                messageInputField.clear();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
