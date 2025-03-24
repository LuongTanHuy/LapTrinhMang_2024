package laptrinhmang.ltm_chat.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;

public class CallVideoController {
    @FXML
    private Circle C_Image;
    @FXML
    private Button BT_StopCall, BT_Disconnect, BT_HiddenCamera, BT_HiddenSound;
    @FXML
    private Pane P_Video, P_Connecting;
    private boolean buttonCamera = true;
    private boolean buttonSound = true;

    @FXML
    public void initialize() {
        P_Video.setVisible(false);
        P_Connecting.setVisible(true);

        // Button Stop Call
        FontIcon icon7 = new FontIcon("fas-phone-slash");
        icon7.setIconSize(20);
        icon7.setIconColor(Color.WHITE);
        BT_StopCall.setGraphic(icon7);

        FontIcon icon8 = new FontIcon("fas-phone-slash");
        icon8.setIconSize(20);
        icon8.setIconColor(Color.WHITE);
        BT_Disconnect.setGraphic(icon8);

        // Button Camera
        FontIcon icon9 = new FontIcon("fas-video");
        icon9.setIconSize(20);
        icon9.setIconColor(Color.WHITE);
        BT_HiddenCamera.setGraphic(icon9);

        // Button Sound
        FontIcon icon10 = new FontIcon("fas-microphone");
        icon10.setIconSize(20);
        icon10.setIconColor(Color.WHITE);
        BT_HiddenSound.setGraphic(icon10);

        Image imageCallVideo = new Image(getClass().getResource("/laptrinhmang/ltm_chat/image/avatar.jpg").toExternalForm());
        C_Image.setFill(new ImagePattern(imageCallVideo));
    }

    @FXML
    public void handleButtonSound() {
        if (buttonSound) {
            Platform.runLater(() -> {
                        FontIcon icon = new FontIcon("fas-microphone");
                        icon.setIconSize(20);
                        icon.setIconColor(Color.WHITE);
                        BT_HiddenSound.setGraphic(icon);
                        buttonSound = false;
            });
        } else {
            Platform.runLater(() -> {
                FontIcon icon = new FontIcon("fas-microphone-slash");
                icon.setIconSize(20);
                icon.setIconColor(Color.WHITE);
                BT_HiddenSound.setGraphic(icon);
                buttonSound = true;
            });
        }
    }

    @FXML
    public void handleButtonCamera(){
        if (buttonCamera) {
            Platform.runLater(()->{
                FontIcon icon = new FontIcon("fas-video");
                icon.setIconSize(20);
                icon.setIconColor(Color.WHITE);
                BT_HiddenCamera.setGraphic(icon);
                buttonCamera = false;
            });
        }else{
            Platform.runLater(()->{
                FontIcon icon = new FontIcon("fas-video-slash");
                icon.setIconSize(20);
                icon.setIconColor(Color.WHITE);
                BT_HiddenCamera.setGraphic(icon);
                buttonCamera = true;
            });
        }
    }

    @FXML
    public void stopCallVideo(ActionEvent event){
      Stage stage = (Stage) BT_StopCall.getScene().getWindow();
      stage.close();
    }
}
