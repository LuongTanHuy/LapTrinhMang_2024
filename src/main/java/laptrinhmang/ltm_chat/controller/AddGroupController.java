package laptrinhmang.ltm_chat.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import laptrinhmang.ltm_chat.model.Friend;
import laptrinhmang.ltm_chat.model.Room;
import laptrinhmang.ltm_chat.model.User;
import laptrinhmang.ltm_chat.service.Client.Client;
import laptrinhmang.ltm_chat.service.server.ConnectDB;
import com.google.gson.Gson;

import java.util.*;

public class AddGroupController {

    @FXML
    private TextField groupNameField;

    @FXML
    private TextField searchField;

    @FXML
    private VBox friendListVBox;

    @FXML
    private Button cancelButton;

    @FXML
    private Button createGroupButton;

    private final ConnectDB connectDB = new ConnectDB();
    private Client client;
    private int currentUserId;

    public void setClient(Client client) {
        this.client = client;
        this.currentUserId = client.getCurrentUserID();
    }

    @FXML
    private void initialize() {
        cancelButton.setOnAction(event -> closeWindow());
        createGroupButton.setOnAction(event -> createGroup());

        loadFriendsList();
    }

    private void loadFriendsList() {
        List<User> friendsList = connectDB.getUserList(currentUserId);
        friendListVBox.getChildren().clear();

        for (User friend : friendsList) {
            if (friend.getID() == currentUserId) {
                continue;
            }

            HBox friendItem = new HBox(10);
            CheckBox checkBox = new CheckBox();
            Label friendLabel = new Label(friend.getFullName());
            friendLabel.setId(String.valueOf(friend.getID()));

            friendItem.getChildren().addAll(checkBox, friendLabel);
            friendListVBox.getChildren().add(friendItem);
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
    // token
    private String generateRandomToken(int length) {
        String characters = "0123456789";
        StringBuilder token = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            token.append(characters.charAt(random.nextInt(characters.length())));
        }

        return token.toString();
    }

    private void createGroup() {
        try {
            String groupName = groupNameField.getText().trim();
            if (groupName.isEmpty()) {
                showAlert("Lỗi", "Tên nhóm không được để trống!");
                return;
            }

            Set<Integer> memberIds = new HashSet<>();
            for (Node node : friendListVBox.getChildren()) {
                if (node instanceof HBox) {
                    HBox hBox = (HBox) node;
                    CheckBox checkBox = (CheckBox) hBox.getChildren().get(0);
                    Label label = (Label) hBox.getChildren().get(1);

                    if (checkBox.isSelected()) {
                        memberIds.add(Integer.parseInt(label.getId()));
                    }
                }
            }

            if (memberIds.isEmpty()) {
                showAlert("Lỗi", "Vui lòng chọn ít nhất một thành viên!");
                return;
            }

            String token = generateRandomToken(4);

            Map<String, Object> requestMap = new HashMap<>();

            Map<String, Object> roomMap = new HashMap<>();
            roomMap.put("userId", currentUserId);
            roomMap.put("token", Integer.parseInt(token));
            roomMap.put("name", groupName);

            requestMap.put("room", roomMap);
            requestMap.put("memberIds", new ArrayList<>(memberIds)); // Convert Set to List

            client.sendGroupRequest("CreateGroup", requestMap);

            showAlert("Thành công", "Nhóm đã được tạo thành công!");
            closeWindow();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Lỗi", "Đã xảy ra lỗi khi tạo nhóm!");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
