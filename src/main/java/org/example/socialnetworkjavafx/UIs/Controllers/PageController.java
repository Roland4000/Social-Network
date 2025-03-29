package org.example.socialnetworkjavafx.UIs.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.TextArea;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;
import org.example.socialnetworkjavafx.Entities.User;

public class PageController {


    @FXML
    private ImageView imageView;

    @FXML
    private Label usernameLabel;

    @FXML
    private TextArea bioArea;

    public void initialize() {
        Circle clip = new Circle();
        clip.setCenterX(imageView.getFitWidth() / 2);
        clip.setCenterY(imageView.getFitHeight() / 2);
        clip.setRadius(imageView.getFitWidth() / 2);
        imageView.setClip(clip);
        bioArea.setDisable(true);
    }

    // Method to update user data dynamically
    public void setUser(User user) {
        usernameLabel.setText(user.getUsername());
        bioArea.setText(user.getBio());
        Image image = new Image("file:"+user.getPhotoPath());
        imageView.setImage(image);
    }
}
