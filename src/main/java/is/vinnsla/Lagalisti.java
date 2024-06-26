package is.vinnsla;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.Scanner;

/******************************************************************************
 * Nafn :
 * T-póstur:
 * Viðmótsforritun 2024
 *
 * Vinnsluklasi fyrir lagalista. Lagalisti getur haft lista af Lag.
 * Heldur utan um núverandi lag.
 *
 *****************************************************************************/
public class Lagalisti {
    protected ObservableList<Lag> listi = FXCollections.observableArrayList(); // lagalistinn
    private int index = 0; // núverandi lag
    private String imgPath; // staðsetning myndarinnar fyrir lagalistann
    private String nafnLagalistans; // setja nafn lagalistans

    /**
     * Les skrá með eiginleikum laga og búa til lög.
     *
     * @param nafnASkra nafn á skrá
     * @throws IOException
     */
    public void lesaLog(String nafnASkra) throws IOException {
        System.out.println(System.getProperty("user.dir"));
        File file = new File(System.getProperty("user.dir") + "/src/main/resources/is/vinnsla/" + nafnASkra);
        Scanner scanner = new Scanner(file, StandardCharsets.UTF_8);
        String[] lina;
        try {
            // lesa gögn ur skrá og búa til Lag hlut
            while (scanner.hasNextLine()) {

                lina = scanner.nextLine().split(" ");
                listi.add(new Lag(lina[0], lina[3], lina[1], Integer.parseInt(lina[2])));
            }
            scanner.close();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    /**
     * Index á næsta lag á lagalista.
     */
    public void naesti() {
        index = ++index % listi.size();
    }

    public void random() {
        Random rng = new Random();
        index = rng.nextInt(0, listi.size());
    }

    /**
     * Index á fyrra lag á lagalista (Tobba).
     */
    public void fyrri() {
        if (index == 0) {
            index = listi.size() - 1; // fer í aftasta lag á lista svo lendum ekki out of bounds
        } else {
            index = --index % listi.size();
        }
    }

    // Get og Set aðferðir

    public ObservableList<Lag> getListi() {
        return listi;
    }

    public void setIndex(int selectedIndex) {
        index = selectedIndex;
    }

    public int getIndex() {
        return index;
    }

    public void setNafnLagalistans(String nafnLagalistans) {
        this.nafnLagalistans = nafnLagalistans;
    }

    public String getNafnLagalistans() {
        return nafnLagalistans;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void addLagToList(Lag lag) {
        listi.add(lag);
    }

}
