/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unipv.gui.manager;

import it.unipv.conversion.MovieToCSV;
import it.unipv.gui.common.Movie;
import it.unipv.utils.ApplicationUtils;
import it.unipv.utils.StringReferences;

import javax.swing.*;

/**
 * Form utilizzato per la creazione del movie
 *    Esso è stato creato attraverso il builder Form di NetBeans, quindi il file
 *    "RegisterForm.form" associato è possibile vederlo/modificarlo
 *    correttamente solamente da NetBeans stesso.
 */
public class MovieEditor extends JFrame {


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        imgLabel = new javax.swing.JLabel();
        titleLabel = new javax.swing.JLabel();
        directionLabel = new javax.swing.JLabel();
        castLabel = new javax.swing.JLabel();
        timeLabel = new javax.swing.JLabel();
        yearLabel = new javax.swing.JLabel();
        imgTextField = new javax.swing.JTextField();
        titleTextField = new javax.swing.JTextField();
        directionTextField = new javax.swing.JTextField();
        castTextField = new javax.swing.JTextField();
        timeTextField = new javax.swing.JTextField();
        yearTextField = new javax.swing.JTextField();
        searchButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        plotTextArea = new javax.swing.JTextArea();
        genreLabel = new javax.swing.JLabel();
        genreTextField = new javax.swing.JTextField();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        saveItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        imgLabel.setText("Locandina");

        titleLabel.setText("Titolo");

        directionLabel.setText("Regia");

        castLabel.setText("Cast");

        timeLabel.setText("Durata");

        yearLabel.setText("Anno");

        searchButton.setText("...");
        searchButton.setPreferredSize(new java.awt.Dimension(6, 20));

        plotTextArea.setColumns(20);
        plotTextArea.setRows(5);
        jScrollPane1.setViewportView(plotTextArea);

        genreLabel.setText("Genere");

        jMenu1.setText("File");

        saveItem.setText("Salva");
        saveItem.setHideActionText(true);
        jMenu1.add(saveItem);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(imgLabel)
                            .addComponent(titleLabel)
                            .addComponent(directionLabel)
                            .addComponent(castLabel)
                            .addComponent(timeLabel)
                            .addComponent(yearLabel)
                            .addComponent(genreLabel))
                        .addGap(102, 102, 102)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(genreTextField)
                            .addComponent(castTextField)
                            .addComponent(timeTextField)
                            .addComponent(yearTextField)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(imgTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(searchButton, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(titleTextField)
                            .addComponent(directionTextField))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(imgTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchButton, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(imgLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(titleLabel)
                    .addComponent(titleTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(genreLabel)
                    .addComponent(genreTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(directionLabel)
                    .addComponent(directionTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(castLabel)
                    .addComponent(castTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(timeLabel)
                    .addComponent(timeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(yearLabel)
                    .addComponent(yearTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE)
                .addContainerGap())
        );

        titleLabel.getAccessibleContext().setAccessibleName("titleLabel");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel castLabel;
    private javax.swing.JTextField castTextField;
    private javax.swing.JLabel directionLabel;
    private javax.swing.JTextField directionTextField;
    private javax.swing.JLabel genreLabel;
    private javax.swing.JTextField genreTextField;
    private javax.swing.JLabel imgLabel;
    private javax.swing.JTextField imgTextField;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea plotTextArea;
    private javax.swing.JMenuItem saveItem;
    private javax.swing.JButton searchButton;
    private javax.swing.JLabel timeLabel;
    private javax.swing.JTextField timeTextField;
    private javax.swing.JLabel titleLabel;
    private javax.swing.JTextField titleTextField;
    private javax.swing.JLabel yearLabel;
    private javax.swing.JTextField yearTextField;
    // End of variables declaration//GEN-END:variables

    private boolean wasItAlreadyCreated;
    private ManagerForm movieListViewer;
    private Movie movie;

    /**
     * Costruttore del form utilizzato quando il film viene creato da zero
     */
    MovieEditor(ManagerForm movieListViewer) {
        wasItAlreadyCreated = false;
        this.movieListViewer = movieListViewer;
        initComponents();
        setFileChooser(this);
        setSaveItem();
        initFrame("Nuovo film");
    }

    /**
     * Costruttore del form utilizzato quando il film viene modificato a partire da uno già esistente
     * @param movie -> film da modificare
     * @param movieListViewer -> il form della lista dei film
     */
    MovieEditor(Movie movie
            , ManagerForm movieListViewer) {
        wasItAlreadyCreated = true;
        this.movie = movie;
        this.movieListViewer = movieListViewer;
        initComponents();
        setComponents();
        setFileChooser(this);
        setSaveItem();
        initFrame("Modifica a " + movie.getTitolo());
    }

    /**
     * Se è un film da modificare, imposto le textfield settandole con il testo
     *    delle informazioni del film da modificare
     */
    private void setComponents() {
        imgTextField.setText(movie.getLocandinaPath());
        titleTextField.setText(movie.getTitolo());
        genreTextField.setText(movie.getGenere());
        directionTextField.setText(movie.getRegia());
        castTextField.setText(movie.getCast());
        timeTextField.setText(movie.getDurata());
        yearTextField.setText(movie.getAnno());
        plotTextArea.setText(movie.getTrama());
    }

    /**
     * Metodo per settare le impostazioni base del form
     * @param title -> imposto il titolo del form con il titolo del film
     */
    private void initFrame(String title) {
        setTitle(title);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    /**
     * Metodo per impostare il menu Salva
     */
    private void setSaveItem() {
        saveItem.addActionListener( e -> {
            if( imgTextField.getText().trim().equalsIgnoreCase("")
             || titleTextField.getText().trim().equalsIgnoreCase("")
             || genreTextField.getText().trim().equalsIgnoreCase("")
             || directionTextField.getText().trim().equalsIgnoreCase("")
             || castTextField.getText().trim().equalsIgnoreCase("")
             || timeTextField.getText().trim().equalsIgnoreCase("")
             || yearTextField.getText().trim().equalsIgnoreCase("")) {
                JOptionPane.showMessageDialog(this, "Devi compilare tutti i campi!");
            } else {
                if(!wasItAlreadyCreated) {
                    MovieToCSV.appendInfoMovieToCSV(getMovieFromTextFields(), StringReferences.FILMFOLDERPATH, true);
                    movieListViewer.triggerNewMovieEvent();
                } else {
                    movieListViewer.triggerOverwriteMovieEvent(getMovieFromTextFields());
                }
            }
        });
    }

    /**
     * Metodo utilizzato per creare una instanza del film riempita dalle informazioni inserite nelle textfield
     * @return -> instanza del film riempito con le informazioni inserite nelle textfield
     */
    private Movie getMovieFromTextFields() {
        Movie m = new Movie();
        m.setLocandinaPath(imgTextField.getText());
        m.setTitolo(titleTextField.getText());
        m.setGenere(genreTextField.getText());
        m.setRegia(directionTextField.getText());
        m.setCast(castTextField.getText());
        m.setDurata(timeTextField.getText());
        m.setAnno(yearTextField.getText());
        m.setTrama(plotTextArea.getText());
        if(!wasItAlreadyCreated) {
            m.setCodice(ApplicationUtils.getUUID());
        } else {
            m.setCodice(movie.getCodice());
        }
        return m;
    }

    private JFileChooser fileChooser;

    /**
     * Metodo utilizzato per impostare il file chooser con estensioni .jpg, .png, .gif
     * @param summoner -> è il frame da cui deve essere evocato il file chooser, ovvero MovieEditor
     */
    private void setFileChooser(JFrame summoner) {
        searchButton.addActionListener(e -> {
            fileChooser = new JFileChooser();
            fileChooser.setAcceptAllFileFilterUsed(false);
            addFileTypeFilter(".jpg", "JPEG Image");
            addFileTypeFilter(".png", "PNG Image");
            addFileTypeFilter(".gif", "GIF Image");
            if (fileChooser.showOpenDialog(summoner) == JFileChooser.APPROVE_OPTION) {
                imgTextField.setText(fileChooser.getSelectedFile().getAbsolutePath());

            }
        });
    }

    /**
     * Metodo utilizzato per aggiungere filtri estensione al file chooser
     * @param extension -> estensione da aggiungere
     * @param description -> descrizione dell'estensione da aggiungere
     */
    private void addFileTypeFilter(String extension, String description) {
        FileTypeFilter filter = new FileTypeFilter(extension, description);
        fileChooser.addChoosableFileFilter(filter);
    }

}
