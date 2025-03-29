package org.example.socialnetworkjavafx.UIs.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import org.example.socialnetworkjavafx.Entities.User;

import java.util.List;

public class AllUsers {
    @FXML
    private TableView<User> usersTable;

    @FXML
    private TableColumn<User, String> usernameColumn;

    @FXML
    private TableColumn<User, String> passwordColumn;

    public void initialize() {
        usernameColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getUsername()));
        passwordColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getPassword()));
    }

    public void setUsers(List<User> users) {
        usersTable.getItems().addAll(users);
    }
}
