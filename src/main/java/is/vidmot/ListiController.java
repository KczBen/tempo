package is.vidmot;

import is.vinnsla.Lag;
import is.vinnsla.Lagalistar;
import is.vinnsla.Lagalisti;
import is.vinnsla.TimeConverter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.effect.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import javafx.scene.Scene;

import java.util.Optional;

/******************************************************************************
 * Nafn :
 * T-póstur:
 * Viðmótsforritun 2024
 *
 * Controller fyrir lagalistann.
 * getur:
 *
 * -- valið lag
 * -- play / pause
 * -- farið heim
 *****************************************************************************/
public class ListiController {
    private boolean isMuted = false;
    private double setbackVol = 100;

    private final String PAUSE = "images/pause.png";
    private final String PlAY = "images/play.png";
    private final String MUTE = "images/volume_mute.png";
    private final String UNMUTE = "images/volume_unmute.png";

    @FXML
    public ProgressBar fxProgressBar; // progress bar fyrir spilun á lagi
    @FXML
    protected ImageView fxPlayPauseView; // mynd fyrir play/pause hnappinn
    @FXML
    protected ListView<Lag> fxListView; // lagalistinn
    @FXML
    protected Label fxNafnLagalistans;
    @FXML
    protected ImageView fxMyndLagView; // mynd fyrir lagið
    @FXML
    protected Slider fxVolumeSlider;
    @FXML
    protected ImageView fxMuteIcon;
    @FXML
    protected Button fxStartTime;
    @FXML
    protected Button fxStopTime;
    @FXML
    protected ImageView fxLoopButton;
    @FXML
    protected ImageView fxShuffleButton;
    @FXML
    protected Label fxCurrentTime;

    // vinnslan
    private Lagalisti lagalisti; // lagalistinn
    private MediaPlayer player; // ein player breyta per forritið
    private Lag validLag; // núverandi valið lag
    private Boolean shuffle = false; // Don't shuffle by default
    private Duration startTime = Duration.ZERO;
    private Duration stopTime = Duration.INDEFINITE;
    private boolean lightModeOn = true;

    /**
     * Frumstillir lagalistann og tengir hann við ListView viðmótshlut.
     */
    public void initialize() {
        // setur lagalistann sem núverandi lagalista úr Lagalistar
        lagalisti = Lagalistar.getNuverandi();

        // tengdu lagalistann við ListView-ið
        fxListView.setItems(lagalisti.getListi());

        if (!lagalisti.getListi().isEmpty()) {
            fxNafnLagalistans.setText(lagalisti.getNafnLagalistans());

            // man hvaða lag var síðast spilað á lagalistanum og setur það sem valið stak á
            // ListView
            fxListView.getSelectionModel().select(lagalisti.getIndex());
            fxListView.requestFocus(); // setur lagið í focus
            veljaLag(); // Lætur lagalista vita hvert valda lagið er í viðmótinu og uppfærir myndina
                        // fyrir lagið
            setjaPlayer(); // setur upp player
            setjaVolume(); // virkjar hljóstyrkinn
            fxCurrentTime.setText("--:--");

            // Enable all the controls - used for when a song is added to an empty playlist
            fxListView.setDisable(false);
            fxPlayPauseView.setDisable(false);
            fxProgressBar.setDisable(false);
            fxMuteIcon.setDisable(false);
            fxVolumeSlider.setDisable(false);
            fxStartTime.setDisable(false);
            fxStopTime.setDisable(false);
        }

        else {
            fxNafnLagalistans.setText(lagalisti.getNafnLagalistans());
            fxMyndLagView.setImage(new Image(getClass().getResource("media/default.jpg").toExternalForm()));

            // slökkva á alla virkni (á meðan það eru engin lög á lagalistanum en það þarf
            // að bæta við þessum skilyrðum)
            fxListView.setDisable(true);
            fxPlayPauseView.setDisable(true);
            fxProgressBar.setDisable(true);
            fxMuteIcon.setDisable(true);
            fxVolumeSlider.setDisable(true);
            fxStartTime.setDisable(true);
            fxStopTime.setDisable(true);
        }
    }

    /**
     * Bregðst við músaratburði og spila valið lag.
     *
     * @param mouseEvent
     */
    @FXML
    protected void onValidLag(MouseEvent mouseEvent) {
        System.out.println(fxListView.getSelectionModel().getSelectedItem());
        veljaLag(); // Lætur lagalista vita hvert valda lagið er í viðmótinu og uppfærir myndina
                    // fyrir lagið
        spilaLag(); // spila lagið
    }

    /**
     * Lagið er pásað ef það er í spilun, lagið er spilað ef það er í pásu.
     *
     * @param actionEvent ónotað
     */
    @FXML
    protected void onPlayPause(ActionEvent actionEvent) {
        // ef player-inn er spilandi
        if (player.getStatus().equals(MediaPlayer.Status.PLAYING)) {
            setjaMynd(fxPlayPauseView, PlAY); // uppfærðu myndina með play (ör)
            player.pause(); // pásaðu spilarann
        }

        else if (player.getStatus().equals(MediaPlayer.Status.PAUSED)) {
            setjaMynd(fxPlayPauseView, PAUSE); // uppfærðu myndina með pause
            player.play(); // haltu áfram að spila
        }
    }

    /**
     * Fara aftur í heima view. Ef spilari er til stöðva spilarann
     *
     * @param actionEvent ónotað
     */
    @FXML
    protected void onHeim(ActionEvent actionEvent) {
        // stoppaðu player ef hann er ekki null
        if (player != null) {
            player.stop();
        }
        ViewSwitcher.switchTo(View.HEIMA, true); // farðu í HEIMA senuna með ViewSwitcher
    }

    /**
     * Lætur laga lista vita hvert valda lagið er. Uppfærir myndina fyrir lagið.
     */
    private void veljaLag() {
        if (!fxListView.getItems().isEmpty()) {
            validLag = fxListView.getSelectionModel().getSelectedItem(); // hvaða lag er valið
            if (validLag != null) {
                // láttu lagalista vita um indexinn á völdu lagi
                lagalisti.setIndex(fxListView.getSelectionModel().getSelectedIndex());
                setjaMynd(fxMyndLagView, validLag.getMynd()); // uppfæra myndina fyrir lagið
            }
        }
    }

    /**
     * Spilar lagið.
     */
    private void spilaLag() {
        setjaMynd(fxPlayPauseView, PAUSE);
        clearPoints(); // Clear start and end points
        setjaPlayer(); // Búa til nýjan player
        player.play(); // setja spilun í gang
    }

    /**
     * Setur mynd með nafni á ImageView.
     *
     * @param fxImageView viðmótshluturinn sem á að uppfærast
     * @param nafnMynd    nafn á myndinni
     */
    private void setjaMynd(ImageView fxImageView, String nafnMynd) {
        System.out.println("nafn á mynd " + nafnMynd);
        Image image;
        // Check if image is selected from outside
        try {
            if (nafnMynd.startsWith("file:/")) {
                image = new Image(nafnMynd);
            }

            else {
                image = new Image(getClass().getResource(nafnMynd).toExternalForm());
            }

            try {
                fxImageView.setImage(image);
            }

            catch (Exception e) {
                e.printStackTrace();
                System.err.println("No image! did you set an image?");
            }
        } catch (Exception e) {
            fxImageView.setImage(new Image(getClass().getResource("media/default.jpg").toExternalForm()));
        }
    }

    /**
     * Setur upp player fyrir lagið, þ.m.t. at setja handler á hvenær lagið stoppar
     * og tengja
     * lagið við progress bar.
     */
    private void setjaPlayer() {
        if (player != null) {
            player.stop(); // Stoppa player-inn ef hann var ekki stopp
        }

        // Smíða nýjan player með nýju Media fyrir lagið
        String mediaSource = validLag.getMedia();
        Media media;
        // Check if song is selected from outside
        if (mediaSource.startsWith("file:/")) {
            media = new Media(mediaSource);
        }

        else {
            media = new Media(getClass().getResource(mediaSource).toExternalForm());
        }
        player = new MediaPlayer(media);

        // Láta player vita hvenær lagið endar - stop time
        player.setStartTime(this.startTime);
        player.setStopTime(this.stopTime);

        // setja fall sem er keyrð þegar lagið hættir
        player.setOnEndOfMedia(this::naestaLag);
        uppfaeraVolume();

        // setja listener tengingu á milli player og progress bar
        player.currentTimeProperty().addListener((observable, old, newValue) -> {
            fxProgressBar.setProgress(newValue.divide(validLag.getLengd()).toMillis());
            fxCurrentTime.setText(TimeConverter.convertTime(player.getCurrentTime(), false));
        });

        fxProgressBar.setOnMouseClicked(event -> {
            player.seek(new Duration(event.getX() / fxProgressBar.getWidth() * validLag.getLengd()));
        });

        fxStartTime.setText(TimeConverter.convertTime(this.startTime, false));
        fxStopTime.setText(TimeConverter.convertTime(validLag.getLengd(), false));
    }

    /**
     * Setur upp virkni fyrir hljóðstyrkinn innan sildersins.
     */
    private void setjaVolume() {
        fxVolumeSlider.setValue(setbackVol);
        fxVolumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (player != null) {
                player.setVolume(newVal.doubleValue() / 100);

                boolean volumeEkkiNull = newVal.doubleValue() > 0;
                isMuted = !volumeEkkiNull;
                fxMuteIcon.setDisable(!volumeEkkiNull);

                if (volumeEkkiNull) {
                    uppfaeraMuteIcon(MUTE);
                }

                else {
                    uppfaeraMuteIcon(UNMUTE);
                }
            }
        });
        fxMuteIcon.setOnMouseClicked(event -> setjaMute());
    }

    /**
     * Útfærsla á mute/unmute virkni fyrir takkann (ImageView hlut).
     */
    private void setjaMute() {
        if (isMuted) {
            player.setVolume(setbackVol / 100);
            uppfaeraMuteIcon(MUTE);
            isMuted = false;
        }

        else {
            setbackVol = player.getVolume() * 100;
            player.setVolume(0);
            uppfaeraMuteIcon(UNMUTE);
            isMuted = true;
        }
        uppfaeraVolume();
    }

    /**
     * Gætir þess að hljóðstyrkurinn varðveitist.
     */
    private void uppfaeraVolume() {
        if (player != null) {
            if (!isMuted) {
                player.setVolume(fxVolumeSlider.getValue() / 100);
                uppfaeraMuteIcon(MUTE);
            }

            else {
                player.setVolume(0);
                uppfaeraMuteIcon(UNMUTE);
            }
        }
    }

    /**
     * Uppfærir mynd á mute/unmute takka (ImageView hlut).
     *
     * @param muteIcon string sem lýsir myndinni fyrir mute/unmute takka
     */
    private void uppfaeraMuteIcon(String muteIcon) {
        setjaMynd(fxMuteIcon, muteIcon);
    }

    /**
     * Næsta lag er spilað. Kallað á þessa aðferð þegar fyrra lag á listanum endar.
     */
    private void naestaLag() {
        // the setOnEndOfMedia property overrides cycleCount, this prevents the next
        // song from playing when it ends
        if (player.getCycleCount() != 1) {
            return;
        }

        if (this.shuffle == false) {
            lagalisti.naesti();
        }

        else {
            lagalisti.random();
        }
        // uppfæra ListView til samræmis, þ.e. að næsta lag sé valið
        fxListView.getSelectionModel().selectIndices(lagalisti.getIndex());

        veljaLag(); // velja lag
        spilaLag(); // spila lag
        uppfaeraVolume();
    }

    private void fyrraLag() {
        lagalisti.fyrri();
        fxListView.getSelectionModel().selectIndices(lagalisti.getIndex());
        veljaLag();
        spilaLag();
        uppfaeraVolume();
    }

    private void toggleLoop() {
        if (player.getCycleCount() == 1) {

            ColorAdjust active = new ColorAdjust();
            active.setHue(177);
            active.setSaturation(0.53);
            active.setBrightness(0.71);
            fxLoopButton.setEffect(active);

            player.setCycleCount(MediaPlayer.INDEFINITE);

            // If loop points have been set, loop from there
            if (this.startTime != Duration.ZERO || this.stopTime != new Duration(validLag.getLengd())) {
                // Check if seek head is out of bounds for the repeat, set to start if so
                if (player.getCurrentTime().greaterThan(this.stopTime)
                        || player.getCurrentTime().lessThan(this.startTime)) {
                    player.seek(startTime);
                }

                player.setStartTime(this.startTime);
                player.setStopTime(this.stopTime);
            }
            player.setCycleCount(MediaPlayer.INDEFINITE);
        }

        else {
            // Stop looping and clear loop points
            player.setCycleCount(1);
            clearPoints();
            player.setStartTime(this.startTime);
            player.setStopTime(this.stopTime);
        }
    }

    private void toggleShuffle() {
        if (this.shuffle == false) {
            this.shuffle = true;

            ColorAdjust active = new ColorAdjust();
            active.setHue(177);
            active.setSaturation(0.53);
            active.setBrightness(0.71);
            fxShuffleButton.setEffect(active);

        }

        else {
            this.shuffle = false;

            ColorAdjust inactive = new ColorAdjust();
            inactive.setHue(0);
            inactive.setSaturation(0);
            inactive.setBrightness(0);
            fxShuffleButton.setEffect(inactive);
        }
    }

    private void setStart() {
        if (!player.getCurrentTime().greaterThanOrEqualTo(this.stopTime)) {
            this.startTime = player.getCurrentTime();
            fxStartTime.setText(TimeConverter.convertTime(player.getCurrentTime(), true));
        }
    }

    private void setStop() {
        if (!player.getCurrentTime().lessThanOrEqualTo(this.startTime)) {
            this.stopTime = player.getCurrentTime();
            fxStopTime.setText(TimeConverter.convertTime(player.getCurrentTime(), true));
        }
    }

    private void clearPoints() {
        ColorAdjust inactive = new ColorAdjust();
        inactive.setHue(0);
        inactive.setSaturation(0);
        inactive.setBrightness(0);
        fxLoopButton.setEffect(inactive);

        this.startTime = Duration.ZERO;
        this.stopTime = new Duration(validLag.getLengd());
        fxStartTime.setText(TimeConverter.convertTime(this.startTime, false));
        fxStopTime.setText(TimeConverter.convertTime(validLag.getLengd(), false));
    }

    @FXML
    public void onClearPoints(ActionEvent actionEvent) {
        clearPoints();
    }

    @FXML
    public void onSetStart(ActionEvent actionEvent) {
        setStart();
    }

    @FXML
    public void onSetStop(ActionEvent actionEvent) {
        setStop();
    }

    @FXML
    public void onLoop(ActionEvent actionEvent) {
        toggleLoop();
    }

    @FXML
    public void onShuffle(ActionEvent actionEvent) {
        toggleShuffle();
    }

    @FXML
    public void onNextSong(ActionEvent actionEvent) {
        // Need to first stop looping
        player.setCycleCount(1);
        naestaLag();
    }

    @FXML
    public void onPrevSong(ActionEvent actionEvent) {
        fyrraLag();
    }

    public void switchMode(ActionEvent actionEvent) {
        Scene scene = ViewSwitcher.getScene();
        lightModeOn = !lightModeOn;
        if (lightModeOn) {
            scene.getStylesheets().clear(); // Clear existing stylesheets
            scene.getStylesheets()
                    .add(PlayerApplication.class.getResource("/is/vidmot/css/lightMode.css").toExternalForm());
        }

        else {
            scene.getStylesheets().clear(); // Clear existing stylesheets
            scene.getStylesheets()
                    .add(PlayerApplication.class.getResource("/is/vidmot/css/darkMode.css").toExternalForm());
        }
    }

    public void openAddSong(ActionEvent actionEvent) {

        LagDialog dialog = new LagDialog();
        Optional<Lag> utkoma = dialog.showAndWait();

        utkoma.ifPresent(lag -> {
            lagalisti.addLagToList(lag);
        });

        if (this.player == null) {
            initialize();
        }
    }
}