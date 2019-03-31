/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unipv.gui.common;

import it.unipv.utils.ApplicationException;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Classe utilizzare per vedere le informazioni riguardanti un dato film.
 *    È stata creata attraverso il builder Form di NetBeans, quindi il file
 *    "MovieViewer.form" associato è possibile vederlo/modificarlo
 *    correttamente solamente da NetBeans stesso.
 */
public class MovieViewer extends javax.swing.JFrame {

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        titoloLabel = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        regiaLabel = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        castLabel = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        durataLabel = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        annoLabel = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tramaTextArea = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();
        locandinaLabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        genreLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        titoloLabel.setFont(new java.awt.Font("Tahoma", 0, 28)); // NOI18N
        titoloLabel.setText("jLabel3");

        jLabel4.setText("Regia");

        regiaLabel.setText("jLabel5");

        jLabel6.setText("Cast");

        castLabel.setText("jLabel6");

        jLabel8.setText("Durata");

        durataLabel.setText("jLabel6");

        jLabel10.setText("Anno");

        annoLabel.setText("jLabel6");

        jScrollPane2.setBorder(null);

        tramaTextArea.setEditable(false);
        tramaTextArea.setColumns(20);
        tramaTextArea.setRows(5);
        tramaTextArea.setBorder(null);
        tramaTextArea.setOpaque(false);
        jScrollPane2.setViewportView(tramaTextArea);

        locandinaLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 144, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(locandinaLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 79, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 213, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(locandinaLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE))
        );

        jLabel1.setText("Genere");

        genreLabel.setText("jLabel2");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 558, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(titoloLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 392, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel10)
                                    .addComponent(jLabel1))
                                .addGap(36, 36, 36)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(genreLabel)
                                    .addComponent(annoLabel)
                                    .addComponent(durataLabel)
                                    .addComponent(castLabel)
                                    .addComponent(regiaLabel))))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(titoloLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(genreLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(regiaLabel)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(castLabel)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(durataLabel)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(annoLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel10))))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel annoLabel;
    private javax.swing.JLabel castLabel;
    private javax.swing.JLabel durataLabel;
    private javax.swing.JLabel genreLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel locandinaLabel;
    private javax.swing.JLabel regiaLabel;
    private javax.swing.JLabel titoloLabel;
    private javax.swing.JTextArea tramaTextArea;
    // End of variables declaration//GEN-END:variables

    public MovieViewer(Movie movie) {
        initComponents();

        setComponents(movie);
        initFrame(movie.getTitolo());
    }

    //Setto il contenuto dei componenti che mostrano le informazioni del film
    private void setComponents(Movie movie) {
        setLocandina(movie.getLocandinaPath());
        titoloLabel.setText(movie.getTitolo());
        genreLabel.setText(movie.getGenere());
        regiaLabel.setText(movie.getRegia());
        castLabel.setText(movie.getCast());
        durataLabel.setText(movie.getDurata());
        annoLabel.setText(movie.getAnno());
        tramaTextArea.setText(movie.getTrama());
        tramaTextArea.setEditable(false);
        tramaTextArea.setBorder(null);
    }

    //Qua viene settata la locandina. Essa deve essere ridimensionata per farla apparire correttamente nella label.
    private void setLocandina(String path) {
        ImageIcon img = new ImageIcon(getScaledImageFromPath(path, locandinaLabel.getWidth(), locandinaLabel.getHeight()));
        locandinaLabel.setIcon(img);
    }

    //Metodo che va a ridimensionare l'immagine data una certa altezza ed una certa larghezza (in questo caso quelli della label
    private Image getScaledImageFromPath(String path, int w, int h){
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(path));
        } catch (IOException e) {
            throw new ApplicationException(e);
        } finally {
            if(img!=null){ img.flush(); }
        }
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(img, 0, 0, w, h, null);
        g2.dispose();

        return resizedImg;
    }

    //Metodo utilizzato per settare le impostazioni base del form
    private void initFrame(String titolo) {
        setTitle(titolo);
        //Crea il frame al centro dello schermo
        setLocationRelativeTo(null);
        setVisible(true);
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
}
