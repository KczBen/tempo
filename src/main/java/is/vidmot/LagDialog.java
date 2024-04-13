package is.vidmot;

import is.vinnsla.Askrifandi;
import is.vinnsla.Lag;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;

import java.io.IOException;

public class LagDialog extends Dialog<Lag> {


    public LagDialog() {

        setDialogPane(lesaDialog());
        setResultConverter();
        setTitle("Bæta við nýju lagi");
    }

    /**
     * Færa gögn úr viðmótshlutum í dialog í vinnsluhlut
     */
    private void setResultConverter() {

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
