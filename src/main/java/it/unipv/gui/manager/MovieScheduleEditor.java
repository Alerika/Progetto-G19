/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unipv.gui.manager;

import it.unipv.conversion.CSVToMovieScheduleList;
import it.unipv.conversion.MovieScheduleToCSV;
import it.unipv.gui.common.Movie;
import it.unipv.utils.ApplicationException;
import it.unipv.utils.DateLabelFormatter;
import it.unipv.utils.DataReferences;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import javax.swing.text.DateFormatter;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class MovieScheduleEditor extends javax.swing.JFrame {

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainContainer = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        datePickerContainer = new javax.swing.JPanel();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(20, 0), new java.awt.Dimension(20, 0), new java.awt.Dimension(20, 32767));
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(20, 0), new java.awt.Dimension(20, 0), new java.awt.Dimension(20, 32767));
        jLabel2 = new javax.swing.JLabel();
        timePickerContainer = new javax.swing.JPanel();
        filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(20, 0), new java.awt.Dimension(20, 0), new java.awt.Dimension(20, 32767));
        filler4 = new javax.swing.Box.Filler(new java.awt.Dimension(20, 0), new java.awt.Dimension(20, 0), new java.awt.Dimension(20, 32767));
        jLabel3 = new javax.swing.JLabel();
        hallComboBox = new javax.swing.JComboBox<>();
        jMenuBar1 = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        saveItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        mainContainer.setLayout(new java.awt.GridLayout(5, 2));

        jLabel1.setText("Giorno");
        mainContainer.add(jLabel1);

        javax.swing.GroupLayout datePickerContainerLayout = new javax.swing.GroupLayout(datePickerContainer);
        datePickerContainer.setLayout(datePickerContainerLayout);
        datePickerContainerLayout.setHorizontalGroup(
            datePickerContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        datePickerContainerLayout.setVerticalGroup(
            datePickerContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 24, Short.MAX_VALUE)
        );

        mainContainer.add(datePickerContainer);
        mainContainer.add(filler1);
        mainContainer.add(filler2);

        jLabel2.setText("Ora");
        mainContainer.add(jLabel2);

        javax.swing.GroupLayout timePickerContainerLayout = new javax.swing.GroupLayout(timePickerContainer);
        timePickerContainer.setLayout(timePickerContainerLayout);
        timePickerContainerLayout.setHorizontalGroup(
            timePickerContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        timePickerContainerLayout.setVerticalGroup(
            timePickerContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 24, Short.MAX_VALUE)
        );

        mainContainer.add(timePickerContainer);
        mainContainer.add(filler3);
        mainContainer.add(filler4);

        jLabel3.setText("Sala");
        mainContainer.add(jLabel3);
        mainContainer.add(hallComboBox);

        fileMenu.setText("File");

        saveItem.setText("Salva");
        fileMenu.add(saveItem);

        jMenuBar1.add(fileMenu);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mainContainer, javax.swing.GroupLayout.DEFAULT_SIZE, 311, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mainContainer, javax.swing.GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel datePickerContainer;
    private javax.swing.JMenu fileMenu;
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    private javax.swing.Box.Filler filler3;
    private javax.swing.Box.Filler filler4;
    private javax.swing.JComboBox<String> hallComboBox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel mainContainer;
    private javax.swing.JMenuItem saveItem;
    private javax.swing.JPanel timePickerContainer;
    // End of variables declaration//GEN-END:variables

    private JDatePickerImpl datePicker;
    private JSpinner timePicker;
    private ManagerForm managerForm;
    private Movie movie;

    public MovieScheduleEditor( ManagerForm managerForm
                              , Movie movie
                              , List<String> hallNames) {
        this.managerForm = managerForm;
        this.movie = movie;
        initComponents();
        initDatePicker();
        initTimePicker();
        initHallSelector(hallNames);
        initSaveItem();
        initFrame();
    }

    private void initSaveItem() {
        saveItem.addActionListener(e->{
            String date = datePicker.getJFormattedTextField().getText();
            String time = getTimeFromTimePickerSpinner();
            String hall = Objects.requireNonNull(hallComboBox.getSelectedItem().toString());

            if(date.trim().equalsIgnoreCase("")){
                JOptionPane.showMessageDialog(this, "Devi inserire una data!");
            } else if (checkIfDateIsPassed(date)) {
                JOptionPane.showMessageDialog(this, "Non puoi programmare un film nel passato!");
            } else if(checkIfItAlreasyExistsInThatTime(date, hall, time) ) {
                JOptionPane.showMessageDialog(this, "C'è già una programmazione in questo periodo!");
            } else {
                MovieSchedule movieSchedule = new MovieSchedule();
                movieSchedule.setMovieCode(movie.getCodice());
                movieSchedule.setDate(date);
                movieSchedule.setTime(time);
                movieSchedule.setHallName(hall);
                MovieScheduleToCSV.createCSVFromMovieSchedule(movieSchedule, DataReferences.MOVIESCHEDULEFILEPATH, true);
                managerForm.triggerNewScheduleEvent(movie.getCodice());
                JOptionPane.showMessageDialog(this, "Salvataggio riuscito correttamente!");
            }
        });
    }

    private boolean checkIfItAlreasyExistsInThatTime(String dateToCheck, String hall, String time) {
        for(MovieSchedule ms : CSVToMovieScheduleList.getMovieScheduleListFromCSV(DataReferences.MOVIESCHEDULEFILEPATH)) {
            if( ms.getTime().trim().equalsIgnoreCase(time)
             && ms.getHallName().trim().equalsIgnoreCase(hall)
             && ms.getDate().trim().equalsIgnoreCase(dateToCheck)) {
                return true;
            } else if( checkIfTimeIsBetweenPreviousTimeAndGap(ms.getTime(), Integer.parseInt(movie.getDurata()), time)
                    && ms.getHallName().trim().equalsIgnoreCase(hall)
                    && ms.getDate().trim().equalsIgnoreCase(dateToCheck)) {
                return true;
            }

        }
        return false;
    }

    private Calendar gapCalculator(String myTime, int movieTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(sdf.parse((myTime)));
        } catch (ParseException e) {
            throw new ApplicationException(e);
        }
        cal.add(Calendar.MINUTE, movieTime);
        cal.add(Calendar.MINUTE, DataReferences.PAUSEAFTERMOVIE);
        return cal;
    }

    private boolean checkIfTimeIsBetweenPreviousTimeAndGap(String previousTime, int movieTime, String myTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Calendar previousActualTime = Calendar.getInstance();
        Calendar gapActualTime = gapCalculator(previousTime, movieTime);
        Calendar myActualTime = Calendar.getInstance();
        try {
            previousActualTime.setTime(sdf.parse((previousTime)));
            myActualTime.setTime(sdf.parse(myTime));
        } catch (ParseException e) {
            throw new ApplicationException(e);
        }
        return myActualTime.after(previousActualTime) && myActualTime.before(gapActualTime);
    }

    private boolean checkIfDateIsPassed(String toCheck){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date dateToCheck;
        try {
            dateToCheck = sdf.parse(toCheck);
        } catch (ParseException e) {
            throw new ApplicationException(e);
        }
        return dateToCheck.before(new Date());
    }

    private String getTimeFromTimePickerSpinner() {
        return new SimpleDateFormat("HH:mm").format(timePicker.getValue());
    }


    private void initDatePicker() {
        Properties p = new Properties();
        p.put("text.today", "Oggi");
        p.put("text.month", "Mese");
        p.put("text.year", "Anno");
        datePicker = new JDatePickerImpl(new JDatePanelImpl(new UtilDateModel(),p), new DateLabelFormatter());
        datePickerContainer.add(datePicker);
        datePickerContainer.setLayout(new GridLayout(1,1));
    }

    private void initTimePicker() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 24);
        calendar.set(Calendar.MINUTE, 0);

        SpinnerDateModel model = new SpinnerDateModel();
        model.setValue(calendar.getTime());

        timePicker = new JSpinner(model);

        JSpinner.DateEditor editor = new JSpinner.DateEditor(timePicker, "HH:mm");
        DateFormatter formatter = (DateFormatter)editor.getTextField().getFormatter();
        formatter.setAllowsInvalid(false);
        formatter.setOverwriteMode(true);

        timePicker.setEditor(editor);

        timePickerContainer.add(timePicker);
        timePickerContainer.setLayout(new GridLayout(1,1));

    }

    private void initHallSelector(List<String> hallNames) {
        hallComboBox.setModel(new DefaultComboBoxModel(hallNames.toArray()));
    }

    private void initFrame() {
        setTitle("Programmazione " + movie.getTitolo());
        setVisible(true);
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }
}
