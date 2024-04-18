package is.vidmot;

import is.vinnsla.Askrifandi;
import is.vinnsla.Lagalistar;
import is.vinnsla.Lagalisti;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/******************************************************************************
 * Nafn :
 * T-póstur:
 * Viðmótsforritun 2024
 *
 * Controller fyrir forsíðuna
 *
 * Getur valið, búið til, endurnefnt, eytt lagalista.
 *
 *****************************************************************************/
public class PlayerController {

    // fastar
    public static final String ASKRIFANDI = "Áskrifandi";
    private boolean lightModeOn = true;
    private ContextMenu contextMenu;

    // viðmótshlutir
    @FXML
    protected Button fxAskrifandi;
    @FXML
    private GridPane fxListarNotandans;
    @FXML
    private Button fxBuaTilLagalista;

    /**
     * Frumstilling eftir að hlutur hefur verið smíðaður og .fxml skrá lesin.
     */
    public void initialize() {
        Lagalistar.frumstilla();
        uppfaeraLagalistana();
    }

    /**
     * Færir og birtir lagalista notandans á heimasíðunni.
     */
    private void uppfaeraLagalistana() {
        hreinsaReita();
        Lagalisti lagalistar[] = Lagalistar.getAllaLista();
        int rod, dalkur;

        for (int i = 4; i < lagalistar.length; ++i) {
            Lagalisti lagalisti = lagalistar[i];
            final int index = i;

            if (lagalisti != null) {
                ImageView imageView = new ImageView(new Image(lagalisti.getImgPath()));
                imageView.setFitHeight(70);
                imageView.setFitWidth(150);

                imageView.setOnMouseClicked(event -> {
                    if (event.getButton() == MouseButton.PRIMARY) {
                        Node node = (Node) event.getSource();
                        int j = GridPane.getRowIndex(node);
                        int k = GridPane.getColumnIndex(node);
                        Lagalistar.setIndex(j * 2 + k);

                        ViewSwitcher.switchTo(View.LAGALISTI, false);
                    } else if (event.getButton() == MouseButton.SECONDARY) {
                        virkjaContextMenu((Node) event.getSource(), index);
                    }
                });

                rod = i / 2;
                dalkur = i % 2;
                fxListarNotandans.add(imageView, dalkur, rod);
            }
        }
        customBendill();
    }

    /**
     * Setur upp context menu fyrir lagalistana.
     *
     * @param node  tilvik af lagalista
     * @param index tala sem auðkennir lagalistann
     */
    private void virkjaContextMenu(Node node, int index) {
        if (contextMenu != null) {
            contextMenu.hide();
        }
        contextMenu = new ContextMenu();

        MenuItem endurnefnaLista = new MenuItem("Endurnefna");
        endurnefnaLista.setOnAction(event -> endurnefnaLista(index));

        MenuItem breytaMynd = new MenuItem("Breyta mynd");
        breytaMynd.setOnAction(event -> breytaMynd(index));

        MenuItem eydaLista = new MenuItem("Eyða");
        eydaLista.setOnAction(event -> eydaLista(index));

        contextMenu.getItems().addAll(endurnefnaLista, breytaMynd, eydaLista);
        node.setOnContextMenuRequested(event -> contextMenu.show(node, event.getScreenX(), event.getScreenY()));
        ;
    }

    /**
     * Endurnefnir lagalistann.
     *
     * @param index tala sem auðkennir lagalistann
     */
    private void endurnefnaLista(int index) {
        TextInputDialog dialog = new TextInputDialog();

        dialog.getDialogPane().getButtonTypes().clear();
        ButtonType jaTakki = new ButtonType("ÓK", ButtonBar.ButtonData.OK_DONE);
        ButtonType neiTakki = new ButtonType("Hætta við", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(jaTakki, neiTakki);

        dialog.setTitle("Endurnefna lagalistann");
        dialog.setHeaderText("Sláðu inn nýtt nafn");

        Optional<String> utkoma = dialog.showAndWait();
        utkoma.ifPresent(nafn -> {
            if (!nafn.isEmpty()) {
                Lagalistar.getLagalistann(index).setNafnLagalistans(nafn);
                uppfaeraLagalistana();
            } else {
                Lagalistar.getLagalistann(index).setNafnLagalistans("Nýr lagalisti " + index);
                uppfaeraLagalistana();
            }
        });
    }

    /**
     * Eyðir lagalistanum.
     *
     * @param index tala sem auðkennir lagalistann
     */
    private void eydaLista(int index) {
        if (index >= 4) {
            ButtonType jaTakki = new ButtonType("Já", ButtonBar.ButtonData.YES);
            ButtonType neiTakki = new ButtonType("Nei", ButtonBar.ButtonData.NO);
            Alert stadfesta = new Alert(Alert.AlertType.CONFIRMATION, "Viltu eyða þessum lagalista?", jaTakki,
                    neiTakki);
            stadfesta.setTitle("Eyða lagalistanum");
            stadfesta.setHeaderText("Staðfesting");

            Optional<ButtonType> utkoma = stadfesta.showAndWait();
            if (utkoma.isPresent() && utkoma.get().getButtonData().equals(ButtonBar.ButtonData.YES)) {
                Lagalistar.eydaLagalista(index);
                uppfaeraLagalistana();
            }
        } else {
            ButtonType okTakki = new ButtonType("ÓK", ButtonBar.ButtonData.OK_DONE);
            Alert alert = new Alert(Alert.AlertType.ERROR, "Ekki er hægt að eyða sjálfgefnum lagalistum.", okTakki);
            alert.setTitle("Tókst ekki að eyða lagalistanum");
            alert.setHeaderText("Villa");
            alert.show();
        }
    }

    /**
     * Breytir mynd lagalistand.
     *
     * @param index tala sem auðkennir lagalista
     */
    private void breytaMynd(int index) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Veldu nýja mynd fyrir lagalistann");
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            String nyttImgPath = (file.toURI().toString());
            Lagalistar.getLagalistann(index).setImgPath(nyttImgPath);
            uppfaeraMynd(index, nyttImgPath);
        }
    }

    /**
     * Uppfærir mynd lagalistans.
     *
     * @param index       tala sem auðkennir lagalista
     * @param nyttImgPath staðsetning nýju myndarinnar
     */
    private void uppfaeraMynd(int index, String nyttImgPath) {
        int rod = index / 2;
        int dalkur = index % 2;

        Node node = getNodeMyndarinnar(rod, dalkur, fxListarNotandans);
        if (node instanceof ImageView) {
            ((ImageView) node).setImage(new Image(nyttImgPath));
        }
    }

    /**
     * Leitar að viðeigandi myndreit lagalistans.
     *
     * @param rod    röð myndreitsins
     * @param dalkur dalkur myndreitsins
     * @param grind  GridPane hlutur
     * @return viðeigandi node hlutur
     */
    private Node getNodeMyndarinnar(int rod, int dalkur, GridPane grind) {
        for (Node node : grind.getChildren()) {
            if (GridPane.getRowIndex(node) != null && GridPane.getColumnIndex(node) != null &&
                    GridPane.getRowIndex(node) == rod && GridPane.getColumnIndex(node) == dalkur) {
                return node;
            }
        }
        return null;
    }

    /**
     * Býr til lista af reitum sem má eyða og eyðir þeim.
     */
    private void hreinsaReita() {
        List<Node> eyddirLagalistar = new ArrayList<>();

        for (Node node : fxListarNotandans.getChildren()) {
            Integer rod = GridPane.getRowIndex(node);
            Integer dalkur = GridPane.getColumnIndex(node);

            if (rod != null && dalkur != null) {
                if (rod * 2 + dalkur >= 4) {
                    eyddirLagalistar.add(node);
                }
            }
        }
        fxListarNotandans.getChildren().removeAll(eyddirLagalistar);
    }

    /**
     * Setur nýjan bendil þegar miðað er á lagalista.
     */
    private void customBendill() {
        for (Node node : fxListarNotandans.getChildren()) {
            node.setCursor(Cursor.HAND);
        }
    }

    /**
     * Dialog gluggi sem birtist ef fjöldi lagalista fer út fyrir.
     */
    private void ekkertPlassDialog() {
        ButtonType iLagiTakki = new ButtonType("Í lagi", ButtonBar.ButtonData.OK_DONE);
        Alert alert = new Alert(Alert.AlertType.ERROR, "Eyða?", iLagiTakki);
        alert.setTitle("Tókst ekki að búa til lagalistann");
        alert.setHeaderText("Villa");
        alert.setContentText("Ekkert pláss fyrir nýjan lagalista. Hámarksfjöldi er 10.");
        alert.showAndWait();
    }

    /**
     * Skiptir á milli dökks og ljóss þema.
     *
     * @param actionEvent
     */
    public void switchMode(ActionEvent actionEvent) {
        Scene scene = ViewSwitcher.getScene();
        lightModeOn = !lightModeOn;
        if (lightModeOn) {
            scene.getStylesheets().clear(); // Clear existing stylesheets
            scene.getStylesheets()
                    .add(PlayerApplication.class.getResource("/is/vidmot/css/lightMode.css").toExternalForm());
        } else {
            scene.getStylesheets().clear(); // Clear existing stylesheets
            scene.getStylesheets()
                    .add(PlayerApplication.class.getResource("/is/vidmot/css/darkMode.css").toExternalForm());
        }
    }

    /**
     * Loggar áskrifanda inn.
     *
     * @param actionEvent
     */
    @FXML
    public void onLogin(ActionEvent actionEvent) {
        // býr til nýjan dialog með tómum áskrifanda
        AskrifandiDialog dialog = new AskrifandiDialog(new Askrifandi(ASKRIFANDI));

        // sýndu dialoginn
        Optional<Askrifandi> utkoma = dialog.showAndWait();

        // Ef fékkst svar úr dialognum setjum við nafnið á áskrifandanum í
        // notendaviðmótið
        utkoma.ifPresent(a -> {
            fxAskrifandi.setText(a.getNafn());
        });
    }

    /**
     * Atburðarhandler fyrir að velja lagalista. Sá lagalisti er settur og farið í
     * senu fyrir þann lista.
     *
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
     * Virkjar lagalista.
     *
     * @param event
     */
    @FXML
    protected void onBuaTilLagalista(ActionEvent event) {
        LagalistiDialog dialog = new LagalistiDialog();
        Optional<Lagalisti> utkoma = dialog.showAndWait();
        utkoma.ifPresent(lagalisti -> {
            if (!Lagalistar.baetaALista(lagalisti)) {
                ekkertPlassDialog();
            }
            uppfaeraLagalistana();
        });
    }
}