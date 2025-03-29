package org.example.socialnetworkjavafx.UIs.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import org.example.socialnetworkjavafx.Entities.Friendship;

import java.util.List;

public class AllFriendships {
    @FXML
    private TableView<Friendship> friendshipsTable;

    @FXML
    private TableColumn<Friendship, String> username1Column;

    @FXML
    private TableColumn<Friendship, String> username2Column;

    @FXML
    private TableColumn<Friendship, String> dateTimeColumn;

    @FXML
    private TableColumn<Friendship, String> pendingColumn;


    public void initialize() {
        username1Column.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getUser1()));
        username2Column.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getUser2()));
        dateTimeColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDateTime().toString()));
        pendingColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().isPending() ? "true" : "false"));
    }

    public void setFriendships(List<Friendship> friendships) {
        friendshipsTable.getItems().addAll(friendships);
    }
}
