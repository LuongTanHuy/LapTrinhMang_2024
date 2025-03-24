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

public class AddMemberController {

    @FXML
    private VBox friendListVBox;

    @FXML
    private Button cancelButton;

    @FXML
    private Button addMember;

    private final ConnectDB connectDB = new ConnectDB();
    private int token;
    private Client client;

    public void setAdd(int token, Client client) {
        this.token = token;
        this.client = client;
        loadFriendsList();
    }

    @FXML
    private void initialize() {
        cancelButton.setOnAction(event -> closeWindow());
        addMember.setOnAction(event -> addMemberGroup()); // Gắn sự kiện cho nút Add Member
    }

    private void loadFriendsList() {
        // Lấy danh sách người dùng chưa có trong nhóm
        List<User> usersNotInRoom = connectDB.getUsersNotInRoomByToken(token);
        friendListVBox.getChildren().clear();

        if (usersNotInRoom.isEmpty()) {
            Label noUsersLabel = new Label("Không có người dùng nào để thêm vào nhóm.");
            friendListVBox.getChildren().add(noUsersLabel);
            return;
        }

        for (User user : usersNotInRoom) {
            HBox friendItem = new HBox(10);
            CheckBox checkBox = new CheckBox();
            Label friendLabel = new Label(user.getFullName());
            friendLabel.setId(String.valueOf(user.getID()));

            friendItem.getChildren().addAll(checkBox, friendLabel);
            friendListVBox.getChildren().add(friendItem);
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    private void addMemberGroup() {
        try {
            // Tập hợp các userID được chọn từ danh sách
            Set<Integer> memberIds = new HashSet<>();
            for (Node node : friendListVBox.getChildren()) {
                if (node instanceof HBox hBox) {
                    CheckBox checkBox = (CheckBox) hBox.getChildren().get(0);
                    Label label = (Label) hBox.getChildren().get(1);

                    if (checkBox.isSelected()) {
                        memberIds.add(Integer.parseInt(label.getId())); // Thêm userID vào danh sách
                    }
                }
            }

            if (memberIds.isEmpty()) {
                showAlert("Lỗi", "Vui lòng chọn ít nhất một thành viên!");
                return;
            }

            Map<String, Object> requestMap = new HashMap<>();
            requestMap.put("token", token);
            requestMap.put("memberIds", new ArrayList<>(memberIds));

            client.addGroupRequest("AddMemberGroup", requestMap);

            showAlert("Thành công", "Các thành viên đã được thêm vào nhóm thành công!");
            closeWindow();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Lỗi", "Đã xảy ra lỗi khi thêm thành viên vào nhóm!");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }


}
