package it.unipv.gui.common;

import it.unipv.conversion.CSVToDraggableSeats;
import it.unipv.utils.StringReferences;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;


/**
 * Questa classe viene utilizzata, per ora, per visualizzare SOLAMENTE
 *   l'attuale piantina salvata su file. Più avanti sarà utilizzata
 *   dalla parte dell'utente per poter prenotare i posti e visualizzare
 *   quali sono occupati o meno
 */
public class HallViewer extends JFrame {
    private String nomeSala;
    private JPanel undraggableSeatsPanel;
    private List<MyDraggableSeat> draggableSeats = new ArrayList<>();

    public HallViewer(String nomeSala) {
        this.nomeSala = nomeSala;
        initDraggableSeatsList();
        initUndraggableSeatsPanel();
        initFrame();
    }

    /* Vado a leggere dal file (che riconosco tramite il parametro nomeSala)
     *   le informazioni riguardanti i posti a sedere e li salvo in una lista
     */
    private void initDraggableSeatsList() {
        draggableSeats = CSVToDraggableSeats.getMyDraggableSeatListFromCSV(StringReferences.PIANTINAFOLDERPATH+nomeSala+".csv");
    }

    /* Inizializzo il JPanel che all'interno ospiterà i posti a sedere;
     *   i posti a sedere li aggiungo scorrendo quelli presenti nella lista
     *   popolata precedentemente in "initDraggableSeatsList"
     */
    private void initUndraggableSeatsPanel() {
        undraggableSeatsPanel = new JPanel();
        undraggableSeatsPanel.setMinimumSize(new Dimension(300, 150));
        undraggableSeatsPanel.setLayout(null);

        for(MyDraggableSeat mds : draggableSeats) {
            undraggableSeatsPanel.add(mds);
            mds.setIsItDraggable(false);
            repaint();
        }
    }

    // Imposto tutte le informazioni riguardanti il JFrame
    private void initFrame() {
        setTitle("Piantina " + nomeSala);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                setVisible(false);
            }
        });
        setLayout(new GridLayout(1,1));
        add(undraggableSeatsPanel);
        setSize(700,500);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
