package org.example.socialnetworkjavafx.UIs.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;

import java.io.File;

public class RegisterController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextArea bioArea;
    @FXML
    private Button registerButton;

    @FXML
    private Button uploadButton;

    @FXML
    private ImageView ImageView;

    @FXML
    private Label photoPathLabel;

    private CliController cliController;

    @FXML
    void initialize() {
        Circle clip = new Circle();
        clip.setCenterX(ImageView.getFitWidth() / 2);
        clip.setCenterY(ImageView.getFitHeight() / 2);
        clip.setRadius(ImageView.getFitWidth() / 2);
        ImageView.setClip(clip);
    }

    public void setCliController(CliController cliController) {
        this.cliController = cliController;
    }

    @FXML
    private void handleRegister() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String photoPath =photoPathLabel.getText().trim();
        String bio = bioArea.getText().trim();

        if (username.isEmpty() || password.isEmpty() || photoPath.isEmpty() || bio.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Fields are required!");
            return;
        }

        cliController.registerUser(username, password, photoPath, bio);
        Alert a =  new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Success");
        a.setHeaderText(null);
        a.setContentText("User successfully registered.");
        a.showAndWait();
    }

    @FXML
    public void handleUploadImage() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select an Image");


        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );


        File selectedFile = fileChooser.showOpenDialog(uploadButton.getScene().getWindow());

        if (selectedFile != null) {

            Image image = new Image(selectedFile.toURI().toString());
            ImageView.setImage(image);
            photoPathLabel.setVisible(false);
            photoPathLabel.setText(selectedFile.getAbsolutePath());
        } else {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Error");
            a.setHeaderText(null);
            a.setContentText("No image selected");
            a.showAndWait();
        }
    }
}