package laptrinhmang.ltm_chat;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import laptrinhmang.ltm_chat.controller.AddGroupController;
import laptrinhmang.ltm_chat.service.Client.Client;

import java.io.IOException;

public class Group extends Application {
    private Client client;

    public Group(Client client) {
        this.client = client;
    }

    @Override
    public void start(Stage stage) throws IOException {
        // Load FXML file
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("screen/Group.fxml"));
        Parent root = loader.load();

        // Lấy controller và truyền Client vào
        AddGroupController controller = loader.getController();
        controller.setClient(this.client);

        // Thiết lập và hiển thị Stage
        stage.setTitle("Tạo Nhóm");
        stage.setScene(new Scene(root));
        stage.show();
    }
}
