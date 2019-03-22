package it.unipv.gui.manager;

import it.unipv.conversion.CSVToDraggableSeats;
import it.unipv.conversion.DraggableSeatsToCSV;
import it.unipv.gui.common.MyDraggableSeat;
import it.unipv.utils.StringReferences;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;


/**
 *  Questa classe viene utilizzata principalmente per creare/modificare la piantina di una sala.
 */
class HallEditor extends JFrame{
    private List<String> createdSeatsName = new ArrayList<>();
    private JPanel draggableSeatsPanel;
    private List<MyDraggableSeat> draggableSeatsList = new ArrayList<>();
    private String nomeSala;
    private ManagerUI summoner;

    /**
     * @param nomeSala -> viene utilizzato per il nome del file e per il titolo del frame;
     * @param summoner -> viene utilizzato per triggerare l'evento di update della tabella
     * @param wasItAlreadyCreated -> viene utilizzato per stabilire se si è in modalità nuova sala o modifica sala esistente
     */
    HallEditor( String nomeSala
              , ManagerUI summoner
              , boolean wasItAlreadyCreated) {
        this.nomeSala = nomeSala;
        this.summoner = summoner;

        initMenuBar();
        initDraggableSeatsPanel();
        initFrame();

        if(wasItAlreadyCreated) {
            initDraggableSeatsList();
        }

    }

    /* Se la sala è già stata creata vuol dire che esistono dei posti in precedenza,
     *    quindi vado a leggerli da file e a disegnarli di conseguenza.
    */
    private void initDraggableSeatsList() {
        draggableSeatsList = CSVToDraggableSeats.getMyDraggableSeatListFromCSV(StringReferences.PIANTINAFOLDERPATH+nomeSala+".csv");
        for(MyDraggableSeat mds : draggableSeatsList) {
            draggableSeatsPanel.add(mds);
            createdSeatsName.add(mds.getText());
            mds.setIsItDraggable(true);
            repaint();
        }
    }

    /* Creazione del menuBar
     *   File -> Salva, permette di salvare all'interno del CSV le posizioni dei posti a sedere creati
     *      il nome del file viene impostato grazie a "nomeSala".
     *   Modifica -> Aggiungi, permette di aggiungere un nuovo posto e di trascinarlo dove si vuole nella piantina
    */
    private void initMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);

        JMenuItem saveItem = new JMenuItem("Salva");
        fileMenu.add(saveItem);
        saveItem.addActionListener( e -> {
            DraggableSeatsToCSV.createCSVFromDraggableSeatsList( draggableSeatsList
                                                               , StringReferences.PIANTINAFOLDERPATH + nomeSala +".csv"
                                                               , false);
            JOptionPane.showMessageDialog(this, "Piantina salvata con successo!");
            summoner.repaintHallTable();
        });

        JMenu editMenu = new JMenu("Modifica");
        menuBar.add(editMenu);

        JMenuItem insertNewSeatItem = new JMenuItem("Aggiungi");
        editMenu.add(insertNewSeatItem);
        insertNewSeatItem.addActionListener(e -> {
            MyDraggableSeat mc = createNewDraggableSeat();
            draggableSeatsPanel.add(mc);
            draggableSeatsList.add(mc);
            repaint();
        });
    }

    // Inizializzazione del pannnello che rappresenterà la piantina
    private void initDraggableSeatsPanel() {
        draggableSeatsPanel = new JPanel();
        draggableSeatsPanel.setMinimumSize(new Dimension(300, 150));
        draggableSeatsPanel.setLayout(null);
    }

    /* Quando si crea un nuovo posto a sedere viene chiesto il nome:
     *    se il nome è già stato usato, non viene impostato, oppure è una stringa
     *    vuota o di soli spazi, viene ritenuto non opportuno.
     * Ad ogni posto a sedere viene impostato un menu apribile con un
     *    click destro del mouse; questo menu contiene la possibilità di eliminare
     *    il posto a sedere selezionato.
    */
    private MyDraggableSeat createNewDraggableSeat() {
        MyDraggableSeat mc = new MyDraggableSeat(0,0);
        mc.setIsItDraggable(true);

        boolean killLoop = false;
        while(!killLoop) {
            boolean alreadyThere = false;
            boolean isAGoodName = false;

            String name = JOptionPane.showInputDialog(this, "Inserisci il nome del posto");
            if(!name.equalsIgnoreCase("") || name.trim().length()!=0) {
                isAGoodName=true;
            }
            for(String s : createdSeatsName) {
                if(name.trim().toLowerCase().equalsIgnoreCase(s.trim().toLowerCase())) {
                    alreadyThere = true;
                }
            }
            if(isAGoodName) {
                if(!alreadyThere) {
                    mc.setText(name);
                    mc.setComponentPopupMenu(initJPopupMenu(name));
                    createdSeatsName.add(name);
                    killLoop = true;
                } else {
                    JOptionPane.showMessageDialog(this, "Nome già esistente!");
                    killLoop = false;
                }
            } else {
                JOptionPane.showMessageDialog(this, "Inserisci un nome!");
                killLoop = false;
            }

        }
        return mc;
    }

    // Inizializzazione menu richiamabile con un click destro sul posto a sedere
    private JPopupMenu initJPopupMenu(String name) {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem deleteItem = new JMenuItem("Rimuovi Posto");
        deleteItem.addActionListener(e -> rimuoviPosto(name));
        popupMenu.add(deleteItem);
        return popupMenu;
    }

    // La parte che effettivamente rimuove il posto a sedere
    private void rimuoviPosto(String name) {
        boolean status = false;
        MyDraggableSeat toRemove = null;
        for(MyDraggableSeat djl : draggableSeatsList) {
            if(name.trim().equalsIgnoreCase(djl.getText().trim())) {
                status = true;
                toRemove = djl;
                break;
            }
        }
        if(status) {
            createdSeatsName.remove(name);
            draggableSeatsList.remove(toRemove);
            draggableSeatsPanel.remove(toRemove);
            repaint();
        }
    }

    // Inizializzazione dei parametri di base del frame
    private void initFrame() {
        setTitle("Editor " + nomeSala);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(1,1));
        add(draggableSeatsPanel);
        setSize(700,500);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}