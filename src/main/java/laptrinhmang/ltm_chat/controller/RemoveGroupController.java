package laptrinhmang.ltm_chat.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import laptrinhmang.ltm_chat.service.Client.Client;

public class RemoveGroupController {

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnConfirm;

    private int token;
    private Client client;

    public void setClient(Client client) {
        this.client = client;
    }

    public void setGroupDetails(int token, Client client) {
        this.token = token;
        this.client = client;
        System.out.println("Token received: " + token);

    }

    private void handleCancel() {
        System.out.println("Người dùng đã nhấn 'Không'.");
        closeWindow();
    }


    private void closeWindow() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void initialize() {
        btnCancel.setOnAction(event -> handleCancel());

        // sự kiện gửi yêu cầu remove
        btnConfirm.setOnAction(event -> handleConfirm());

    }
    private void handleConfirm() {
        if (client != null) {
            client.sendRemoveGroupRequest(token);
            showAlert("Yêu cầu đã được gửi", "Yêu cầu giải tán nhóm đã được gửi thành công.");
        } else {
            showAlert("Lỗi", "Không thể kết nối đến Client.");
        }

        // Đóng cửa sổ
        closeWindow();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
