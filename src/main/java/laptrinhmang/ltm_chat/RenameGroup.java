package laptrinhmang.ltm_chat;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import laptrinhmang.ltm_chat.controller.EditGroupNameController;
import laptrinhmang.ltm_chat.service.Client.Client;

import java.io.IOException;

public class RenameGroup extends Application {
    private String currentGroupName;
    private int token;
    private Client client;

    public RenameGroup(String currentGroupName, int token, Client client) {
        this.currentGroupName = currentGroupName;
        this.token = token;
        this.client = client;
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(RenameGroup.class.getResource("screen/RenameGroup.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 300);

        // Lấy controller và thiết lập dữ liệu
        EditGroupNameController controller = fxmlLoader.getController();
        controller.setCurrentGroupName(currentGroupName);
        controller.setRoomData(token, client);

        // Thiết lập giao diện
        stage.setTitle("Đổi Tên Nhóm");
        stage.setScene(scene);
        stage.show();
    }
}
