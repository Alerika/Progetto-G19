package it.unipv.main;

import it.unipv.gui.common.MainCinemaUI;

import java.awt.*;

/**
 *  Progetto "Prenotazione Cinema" (G19) del corso di Ingegneria del Software 2019, UNIPV
 */
public class Core {

    public static void main(String[] args) {
        EventQueue.invokeLater(MainCinemaUI::new);
    }

}
