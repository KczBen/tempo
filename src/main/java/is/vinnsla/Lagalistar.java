package is.vinnsla;
/******************************************************************************
 *  Nafn    :
 *  T-póstur:
 *  Viðmótsforritun 2024
 *
 *  Vinnsluklasi fyrir lagalista. Geymir 2 lagalista
 *****************************************************************************/

import java.io.IOException;
import java.util.List;

public class Lagalistar {

    // fastar
    public static final String SKRA_FANNST_EKKI = "skrá fannst ekki ";
    public static final String SUFFIX = ".dat";
    public static final String NAFN = "listi";
    private static int index; // index á núverandi lagalista
    private static final int SJALFKRAFA_LISTAR = 4;
    private static final int MAX_LAGALISTAR = 10;

    private static final Lagalisti lagalistar[] = new Lagalisti [MAX_LAGALISTAR]; // lagalistar

    /**
     * Setur upp lagalistana. Les innihald þeirra úr skrám sem heita listi1.dat listi2.dat o.s.frv.
     */
    public static void frumstilla() {
        for (int i = 0; i < SJALFKRAFA_LISTAR; i++) {
            lagalistar[i] = new Lagalisti();
            try {
                lagalistar[i].lesaLog(NAFN + (i + 1)+ SUFFIX);
                lagalistar[i].setNafnLagalistans("Lagalisti " + (i + 1));
                System.out.println (lagalistar[i]);
            } catch (IOException e) {
                System.out.println (SKRA_FANNST_EKKI + i);
                throw new RuntimeException(e);
            }
        }
        lagalistar[0].setImgPath("media/random.jpg");
        lagalistar[1].setImgPath("media/cover1.jpg");
        lagalistar[2].setImgPath("media/cover2.jpg");
        lagalistar[3].setImgPath("media/cover3.jpg");
    }

    // get og set aðferðir
    public static Lagalisti getNuverandi() {
        return lagalistar[index];
    }

    public static void setIndex(int nyttIndex) {
        if (nyttIndex >= 0 && nyttIndex < lagalistar.length) {
            index = nyttIndex;
        } else {
            throw new IllegalArgumentException(("Skakkt index " + nyttIndex));
        }
    }

    // get alla lagalista sem ArrayList
    public static Lagalisti[] getAllaLista() {
        return lagalistar;
    }

    public static Lagalisti getLagalistann(int index) {
        if (index >= 0 && index >= lagalistar.length) {
            return lagalistar[index];
        } else {
            throw new IllegalArgumentException("Skakkt index: " + index);
        }
    }

    // bæta lagalista í fylkið af lagalistum
    public static void baetaALista(Lagalisti lagalisti) {
        for (int i = 0; i < lagalistar.length; i++) {
            if (lagalistar[i] == null) {
                lagalistar[i] = lagalisti;
                if (lagalisti.getNafnLagalistans().isEmpty()) {
                    lagalisti.setNafnLagalistans("Nýr lagalisti " + (i + 1));
                }
                return;
            }
        }
        System.out.println("Ekkert pláss fyrir nýjan lagalista.");
    }

}
