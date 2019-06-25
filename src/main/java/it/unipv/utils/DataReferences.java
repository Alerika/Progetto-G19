package it.unipv.utils;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class DataReferences {

    public final static String INFOUSERDIR = "data" + File.separator + "utenti";
    public final static String INFOUSERFILE = "data" + File.separator + "utenti" + File.separator + "info.txt";
    public final static int PAUSEAFTERMOVIE = 30; //min

    public final static String ADMINUSERNAME = "Admin";
    public final static String ADMINPASSWORD = "Admin";

    public final static int MYDRAGGABLESEATWIDTH = 30;
    public final static int MYDRAGGABLESEATHEIGTH = 25;
    public final static char[] ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    public final static String DBNAME = "z6xOH9WKhI";
    public final static String DBPASS = "NSpPIYAmt3";

    public final static List<String> TIPS = Arrays.asList( "Benvenuto in Golden Movie Studio!"
                                                 , "Hai già visto le nuove uscite?"
                                                 , "Non riesci ad accedere? Prova a resettare la password!"
                                                 , "Vorresti contattarci? Scopri come nella pagina delle informazioni!"
                                                 , "Ricordati di non perdere il codice utente: potrebbe servirti per resettare la password!"
                                                 , "Ricordati che devi aver effettuato l'accesso per poter prenotare una data!");
}
