package laptrinhmang.ltm_chat;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class StartClient1 extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader_Chat = new FXMLLoader(StartClient1.class.getResource("screen/hello-view.fxml"));
        Scene sceneChat = new Scene(fxmlLoader_Chat.load(), 950, 500);
        stage.setTitle("LTM_CHAT");
        stage.setScene(sceneChat);
        stage.show();

    }

    public static void main(String[] args) {
        // Run Application
        launch();
    }
}