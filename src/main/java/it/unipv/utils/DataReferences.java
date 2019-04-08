package it.unipv.utils;

import java.io.File;

/**
 *  Classe dove vengono impostate tutte le possibili stringhe utili al progetto
 */
public class DataReferences {

    public final static String PIANTINAFOLDERPATH = "data" + File.separator + "piantine" + File.separator;
    public final static String FILMFOLDERPATH = "data" + File.separator + "film" + File.separator +"lista film.csv";
    public final static String USERFOLDERPATH = "data" + File.separator + "utenti" + File.separator + "lista utenti.csv";
    public static final String INFOUSERFILE = "data" + File.separator + "utenti" + File.separator + "info.txt";
    public static final String PRICESFILEPATH = "data" + File.separator + "prezzi" + File.separator + "prezzi.csv";
    public static final String MOVIESCHEDULEFILEPATH = "data" + File.separator + "programmazione" + File.separator + "programmazione.csv";
    public static final int PAUSEAFTERMOVIE = 30; //min


    public final static String ADMINUSERNAME = "Admin";
    public final static String ADMINPASSWORD = "Admin";

    public final static int MYDRAGGABLESEATWIDTH = 30;
    public final static int MYDRAGGABLESEATHEIGTH = 25;
    public final static char[] ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
}
