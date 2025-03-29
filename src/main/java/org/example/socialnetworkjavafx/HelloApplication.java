package org.example.socialnetworkjavafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.socialnetworkjavafx.Entities.Network;
import org.example.socialnetworkjavafx.Factories.ValidatorFactory;
import org.example.socialnetworkjavafx.Infrastructure.DataBaseConnection;
import org.example.socialnetworkjavafx.Infrastructure.FriendshipDbRepository;
import org.example.socialnetworkjavafx.Infrastructure.MessageDbRepository;
import org.example.socialnetworkjavafx.Infrastructure.UserDbRepository;
import org.example.socialnetworkjavafx.Services.MessageService;
import org.example.socialnetworkjavafx.Services.NetworkService;
import org.example.socialnetworkjavafx.Services.UserService;
import org.example.socialnetworkjavafx.UIs.Controllers.CliController;
import org.example.socialnetworkjavafx.UIs.Controllers.LandingController;
import org.example.socialnetworkjavafx.Validators.FriendshipValidator;
import org.example.socialnetworkjavafx.Validators.UserValidator;
import org.example.socialnetworkjavafx.Validators.ValidStrategy;

import java.io.IOException;
import java.sql.SQLException;

public class HelloApplication extends Application {
    LandingController controller;

    private void initView(Stage primaryStage, CliController cli_controller) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Views/Landing.fxml"));

        AnchorPane userLayout = fxmlLoader.load();
        primaryStage.setScene(new Scene(userLayout));

        LandingController controller = fxmlLoader.getController();
        controller.setController(cli_controller);
    }

    @Override
    public void start(Stage stage) throws IOException{
        Network network = new Network();
        ValidatorFactory vf = ValidatorFactory.getInstance();
        UserValidator userValidator = (UserValidator) vf.createValidator(ValidStrategy.USER);
        FriendshipValidator friendshipValidator = (FriendshipValidator) vf.createValidator(ValidStrategy.FRIENDSHIP);

        DataBaseConnection dbCon;
        try {
            dbCon = new DataBaseConnection("jdbc:postgresql://localhost:5432/social-network", "postgres", "roland");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return;
        }

        UserDbRepository userRepo = new UserDbRepository(dbCon.getConnection());
        FriendshipDbRepository friendshipRepo = new FriendshipDbRepository(dbCon.getConnection());

        UserService userService = new UserService(userRepo);
        NetworkService networkService = new NetworkService(friendshipRepo, userRepo, network);

        MessageDbRepository messageRepo = new MessageDbRepository(dbCon.getConnection());
        MessageService messageService = new MessageService(messageRepo);

        CliController controller = new CliController(userService, networkService, messageService);

        initView(stage, controller);
        stage.setWidth(800);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}