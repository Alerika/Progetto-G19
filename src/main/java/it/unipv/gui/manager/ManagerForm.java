/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unipv.gui.manager;

import it.unipv.conversion.CSVToMovieList;
import it.unipv.conversion.FileToHallList;
import it.unipv.conversion.MovieToCSV;
import it.unipv.gui.common.HallViewer;
import it.unipv.gui.common.MainCinemaUI;
import it.unipv.gui.common.Movie;
import it.unipv.gui.common.MovieViewer;
import it.unipv.utils.ApplicationUtils;
import it.unipv.utils.StringReferences;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class ManagerForm extends javax.swing.JFrame {

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainContentPanel = new javax.swing.JPanel();
        hallTableContainer = new javax.swing.JScrollPane();
        movieTableContainer = new javax.swing.JScrollPane();
        menuBar = new javax.swing.JMenuBar();
        addMenu = new javax.swing.JMenu();
        newHallItem = new javax.swing.JMenuItem();
        newMovieItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout mainContentPanelLayout = new javax.swing.GroupLayout(mainContentPanel);
        mainContentPanel.setLayout(mainContentPanelLayout);
        mainContentPanelLayout.setHorizontalGroup(
            mainContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainContentPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(hallTableContainer, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(movieTableContainer, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
                .addContainerGap())
        );
        mainContentPanelLayout.setVerticalGroup(
            mainContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(hallTableContainer, javax.swing.GroupLayout.DEFAULT_SIZE, 460, Short.MAX_VALUE)
            .addComponent(movieTableContainer)
        );

        addMenu.setText("Nuovo");

        newHallItem.setText("Sala");
        addMenu.add(newHallItem);

        newMovieItem.setText("Film");
        addMenu.add(newMovieItem);

        menuBar.add(addMenu);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mainContentPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mainContentPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu addMenu;
    private javax.swing.JScrollPane hallTableContainer;
    private javax.swing.JPanel mainContentPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JScrollPane movieTableContainer;
    private javax.swing.JMenuItem newHallItem;
    private javax.swing.JMenuItem newMovieItem;
    // End of variables declaration//GEN-END:variables

    private MainCinemaUI mainCinemaUI;
    private List<String> hallNames = new ArrayList<>();
    private JTable hallTable;
    private DefaultTableModel hallTableDTM;
    private JTable movieTable;
    private DefaultTableModel movieTableDTM;
    private List<Movie> movies = new ArrayList<>();

    public ManagerForm(MainCinemaUI mainCinemaUI) {
        this.mainCinemaUI = mainCinemaUI;
        initComponents();
        addListenerToMenuItem();
        addHallTable();
        addMovieTable();
        initFrame();
    }

    private void initHallNamesList() {
        hallNames = FileToHallList.getHallList();
    }
    private void initMovieList() {
        movies = CSVToMovieList.getMovieListFromCSV(StringReferences.FILMFOLDERPATH);
    }

    private void initHallTable(Object[][] rowData, Object[] columnNames) {
        hallTableDTM = initDTM(rowData, columnNames);
        hallTable = initTable(hallTableDTM);
        hallTable.setComponentPopupMenu(initJPopupMenuForHallTable(hallTable));
        hallTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new HallViewer(hallNames.get(hallTable.rowAtPoint(e.getPoint())));

            }
        });
    }

    private void initMovieTable(Object[][] rowData, Object[] columnNames) {
        movieTableDTM = initDTM(rowData, columnNames);
        movieTable = initTable(movieTableDTM);
        movieTable.setComponentPopupMenu(initJPopupMenuForMovieTable(movieTable));
        movieTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new MovieViewer(movies.get(movieTable.rowAtPoint(e.getPoint())));
            }
        });
    }

    private DefaultTableModel initDTM(Object[][] rowData, Object[] columnNames) {
        return new DefaultTableModel(rowData, columnNames) {
            public boolean isCellEditable(int row, int column)     {
                return false;
            }
        };
    }

    private JTable initTable(DefaultTableModel dtm) {
        JTable res = new JTable();
        res.getTableHeader().setUI(null);
        res.setModel(dtm);

        res.setOpaque(false);
        ((DefaultTableCellRenderer)res.getDefaultRenderer(Object.class)).setOpaque(false);
        return res;
    }

    private int selectedHallTableRow;
    private JPopupMenu initJPopupMenuForHallTable(JTable hallTable) {
        JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem modifyItem = new JMenuItem("Modifica");
        modifyItem.addActionListener(e -> new HallEditor(hallNames.get(selectedHallTableRow), this, true));
        popupMenu.add(modifyItem);

        JMenuItem deleteItem = new JMenuItem("Rimuovi");
        deleteItem.addActionListener(e -> {
            int reply = JOptionPane.showConfirmDialog(this, "Vuoi davvero eliminare la piantina " + hallNames.get(selectedHallTableRow) + " ?");
            if (reply == JOptionPane.YES_OPTION) {
                ApplicationUtils.removeFileFromPath(StringReferences.PIANTINAFOLDERPATH + hallNames.get(selectedHallTableRow) + ".csv");
                hallNames.remove(selectedHallTableRow);
                repaintAndUpdateTable(hallTableDTM, getRowDataForHallTable(), getColumnNames());
            }
        });
        popupMenu.add(deleteItem);
        popupMenu.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                SwingUtilities.invokeLater(() -> {
                    selectedHallTableRow = hallTable.rowAtPoint(SwingUtilities.convertPoint(popupMenu, new Point(0, 0), hallTable));
                    if (selectedHallTableRow > -1) {
                        hallTable.setRowSelectionInterval(selectedHallTableRow, selectedHallTableRow);
                    }
                });
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
            }
        });
        return popupMenu;
    }

    private int selectedMovieTableRow;
    private JPopupMenu initJPopupMenuForMovieTable(JTable movieTable) {
        JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem modifyItem = new JMenuItem("Modifica");
        modifyItem.addActionListener(e -> new MovieEditor(movies.get(selectedMovieTableRow), this));
        popupMenu.add(modifyItem);

        JMenuItem deleteItem = new JMenuItem("Rimuovi");
        deleteItem.addActionListener(e -> {
            int reply =
                    JOptionPane.showConfirmDialog(null
                                                , "Sei sicuro di voler eliminare dalla lista il film " + movies.get(selectedMovieTableRow).getTitolo() +"?");
            if(reply == JOptionPane.YES_OPTION) {
                movies.remove(selectedMovieTableRow);
                MovieToCSV.createCSVFromMovieList(movies, StringReferences.FILMFOLDERPATH, false);
                movieTableDTM.removeRow(selectedMovieTableRow);
                mainCinemaUI.triggerModificationToFilmList();
                repaintAndUpdateTable(movieTableDTM, getRowDataForMovieTable(), getColumnNames());
            }
        });
        popupMenu.add(deleteItem);
        popupMenu.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                SwingUtilities.invokeLater(() -> {
                    selectedMovieTableRow = movieTable.rowAtPoint(SwingUtilities.convertPoint(popupMenu, new Point(0, 0), movieTable));
                    if (selectedMovieTableRow > -1) {
                        movieTable.setRowSelectionInterval(selectedMovieTableRow, selectedMovieTableRow);
                    }
                });
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {

            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {

            }
        });
        return popupMenu;
    }

    private Object[][] getRowDataForHallTable() {
        Object rowData[][] = new Object[hallNames.size()][1];
        for(int i=0; i<hallNames.size(); i++) {
            rowData[i][0] = ""+hallNames.get(i);
        }
        return rowData;
    }

    private Object[][] getRowDataForMovieTable() {
        Object rowData[][] = new Object[movies.size()][1];
        for(int i = 0; i< movies.size(); i++) {
            rowData[i][0] = movies.get(i).getTitolo();
        }
        return rowData;
    }

    private Object[] getColumnNames() { return new Object[]{""}; }

    void triggerModificationToHallList() {
        initHallNamesList();
        repaintAndUpdateTable(hallTableDTM, getRowDataForHallTable(), getColumnNames());
    }

    void triggerNewMovieEvent() {
        mainCinemaUI.triggerModificationToFilmList();
        initMovieList();
        repaintAndUpdateTable(movieTableDTM, getRowDataForMovieTable(), getColumnNames());
    }

    void triggerOverwriteMovieEvent(Movie movie) {
        Movie toRemove = null;
        for(Movie m : movies) {
            if(m.getCodice().trim().equalsIgnoreCase(movie.getCodice().trim())) {
                toRemove = m;
                break;
            }
        }
        if(toRemove!=null) {
            movies.remove(toRemove);
            movies.add(movie);
            MovieToCSV.createCSVFromMovieList(movies, StringReferences.FILMFOLDERPATH, false);
            mainCinemaUI.triggerModificationToFilmList();
            repaintAndUpdateTable(movieTableDTM, getRowDataForMovieTable(), getColumnNames());
        }
    }

    private void repaintAndUpdateTable(DefaultTableModel dtm, Object[][] rowData, Object[] columnNames) {
        dtm.setDataVector(rowData, columnNames);
        dtm.fireTableDataChanged();
    }

    private void addMovieTable() {
        initMovieList();
        initMovieTable(getRowDataForMovieTable(), getColumnNames());
        movieTableContainer.setViewportView(movieTable);
    }

    private void addHallTable() {
        initHallNamesList();
        initHallTable(getRowDataForHallTable(), getColumnNames());
        hallTableContainer.setViewportView(hallTable);
        hallTableContainer.setOpaque(false);
        hallTableContainer.getViewport().setOpaque(false);
        hallTableContainer.setColumnHeader(null);
    }

    private void addListenerToMenuItem() {
        newHallItem.addActionListener(e->{
            String nomeSala = JOptionPane.showInputDialog(this, "Inserisci il nome della sala");
            if(nomeSala.equalsIgnoreCase("") || nomeSala.trim().length()==0) {
                JOptionPane.showMessageDialog(this, "Devi inserire un nome!");
            } else if(!nomeSala.equalsIgnoreCase("")) {
                new HallEditor(nomeSala, this, false);
            }
        });

        newMovieItem.addActionListener(e -> new MovieEditor(this));
    }

    private void initFrame() {
        setTitle("Sezione manager");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
