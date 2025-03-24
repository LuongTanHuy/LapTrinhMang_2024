package laptrinhmang.ltm_chat;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import laptrinhmang.ltm_chat.controller.AddMemberController;
import laptrinhmang.ltm_chat.service.Client.Client;

import java.io.IOException;

public class AddMember extends Application {
    private int token;
    private Client client;

    public AddMember(Client client, int token){
        this.client = client;
        this.token = token;
    }
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader_AddGroup = new FXMLLoader(AddMember.class.getResource("screen/AddMember.fxml"));
        Scene sceneChat = new Scene(fxmlLoader_AddGroup.load(), 400, 500);

        //Lay controller
        AddMemberController addMemberController = fxmlLoader_AddGroup.getController();
        addMemberController.setAdd(token, client);

        stage.setScene(sceneChat);
        stage.show();
    }


}