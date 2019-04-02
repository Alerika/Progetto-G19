/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unipv.gui.common;

import it.unipv.conversion.CSVToMovieList;
import it.unipv.gui.login.LoginForm;
import it.unipv.utils.DataReferences;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class MainCinemaUI extends javax.swing.JFrame {

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        menuPanel = new javax.swing.JPanel();
        menuLabel = new javax.swing.JLabel();
        cinemaNameLabel = new javax.swing.JLabel();
        loginLabel = new javax.swing.JLabel();
        movieListPanel = new javax.swing.JScrollPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        menuPanel.setBackground(new java.awt.Color(0, 0, 0));
        menuPanel.setForeground(new java.awt.Color(255, 255, 255));
        menuPanel.setLayout(new java.awt.GridLayout(1, 0));

        menuLabel.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        menuLabel.setForeground(new java.awt.Color(255, 255, 255));
        menuLabel.setText("  ☰");
        menuLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        menuPanel.add(menuLabel);

        cinemaNameLabel.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        cinemaNameLabel.setForeground(new java.awt.Color(255, 255, 255));
        cinemaNameLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cinemaNameLabel.setText("UNIPV Cinema    ");
        menuPanel.add(cinemaNameLabel);

        loginLabel.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        loginLabel.setForeground(new java.awt.Color(255, 255, 255));
        loginLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        loginLabel.setText("Accedi/Registrati     ");
        loginLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        menuPanel.add(loginLabel);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(movieListPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 602, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(menuPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 602, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(88, 88, 88)
                .addComponent(movieListPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 329, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addComponent(menuPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 328, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel cinemaNameLabel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel loginLabel;
    private javax.swing.JLabel menuLabel;
    private javax.swing.JPanel menuPanel;
    private javax.swing.JScrollPane movieListPanel;
    // End of variables declaration//GEN-END:variables

    private boolean alreadyLogged = false;
    private JTable filmListTable;
    private DefaultTableModel dtm;
    private List<Movie> movies;
    private LoginForm loginForm;

    public MainCinemaUI() {
        initComponents();
        loginForm = new LoginForm(this);
        loginForm.setSecretMode(false);
        initMouseListenerForLabels();
        initFilmList();
        initFilmListPanel(getRowData(), getColumnNames());
        initFrame();
    }

    private void initMouseListenerForLabels() {
        loginLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(!alreadyLogged) {
                    loginForm.setSecretMode(false);
                    loginForm.setVisible(true);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                loginLabel.setForeground(Color.YELLOW);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                loginLabel.setForeground(Color.WHITE);
            }
        });

        cinemaNameLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount()==10) {
                    loginForm.setSecretMode(true);
                    loginForm.setVisible(true);
                }
            }
        });

        menuLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showPopup(e, initJPopupMenu());
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                menuLabel.setForeground(Color.YELLOW);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                menuLabel.setForeground(Color.WHITE);
            }
        });
    }

    private JPopupMenu initJPopupMenu() {
        JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem hallViewerItem = new JMenuItem("Lista sale");
        hallViewerItem.addActionListener(e -> JOptionPane.showMessageDialog(this, "Devo essere ancora implementato! :D"));
        popupMenu.add(hallViewerItem);

        if(alreadyLogged) {
            JMenuItem reservedAreaItem = new JMenuItem("Area riservata");
            reservedAreaItem.addActionListener(e -> JOptionPane.showMessageDialog(this, "Devo essere ancora implementato! :D"));
            popupMenu.add(reservedAreaItem);

            JMenuItem logoutItem = new JMenuItem("Logout");
            logoutItem.addActionListener(e -> {
                loginForm.triggerLogoutEvent();
                loginLabel.setText("Accedi/Registrati    ");
                alreadyLogged = false;
            });
            popupMenu.add(logoutItem);
        }

        JMenuItem infoItem = new JMenuItem("Info e orari");
        infoItem.addActionListener(e -> JOptionPane.showMessageDialog(this, "Devo essere ancora implementato! :D"));
        popupMenu.add(infoItem);
        return popupMenu;
    }

    /*
     * Solitamente JPopupMenu viene utilizzato con un click destro del mouse;
     *    visto che però in questo caso non ha senso, lo disegno appena sotto al
     *    quadratino del menu quando clicco con il sinistro del mouse.
     */
    private void showPopup(MouseEvent ae, JPopupMenu menu) {
        Component b=(Component)ae.getSource();
        Point p=menuLabel.getLocationOnScreen();
        menu.show(this,0,0);
        menu.setLocation(p.x+5,p.y+b.getHeight()-30);
    }

    public void triggerLoginEvent(String username) {
        loginLabel.setText(username + "    ");
        alreadyLogged = true;
        repaint();
    }

    private void initFrame() {
        setTitle("UNIPV Cinema");
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    /**
     * Metodo per instanziare la lista dei film dal file .csv
     */
    private void initFilmList() {
        movies = CSVToMovieList.getMovieListFromCSV(DataReferences.FILMFOLDERPATH);
    }

    /**
     * Metodo utilizzato per creare le righe della tabella
     * @return -> una matrice di Object che rappresenta le righe della tabella
     */
    private Object[][] getRowData() {
        Object rowData[][] = new Object[movies.size()][3];

        for(int i = 0; i< movies.size(); i++) {
            rowData[i][0] = movies.get(i).getTitolo();
        }
        return rowData;
    }

    /**
     * Metodo utilizzato per dare un nome alle colonne della tabella, anche se le nascondo
     * @return -> un array di Object che rappresenta il nome delle colonne della tabella
     */
    private Object[] getColumnNames() { return new Object[]{""}; }

    /**
     * Creazione della tabella che mostrerà la lista dei film
     * @param rowData -> matrice che rappresenta le righe della tabella
     * @param columnNames -> array che rappresenta i nomi delle colonne della tabella
     */
    private void initFilmListTable(Object[][] rowData, Object[] columnNames) {
        dtm = new DefaultTableModel(rowData, columnNames) {
            public boolean isCellEditable(int row, int column)     {
                return false;
            }
        };
        filmListTable = new JTable();
        filmListTable.getTableHeader().setUI(null);
        filmListTable.setModel(dtm);

        filmListTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = filmListTable.rowAtPoint(e.getPoint());
                int col = filmListTable.columnAtPoint(e.getPoint());

                if(col == 0) {
                    new MovieViewer(movies.get(row));
                }

            }
        });
        filmListTable.setOpaque(false);
        ((DefaultTableCellRenderer)filmListTable.getDefaultRenderer(Object.class)).setOpaque(false);
    }

    /**
     * Instanzio il panel che contiene la tabella
     * @param rowData -> matrice che rappresenta le righe della tabella
     * @param columnNames -> array che rappresenta i nomi delle colonne della tabella
     */
    private void initFilmListPanel(Object[][] rowData, Object[] columnNames) {
        initFilmListTable(rowData, columnNames);
        movieListPanel.setOpaque(false);
        movieListPanel.getViewport().setOpaque(false);
        movieListPanel.setColumnHeader(null);
        movieListPanel.setViewportView(filmListTable);
    }

    public void triggerModificationToFilmList() {
        initFilmList();
        triggerChangeToJTable();
    }

    private void triggerChangeToJTable() {
        dtm.setDataVector(getRowData(), getColumnNames());
        dtm.fireTableDataChanged();
    }

}
