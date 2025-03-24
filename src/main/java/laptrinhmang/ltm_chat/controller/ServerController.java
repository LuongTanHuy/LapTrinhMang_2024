package laptrinhmang.ltm_chat.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import laptrinhmang.ltm_chat.ModelChat.SocketUser;
import laptrinhmang.ltm_chat.service.server.Server;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

public class ServerController {
    @FXML
    private Label LB_IP,LB_StatusServer,LB_QuantityClient ;
    @FXML
    private TextField TF_Port;
    private Server server = new Server(this);
    @FXML
    private TableColumn<SocketUser,String> TC_STT, TC_IP, TC_Client, TC_TimeIn,TC_TimeOut,TC_Status,TC_Lock,TC_Friend,TC_Group;
    @FXML
    private TableView TB_User;
    @FXML
    private Button BT_Run;
    @FXML
    private Button BT_Stop;
    private ObservableList<SocketUser> userList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        try {
            LB_StatusServer.setText("Server đang dừng...");

            InetAddress inetAddress = InetAddress.getLocalHost();
            // Lấy địa chỉ IP dưới dạng chuỗi
            String ipAddress = inetAddress.getHostAddress();
            LB_IP.setText("IP: " + ipAddress);

            // Button Start
            FontIcon iconStart = new FontIcon("fas-play");
            iconStart.setIconSize(14);
            iconStart.setIconColor(Color.WHITE);
            BT_Run.setGraphic(iconStart);

            BT_Stop.setVisible(false);

            //Table
            TC_STT.setCellValueFactory(new PropertyValueFactory<>("id"));
            TC_IP.setCellValueFactory(new PropertyValueFactory<>("ip"));
            TC_Client.setCellValueFactory(new PropertyValueFactory<>("fullName"));
            TC_TimeIn.setCellValueFactory(new PropertyValueFactory<>("timeIn"));
            TC_TimeOut.setCellValueFactory(new PropertyValueFactory<>("timeOut"));
            TC_Status.setCellValueFactory(new PropertyValueFactory<>("status"));
            TC_Friend.setCellValueFactory(new PropertyValueFactory<>("quantityFriend"));
            TC_Group.setCellValueFactory(new PropertyValueFactory<>("quantityGroup"));

        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void stopServer(){
        new Thread(()->{
            Platform.runLater(()->{
                this.server.stopServer();
                LB_StatusServer.setText("Server đang dừng...");
                this.LB_QuantityClient.setText("");
                this.TF_Port.setText("");

                BT_Run.setDisable(false);
                BT_Run.setOpacity(1);
                BT_Stop.setVisible(false);
            });
        }).start();


    }

    public void addNotification(SocketUser socketUser) {
        try {
            this.LB_QuantityClient.setText("Client: " + this.server.quantityClient() + " đã kết nối");
            userList.add(socketUser);
            TB_User.setItems(userList);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void updateNotification(SocketUser socketUser){
        try {
            for(SocketUser updateSocketUser : userList){
                if(updateSocketUser.getSocket() == socketUser.getSocket()){
                    int index = userList.indexOf(updateSocketUser);

                    updateSocketUser.setId(socketUser.getId());
                    updateSocketUser.setIp(socketUser.getIp());
                    updateSocketUser.setFullName(socketUser.getFullName());
                    updateSocketUser.setTimeIn(socketUser.getTimeIn());
                    updateSocketUser.setTimeOut(socketUser.getTimeOut());
                    updateSocketUser.setStatus(socketUser.getStatus());
                    updateSocketUser.setQuantityFriend(socketUser.getQuantityFriend());
                    updateSocketUser.setQuantityGroup(socketUser.getQuantityGroup());

                    userList.set(index,updateSocketUser);

                }
            }
             Platform.runLater(()->{this.LB_QuantityClient.setText("Client: " + this.server.quantityClient() + " đã kết nối");});
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void startServer() {
        new Thread(() -> {
            Platform.runLater(()->{
                this.LB_StatusServer.setText("Server đang chạy...");
                this.LB_QuantityClient.setText("Client: " + this.server.quantityClient() + " đã kết nối");

                // Button Stop
                FontIcon iconStop = new FontIcon("far-stop-circle");
                iconStop.setIconSize(16);
                iconStop.setIconColor(Color.WHITE);
                BT_Stop.setGraphic(iconStop);
                BT_Stop.setVisible(true);

                BT_Run.setDisable(true);
                BT_Run.setOpacity(0.5);
            });

            this.server.StartServer(Integer.parseInt(this.TF_Port.getText()));
        }).start();
    }
}
