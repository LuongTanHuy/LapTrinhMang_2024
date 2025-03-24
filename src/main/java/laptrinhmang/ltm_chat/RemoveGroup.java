package laptrinhmang.ltm_chat;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import laptrinhmang.ltm_chat.controller.RemoveGroupController;
import laptrinhmang.ltm_chat.service.Client.Client;

import java.io.IOException;

public class RemoveGroup extends Application {
    private int token;
    private Client client;

    public RemoveGroup(int token, Client client){
        this.token = token;
        this.client = client;
    }
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(RemoveGroup.class.getResource("screen/RemoveGroup.fxml"));
        Scene sceneChat = new Scene(fxmlLoader.load(), 400, 170);

        // Lay controller
        RemoveGroupController removeGroupController = fxmlLoader.getController();
        removeGroupController.setGroupDetails(token, client);
        stage.setTitle("Remove Group");
        stage.setScene(sceneChat);
        stage.show();
    }
}
