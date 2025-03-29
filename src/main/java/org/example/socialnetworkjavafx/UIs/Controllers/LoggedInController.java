package org.example.socialnetworkjavafx.UIs.Controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.socialnetworkjavafx.Entities.Friendship;
import org.example.socialnetworkjavafx.Entities.User;
import org.example.socialnetworkjavafx.Utils.Event.FriendshipEntityChangeEvent;
import org.example.socialnetworkjavafx.Utils.Observer.Observable;
import org.example.socialnetworkjavafx.Utils.Observer.Observer;
import org.example.socialnetworkjavafx.Utils.Paging.Page;
import org.example.socialnetworkjavafx.Utils.Paging.Pageable;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

public class LoggedInController implements Observer<FriendshipEntityChangeEvent> {
    private CliController cliController;
    private User loggedUser;
    private int currentPage = 0;
    private int pageSize = 4;
    private int numberOfElements = 0;
    private boolean existingRequests;

    @FXML
    private Label welcomeMessage;

    @FXML
    private ListView<String> friendsListView;

    @FXML
    private ListView<String> friendRequestsListView;

    @FXML
    private ListView<String> sentRequestsListView;

    @FXML
    private void initialize() {
    }

    public void setUser(User user, CliController cliController) {
        this.cliController = cliController;
        this.loggedUser = user;

        welcomeMessage.setText("Welcome " + loggedUser.getUsername() + "!");
        cliController.addObserver(this);
        updatenumberOfElements(cliController);
        currentPage=0;
        existingRequests = false;
        updateView();
        if (existingRequests) {
            showAlert(Alert.AlertType.INFORMATION, "Incoming Friend Requests",
                    "You have new friend requests!");
        }
    }

    private void updatenumberOfElements(CliController cliController) {
        numberOfElements=cliController.getAllFriendships().stream().filter(friendship -> !friendship.isPending() &&
                        (friendship.getUser1().equals(loggedUser.getUsername()) ||
                                friendship.getUser2().equals(loggedUser.getUsername())))
                .map(friendship -> friendship.getUser1().equals(loggedUser.getUsername())
                        ? friendship.getUser2() : friendship.getUser1())
                .toList().size();
        updateView();
    }

    private void updateView() {

        Page<Friendship> page = cliController.findAllOnPage(new Pageable(currentPage, pageSize),loggedUser.getUsername());

        List<String> friends = StreamSupport.stream(page.getElementsOnPage().spliterator(), false)
                .filter(friendship -> !friendship.isPending() &&
                        (friendship.getUser1().equals(loggedUser.getUsername()) ||
                                friendship.getUser2().equals(loggedUser.getUsername())))
                .map(friendship -> friendship.getUser1().equals(loggedUser.getUsername())
                        ? friendship.getUser2() : friendship.getUser1())
                .toList();

        friendsListView.setItems(FXCollections.observableArrayList(friends));

        List<String> requests = cliController.getAllFriendships().stream()
                .filter(friendship -> friendship.isPending() &&
                        !friendship.getInitiator().equals(loggedUser.getUsername()) &&
                        (friendship.getUser1().equals(loggedUser.getUsername()) ||
                                friendship.getUser2().equals(loggedUser.getUsername())))
                .map(friendship -> friendship.getUser1().equals(loggedUser.getUsername())
                        ? friendship.getUser2()
                        : friendship.getUser1())
                .toList();
        if (!requests.isEmpty())
            existingRequests=true;
        friendRequestsListView.setItems(FXCollections.observableArrayList(requests));

        List<String> sentRequests = cliController.getAllFriendships().stream()
                .filter(friendship -> friendship.isPending() &&
                        friendship.getInitiator().equals(loggedUser.getUsername()))
                .map(friendship -> friendship.getUser1().equals(loggedUser.getUsername())
                        ? friendship.getUser2()
                        : friendship.getUser1())
                .toList();
        sentRequestsListView.setItems(FXCollections.observableArrayList(sentRequests));
    }

    @FXML
    private void onAddFriend() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Friend");
        dialog.setHeaderText("Add a new friend");
        dialog.setContentText("Enter friend's username:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(friendUsername -> {
            try {
                cliController.addFriend(loggedUser.getUsername(), friendUsername);
                updateView();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    private void onRemoveFriend() {
        String selectedFriend = friendsListView.getSelectionModel().getSelectedItem();
        if (selectedFriend != null) {
            try {
                cliController.removeFriend(loggedUser.getUsername(), selectedFriend);
                updateView();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void onAcceptRequest() {
        String selectedRequest = friendRequestsListView.getSelectionModel().getSelectedItem();
        if (selectedRequest != null) {
            try {
                cliController.acceptFriendship(loggedUser.getUsername(), selectedRequest);
                updateView();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void onDeclineRequest() {
        String selectedRequest = friendRequestsListView.getSelectionModel().getSelectedItem();
        if (selectedRequest != null) {
            try {
                cliController.declineFriendship(loggedUser.getUsername(), selectedRequest);
                updateView();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void onSendMessage() {
        String selectedFriend = friendsListView.getSelectionModel().getSelectedItem();
        if (selectedFriend != null) {
            try {
                FXMLLoader loader = new FXMLLoader(LoggedInController.class.getResource("/org/example/socialnetworkjavafx/Views/Message.fxml"));

                Pane root = loader.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));

                stage.initModality(Modality.APPLICATION_MODAL);

                MessageController messageController = loader.getController();
                messageController.setUsers(loggedUser.getUsername(), selectedFriend, cliController);
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void onLogOut() {
        loggedUser = null;
        cliController.removeObserver(this);
        Stage stage = (Stage) welcomeMessage.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onNextPage(ActionEvent actionEvent) {
        if (currentPage*pageSize<numberOfElements-pageSize) {
        currentPage++;
        updateView();
    }
    }

    @FXML
    private void onPrevPage(ActionEvent actionEvent){
        if (currentPage > 0) {
            currentPage--;
            updateView();
        }
    }

    @Override
    public void update(FriendshipEntityChangeEvent event) {
        updatenumberOfElements(cliController);
        updateView();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleViewProfile(ActionEvent actionEvent) {
        String selectedFriend = friendsListView.getSelectionModel().getSelectedItem();
        if (selectedFriend != null) {
            try {
                FXMLLoader loader = new FXMLLoader(LoggedInController.class.getResource("/org/example/socialnetworkjavafx/Views/Page.fxml"));

                Pane root = loader.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));

                stage.initModality(Modality.APPLICATION_MODAL);

                PageController pageController = loader.getController();

                User user = cliController.getAllUsers().stream().filter(u -> u.getUsername().equals(selectedFriend)).findFirst().get();

                pageController.setUser(user);
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else return;
    }
}