package is.vidmot;
/******************************************************************************
 *  Nafn    :
 *  T-póstur:
 *  Viðmótsforritun 2024
 *
 *  Controller fyrir forsíðuna
 *
 *  Getur valið lagalista
 *
 *****************************************************************************/
import is.vinnsla.Lagalisti;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class LagalistiDialog extends Dialog<Lagalisti> {
    private String imgPath;

    @FXML
    private TextField fxNafnLagalistans;

    @FXML
    private Button fxVeljaMynd;

    @FXML
    private Button fxBaetaVidLagalistann;

    public LagalistiDialog() {
        DialogPane dialogPane = lesaDialog();
        setDialogPane(lesaDialog());
        setTitle("Nýr lagalisti");

// reserved 1

        fxVeljaMynd.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Veldu mynd fyrir lagalistann");
            File file = fileChooser.showOpenDialog(null);

            if (file != null) {
                imgPath = (file.toURI().toString());
            }
        });
// reserved 2

        setResultConverter();
    }

    /**
     * Les inn dialogsviðmót.
     *
     * @return      tilbúið DialogPane
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
     * Færa gögnin úr viðmótshlutum í dialog í vinnsluhlut, aftrar því að gögnin eru tóm.
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

    /**
     * Bætair við lagalistanum á listann af lagalistum og uppfærir
     * viðmótið samkvæmt því.
     *
     * @param event     þegar notandi býr til nýjan lagalista og smellur á "Bæta við"
     */
    @FXML
    private void onNyjanLagalista(ActionEvent event) {
        LagalistiDialog dialog = new LagalistiDialog();
        Optional<Lagalisti> result = dialog.showAndWait();
        result.ifPresent(lagalisti -> {
            // ...
        });
    }
}
