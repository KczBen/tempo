package is.vinnsla;
/******************************************************************************
 *  Nafn    :
 *  T-póstur:
 *  Viðmótsforritun 2024
 *
 *  Vinnsluklasi fyrir lagalista. Geymir 2 lagalista
 *****************************************************************************/

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Lagalistar {

    // fastar
    public static final String SKRA_FANNST_EKKI = "skrá fannst ekki ";
    public static final String SUFFIX = ".dat";
    public static final String NAFN = "listi";
    private static int index; // index á núverandi lagalista

    private static final Lagalisti[] listar = new Lagalisti [2]; // lagalistar
    private static final List<Lagalisti> lagalistar = new ArrayList<>(); // listi notandans af lagalistum

    /**
     * Setur upp lagalistana. Les innihald þeirra úr skrám sem heita listi1.dat listi2.dat o.s.frv.
     */
    public static void frumstilla() {
        for (int i = 0; i < listar.length; i++) {
            listar[i] = new Lagalisti ();
            try {
                listar[i].lesaLog(NAFN +(i+1)+ SUFFIX);
                System.out.println (listar[i]);
            } catch (IOException e) {
                System.out.println (SKRA_FANNST_EKKI +i);
                throw new RuntimeException(e);
            }
        }
    }

    // get og set aðferðir
    public static Lagalisti getNuverandi() {
        return listar[index];
    }

    public static void setIndex(int index) {
        Lagalistar.index = index;
    }

    // get alla lagalista sem ArrayList
    public static List<Lagalisti> getLagalista() {
        return lagalistar;
    }

    // bæta lagalista í fylkið af lagalistum
    public static void baetaALista(Lagalisti lagalisti) {
        lagalistar.add(lagalisti);
    }

}
