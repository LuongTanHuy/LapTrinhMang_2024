package laptrinhmang.ltm_chat;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class StartClient2 extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartClient2.class.getResource("screen/hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 950, 500);
        stage.setTitle("LTM_CHAT");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        // Run Application
        launch();
    }
}
