package org.example.socialnetworkjavafx.UIs.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.socialnetworkjavafx.Entities.Friendship;
import org.example.socialnetworkjavafx.Entities.User;
import org.example.socialnetworkjavafx.HelloApplication;

import java.io.IOException;
import java.util.List;

public class LandingController {
    CliController cliController;

    public LandingController() {
    }

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label messageLabel;

    public void setController(CliController cliController) {
        this.cliController = cliController;
    }

    @FXML
    private void onLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try {
            User loggedInUser = cliController.logIn(username, password);

            if (loggedInUser != null) {
                openLoggedInPage(loggedInUser);
            } else {
                messageLabel.setText("Invalid username or password!");
            }
        } catch (Exception e) {
            messageLabel.setText("Error during login: " + e.getMessage());
        }
    }

    @FXML
    private void openLoggedInPage(User loggedInUser) {
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("/org/example/socialnetworkjavafx/Views/LoggedIn.fxml"));

            Pane root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));

            stage.initModality(Modality.APPLICATION_MODAL);

            LoggedInController loggedInController = loader.getController();
            loggedInController.setUser(loggedInUser, cliController);
            stage.show();

        } catch (IOException e) {
            messageLabel.setText("Error loading logged-in page: " + e.getMessage());
        }
    }

    @FXML
    public void onRegister() {
//        String username = usernameField.getText().trim();
//        String password = passwordField.getText().trim();
//
//        if (username.isEmpty() || password.isEmpty()) {
//            messageLabel.setText("Username and Password cannot be empty.");
//            return;
//        }
//
//        cliController.registerUser(username, password);
//        showAlert(Alert.AlertType.INFORMATION, "Registration Successful",
//                "Account created for " + username + ". You can now log in.");
        try{
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("/org/example/socialnetworkjavafx/Views/Register.fxml"));
            Pane root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            RegisterController registerController = loader.getController();
            registerController.setCliController(cliController);
            stage.show();
        }
        catch(Exception e){
            messageLabel.setText("Error during register: " + e.getMessage());
        }
      }

    @FXML
    private void onViewAllUsers() {
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("/org/example/socialnetworkjavafx/Views/AllUsers.fxml"));
            Pane root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));

            stage.initModality(Modality.APPLICATION_MODAL); // Makes it a pop-up

            AllUsers controller = loader.getController();
            List<User> users = cliController.getAllUsers();
            controller.setUsers(users);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onViewAllFriendships() {
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("/org/example/socialnetworkjavafx/Views/AllFriendships.fxml"));
            Pane root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));

            stage.initModality(Modality.APPLICATION_MODAL);

            AllFriendships controller = loader.getController();
            List<Friendship> friendships = cliController.getAllFriendships();
            controller.setFriendships(friendships);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
