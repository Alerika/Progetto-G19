/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unipv.gui.common;

import it.unipv.utils.ApplicationException;
import it.unipv.utils.ApplicationUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author Andrea
 */
public class MovieViewer extends javax.swing.JFrame {

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel2.setText("Titolo");

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
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(229, 229, 229)
                .addComponent(locandinaLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(231, 231, 231))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(locandinaLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 558, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel10)
                                    .addComponent(jLabel2))
                                .addGap(37, 37, 37)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(titoloLabel)
                                    .addComponent(annoLabel)
                                    .addComponent(durataLabel)
                                    .addComponent(castLabel)
                                    .addComponent(regiaLabel))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(titoloLabel))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(regiaLabel))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(castLabel))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(durataLabel))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addComponent(annoLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel annoLabel;
    private javax.swing.JLabel castLabel;
    private javax.swing.JLabel durataLabel;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
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

    private void setComponents(Movie movie) {
        tramaTextArea.setBorder(null);
        setLocandina(movie.getLocandinaPath());
        titoloLabel.setText(movie.getTitolo());
        regiaLabel.setText(movie.getRegia());
        castLabel.setText(movie.getCast());
        durataLabel.setText(movie.getDurata());
        annoLabel.setText(movie.getAnno());

        tramaTextArea.setText(movie.getTrama());
        tramaTextArea.setEditable(false);
    }

    private void setLocandina(String path) {
        Image image = getScaledImageFromPath(path, locandinaLabel.getWidth(), locandinaLabel.getHeight());
        ImageIcon img = new ImageIcon(image);
        locandinaLabel.setIcon(img);
    }

    private Image getScaledImageFromPath(String path, int w, int h){
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(path));
        } catch (IOException e) {
            throw new ApplicationException(e);
        } finally {
            img.flush();
        }
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(img, 0, 0, w, h, null);
        g2.dispose();

        return resizedImg;
    }

    private void initFrame(String titolo) {
        setTitle(titolo);
        setLocationRelativeTo(null);
        setVisible(true);
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
}
