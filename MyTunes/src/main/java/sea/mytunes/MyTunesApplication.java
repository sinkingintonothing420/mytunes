package sea.mytunes;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MyTunesApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MyTunesApplication.class.getResource("MyTunesView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("MyTunes");
        stage.setScene(scene);
        stage.show();
    }
}
