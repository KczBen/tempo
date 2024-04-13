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
import is.vinnsla.Askrifandi;
import is.vinnsla.Lagalistar;
import is.vinnsla.Lagalisti;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.util.List;
import java.util.Optional;

public class PlayerController  {

    // fastar
    public static final String ASKRIFANDI = "Áskrifandi";

    // viðmótshlutir
    @FXML
    protected Button fxAskrifandi;

    @FXML
    private GridPane fxListarNotandans;

    @FXML
    private Button fxBuaTilLagalista;

    // frumstilling eftir að hlutur hefur verið smíðaður og .fxml skrá lesin
    public void initialize() {
        Lagalistar.frumstilla();
        uppfaeraLagalistana();
    }

    /**
     * Færir lagalista notandans yfir á borðið.
     */
    private void uppfaeraLagalistana() {
        Lagalisti lagalistar[] = Lagalistar.getAllaLista();
        int rod, dalkur;

        for (int i = 4; i < lagalistar.length; ++i) {
            Lagalisti lagalisti = lagalistar[i];

            if (lagalisti != null) {
                ImageView imageView = new ImageView(new Image(lagalisti.getImgPath()));
                imageView.setFitHeight(50);
                imageView.setFitWidth(100);

                // imageView.setCursor(Cursor.HAND);
                imageView.setOnMouseClicked(event -> {
                    Node node = (Node) event.getSource();
                    int j = GridPane.getRowIndex(node);
                    int k = GridPane.getColumnIndex(node);
                    Lagalistar.setIndex(j * 2 + k);

                    ViewSwitcher.switchTo(View.LAGALISTI, false);
                });

                rod = i / 2;
                dalkur = i % 2;
                fxListarNotandans.add(imageView, dalkur, rod);
            }
        }
    }

    /**
     * Atburðarhandler fyrir að velja lagalista. Sá lagalisti er settur og farið í senu fyrir þann lista
     * @param mouseEvent
     */
    @FXML
    protected void onVeljaLista(ActionEvent mouseEvent) {
        // hvaða reitur var valinn
        int i = GridPane.getRowIndex((Node) mouseEvent.getSource());
        int j = GridPane.getColumnIndex((Node) mouseEvent.getSource());
        // skiptum yfir í lagalistann í vinnslunni sem var valið
        Lagalistar.setIndex(i * 2 + j);
        // skiptum yfir í LAGALISTI view
        ViewSwitcher.switchTo(View.LAGALISTI, false);
    }

    /**
     * Loggar áskrifanda inn
     *
     * @param actionEvent
     */
    public void onLogin(ActionEvent actionEvent) {
        // býr til nýjan dialog með tómum áskrifanda
        AskrifandiDialog dialog = new AskrifandiDialog(new Askrifandi(ASKRIFANDI));
        // sýndu dialoginn
        Optional<Askrifandi> utkoma = dialog.showAndWait();
        // Ef fékkst svar úr dialognum setjum við nafnið á áskrifandanum í notendaviðmótið
        utkoma.ifPresent (a -> {
            fxAskrifandi.setText(a.getNafn());});
    }

    /**
     * Virkjar alla lagalistana.
     *
     * @param event
     */
    @FXML
    protected void onBuaTilLagalista(ActionEvent event) {
        LagalistiDialog dialog = new LagalistiDialog();
        Optional<Lagalisti> utkoma = dialog.showAndWait();
        utkoma.ifPresent(lagalisti -> {
            Lagalistar.baetaALista(lagalisti);
            uppfaeraLagalistana();
        });
    }
}