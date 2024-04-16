package is.vidmot;

import is.vinnsla.Lagalisti;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;

/******************************************************************************
 * Nafn :
 * T-póstur:
 * Viðmótsforritun 2024
 *
 * Dialog sem lætur nefna og búa til lagalista svo og velja mynd fyrir
 * þann lagalista.
 *
 *****************************************************************************/
public class LagalistiDialog extends Dialog<Lagalisti> {
    private String imgPath;

    @FXML
    private TextField fxNafnLagalistans;
    @FXML
    private Button fxVeljaMynd;
    @FXML
    private Button fxBaetaVidLagalistann;

    /**
     * Býr til dialog þegar notandi ákveður að búa til lagalista.
     */
    public LagalistiDialog() {
        DialogPane dialogPane = lesaDialog();
        setDialogPane(lesaDialog());
        setTitle("Nýr lagalisti");

        fxVeljaMynd.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Veldu mynd fyrir lagalistann");
            File file = fileChooser.showOpenDialog(null);

            if (file != null) {
                imgPath = (file.toURI().toString());
            }
        });
        setResultConverter();
    }

    /**
     * Les inn dialogsviðmót.
     *
     * @return tilbúið DialogPane
     */
    private DialogPane lesaDialog() {
        FXMLLoader fxmlLoader = new FXMLLoader(LagalistiDialog.class.getResource(View.NYRLAGALISTI.getFileName()));
        try {
            fxmlLoader.setController(this);
            return fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * Færa gögnin úr viðmótshlutum í dialog í vinnsluhlut, aftrar því að gögnin séu
     * tóm.
     */
    private void setResultConverter() {
        setResultConverter(b -> {
            if (b.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                Lagalisti nyrLagalisti = new Lagalisti();
                nyrLagalisti.setNafnLagalistans(fxNafnLagalistans.getText());

                if (imgPath != null && !imgPath.trim().isEmpty()) {
                    nyrLagalisti.setImgPath(imgPath);
                } else {
                    String sjalfgefidMynd = getClass().getResource("media/default.jpg").toExternalForm();
                    nyrLagalisti.setImgPath(sjalfgefidMynd);
                }
                return nyrLagalisti;
            }
            return null;
        });
    }
}