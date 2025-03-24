package laptrinhmang.ltm_chat.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;

import laptrinhmang.ltm_chat.*;
import laptrinhmang.ltm_chat.ModelChat.Friend_ListMessage;
import laptrinhmang.ltm_chat.ModelChat.Group_ListMessage;
import laptrinhmang.ltm_chat.model.Friend;
import laptrinhmang.ltm_chat.model.Room;
import laptrinhmang.ltm_chat.ModelChat.UserChat;
import laptrinhmang.ltm_chat.service.Client.Client;
import laptrinhmang.ltm_chat.model.User;

import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClientController {
    @FXML
    private PasswordField TF_Password_SN2, TF_Password_SN1, TF_Password_LG;
    @FXML
    private TextField TF_Phone_SN, TF_FullName_SN1, TF_Phone_LG, TF_Message, TF_Search;
    @FXML
    private VBox VB_Chat, VB_Menu;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private ImageView iconAvatar, IM_Hello;
    @FXML
    private Button BT_SendChat, BT_AddFriend, BT_AddGroup, BT_LogOut, BT_File, BT_CallVideo, BT_AddMember,BT_DeleteMember, BT_Emoji, BT_Edit, BT_Trash,BT_Audio;
    @FXML
    private Pane P_Chat, P_Login, P_SignUp, P_Hello, P_FrameChat;
    @FXML
    public Label LB_Chat, LB_Hello, LB_FullName, LB_Friend, LB_Group;
    @FXML
    private ListView<String> LV_SearchResults;
    @FXML
    private VBox mainContent;
    @FXML
    private AnchorPane emojiPane;
    @FXML
    private GridPane emojiGrid;
    private String typeChat = "";
    private List<Friend_ListMessage> listMessageFriend = new ArrayList<>();
    private List<Group_ListMessage> listMessageGroup = new ArrayList<>();
    private TargetDataLine targetLine;
    private boolean isRecording = false; // Trạng thái đang ghi âm
    private String filePathToSend; // Đường dẫn file sẽ được gửi

    public User receiver = new User();
    private User user;
    public Room room = new Room();
    private String IPAddress = "localhost";//192.168.1.47
    private Client client = new Client(IPAddress, this);

    @FXML
    public void initialize() {
        //Chat
        LB_FullName.setOnMouseClicked(mouseEvent -> {
            P_Hello.setVisible(true);
            P_FrameChat.setVisible(false);
        });

        Image avatarHello = new Image(getClass().getResource("/laptrinhmang/ltm_chat/image/hello.png").toExternalForm());
        IM_Hello.setImage(avatarHello);

        // Show Or Hidden View
        P_SignUp.setVisible(false);
        P_Chat.setVisible(false);
        P_Login.setVisible(true);
//        LV_SearchResults.setVisible(true);

        // Button Call Video
        FontIcon icon6 = new FontIcon("fas-video");
        icon6.setIconSize(15);
        icon6.setIconColor(Color.GREEN);
        BT_CallVideo.setGraphic(icon6);

        // Button LogOut
        FontIcon icon4 = new FontIcon("fas-sign-out-alt");
        icon4.setIconSize(15);
        icon4.setIconColor(Color.WHITE);
        BT_LogOut.setGraphic(icon4);

        // Button File
        FontIcon icon5 = new FontIcon("fas-file-export");
        icon5.setIconSize(17);
        icon5.setIconColor(Color.GREEN);
        BT_File.setGraphic(icon5);

        // Button Send for One-to-One
        FontIcon iconOneToOne = new FontIcon("fas-paper-plane");
        iconOneToOne.setIconSize(17);
        iconOneToOne.setIconColor(Color.GREEN);
        BT_SendChat.setGraphic(iconOneToOne);

        // Button icon Audio Voice
        FontIcon iconAudio = new FontIcon("fas-microphone");
        iconAudio.setIconSize(18);
        iconAudio.setIconColor(Color.GREEN);
        BT_Audio.setGraphic(iconAudio);

        // Button icon Icon
        FontIcon icon = new FontIcon("fas-smile");
        icon.setIconSize(19);
        icon.setIconColor(Color.GREEN);
        BT_Emoji.setGraphic(icon);

        // Button AddFriend
//        FontIcon icon2 = new FontIcon("fas-user-plus");
//        icon2.setIconSize(15);
//        icon2.setIconColor(Color.WHITE);
//        BT_AddFriend.setGraphic(icon2);
//        BT_AddFriend.setStyle("-fx-background-color: #4294e3;");

        // Button AddGroup
        FontIcon icon3 = new FontIcon("fas-users");
        icon3.setIconSize(15);
        icon3.setIconColor(Color.WHITE);
        BT_AddGroup.setGraphic(icon3);
        BT_AddGroup.setStyle("-fx-background-color:  #4294e3;");
        BT_AddGroup.setOnAction(event -> startGroupWindow());

        // Remove Group
        FontIcon trashIcon = new FontIcon("fas-trash");
        trashIcon.setIconSize(15);
        trashIcon.setIconColor(Color.GRAY);
        BT_Trash.setGraphic(trashIcon);

        // Add Member
        FontIcon addIcon = new FontIcon("fas-user-plus");
        addIcon.setIconSize(15);
        addIcon.setIconColor(Color.RED);
        BT_AddMember.setGraphic(addIcon);
        BT_AddMember.setOnAction(event -> startAddMemberWindow());

        //Delete Member
        FontIcon deleteIcon = new FontIcon("fas-users");
        deleteIcon.setIconSize(17);
        deleteIcon.setIconColor(Color.RED);
        BT_DeleteMember.setGraphic(deleteIcon);
        BT_DeleteMember.setOnAction(event -> startDeleteMemberWindow());

        // Edit Name Group
        FontIcon editIcon = new FontIcon("fas-edit");
        editIcon.setIconSize(15);
        editIcon.setIconColor(Color.GRAY);
        BT_Edit.setGraphic(editIcon);

        FontIcon iconEmoji = new FontIcon("fas-smile");
        iconEmoji.setIconSize(15);
        iconEmoji.setIconColor(Color.GREEN);
        BT_Emoji.setGraphic(iconEmoji);
        // Xử lý khi nhấn vào ô tìm kiếm
        TF_Search.setOnMouseClicked(event -> {
            LV_SearchResults.setVisible(true);
            LV_SearchResults.setManaged(true);
            mainContent.setVisible(false);
            mainContent.setManaged(false);
        });

        // Xử lý khi click ra ngoài
        Platform.runLater(() -> {
            mainContent.getScene().addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
                // Nếu không click vào TextField hay ListView, quay lại trạng thái mặc định
                if (!TF_Search.isFocused() && !LV_SearchResults.isFocused()) {
                    LV_SearchResults.setVisible(false);
                    LV_SearchResults.setManaged(false);
                    mainContent.setVisible(true);
                    mainContent.setManaged(true);
                }
            });
        });
        TF_Search.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleSearch();
            }
        });
        // Xử lý khi nhấn vào một mục trong ListView
        LV_SearchResults.setOnMouseClicked(event -> {
            String selectedItem = LV_SearchResults.getSelectionModel().getSelectedItem();

            if (selectedItem != null) {
                // Đóng danh sách tìm kiếm và mở giao diện chat
                LV_SearchResults.setVisible(false);
                LV_SearchResults.setManaged(false);
                TF_Search.clear();
                mainContent.setVisible(true);
                mainContent.setManaged(true);

                if (selectedItem.startsWith("User: ")) {
                    // Xử lý user
                    String[] parts = selectedItem.substring(6).split(" - ");
                    if (parts.length == 2) {
                        String name = parts[0].trim();
                        String phone = parts[1].trim();
                        cardSearchUser(name, phone);
                    }
                } else if (selectedItem.startsWith("Group: ")) {
                    // Xử lý group
                    String groupName = selectedItem.substring(7).trim();
                    cardSearchGroup(groupName);
                }
            }
        });


    }

    private void handleSearch() {
        if (client != null) {
            String keyword = TF_Search.getText().trim();
            if (!keyword.isEmpty()) {
                client.sendSearchRequest(keyword); // Gửi yêu cầu tìm kiếm đến server
            }
        }
    }

    public void updateSearchResults(List<String> searchResults ) {
        Platform.runLater(() -> {
            LV_SearchResults.getItems().clear();
            LV_SearchResults.getItems().addAll(searchResults );
        });
    }

    @FXML
    public void startRenameGroupWindow() {
        Platform.runLater(() -> {
            try {
                String currentGroupName = LB_Chat.getText();
                int token = room.getToken();

                RenameGroup renameGroup = new RenameGroup(currentGroupName, token, client);
                Stage stage = new Stage();
                renameGroup.start(stage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    public void startRemoveGroup() {
        Platform.runLater(() -> {
            try {
                int token = room.getToken();
                RemoveGroup removeGroup = new RemoveGroup(token, client);
                Stage stage = new Stage();
                removeGroup.start(stage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
    @FXML
    public void startAddMemberWindow() {
        Platform.runLater(() -> {
            try {
                int token = room.getToken();
                AddMember addMember = new AddMember(client, token);
                Stage stage = new Stage();
                addMember.start(stage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @FXML
    public void startDeleteMemberWindow() {
        Platform.runLater(() -> {
            try {
                int token = room.getToken();
                DeleteMember deleteMember = new DeleteMember(token, client);
                Stage stage = new Stage();
                deleteMember.start(stage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @FXML
    public void startGroupWindow() {
        Platform.runLater(() -> {
            try {
                Stage stage = new Stage();
                new Group(client).start(stage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private String getTime() {
        LocalDateTime now = LocalDateTime.now();

        // Định dạng thời gian theo mong muốn
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Chuyển thời gian thành chuỗi
        return now.format(formatter);
    }

    @FXML
    public void startCallVideo() {
        Platform.runLater(() -> {
            try {
                Stage stage = new Stage();
                new CallVideo().start(stage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @FXML
    public void receiveCallVideo() {
        Platform.runLater(() -> {
            try {
                Stage stage = new Stage();
                new ReceiveCallVideo().start(stage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @FXML
    public void handleLogOut() {
        P_Login.setVisible(true);
        P_SignUp.setVisible(false);
        P_Chat.setVisible(false);
        user = null;
//        room = null;

        this.client.clearListFriend();
        this.client.clearListRoom();
        this.client.clearMessage();
        this.VB_Menu.getChildren().clear();
    }

    @FXML
    public void handleSignUp() {
        List<String> message = new ArrayList<>();
        message.add(0, "SignUp");
        message.add(1, this.TF_FullName_SN1.getText());
        message.add(2, this.TF_Phone_SN.getText());
        message.add(3, this.TF_Password_SN1.getText());
        this.client.sendRequest(message);

        this.TF_FullName_SN1.setText("");
        this.TF_Phone_SN.setText("");
        TF_Password_SN1.setText("");
        TF_Password_SN2.setText("");

    }

    @FXML
    public void handleLogin() {
        List<String> message = new ArrayList<>();
        message.add(0, "Login");
        message.add(1, this.TF_Phone_LG.getText());
        message.add(2, this.TF_Password_LG.getText());
        if (this.TF_Phone_LG.getText().equals("") || this.TF_Password_LG.getText().equals("")) {
            this.TF_Phone_LG.setText("");
            this.TF_Password_LG.setText("");
        } else {
            this.client.sendRequest(message);
        }

        this.TF_Phone_LG.setText("");
        this.TF_Password_LG.setText("");
    }

    @FXML
    public void showView(ActionEvent event) {
        Button clickedButton = (Button) event.getSource(); // Lấy Button vừa nhấn
        String view = clickedButton.getText();  // Lấy văn bản từ Button

        switch (view) {
            case "Bạn đã có Tài Khoản ? Đăng Nhập":
                P_Login.setVisible(true);
                P_SignUp.setVisible(false);
                P_Chat.setVisible(false);
                break;
            case "Bạn chưa có Tài Khoản ? Đăng Ký":
                P_Login.setVisible(false);
                P_SignUp.setVisible(true);
                P_Chat.setVisible(false);
                break;
        }
    }

    @FXML
    private void handleChooseEmoji() {
        if (!emojiPane.isVisible()) {
            emojiGrid.getChildren().clear();
            List<String> emojiList = Arrays.asList(
                    "😊", "😂", "😍", "😘", "😎",
                    "👍", "🎉", "💖", "😻", "😼",
                    "😆", "🥳", "😢", "😡", "🤔",
                    "🙄", "😇", "😈", "🤗", "😜"
            );

            int column = 0;
            int row = 0;
            for (String emoji : emojiList) {
                Button emojiButton = new Button(emoji);
                emojiButton.setStyle("-fx-font-size: 21; -fx-text-fill: #000000; -fx-background-color: #e6e5e5; -fx-padding: 8; -fx-border-color: #d3d3d3; -fx-border-radius: 5;");
                emojiButton.setOnAction(event -> {
                    TF_Message.appendText(emoji);
                });
                emojiGrid.add(emojiButton, column, row);
                column++;
                if (column == 5) {
                    column = 0;
                    row++;
                }
            }
            emojiPane.setVisible(true); // Show the emoji pane

            // Hide the emoji pane when clicking outside it
            emojiPane.getScene().addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                if (!emojiPane.isHover()) {
                    emojiPane.setVisible(false); // Hide emoji pane if click is outside
                }
            });

        } else {
            emojiPane.setVisible(false); // Toggle visibility if already visible
        }
    }

    @FXML
    public void handleSendMessageChat() {
        switch (typeChat) {
            case "OneToMany":
                sendMessageChatOneToMany();
                break;
            case "OneToOne":
                sendMessageChatOneToOne();
                break;
        }
    }

    public void sendMessageChatOneToOne() {
        String message = TF_Message.getText().trim();
        if (!message.isEmpty()) {
            // Tạo tin nhắn mới và hiển thị trên giao diện
            Friend_ListMessage newMessage = new Friend_ListMessage(message, getTime(), "text", "", receiver.getID(), user.getID());
            addChatOneToOne("Right", newMessage);
            listMessageFriend.add(newMessage);

            // Gửi yêu cầu tin nhắn lên server
            List<String> listMessage = new ArrayList<>();
            listMessage.add("ChatOneToOne"); // requestType
            listMessage.add("text"); // typeMessage
            listMessage.add(message); // message
            listMessage.add(String.valueOf(user.getID())); // userID_Send
            listMessage.add(String.valueOf(receiver.getID())); // userID_Receive
            client.sendRequest(listMessage);

            // Cập nhật giao diện và làm trống trường nhập
            scrollPane.layout();
            scrollPane.setVvalue(1.0);
            TF_Message.clear();
        }
    }

    public void sendMessageChatOneToMany() {
        String message = TF_Message.getText().trim();
        if (!message.isEmpty()) {
            // Tạo tin nhắn mới và hiển thị trên giao diện
            Group_ListMessage newMessage = new Group_ListMessage(message, getTime(), "text", "", user.getFullName(), room.getToken(), room.getName(), user.getID());
            addChatOneToMany("Right", newMessage);
            listMessageGroup.add(newMessage);

            // Gửi yêu cầu tin nhắn lên server
            List<String> listMessage = new ArrayList<>();
            listMessage.add("ChatOneToMany"); // requestType
            listMessage.add("text"); // typeMessage
            listMessage.add(message); // message
            listMessage.add(String.valueOf(user.getID())); // userID_Send
            listMessage.add(String.valueOf(room.getToken())); // token Room
            listMessage.add(room.getName());
            listMessage.add(user.getFullName());
            client.sendRequest(listMessage);

            // Cập nhật giao diện và làm trống trường nhập
            scrollPane.layout();
            scrollPane.setVvalue(1.0);
            TF_Message.clear();
        }
    }

    @FXML
    public void handleFileChoose(ActionEvent event) {
        // Lấy Stage từ Node (ví dụ Button)
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Mở hộp thoại chọn file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chọn tệp");

        // Tùy chọn bộ lọc file
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All Files", "*.*"));

        // Hiển thị hộp thoại chọn file
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            // Gửi file lên server sau khi chọn
            client.sendFileToServer(file, typeChat);
        }
    }

    public void sendFileChatOneToMany(String fileName, String path) {
        if (!fileName.isEmpty()) {
            this.addMessageGroup(new Group_ListMessage(fileName, this.getTime(), "file", path,this.user.getFullName(),this.room.getToken(),this.room.getName(),this.getUser().getID()));
            this.addChatOneToMany("Right", new Group_ListMessage(fileName, this.getTime(), "file", path,this.user.getFullName(),this.room.getToken(),this.room.getName(),this.getUser().getID()));

            List<String> listMessage = new ArrayList<>();
            listMessage.add("FileOneToMany");// requestType
            listMessage.add("file");//typeMessage
            listMessage.add(fileName);//fileName
            listMessage.add(path);//path
            listMessage.add(String.valueOf(user.getID()));//userID_Send
            listMessage.add(String.valueOf(room.getToken()));//token Room
            listMessage.add(room.getName());
            listMessage.add(user.getFullName());

            client.sendRequest(listMessage);

            scrollPane.layout();
            scrollPane.setVvalue(1.0);
            TF_Message.clear();
        }
    }

    public void sendFileChatOneToOne(String fileName, String path) {
        if (!fileName.isEmpty()) {
            this.addMessageFriend( new Friend_ListMessage(fileName,this.getTime(),"file",path, receiver.getID(), user.getID()));
            this.addChatOneToOne("Right", new Friend_ListMessage(fileName,this.getTime(),"file",path, receiver.getID(), user.getID()));

            List<String> listMessage = new ArrayList<>();
            listMessage.add("FileOneToOne");// requestType
            listMessage.add("file");//typeMessage
            listMessage.add(fileName);//FileName
            listMessage.add(path);//Path
            listMessage.add(String.valueOf(user.getID()));//userID_Send
            listMessage.add(String.valueOf(receiver.getID()));//userID_Receive

            client.sendRequest(listMessage);

            scrollPane.layout();
            scrollPane.setVvalue(1.0);
            TF_Message.clear();
        }
    }

    public void sendAudioFile(String audioFileName, String audioFilePath) {
        if (!audioFileName.isEmpty() && !audioFilePath.isEmpty()) {
            // Thêm thông điệp vào danh sách chat và giao diện
            this.addMessageFriend(new Friend_ListMessage(audioFileName, this.getTime(), "audio", audioFilePath, receiver.getID(), user.getID()));
            this.addChatOneToOne("Right", new Friend_ListMessage(audioFileName, this.getTime(), "audio", audioFilePath, receiver.getID(), user.getID()));

            // Chuẩn bị danh sách thông điệp để gửi tới server
            List<String> listMessage = new ArrayList<>();
            listMessage.add("FileOneToOne"); // Loại yêu cầu
            listMessage.add("audio");       // Kiểu thông điệp
            listMessage.add(audioFileName); // Tên file âm thanh
            listMessage.add(audioFilePath); // Đường dẫn file âm thanh
            listMessage.add(String.valueOf(user.getID())); // ID người gửi
            listMessage.add(String.valueOf(receiver.getID())); // ID người nhận

            // Gửi yêu cầu tới server
            client.sendRequest(listMessage);

            // Cập nhật giao diện người dùng
            scrollPane.layout();
            scrollPane.setVvalue(1.0);
            TF_Message.clear();
        } else {
            System.out.println("File âm thanh không hợp lệ hoặc đường dẫn trống.");
        }
    }

    public void sendAudioFileChatOneToMany(String audioFileName, String audioFilePath) {
        if (!audioFileName.isEmpty() && !audioFilePath.isEmpty()) {
            this.addMessageGroup(new Group_ListMessage(audioFileName, this.getTime(), "audio", audioFilePath,
                    this.user.getFullName(), this.room.getToken(), this.room.getName(), this.user.getID()));
            this.addChatOneToMany("Right", new Group_ListMessage(audioFileName, this.getTime(), "audio", audioFilePath,
                    this.user.getFullName(), this.room.getToken(), this.room.getName(), this.user.getID()));

            List<String> listMessage = new ArrayList<>();
            listMessage.add("FileOneToMany"); // Loại yêu cầu
            listMessage.add("audio");        // Kiểu thông điệp
            listMessage.add(audioFileName);  // Tên file âm thanh
            listMessage.add(audioFilePath);  // Đường dẫn file âm thanh
            listMessage.add(String.valueOf(user.getID())); // ID người gửi
            listMessage.add(String.valueOf(room.getToken())); // Token của phòng
            listMessage.add(room.getName()); // Tên phòng
            listMessage.add(user.getFullName()); // Tên đầy đủ của người gửi

            // Gửi yêu cầu tới server
            client.sendRequest(listMessage);

            // Cập nhật giao diện người dùng
            scrollPane.layout();
            scrollPane.setVvalue(1.0);
            TF_Message.clear();
        } else {
            System.out.println("File âm thanh không hợp lệ hoặc đường dẫn trống.");
        }
    }

    @FXML
    private void handleFileAudio(ActionEvent event) {
        // Tạo popup thu âm
        Stage audioPopup = new Stage();
        audioPopup.initModality(Modality.APPLICATION_MODAL);
        audioPopup.setTitle("Thu âm");

        // Giao diện
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.setAlignment(Pos.CENTER);

        Label instructionLabel = new Label("Nhấn nút dưới đây để bắt đầu thu âm:");
        Button recordButton = new Button("Bắt đầu thu âm");
        Button stopButton = new Button("Dừng thu âm");
        Button sendOneToOneButton = new Button("Gửi cho cá nhân");
        Button sendOneToManyButton = new Button("Gửi cho nhóm");
        stopButton.setDisable(true);
        sendOneToOneButton.setDisable(true);
        sendOneToManyButton.setDisable(true);

        Label statusLabel = new Label("Trạng thái: Đang chờ...");
        layout.getChildren().addAll(instructionLabel, recordButton, stopButton, sendOneToOneButton, sendOneToManyButton, statusLabel);

        // Scene cho popup
        Scene popupScene = new Scene(layout, 300, 300);
        audioPopup.setScene(popupScene);

        // Tạo đường dẫn file duy nhất
        String directoryPath = "D:\\DACS4\\LTM_CHAT\\src\\main\\resources\\laptrinhmang\\ltm_chat\\audio";
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs(); // Tạo thư mục nếu chưa tồn tại
        }

        String uniqueFileName = "recorded_audio_" + System.currentTimeMillis() + ".wav";
        String audioFilePath = directoryPath + "\\" + uniqueFileName;

        // Sự kiện thu âm
        recordButton.setOnAction(e -> {
            statusLabel.setText("Trạng thái: Đang thu...");
            recordButton.setDisable(true);
            stopButton.setDisable(false);

            // Bắt đầu thu âm
            startRecording(audioFilePath, statusLabel);
        });

        // Sự kiện dừng thu âm
        stopButton.setOnAction(e -> {
            statusLabel.setText("Trạng thái: Đã dừng thu âm.");
            recordButton.setDisable(false);
            stopButton.setDisable(true);
            sendOneToOneButton.setDisable(false);
            sendOneToManyButton.setDisable(false);

            // Dừng thu âm
            stopRecording(statusLabel);
        });

        // Sự kiện gửi file cho cá nhân
        sendOneToOneButton.setOnAction(e -> {
            sendAudioFile(uniqueFileName, audioFilePath);
            statusLabel.setText("Trạng thái: File đã được gửi cho cá nhân.");
            sendOneToOneButton.setDisable(true);
            sendOneToManyButton.setDisable(true);
        });

        // Sự kiện gửi file cho nhóm
        sendOneToManyButton.setOnAction(e -> {
            sendAudioFileChatOneToMany(uniqueFileName, audioFilePath);
            statusLabel.setText("Trạng thái: File đã được gửi cho nhóm.");
            sendOneToOneButton.setDisable(true);
            sendOneToManyButton.setDisable(true);
        });

        // Hiển thị popup
        audioPopup.showAndWait();
    }

    private void startRecording(String filePath, Label statusLabel) {
        try {
            File directory = new File(filePath).getParentFile();

            // Tạo thư mục nếu chưa tồn tại
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Thiết lập ghi âm
            AudioFormat format = new AudioFormat(44100.0f, 16, 1, true, true);
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
            if (!AudioSystem.isLineSupported(info)) {
                Platform.runLater(() -> statusLabel.setText("Lỗi: Không hỗ trợ thiết bị ghi âm!"));
                return;
            }

            // Mở luồng ghi âm
            targetLine = (TargetDataLine) AudioSystem.getLine(info);
            targetLine.open(format);
            targetLine.start();

            // Tạo tên file duy nhất bằng timestamp
            String timestamp = String.valueOf(System.currentTimeMillis());
            String uniqueFilePath = filePath.replace("recorded_audio.wav", "audio_" + timestamp + ".wav");

            // Ghi âm vào file
            AudioInputStream audioStream = new AudioInputStream(targetLine);
            Thread recordingThread = new Thread(() -> {
                try {
                    AudioSystem.write(audioStream, AudioFileFormat.Type.WAVE, new File(uniqueFilePath));
                    Platform.runLater(() -> statusLabel.setText("Tệp đã lưu tại: " + uniqueFilePath));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });

            recordingThread.start();
            isRecording = true; // Cập nhật trạng thái

            // Cập nhật đường dẫn file sau khi ghi xong
            filePathToSend = uniqueFilePath;
        } catch (LineUnavailableException ex) {
            ex.printStackTrace();
            Platform.runLater(() -> statusLabel.setText("Lỗi: Không thể khởi động ghi âm!"));
        }
    }

    private void stopRecording(Label statusLabel) {
        if (isRecording && targetLine != null) {
            targetLine.stop();
            targetLine.close();
            isRecording = false;
            Platform.runLater(() -> statusLabel.setText("Tệp âm thanh đã được lưu."));
        }
    }

    public void menu(List<User> listFriend, List<Room> listRoom) {
        this.LB_Friend.setText("Bạn Bè: %d".formatted(listFriend.size()));
        this.LB_Group.setText("Nhóm: %d".formatted(listRoom.size()));

        for (int i = 0; i < listRoom.size(); i++) {
            this.cardGroup(listRoom.get(i).getName(), listRoom.get(i).getMemberGroupCount(), listRoom.get(i).getToken());
        }
        for (int i = 0; i < listFriend.size(); i++) {
            this.cardFriend(listFriend.get(i).getID(), listFriend.get(i).getFullName());
        }

    }

    private void refreshChatBox(int receiverID, String typeChat, int token) {
        VB_Chat.getChildren().clear();

        if ("OneToOne".equals(typeChat)) {
            boolean isMessageLoaded = false;

            // Kiểm tra nếu có tin nhắn cho người nhận hiện tại
            for (Friend_ListMessage friendMessage : listMessageFriend) {
                if (friendMessage.getReceiverID() == receiverID) {
                    isMessageLoaded = true;
                    break;
                }
            }

            // Nếu chưa có tin nhắn nào, gửi yêu cầu để lấy tin nhắn
            if (!isMessageLoaded) {
                List<String> listMessage = new ArrayList<>();
                listMessage.add("ListChatOneToOne");
                listMessage.add(String.valueOf(user.getID())); // sender
                listMessage.add(String.valueOf(receiverID));
                client.sendRequest(listMessage);
            }

            // Thêm tin nhắn vào giao diện
            for (Friend_ListMessage friendMessage : listMessageFriend) {
                if (friendMessage.getReceiverID() == receiverID) {
                    if (friendMessage.getUserID() == user.getID()) {
                        addChatOneToOne("Right", friendMessage);
                    } else {
                        addChatOneToOne("Left", friendMessage);
                    }
                }
            }
        } else if ("OneToMany".equals(typeChat)) {
            boolean isGroupMessageLoaded = false;

            // Kiểm tra nếu đã có tin nhắn cho nhóm hiện tại
            for (Group_ListMessage groupMessage : listMessageGroup) {
                if (groupMessage.getToken() == receiverID) {
                    isGroupMessageLoaded = true;
                    break;
                }
            }

            // Nếu chưa có tin nhắn nào, gửi yêu cầu để lấy tin nhắn
            if (!isGroupMessageLoaded) {
                List<String> listMessage = new ArrayList<>();
                listMessage.add("ListChatOneToMany");
                listMessage.add(String.valueOf(receiverID)); // token group
                client.sendRequest(listMessage);
            }

            // Thêm tin nhắn vào giao diện
            for (Group_ListMessage groupMessage : listMessageGroup) {
                if (groupMessage.getToken() == token) {
                    if (groupMessage.getUserID() == user.getID()) {
                        addChatOneToMany("Right", groupMessage);
                    } else {
                        addChatOneToMany("Left", groupMessage);
                    }
                }
            }
        }
    }

    public void showFrameHello() {
        P_Login.setVisible(false);
        P_SignUp.setVisible(false);
        P_Chat.setVisible(true);
        P_Hello.setVisible(true);
        P_FrameChat.setVisible(false);

        LB_Hello.setText("Xin Chào " + user.getFullName() + " !");
    }

    public void cardFriend(int receiverID, String name) {
        // Create the first label for the user's name
        Label label1 = new Label(name);
        label1.setPrefSize(185, 58);
        label1.setTextFill(Color.web("#8e7c7c"));
        label1.setFont(new Font(14));

        // Set margin for the label in HBox
        HBox.setMargin(label1, new Insets(0, 0, 0, 10));

        // Create the second label for "Bạn Bè"
        Label label2 = new Label("Bạn Bè");
        label2.setPrefSize(90, 58);
        label2.setTextFill(Color.web("#4294e3"));
        label2.setFont(Font.font("System", javafx.scene.text.FontWeight.BOLD, javafx.scene.text.FontPosture.ITALIC, 13));
        label2.setAlignment(Pos.BOTTOM_CENTER);

        // Create HBox and add components
        HBox hbox = new HBox();
        hbox.setLayoutX(10);
        hbox.setLayoutY(10);
        hbox.setPrefSize(288, 40);
        hbox.setStyle("-fx-border-color: #4294e3;");
        hbox.getChildren().addAll(label1, label2);

        // Set mouse click event
        hbox.setOnMouseClicked(mouseEvent -> {
            typeChat = "OneToOne"; // Set chat type
            Image avatar = new Image(getClass().getResource("/laptrinhmang/ltm_chat/image/one.png").toExternalForm());
            Circle clip = new Circle(22, 22, 22);
            iconAvatar.setImage(avatar);
            iconAvatar.setClip(clip);

            receiver.setID(receiverID); // Set receiver ID
            room.setToken(0); // Reset token

            refreshChatBox(receiverID, typeChat, 0); // Refresh chat box with the receiver ID
            P_Hello.setVisible(false);
            P_FrameChat.setVisible(true);

            LB_Chat.setText(name); // Set chat header to the user's name
        });

        VB_Menu.getChildren().add(hbox); // Add the HBox to the menu
    }

    public void cardGroup(String name, String memberGroupCount, int token) {
        // Tạo label cho tên nhóm
        Label label1 = new Label(name);
        label1.setPrefSize(196, 60);
        label1.setTextFill(Color.web("#8e7c7c"));
        label1.setFont(new Font(14));
        HBox.setMargin(label1, new Insets(0, 0, 0, 10));

        // Tạo label cho số lượng thành viên
        Label label2 = new Label("Thành viên: " + memberGroupCount);
        label2.setPrefSize(90, 35);
        label2.setTextFill(Color.web("#8e7c7c"));
        label2.setFont(Font.font("System", FontWeight.BOLD, 13));

        // Tạo label cho tên nhóm
        Label label3 = new Label("Nhóm");
        label3.setPrefSize(90, 32);
        label3.setTextFill(Color.web("#577dfa"));
        label3.setFont(Font.font("System", FontWeight.BOLD, FontPosture.ITALIC, 13));
        label3.setAlignment(Pos.CENTER);

        // Gộp các label vào VBox
        VBox vbox = new VBox();
        vbox.setPrefSize(100, 200);
        vbox.getChildren().addAll(label2, label3);

        // Tạo HBox chứa các thành phần
        HBox hbox = new HBox();
        hbox.setId("group_" + token); // gán id duy nhất cho hbox là token để giải tán nhóm dựa vào mã token :v
        hbox.setPrefSize(288, 40);
        hbox.setStyle("-fx-border-color: #4294e3;");
        hbox.getChildren().addAll(label1, vbox);

        // Xử lý sự kiện click vào nhóm
        hbox.setOnMouseClicked(mouseEvent -> {
            typeChat = "OneToMany"; // Đặt loại chat
            Image avatar = new Image(getClass().getResource("/laptrinhmang/ltm_chat/image/many.png").toExternalForm());
            Circle clip = new Circle(22, 22, 22);
            iconAvatar.setImage(avatar);
            iconAvatar.setClip(clip);

            room.setToken(token); // Gán token của nhóm
            room.setName(name); // Gán tên nhóm
            receiver.setID(0); // Reset ID của người nhận

            refreshChatBox(token, typeChat, token); // Làm mới khung chat với token nhóm
            P_Hello.setVisible(false);
            P_FrameChat.setVisible(true);

            LB_Chat.setText(name); // Cập nhật tên nhóm trong giao diện chat
        });

        // Thêm nhóm mới vào giao diện
        VB_Menu.getChildren().add(hbox);
    }

    public void cardSearchUser(String name, String phone) {
        // Tạo label cho tên
        Label label1 = new Label(name);
        label1.setPrefSize(185, 58);
        label1.setTextFill(Color.web("#8e7c7c"));
        label1.setFont(new Font(14));

        HBox.setMargin(label1, new Insets(0, 0, 0, 10));

        // Tạo label cho số điện thoại
        Label label2 = new Label(phone);
        label2.setPrefSize(90, 58);
        label2.setTextFill(Color.web("#4294e3"));
        label2.setFont(Font.font("System", FontWeight.BOLD, FontPosture.ITALIC, 13));
        label2.setAlignment(Pos.BOTTOM_CENTER);

        // Tạo HBox chứa các thành phần
        HBox hbox = new HBox();
        hbox.setLayoutX(10);
        hbox.setLayoutY(10);
        hbox.setPrefSize(288, 40);
        hbox.setStyle("-fx-border-color: #4294e3;");
        hbox.getChildren().addAll(label1, label2);

        // Xử lý sự kiện click
        hbox.setOnMouseClicked(mouseEvent -> {
            typeChat = "OneToOne"; // Set chat type
            Image avatar = new Image(getClass().getResource("/laptrinhmang/ltm_chat/image/one.png").toExternalForm());
            Circle clip = new Circle(22, 22, 22);
            iconAvatar.setImage(avatar);
            iconAvatar.setClip(clip);

            room.setToken(0); // Reset token

            P_Hello.setVisible(false);
            P_FrameChat.setVisible(true);

            LB_Chat.setText(name); // Set chat header to the user's name
        });

        VB_Menu.getChildren().add(hbox);
    }

    public void cardSearchGroup(String name) {
        // Tạo label cho tên nhóm
        Label label1 = new Label(name);
        label1.setPrefSize(196, 60);
        label1.setTextFill(Color.web("#8e7c7c"));
        label1.setFont(new Font(14));
        HBox.setMargin(label1, new Insets(0, 0, 0, 10));

        // Tạo label cho thông tin nhóm
        Label label2 = new Label("Nhóm");
        label2.setPrefSize(90, 35);
        label2.setTextFill(Color.web("#4294e3"));
        label2.setFont(Font.font("System", FontWeight.BOLD, FontPosture.ITALIC, 13));

        // Gộp các label vào VBox
        VBox vbox = new VBox();
        vbox.setPrefSize(100, 200);
        vbox.getChildren().addAll(label2);

        // Tạo HBox chứa các thành phần
        HBox hbox = new HBox();
        hbox.setPrefSize(288, 40);
        hbox.setStyle("-fx-border-color: #4294e3;");
        hbox.getChildren().addAll(label1, vbox);

        // Xử lý sự kiện click
        hbox.setOnMouseClicked(mouseEvent -> {
            typeChat = "OneToMany"; // Đặt loại chat
            Image avatar = new Image(getClass().getResource("/laptrinhmang/ltm_chat/image/many.png").toExternalForm());
            Circle clip = new Circle(22, 22, 22);
            iconAvatar.setImage(avatar);
            iconAvatar.setClip(clip);

            room.setName(name); // Gán tên nhóm
            receiver.setID(0); // Reset ID của người nhận

            P_Hello.setVisible(false);
            P_FrameChat.setVisible(true);

            LB_Chat.setText(name); // Cập nhật tên nhóm trong giao diện chat
        });

        VB_Menu.getChildren().add(hbox);
    }

    public void updateCardGroup(String name, String memberGroupCount, int token) {
        boolean found = false;

        // Tìm HBox theo ID để cập nhật
        for (Node node : VB_Menu.getChildren()) {
            if (node instanceof HBox && node.getId() != null && node.getId().equals("group_" + token)) {
                HBox hbox = (HBox) node;

                // Cập nhật tên nhóm
                Label labelName = (Label) hbox.getChildren().get(0);
                labelName.setText(name);

                // Cập nhật số lượng thành viên
                VBox vbox = (VBox) hbox.getChildren().get(1);
                Label labelMemberCount = (Label) vbox.getChildren().get(0);
                labelMemberCount.setText("Thành viên: " + memberGroupCount);

                // Đánh dấu đã tìm thấy
                found = true;
                break;
            }
        }

        // Nếu không tìm thấy, tạo thẻ mới
        if (!found) {
            cardGroup(name, memberGroupCount, token);
        }

        // Cập nhật giao diện bên phải
        if (room.getToken() == token) {
            typeChat = "OneToMany"; // Đặt loại chat
            Image avatar = new Image(getClass().getResource("/laptrinhmang/ltm_chat/image/many.png").toExternalForm());
            Circle clip = new Circle(22, 22, 22);
            iconAvatar.setImage(avatar);
            iconAvatar.setClip(clip);

            room.setToken(token); // Gán token của nhóm
            room.setName(name); // Gán tên nhóm
            receiver.setID(0); // Reset ID của người nhận

            refreshChatBox(token, typeChat, token); // Làm mới khung chat với token nhóm
            P_Hello.setVisible(false);
            P_FrameChat.setVisible(true);

            LB_Chat.setText(name); // Cập nhật tên nhóm trong giao diện chat
        }
    }

    public void updateGroupMemberCount(String name, String memberGroupCount, int token) {
        HBox groupBox = (HBox) VB_Menu.lookup("#group_" + token);
        if (groupBox != null) {
            VBox vbox = (VBox) groupBox.getChildren().get(1);
            if (vbox != null) {
                Label memberLabel = (Label) vbox.getChildren().get(0);
                if (memberLabel != null) {
                    memberLabel.setText("Thành viên: " + memberGroupCount);
                }
            }

            Label nameLabel = (Label) groupBox.getChildren().get(0);
            if (nameLabel != null) {
                nameLabel.setText(name);
            }
        } else {
            System.out.println("Group with token " + token + " not found.");
        }
    }

    public void removeCardGroup(int token) {
        Platform.runLater(() -> {
            HBox groupToRemove = (HBox) VB_Menu.lookup("#group_" + token);

            if (groupToRemove != null) {
                VB_Menu.getChildren().remove(groupToRemove);
            } else {
                System.out.println("Không tìm thấy nhóm với token " + token + ".");
            }
        });
    }

    public void addChatOneToOne(String LeftOrRight, Friend_ListMessage userChat) {
        if (LeftOrRight.equals("Left")) { // Tin nhắn từ người nhận
            HBox hbox = new HBox();
            hbox.setPrefHeight(96.0);
            hbox.setPrefWidth(600.0);

            // Avatar của người gửi
            Circle profilePic = new Circle();
            profilePic.setRadius(25.0);
            profilePic.setFill(Color.WHITE);
            profilePic.setStroke(Color.web("#4c6ce1"));
            HBox.setMargin(profilePic, new Insets(5.0, 0, 0, 5.0));
            Image imageUser = new Image(getClass().getResource("/laptrinhmang/ltm_chat/image/avatar.jpg").toExternalForm());
            profilePic.setFill(new ImagePattern(imageUser));

            // Vùng chứa tin nhắn
            VBox vbox = new VBox();
            vbox.setPrefHeight(50.0);
            vbox.setPrefWidth(266.0);
            vbox.setStyle("-fx-border-radius: 10; -fx-border-color: #56a1f5;");
            HBox.setMargin(vbox, new Insets(5.0, 0, 5.0, 10.0));

            if (userChat.getType().equals("file")) {
                if (isImageFile(userChat.getMessage())) {
                    // Hiển thị hình ảnh
                    ImageView imageView = new ImageView();
                    Image image = new Image("file:" + userChat.getPath());
                    imageView.setImage(image);
                    imageView.setFitWidth(250);
                    imageView.setPreserveRatio(true);
                    VBox.setMargin(imageView, new Insets(5.0, 5.0, 5.0, 5.0));

                    imageView.setOnMouseClicked(mouseEvent -> {
                        this.client.requestDownFile(userChat.getPath(), userChat.getMessage());
                    });

                    vbox.getChildren().add(imageView);
                } else if (userChat.getMessage().endsWith(".wav")) {
                    // Phát âm thanh
                    Label audioLabel = new Label("Audio: " + userChat.getMessage());
                    audioLabel.setWrapText(true);
                    audioLabel.setPrefHeight(54.0);
                    audioLabel.setPrefWidth(257.0);
                    audioLabel.setFont(new Font(14.0));
                    VBox.setMargin(audioLabel, new Insets(0, 0, 0, 8.0));

                    audioLabel.setOnMouseClicked(mouseEvent -> playAudio(userChat.getPath()));

                    vbox.getChildren().add(audioLabel);
                } else {
                    // Hiển thị tên tệp khác
                    Label fileLabel = new Label("file: " + userChat.getMessage());
                    fileLabel.setWrapText(true);
                    fileLabel.setPrefHeight(54.0);
                    fileLabel.setPrefWidth(257.0);
                    fileLabel.setFont(new Font(14.0));
                    VBox.setMargin(fileLabel, new Insets(0, 0, 0, 8.0));

                    fileLabel.setOnMouseClicked(mouseEvent -> {
                        this.client.requestDownFile(userChat.getPath(), userChat.getMessage());
                    });

                    vbox.getChildren().add(fileLabel);
                }
            } else {
                // Hiển thị tin nhắn văn bản
                Label messageLabel = new Label(userChat.getMessage());
                messageLabel.setWrapText(true);
                messageLabel.setPrefHeight(54.0);
                messageLabel.setPrefWidth(257.0);
                messageLabel.setFont(new Font(14.0));
                VBox.setMargin(messageLabel, new Insets(0, 0, 0, 8.0));
                vbox.getChildren().add(messageLabel);
            }

            // Thời gian gửi tin nhắn
            Label timeLabel = new Label(userChat.getSendTime());
            timeLabel.setAlignment(Pos.CENTER_RIGHT);
            timeLabel.setPrefHeight(18.0);
            timeLabel.setPrefWidth(259.0);
            VBox.setMargin(timeLabel, new Insets(0, 5.0, 0, 8.0));

            vbox.getChildren().add(timeLabel);
            hbox.getChildren().addAll(profilePic, vbox);
            VB_Chat.getChildren().add(hbox);
        } else { // Tin nhắn từ người gửi
            HBox hbox = new HBox();
            hbox.setAlignment(Pos.CENTER_RIGHT);

            VBox vbox = new VBox();
            vbox.setStyle("-fx-background-radius:10; -fx-background-color: #56a1f5; -fx-padding: 10;");
            HBox.setMargin(vbox, new Insets(5.0, 10.0, 5.0, 0));

            if (userChat.getType().equals("file")) {
                if (isImageFile(userChat.getMessage())) {
                    // Hiển thị hình ảnh
                    ImageView imageView = new ImageView();
                    Image image = new Image("file:" + userChat.getPath());
                    imageView.setImage(image);
                    imageView.setFitWidth(250);
                    imageView.setPreserveRatio(true);
                    VBox.setMargin(imageView, new Insets(5.0, 5.0, 5.0, 5.0));

                    imageView.setOnMouseClicked(mouseEvent -> {
                        this.client.requestDownFile(userChat.getPath(), userChat.getMessage());
                    });

                    vbox.getChildren().add(imageView);
                } else if (userChat.getMessage().endsWith(".wav")) {
                    // Phát âm thanh
                    Label audioLabel = new Label("Audio: " + userChat.getMessage());
                    audioLabel.setWrapText(true);
                    audioLabel.setFont(new Font(14.0));
                    audioLabel.setTextFill(Color.WHITE);
                    VBox.setMargin(audioLabel, new Insets(0, 0, 0, 8.0));

                    audioLabel.setOnMouseClicked(mouseEvent -> playAudio(userChat.getPath()));

                    vbox.getChildren().add(audioLabel);
                } else {
                    // Hiển thị tên tệp khác
                    Label fileLabel = new Label("file: " + userChat.getMessage());
                    fileLabel.setWrapText(true);
                    fileLabel.setFont(new Font(14.0));
                    fileLabel.setTextFill(Color.WHITE);
                    VBox.setMargin(fileLabel, new Insets(0, 0, 0, 8.0));

                    fileLabel.setOnMouseClicked(mouseEvent -> {
                        this.client.requestDownFile(userChat.getPath(), userChat.getMessage());
                    });

                    vbox.getChildren().add(fileLabel);
                }
            } else {
                Label messageLabel = new Label(userChat.getMessage());
                messageLabel.setWrapText(true);
                messageLabel.setFont(new Font(14.0));
                messageLabel.setTextFill(Color.WHITE);
                VBox.setMargin(messageLabel, new Insets(0, 0, 0, 8.0));
                vbox.getChildren().add(messageLabel);
            }

            Label timeLabel = new Label(userChat.getSendTime());
            timeLabel.setAlignment(Pos.CENTER_RIGHT);
            timeLabel.setTextFill(Color.LIGHTGRAY);
            VBox.setMargin(timeLabel, new Insets(0, 8.0, 0, 0));
            vbox.getChildren().add(timeLabel);

            Circle profilePic = new Circle();
            profilePic.setRadius(25.0);
            profilePic.setStroke(Color.web("#4c6ce1"));
            Image imageUser = new Image(getClass().getResource("/laptrinhmang/ltm_chat/image/avatar.jpg").toExternalForm());
            profilePic.setFill(new ImagePattern(imageUser));
            HBox.setMargin(profilePic, new Insets(5.0, 5.0, 0, 0));

            hbox.getChildren().addAll(vbox, profilePic);
            VB_Chat.getChildren().add(hbox);
        }

        // Cuộn tới tin nhắn cuối cùng
        scrollPane.layout();
        scrollPane.setVvalue(1.0);
    }

    // Kiểm tra tệp có phải là ảnh
    private boolean isImageFile(String fileName) {
        String lowerCaseFileName = fileName.toLowerCase();
        return lowerCaseFileName.endsWith(".jpg") || lowerCaseFileName.endsWith(".jpeg") || lowerCaseFileName.endsWith(".png") || lowerCaseFileName.endsWith(".gif");
    }

    private void playAudio(String audioPath) {
        try {
            File audioFile = new File(audioPath);
            if (!audioFile.exists()) {
                System.out.println("File không tồn tại: " + audioPath);
                return;
            }

            Media sound = new Media(audioFile.toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(sound);

            mediaPlayer.play();
            System.out.println("Đang phát âm thanh: " + audioPath);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Không thể phát âm thanh: " + audioPath);
        }
    }

    public void addChatOneToMany(String LeftOrRight, Group_ListMessage userChat) {
        if (LeftOrRight.equals("Left")) {
            HBox hbox = new HBox();
            hbox.setPrefHeight(96.0);
            hbox.setPrefWidth(600.0);

            // Avatar người gửi
            Circle profilePic = new Circle();
            profilePic.setRadius(25.0);
            profilePic.setFill(Color.WHITE);
            profilePic.setStroke(Color.web("#4c6ce1"));
            HBox.setMargin(profilePic, new Insets(5.0, 0, 0, 5.0));
            Image imageUser = new Image(getClass().getResource("/laptrinhmang/ltm_chat/image/avatar.jpg").toExternalForm());
            profilePic.setFill(new ImagePattern(imageUser));

            // Vùng chứa tin nhắn
            VBox vbox = new VBox();
            vbox.setPrefHeight(50.0);
            vbox.setPrefWidth(266.0);
            vbox.setStyle("-fx-border-radius: 10; -fx-border-color: #56a1f5;");
            HBox.setMargin(vbox, new Insets(5.0, 0, 5.0, 10.0));

            // Tên người gửi
            Label nameLabel = new Label(userChat.getFullName());
            nameLabel.setPrefHeight(18.0);
            nameLabel.setPrefWidth(280.0);
            nameLabel.setFont(new Font(14.0));
            VBox.setMargin(nameLabel, new Insets(0, 0, 0, 8.0));
            vbox.getChildren().add(nameLabel);

            // Nội dung tin nhắn
            if (userChat.getType().equals("file")) {
                if (isImageFile(userChat.getMessage())) {
                    // Hiển thị hình ảnh
                    ImageView imageView = new ImageView();
                    Image image = new Image("file:" + userChat.getPath());
                    imageView.setImage(image);
                    imageView.setFitWidth(250);
                    imageView.setPreserveRatio(true);
                    VBox.setMargin(imageView, new Insets(5.0, 5.0, 5.0, 5.0));

                    imageView.setOnMouseClicked(mouseEvent -> {
                        this.client.requestDownFile(userChat.getPath(), userChat.getMessage());
                    });

                    vbox.getChildren().add(imageView);
                } else if (userChat.getMessage().endsWith(".wav")) {
                    // Phát âm thanh
                    Label audioLabel = new Label("Audio: " + userChat.getMessage());
                    audioLabel.setWrapText(true);
                    audioLabel.setFont(new Font(14.0));
                    VBox.setMargin(audioLabel, new Insets(0, 0, 0, 8.0));

                    audioLabel.setOnMouseClicked(mouseEvent -> playAudio(userChat.getPath()));
                    vbox.getChildren().add(audioLabel);
                } else {
                    // Tệp khác
                    Label fileLabel = new Label("file: " + userChat.getMessage());
                    fileLabel.setWrapText(true);
                    fileLabel.setFont(new Font(14.0));
                    VBox.setMargin(fileLabel, new Insets(0, 0, 0, 8.0));

                    fileLabel.setOnMouseClicked(mouseEvent -> {
                        this.client.requestDownFile(userChat.getPath(), userChat.getMessage());
                    });
                    vbox.getChildren().add(fileLabel);
                }
            } else {
                // Tin nhắn văn bản
                Label messageLabel = new Label(userChat.getMessage());
                messageLabel.setWrapText(true);
                messageLabel.setFont(new Font(14.0));
                VBox.setMargin(messageLabel, new Insets(0, 0, 0, 8.0));
                vbox.getChildren().add(messageLabel);
            }

            // Thời gian gửi tin nhắn
            Label timeLabel = new Label(userChat.getSendTime());
            timeLabel.setAlignment(Pos.CENTER_RIGHT);
            timeLabel.setPrefHeight(18.0);
            timeLabel.setPrefWidth(259.0);
            VBox.setMargin(timeLabel, new Insets(0, 5.0, 0, 8.0));
            vbox.getChildren().add(timeLabel);

            hbox.getChildren().addAll(profilePic, vbox);
            VB_Chat.getChildren().add(hbox);
        } else {
            HBox hbox = new HBox();
            hbox.setAlignment(Pos.CENTER_RIGHT);

            VBox vbox = new VBox();
            vbox.setStyle("-fx-background-radius:10; -fx-background-color: #56a1f5; -fx-padding: 10;");
            HBox.setMargin(vbox, new Insets(5.0, 10.0, 5.0, 0));

            if (userChat.getType().equals("file")) {
                if (isImageFile(userChat.getMessage())) {
                    ImageView imageView = new ImageView();
                    Image image = new Image("file:" + userChat.getPath());
                    imageView.setImage(image);
                    imageView.setFitWidth(250);
                    imageView.setPreserveRatio(true);
                    VBox.setMargin(imageView, new Insets(5.0, 5.0, 5.0, 5.0));

                    imageView.setOnMouseClicked(mouseEvent -> {
                        this.client.requestDownFile(userChat.getPath(), userChat.getMessage());
                    });

                    vbox.getChildren().add(imageView);
                } else if (userChat.getMessage().endsWith(".wav")) {
                    Label audioLabel = new Label("Audio: " + userChat.getMessage());
                    audioLabel.setWrapText(true);
                    audioLabel.setFont(new Font(14.0));
                    audioLabel.setTextFill(Color.WHITE);
                    VBox.setMargin(audioLabel, new Insets(0, 0, 0, 8.0));

                    audioLabel.setOnMouseClicked(mouseEvent -> playAudio(userChat.getPath()));
                    vbox.getChildren().add(audioLabel);
                } else {
                    Label fileLabel = new Label("file: " + userChat.getMessage());
                    fileLabel.setWrapText(true);
                    fileLabel.setFont(new Font(14.0));
                    fileLabel.setTextFill(Color.WHITE);
                    VBox.setMargin(fileLabel, new Insets(0, 0, 0, 8.0));

                    fileLabel.setOnMouseClicked(mouseEvent -> {
                        this.client.requestDownFile(userChat.getPath(), userChat.getMessage());
                    });
                    vbox.getChildren().add(fileLabel);
                }
            } else {
                Label messageLabel = new Label(userChat.getMessage());
                messageLabel.setWrapText(true);
                messageLabel.setFont(new Font(14.0));
                messageLabel.setTextFill(Color.WHITE);
                VBox.setMargin(messageLabel, new Insets(0, 0, 0, 8.0));
                vbox.getChildren().add(messageLabel);
            }

            Label timeLabel = new Label(userChat.getSendTime());
            timeLabel.setAlignment(Pos.CENTER_RIGHT);
            timeLabel.setTextFill(Color.LIGHTGRAY);
            VBox.setMargin(timeLabel, new Insets(0, 8.0, 0, 0));
            vbox.getChildren().add(timeLabel);

            Circle profilePic = new Circle();
            profilePic.setRadius(25.0);
            profilePic.setStroke(Color.web("#4c6ce1"));
            Image imageUser = new Image(getClass().getResource("/laptrinhmang/ltm_chat/image/avatar.jpg").toExternalForm());
            profilePic.setFill(new ImagePattern(imageUser));
            HBox.setMargin(profilePic, new Insets(5.0, 5.0, 0, 0));

            hbox.getChildren().addAll(vbox, profilePic);
            VB_Chat.getChildren().add(hbox);
        }

        scrollPane.layout();
        scrollPane.setVvalue(1.0);
    }

    public void setUser(User user) {
        this.user = user;
        this.LB_FullName.setText(user.getFullName());
    }

    public User getUser() {
        return user;
    }

    public void setTF_Password_SN2(PasswordField TF_Password_SN2) {
        this.TF_Password_SN2 = TF_Password_SN2;
    }

    public void setListMessageFriend(List<Friend_ListMessage> listMessageFriend) {
        this.listMessageFriend = listMessageFriend;
    }

    public void setListMessageGroup(List<Group_ListMessage> listMessageGroup) {
        this.listMessageGroup = listMessageGroup;
    }

    public List<Friend_ListMessage> getListMessageFriend() {
        return listMessageFriend;
    }

    public List<Group_ListMessage> getListMessageGroup() {
        return listMessageGroup;
    }

    public void addMessageFriend(Friend_ListMessage friend_ListMessage) {
        this.listMessageFriend.add(friend_ListMessage);
    }

    public void addMessageGroup(Group_ListMessage group_ListMessage) {
        this.listMessageGroup.add(group_ListMessage);
    }

}
