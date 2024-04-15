package is.vidmot;

import is.vinnsla.Lag;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.stage.FileChooser;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.io.File;
import java.util.Optional;

public class LagDialog extends Dialog<Lag> {

    private String songPath;
    private String imagePath;
    @FXML
    private Button fxaddsong;
    @FXML
    private Button fxaddphoto;

    public LagDialog() {

        setDialogPane(lesaDialog());
        setResultConverter();
        setTitle("Bæta við nýju lagi");


        fxaddsong.setOnAction(event -> {
            try {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Veldu lag til að bæta við lagalista");
                File selectedFile = fileChooser.showOpenDialog(null);

                if (selectedFile != null) {
                    String fileName = selectedFile.getName();
                    String fileType = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
                    if (!(fileType.equals("mp3") || fileType.equals("mp4"))) {
                        throw new RuntimeException("Ekki rétt tegund af skrá. Vinsamlegast veldu mp3 eða mp4 skrá");
                    }
                    songPath = selectedFile.toURI().toString();
                } else {
                    // Handle the case where no file is selected
                    throw new RuntimeException("Ekkert lag valið. Vinsamlegast veldu lag.");
                }
            } catch (Exception e) {
                // Handle any exceptions
                e.printStackTrace(); // You can replace this with logging or displaying an alert to the user
            }
        });


        fxaddphoto.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Veldu mynd til að bæta við lag");
            File selectedFile = fileChooser.showOpenDialog(null);

            if (selectedFile != null) {
                imagePath = (selectedFile.toURI().toString());
            }else{
                imagePath = "media/default.jpg";
            }
        });
    }

    /**
     * Færa gögn úr viðmótshlutum í dialog í vinnsluhlut
     */
    private void setResultConverter() {
        setResultConverter(b -> {
            if (b.getButtonData() == ButtonBar.ButtonData.OK_DONE) {

                String nafn = songPath.substring(songPath.lastIndexOf('/') + 1,songPath.lastIndexOf('.'));
                int lengd = 1000 * Integer.parseInt(songPath.substring(songPath.lastIndexOf('-') + 1, songPath.lastIndexOf('s')));
                Lag lag = new Lag(songPath, imagePath, nafn, lengd);
                return lag;
            }else{
                return null;
            }

        });


    }

    /**
     * Lesa inn dialog pane úr .fxml skrá
     * @return
     */
    private DialogPane lesaDialog() {
        FXMLLoader fxmlLoader = new FXMLLoader(LagDialog.class.getResource(View.NYTTLAG.getFileName()));
        try {
            fxmlLoader.setController(this); // setur þennan hlut sem controller
            return fxmlLoader.load();       // hlaða inn fxml skránni
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

    }
}
