package it.unipv.gui.manager;

import it.unipv.conversion.FileToHallList;
import it.unipv.gui.common.HallDisplayer;
import it.unipv.utils.ApplicationUtils;
import it.unipv.utils.StringReferences;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 *  Questa è la parte principale del gestore del cinema, dove può
 *     vedere la lista delle sale create, modificarle e aggiungerle.
 *  Cliccando sul nome della sala può visualizzarla (e non modificarla)
 *  Cliccando su modifica può modificare la piantina della sala
 *  Dal menu può aggiungere una nuova sala
 */
public class ManagerUI extends JFrame {

    private JScrollPane hallListPanel;
    private List<String> hallNames = new ArrayList<>();
    private JTable hallTable;
    private DefaultTableModel dtm;

    public ManagerUI() {
        initHallNamesList();
        initHallListPanel();
        initMenuBar();
        initFrame();
    }

    // Inizializzo la lista delle sale tramite la classe "FileToHallList"
    private void initHallNamesList() {
        hallNames = FileToHallList.getHallList();
    }

    /*
    * Inizializzazione del menu:
    *    Aggiungi -> Nuova sala  richiama la classe "HallEditor" per la creazione di una nuova piantina
    *       Al click viene chiesto il nome della nuova sala: se viene lasciato vuoto oppure vengono inseriti
    *       solo spazi, allora il nome viene ritenuto non valido.
    */
    private void initMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu addMenu = new JMenu("Aggiungi");
        menuBar.add(addMenu);

        JMenuItem editorItem = new JMenuItem("Nuova sala");
        addMenu.add(editorItem);
        editorItem.addActionListener( e -> {
            String nomeSala = JOptionPane.showInputDialog(this, "Inserisci il nome della sala");
            if(nomeSala.equalsIgnoreCase("") || nomeSala.trim().length()==0) {
                JOptionPane.showMessageDialog(this, "Devi inserire un nome!");
            } else if(!nomeSala.equalsIgnoreCase("")) {
                new HallEditor(nomeSala, this, false);
            }
        });
    }

    /* Inizializzazione del pannello con all'interno la tabella che a sua volta contiene la lista delle sale
          Ho utilizzato un JScrollPane perché se si hanno tante sale poi si potrebbe aver problemi.
          (anche se non credo che un cinema possa avere millemila sale)
    */
    private void initHallListPanel() {
        hallListPanel = new JScrollPane();
        Object columnNames[] = getColumnNames();
        initHallTable(getRowData(), columnNames);
        hallListPanel.setViewportView(hallTable);
    }

    /* Inizializzazione della tabella contenente la lista delle sale
     *    Sono due colonne:
     *       La prima colonna contiene il nome della sala che, se cliccato, apre la visualizzazione della piantina
     *       La seconda colonna contiene il tasto "modifica" che, se cliccato, apre l'editor della piantina.
     *          Tuttavia, questa funzione è ancora da implementare.
    */
    private void initHallTable(Object[][] rowData, Object[] columnNames) {
        dtm = new DefaultTableModel(rowData, columnNames) {
            public boolean isCellEditable(int row, int column)     {
                return false;
            }
        };
        hallTable = new JTable();
        hallTable.getTableHeader().setUI(null);
        hallTable.setModel(dtm);
        ManagerUI manager = this;
        hallTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = hallTable.rowAtPoint(e.getPoint());
                int col = hallTable.columnAtPoint(e.getPoint());

                if(col == 0) {
                    new HallDisplayer(hallNames.get(row));
                }

                if(col == 1) {
                    new HallEditor(hallNames.get(row), manager, true);
                }

                if(col == 2) {
                    int reply = JOptionPane.showConfirmDialog(manager, "Vuoi davvero eliminare la piantina " + hallNames.get(row) + " ?");
                    if(reply == JOptionPane.YES_OPTION) {
                        ApplicationUtils.removeFileFromPath(StringReferences.PIANTINAFOLDERPATH+hallNames.get(row)+".csv");
                        hallNames.remove(row);
                        dtm.setDataVector(getRowData(), getColumnNames());
                        dtm.fireTableDataChanged();
                        manager.repaint();
                    }
                }

            }
        });
        hallTable.setOpaque(false);
        ((DefaultTableCellRenderer)hallTable.getDefaultRenderer(Object.class)).setOpaque(false);
        hallListPanel.setOpaque(false);
        hallListPanel.getViewport().setOpaque(false);
        hallListPanel.setColumnHeader(null);
    }

    /* Questo metodo viene richiamato dall'editor al momento del salvataggio della nuova piantina
     *   per triggerare l'aggiornamento della tabella.
    */
    void repaintHallTable() {
        initHallNamesList();
        dtm.setDataVector(getRowData(), getColumnNames());
        dtm.fireTableDataChanged();
        repaint();
    }

    //Metodo utilizzato per popolare le righe della tabella
    private Object[][] getRowData() {
        Object rowData[][] = new Object[hallNames.size()][3];
        String hall;
        for(int i=0; i<hallNames.size(); i++) {
            hall = hallNames.get(i);
            rowData[i][0] = ""+hall;
            rowData[i][1] = "Modifica";
            rowData[i][2] = "Rimuovi";
        }
        return rowData;
    }

    //Metodo utilizzato per dare un nome alle colonne della tabella, anche se le ho nascoste
    private Object[] getColumnNames() { return new Object[]{"","",""}; }

    //Inizializzazione dei parametri base del frame
    private void initFrame() {
        setTitle("Sezione Manager");
        add(hallListPanel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new GridLayout(1,1));
        setSize(700,500);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
