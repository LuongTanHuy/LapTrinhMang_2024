package laptrinhmang.ltm_chat;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CallVideo extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader_Video = new FXMLLoader(CallVideo.class.getResource("screen/call.fxml"));
        Scene sceneChat = new Scene(fxmlLoader_Video.load(), 600, 400);

        stage.setTitle("Call Video");
        stage.setScene(sceneChat);
        stage.show();
    }

}