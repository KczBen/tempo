package is.vinnsla;

import java.io.IOException;

/******************************************************************************
 * Nafn :
 * T-póstur:
 * Viðmótsforritun 2024
 *
 * Vinnsluklasi fyrir lagalista. Geymir 10 lagalista, af þeim eru 4 sjálfgefnir
 * lagalistar sem má ekki eyða.
 *****************************************************************************/
public class Lagalistar {
    private static int index; // index á núverandi lagalista

    public static final String SKRA_FANNST_EKKI = "skrá fannst ekki ";
    public static final String SUFFIX = ".dat";
    public static final String NAFN = "listi";
    private static final int SJALFKRAFA_LISTAR = 4;
    private static final int MAX_LAGALISTAR = 10;

    private static final Lagalisti lagalistar[] = new Lagalisti[MAX_LAGALISTAR]; // lagalistar

    /**
     * Setur upp lagalistana. Les innihald þeirra úr skrám sem heita listi1.dat
     * listi2.dat o.s.frv.
     */
    public static void frumstilla() {
        for (int i = 0; i < SJALFKRAFA_LISTAR; i++) {
            lagalistar[i] = new Lagalisti();
            try {
                lagalistar[i].lesaLog(NAFN + (i + 1) + SUFFIX);
                lagalistar[i].setNafnLagalistans("Lagalisti " + (i + 1));
                System.out.println(lagalistar[i]);
            } catch (IOException e) {
                System.out.println(SKRA_FANNST_EKKI + i);
                throw new RuntimeException(e);
            }
        }
        lagalistar[0].setImgPath("media/random.jpg");
        lagalistar[1].setImgPath("media/cover1.jpg");
        lagalistar[2].setImgPath("media/cover2.jpg");
        lagalistar[3].setImgPath("media/cover3.jpg");
    }

    /**
     * Bætir lagalista í fylkið af lagalistum ef það er pláss.
     *
     * @param lagalisti lagalisti að bæta við
     * @return true ef hægt er að búa til nýjan lagalista, annars false
     */
    public static boolean baetaALista(Lagalisti lagalisti) {
        for (int i = 0; i < lagalistar.length; i++) {
            if (lagalistar[i] == null) {
                lagalistar[i] = lagalisti;
                if (lagalisti.getNafnLagalistans().isEmpty()) {
                    lagalisti.setNafnLagalistans("Nýr lagalisti " + (i + 1));
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Eyddir völdum lagalista.
     *
     * @param index tala sem auðkennir lagalista
     */
    public static void eydaLagalista(int index) {
        if (index >= 4 && index < lagalistar.length && lagalistar[index] != null) {
            lagalistar[index] = null;

            for (int i = index; i < lagalistar.length - 1; i++) {
                lagalistar[i] = lagalistar[i + 1];
            }
            lagalistar[lagalistar.length - 1] = null;
        }
    }

    // Get og Set aðferðir

    /**
     * Skila núverandi lagalistanum.
     *
     * @return núverandi lagalisti
     */
    public static Lagalisti getNuverandi() {
        return lagalistar[index];
    }

    /**
     * Setur upp nýtt index fyrir lagalistann.
     *
     * @param nyttIndex tala sem auðkennir lagalista
     */
    public static void setIndex(int nyttIndex) {
        if (nyttIndex >= 0 && nyttIndex < lagalistar.length) {
            index = nyttIndex;
        } else {
            throw new IllegalArgumentException(("Skakkt index " + nyttIndex));
        }
    }

    /**
     * Skilar öllum lagalistunum sem fylki.
     *
     * @return fylki af lagalistunum
     */
    public static Lagalisti[] getAllaLista() {
        return lagalistar;
    }

    /**
     * Skilar ákveðnum lagalista.
     *
     * @param index tala sem auðkennir lagalista
     * @return
     */
    public static Lagalisti getLagalistann(int index) {
        if (index >= 0 && index < lagalistar.length) {
            return lagalistar[index];
        } else {
            throw new IllegalArgumentException("Skakkt index: " + index);
        }
    }
}
