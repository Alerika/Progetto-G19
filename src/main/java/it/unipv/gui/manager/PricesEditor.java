/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unipv.gui.manager;

import it.unipv.conversion.CSVToPrices;
import it.unipv.conversion.PricesToCSV;
import it.unipv.utils.StringReferences;

import javax.swing.*;

public class PricesEditor extends javax.swing.JFrame {

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        baseTextField = new javax.swing.JTextField();
        vipTextField = new javax.swing.JTextField();
        threeDTextField = new javax.swing.JTextField();
        reducedTextField = new javax.swing.JTextField();
        jMenuBar1 = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        saveItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Tariffa base");

        jLabel2.setText("Sovrapprezzo VIP");

        jLabel3.setText("Sovrapprezzo 3D");

        jLabel4.setText("Riduzione");

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
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(48, 48, 48)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(baseTextField)
                    .addComponent(vipTextField)
                    .addComponent(threeDTextField)
                    .addComponent(reducedTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE))
                .addContainerGap(12, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(baseTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(vipTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(threeDTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(reducedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField baseTextField;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JTextField reducedTextField;
    private javax.swing.JMenuItem saveItem;
    private javax.swing.JTextField threeDTextField;
    private javax.swing.JTextField vipTextField;
    // End of variables declaration//GEN-END:variables

    private Prices prices = null;

    public PricesEditor() {
        initComponents();
        initPricesIfExists();
        setComponentIfPricesExists();
        initSaveListener();
        initFrame();
    }

    private void initPricesIfExists(){
        prices = CSVToPrices.getPricesFromCSV(StringReferences.PRICESFILEPATH);
    }

    private void setComponentIfPricesExists(){
        if(prices!=null){
            baseTextField.setText(""+prices.getBase());
            vipTextField.setText(""+prices.getVip());
            threeDTextField.setText(""+prices.getThreed());
            reducedTextField.setText(""+prices.getReduced());
        }
    }

    private void initSaveListener() {
        saveItem.addActionListener(e -> {
            if( baseTextField.getText().trim().equalsIgnoreCase("")
             || vipTextField.getText().trim().equalsIgnoreCase("")
             || threeDTextField.getText().trim().equalsIgnoreCase("")
             || reducedTextField.getText().trim().equalsIgnoreCase("")) {
                JOptionPane.showMessageDialog(this, "Devi compilare tutti i campi!");
            } else {
                if(prices!=null) {
                    prices.setBase(Double.parseDouble(baseTextField.getText()));
                    prices.setVip(Double.parseDouble(vipTextField.getText()));
                    prices.setThreed(Double.parseDouble(threeDTextField.getText()));
                    prices.setReduced(Double.parseDouble(reducedTextField.getText()));
                } else {
                    prices = new Prices( Double.parseDouble(baseTextField.getText())
                                       , Double.parseDouble(vipTextField.getText())
                                       , Double.parseDouble(threeDTextField.getText())
                                       , Double.parseDouble(reducedTextField.getText()));
                }
                PricesToCSV.createCSVFromPrices(prices, StringReferences.PRICESFILEPATH, false);
            }
        });
    }

    private void initFrame() {
        setTitle("Editor prezzi");
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        pack();
    }
}
