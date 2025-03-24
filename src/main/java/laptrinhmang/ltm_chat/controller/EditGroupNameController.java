package laptrinhmang.ltm_chat.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import laptrinhmang.ltm_chat.service.Client.Client; // Đảm bảo import lớp Client

public class EditGroupNameController {

    @FXML
    private ImageView groupAvatar;

    @FXML
    private TextField newGroupNameField;

    @FXML
    private Button cancel;

    @FXML
    private Button confirmButton;

    private String currentGroupName;
    private int token;
    private Client client;

    public void setClient(Client client) {
        this.client = client;
    }

    @FXML
    public void initialize() {
        // Đặt avatar nhóm mặc định
        Image avatar = new Image(getClass().getResource("/laptrinhmang/ltm_chat/image/many.png").toExternalForm());
        groupAvatar.setImage(avatar);

        cancel.setOnAction(event -> {
            Stage stage = (Stage) cancel.getScene().getWindow();
            stage.close();
        });

        confirmButton.setOnAction(event -> {
            String newGroupName = newGroupNameField.getText().trim();
            if (newGroupName.isEmpty()) {
                System.out.println("Tên nhóm không được để trống.");
                return;
            }

            sendUpdateGroupNameRequest(newGroupName);

            Stage stage = (Stage) confirmButton.getScene().getWindow();
            stage.close();
        });
    }

    // Thiết lập tên nhóm hiện tại
    public void setCurrentGroupName(String groupName) {
        this.currentGroupName = groupName;
        newGroupNameField.setText(currentGroupName);
    }

    // Thiết lập dữ liệu phòng (token và client)
    public void setRoomData(int token, Client client) {
        this.token = token;
        this.client = client;
    }

    private void sendUpdateGroupNameRequest(String newGroupName) {
        if (client != null) {
            try {
                client.updateGroupName(token, newGroupName);
            } catch (Exception e) {
                System.out.println("Error sending update request: " + e.getMessage());
            }
        } else {
            System.out.println("Client is not set.");
        }
    }
}
