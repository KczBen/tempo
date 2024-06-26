package is.vidmot;

import is.vinnsla.Lag;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.IOException;
import java.io.File;

import org.jaudiotagger.audio.*;

public class LagDialog extends Dialog<Lag> {

    private String songPath;
    private String imagePath;
    @FXML
    private Button fxaddsong;
    @FXML
    private Button fxaddphoto;
    @FXML
    private Label fxvillubod;
    @FXML
    private Label fxFilePath;
    @FXML
    private ImageView fxLagMynd;

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
                    fxFilePath.setText(fileName);
                    if (!(fileType.equals("mp3") || fileType.equals("mp4") || fileType.equals("wav"))) {
                        fxvillubod.setText("Skrá er ekki á réttu formi. Reyndu aftur");
                    }
                    songPath = selectedFile.toURI().toString();
                }
            } catch (NullPointerException e) {
                fxvillubod.setText("Óvænt villa kom upp. Reyndu aftur að velja skrá");
            }

        });

        fxaddphoto.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Veldu mynd til að bæta við lag");
            File selectedFile = fileChooser.showOpenDialog(null);

            if (selectedFile != null) {
                imagePath = (selectedFile.toURI().toString());
                Image image = new Image(imagePath);
                fxLagMynd.setImage(image);
            } else {
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

                try {
                    String nafn = java.net.URLDecoder.decode(songPath, "UTF-8").substring(songPath.lastIndexOf('/') + 1,
                            songPath.lastIndexOf('.'));
                    AudioFile audioFile = AudioFileIO.read(new File(
                            java.net.URLDecoder.decode(songPath, "UTF-8").substring(songPath.lastIndexOf(':') + 1)));
                    int lengd = 0;
                    lengd = audioFile.getAudioHeader().getTrackLength() * 1000;
                    System.out.println(imagePath);
                    return new Lag(songPath, imagePath, nafn, lengd);
                }

                catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            else {
                return null;
            }

        });

    }

    /**
     * Lesa inn dialog pane úr .fxml skrá
     * 
     * @return
     */
    private DialogPane lesaDialog() {
        FXMLLoader fxmlLoader = new FXMLLoader(LagDialog.class.getResource(View.NYTTLAG.getFileName()));
        try {
            fxmlLoader.setController(this); // setur þennan hlut sem controller
            return fxmlLoader.load(); // hlaða inn fxml skránni
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

    }
}
