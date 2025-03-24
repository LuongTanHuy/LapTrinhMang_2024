package laptrinhmang.ltm_chat.controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import laptrinhmang.ltm_chat.model.User;
import laptrinhmang.ltm_chat.service.Client.Client;
import laptrinhmang.ltm_chat.service.server.ConnectDB;

import java.util.*;

public class DeleteMemberController {

    @FXML
    private VBox friendListGroup;

    @FXML
    private Button cancelButton;

    @FXML
    private Button deleteMember;

    private final ConnectDB connectDB = new ConnectDB();
    private int token;
    private Client client;

    public void setAdd(int token, Client client) {
        this.token = token;
        this.client = client;
        loadFriendsListByToken();
    }

    @FXML
    private void initialize() {
        cancelButton.setOnAction(event -> closeWindow());
        deleteMember.setOnAction(event -> removeSelectedMembers());
    }

    private void loadFriendsListByToken() {
        List<User> members = connectDB.getGroupMembersByToken(token);

        for (User user : members) {
            HBox hBox = new HBox();
            hBox.setSpacing(10);

            CheckBox checkBox = new CheckBox(user.getFullName());
            // Lưu userId vào thuộc tính id của CheckBox
            checkBox.setId(String.valueOf(user.getID()));
            hBox.getChildren().add(checkBox);
            friendListGroup.getChildren().add(hBox);
        }
    }

    private void removeSelectedMembers() {
        try {
            Set<Integer> selectedUserIds = new HashSet<>();
            for (Node node : friendListGroup.getChildren()) {
                if (node instanceof HBox hBox) {
                    CheckBox checkBox = (CheckBox) hBox.getChildren().get(0); // Chỉ có CheckBox
                    if (checkBox.isSelected()) {
                        // Lấy userId từ thuộc tính id của CheckBox
                        selectedUserIds.add(Integer.parseInt(checkBox.getId()));
                    }
                }
            }

            // Kiểm tra nếu không có thành viên nào được chọn
            if (selectedUserIds.isEmpty()) {
                showAlert("Lỗi", "Vui lòng chọn ít nhất một thành viên để xóa!");
                return;
            }

            Map<String, Object> requestDelete = new HashMap<>();
            requestDelete.put("token", token);
            requestDelete.put("memberIds", new ArrayList<>(selectedUserIds));

            client.deleteGroupRequest("RemoveMemberGroup", requestDelete);

            showAlert("Thành công", "Các thành viên đã được xóa khỏi nhóm thành công!");
            closeWindow();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Lỗi", "Đã xảy ra lỗi khi xóa thành viên khỏi nhóm!");
        }
    }


    private void closeWindow() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }


}
