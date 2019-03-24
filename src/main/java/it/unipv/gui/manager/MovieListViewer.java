package it.unipv.gui.manager;

import it.unipv.conversion.CSVToMovieList;
import it.unipv.conversion.MovieToCSV;
import it.unipv.gui.common.Movie;
import it.unipv.gui.common.MovieViewer;
import it.unipv.utils.StringReferences;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class MovieListViewer extends JFrame {
    private JScrollPane filmListPanel;
    private JTable filmListTable;
    private DefaultTableModel dtm;
    private List<Movie> movies;

    public MovieListViewer() {
        initFilmList();
        initFilmListPanel(getRowData(), getColumnNames());
        initFrame();
    }

    private void initFilmList() {
        movies = CSVToMovieList.getMovieListFromCSV(StringReferences.FILMFOLDERPATH);
    }

    private Object[][] getRowData() {
        Object rowData[][] = new Object[movies.size()][3];

        for(int i = 0; i< movies.size(); i++) {
            rowData[i][0] = movies.get(i).getTitolo();
            rowData[i][1] = "Modifica";
            rowData[i][2] = "Rimuovi";
        }
        return rowData;
    }

    private Object[] getColumnNames() { return new Object[]{"","",""}; }

    private void initFilmListPanel(Object[][] rowData, Object[] columnNames) {
        filmListPanel = new JScrollPane();
        initFilmListTable(rowData, columnNames);
        filmListPanel.setOpaque(false);
        filmListPanel.getViewport().setOpaque(false);
        filmListPanel.setColumnHeader(null);
        filmListPanel.setViewportView(filmListTable);
    }

    private void initFilmListTable(Object[][] rowData, Object[] columnNames) {
        dtm = new DefaultTableModel(rowData, columnNames) {
            public boolean isCellEditable(int row, int column)     {
                return false;
            }
        };
        filmListTable = new JTable();
        filmListTable.getTableHeader().setUI(null);
        filmListTable.setModel(dtm);

        MovieListViewer summoner = this;

        filmListTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = filmListTable.rowAtPoint(e.getPoint());
                int col = filmListTable.columnAtPoint(e.getPoint());

                if(col == 0) {
                    new MovieViewer(movies.get(row));
                }

                if(col == 1) {
                    new MovieEditor(movies.get(row), summoner);
                }

                if(col == 2) {
                    int reply = JOptionPane.showConfirmDialog(null, "Sei sicuro di voler eliminare dalla lista il film " + movies.get(row).getTitolo() +"?");
                    if(reply == JOptionPane.YES_OPTION) {
                        movies.remove(row);
                        MovieToCSV.createCSVFromMovieList(movies, StringReferences.FILMFOLDERPATH, false);
                        dtm.removeRow(row);
                        dtm.fireTableDataChanged();
                    }
                }
            }
        });
        filmListTable.setOpaque(false);
        ((DefaultTableCellRenderer)filmListTable.getDefaultRenderer(Object.class)).setOpaque(false);
    }

    private void initFrame() {
        setTitle("Movie Inseriti");
        add(filmListPanel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(1,1));
        setSize(700,500);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void triggerOverwriteMovieEvent(Movie movie) {
        boolean flag = false;
        Movie toRemove = null;
        for(Movie m : movies) {
            if(m.getCodice().trim().equalsIgnoreCase(movie.getCodice().trim())) {
                flag = true;
                toRemove = m;
                break;
            }
        }
        if(flag) {
            movies.remove(toRemove);
            movies.add(movie);
            MovieToCSV.createCSVFromMovieList(movies, StringReferences.FILMFOLDERPATH, false);
            dtm.setDataVector(getRowData(), getColumnNames());
            dtm.fireTableDataChanged();
        }
    }
}
