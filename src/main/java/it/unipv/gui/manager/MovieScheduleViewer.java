package it.unipv.gui.manager;

import it.unipv.conversion.CSVToMovieScheduleList;
import it.unipv.conversion.MovieScheduleToCSV;
import it.unipv.utils.StringReferences;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MovieScheduleViewer extends JScrollPane {

    private List<MovieSchedule> movieSchedules;
    private List<MovieSchedule> realDisplayedSchedules = new ArrayList<>();
    private JTable schedulesTable;
    private DefaultTableModel schedulesTableDTM;
    private String movieCode;
    private String movieTitle;

    public MovieScheduleViewer(String movieCode, String movieTitle) {
        this.movieCode = movieCode;
        this.movieTitle = movieTitle;
        initScrollPanel();
    }

    private void initMovieSchedulesList() {
        movieSchedules = CSVToMovieScheduleList.getMovieScheduleListFromCSV(StringReferences.MOVIESCHEDULEFILEPATH);
    }

    private void initScheduleTable(Object[][] rowData, Object[] columnNames) {
        schedulesTableDTM = initDTM(rowData, columnNames);
        schedulesTable = initTable(schedulesTableDTM);
        schedulesTable.setComponentPopupMenu(initJPopupMenuForMovieTable(schedulesTable));
    }

    private int selectedRow;
    private JPopupMenu initJPopupMenuForMovieTable(JTable movieScheduleTable) {
        JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem deleteItem = new JMenuItem("Rimuovi");
        deleteItem.addActionListener(e -> {
            int reply = JOptionPane.showConfirmDialog(this, "Sei sicuro di voler eliminare dalla lista questa programmazione?");
            if(reply == JOptionPane.YES_OPTION) {
                movieSchedules.remove(realDisplayedSchedules.get(selectedRow));
                realDisplayedSchedules.remove(selectedRow);
                MovieScheduleToCSV.createCSVFromMovieScheduleList(movieSchedules, StringReferences.MOVIESCHEDULEFILEPATH, false);
                schedulesTableDTM.removeRow(selectedRow);
                schedulesTableDTM.setDataVector(getRowData(), getColumnNames());
                schedulesTableDTM.fireTableDataChanged();
            }
        });
        popupMenu.add(deleteItem);
        popupMenu.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                SwingUtilities.invokeLater(() -> {
                    selectedRow = movieScheduleTable.rowAtPoint(SwingUtilities.convertPoint(popupMenu, new Point(0, 0), movieScheduleTable));
                    if (selectedRow > -1) {
                        movieScheduleTable.setRowSelectionInterval(selectedRow, selectedRow);
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

    private Object[][] getRowData() {
        int size = 0;
        for(MovieSchedule m : movieSchedules) {
            if(m.getMovieCode().trim().equalsIgnoreCase(movieCode.trim())) {
                size++;
            }
        }
        Object rowData[][] = new Object[size][1];
        int cont = 0;
        for(MovieSchedule m : movieSchedules) {
            if(m.getMovieCode().trim().equalsIgnoreCase(movieCode.trim())) {
                rowData[cont][0] = movieTitle + "   "
                                 + m.getDate() + "   "
                                 + m.getTime() + "   "
                                 + m.getHallName();
                realDisplayedSchedules.add(m);
                cont++;
            }
        }
        return rowData;
    }

    private Object[] getColumnNames() { return new Object[]{""}; }

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

    private void initScrollPanel() {
        setVisible(true);
        initMovieSchedulesList();
        initScheduleTable(getRowData(), getColumnNames());
        setViewportView(schedulesTable);
        setOpaque(false);
        getViewport().setOpaque(false);
        setColumnHeader(null);
    }
}
