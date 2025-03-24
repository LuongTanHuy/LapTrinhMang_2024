package laptrinhmang.ltm_chat;


import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class StartServer extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage)  throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartServer.class.getResource("screen/server.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 950, 500);
        stage.setTitle("Server Chat");
        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest(event -> {
            // Nếu muốn thoát hẳn ứng dụng sau khi đóng cửa sổ chính
            Platform.exit(); // Thoát khỏi ứng dụng
            System.exit(0);   // Thoát khỏi JVM
        });
    }
}

