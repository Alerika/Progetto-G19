/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unipv.gui.login;

import it.unipv.conversion.CSVToUserList;
import it.unipv.conversion.UserToCSV;
import it.unipv.utils.DataReferences;

import javax.swing.*;
import java.util.List;

/**
 * Form utilizzato per effettuare la registrazione al cinema.
 *    Esso è stato creato attraverso il builder Form di NetBeans, quindi il file
 *    "RegisterForm.form" associato è possibile vederlo/modificarlo
 *    correttamente solamente da NetBeans stesso.
 */
class RegisterForm extends javax.swing.JFrame {

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        usernameTextField = new javax.swing.JTextField();
        registerButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        loginButton = new javax.swing.JButton();
        passwordTextField = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        registerButton.setText("Registrati");

        jLabel1.setText("Già registrato?");

        loginButton.setText("Login");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(74, 74, 74)
                        .addComponent(registerButton)
                        .addGap(0, 64, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(usernameTextField))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(loginButton))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(passwordTextField)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(usernameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(passwordTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(registerButton)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(loginButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton loginButton;
    private javax.swing.JPasswordField passwordTextField;
    private javax.swing.JButton registerButton;
    private javax.swing.JTextField usernameTextField;
    // End of variables declaration//GEN-END:variables

    private LoginForm loginForm;
    private List<User> users;

    /**
     * Costruttore del form; quando inizializzato crea il form di registrazione
     * @param loginForm -> viene utilizzato per ritornare al form principale di login
     */
    RegisterForm(LoginForm loginForm) {
        this.loginForm = loginForm;
        users = CSVToUserList.getUserListFromCSV(DataReferences.USERFILEPATH);
        initComponents();
        initButtonListeners();
        initFrame();
    }

    /**
     * Imposto i listener dei due button presenti, quello per tornare al form di login e quello per registrarsi
     */
    private void initButtonListeners() {
        loginButton.addActionListener(e -> returnToLoginEvent());
        registerButton.addActionListener(e -> doRegisterEvent());
    }

    /**
     * Metodo invocato al click sul button di registrazione:
     *    Se i campi vengono popolati allora si passa alla registrazione.
     */
    private void doRegisterEvent() {
        if( usernameTextField.getText().trim().equalsIgnoreCase("")
         || String.valueOf(passwordTextField.getPassword()).trim().equalsIgnoreCase("")) {
            JOptionPane.showMessageDialog(this, "Devi inserire sia username che password!");

        } else {
            doRegister();
        }
    }

    /**
     * Metodo invocato al click sul button di login:
     *    permette di tornare al form di login e di nascondere questo di registrazione.
     */
    private void returnToLoginEvent() {
        loginForm.setVisible(true);
        setVisible(false);
        clearAllTextField(usernameTextField, passwordTextField);
    }


    /**
     * Metodo che effettivamente si occupa della registrazione
     *    Se non esiste un utente con il nome inserito, allora si procede con la registrazione
     *    altrimenti viene mostrato un messaggio che notifica l'utente dell'esistenza di
     *    un utente che ha scelto il medesimo nickname.
     */
    private void doRegister() {
        if(!isAlreadyThereThisUser()) {
            //Se entro qua vuol dire che non esiste un utente con quel determinato username, quindi può essere registrato
            UserToCSV.appendUserToCSV( new User( usernameTextField.getText()
                                               , String.valueOf(passwordTextField.getPassword()))
                                     , DataReferences.USERFILEPATH
                                     , true );
            JOptionPane.showMessageDialog(this, "Registrazione avvenuta con successo!");
            clearAllTextField(usernameTextField, passwordTextField);
            setVisible(false);
            loginForm.setVisible(true);
            loginForm.triggerNewUser();
        } else {
            //Se entro qua vuol dire che esiste già un utente con quel determinato username, quindi non può essere registrato
            JOptionPane.showMessageDialog(this, "Utente già esistente!");
            clearAllTextField(usernameTextField, passwordTextField);
        }
    }

    /**
     * Metodo che controlla se esiste già un utente con l'username specificato nella textfield
     * @return -> true se esiste un username con quel determinato username, altrimenti false
     */
    private boolean isAlreadyThereThisUser() {
        boolean status = false;
        for(User u : users) {
            if(u.getName().equalsIgnoreCase(usernameTextField.getText())) {
                status = true;
                break;
            }
        }
        return status;
    }

    /**
     * Metodo utilizzato per pulire le textfield quando necessario
     * @param toClear -> le varie textfield da pulire
     */
    private void clearAllTextField(JTextField... toClear) {
        for(JTextField jtf : toClear) {
            jtf.setText("");
        }
    }

    /**
     * Metodo utilizzato per settare le impostazioni base del form
     */
    private void initFrame() {
        setTitle("Registrazione");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(false);
    }
}
