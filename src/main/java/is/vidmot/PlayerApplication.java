package is.vidmot;

import is.vinnsla.Askrifandi;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

/******************************************************************************
 * Nafn :
 * T-póstur:
 * Viðmótsforritun 2024
 *
 * Lýsing : Application klasi fyrir AudioPlayer.
 * Opnar heimasenu (heima-view.fxml).
 *
 *****************************************************************************/
public class PlayerApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(PlayerApplication.class.getResource("heima-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 540);

        scene.getStylesheets()
                .add(PlayerApplication.class.getResource("/is/vidmot/css/lightMode.css").toExternalForm());

        ViewSwitcher.setScene(scene);
        ViewSwitcher.switchTo(View.HEIMA, true);

        stage.setTitle("AudioPlayer");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
