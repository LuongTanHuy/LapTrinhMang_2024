package laptrinhmang.ltm_chat;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import laptrinhmang.ltm_chat.controller.DeleteMemberController;
import laptrinhmang.ltm_chat.service.Client.Client;

import java.io.IOException;

public class DeleteMember extends Application {
    private int token;
    private Client client;

    public DeleteMember(int token, Client client){
        this.token = token;
        this.client = client;
    }
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader_Delete = new FXMLLoader(DeleteMember.class.getResource("screen/DeleteMember.fxml"));
        Scene sceneChat = new Scene(fxmlLoader_Delete.load(), 400, 500);

        // Lấy controller
        DeleteMemberController deleteMemberController = fxmlLoader_Delete.getController();
        deleteMemberController.setAdd(token, client);

        stage.setScene(sceneChat);
        stage.show();
    }

}