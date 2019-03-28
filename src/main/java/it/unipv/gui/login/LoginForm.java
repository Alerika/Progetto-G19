/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unipv.gui.login;

import it.unipv.conversion.CSVToUserList;
import it.unipv.gui.common.User;
import it.unipv.gui.manager.ManagerForm;
import it.unipv.gui.user.UserForm;
import it.unipv.utils.StringReferences;

import javax.swing.*;
import java.util.List;

/**
 * Form utilizzato per effettuare il login al cinema.
 *    Esso è stato creato attraverso il builder Form di NetBeans, quindi il file
 *    "LoginForm.form" associato è possibile vederlo/modificarlo
 *    correttamente solamente da NetBeans stesso.
 */
public class LoginForm extends javax.swing.JFrame {

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        usernameTextField = new javax.swing.JTextField();
        rememberCBox = new javax.swing.JCheckBox();
        loginButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        registerButton = new javax.swing.JButton();
        passwordTextField = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        rememberCBox.setText("Ricordami");

        loginButton.setText("Login");

        jLabel2.setText("Devi registrarti?");

        registerButton.setText("Registrazione");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(usernameTextField)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(rememberCBox))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(loginButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(registerButton, javax.swing.GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE)))
                    .addComponent(passwordTextField))
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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rememberCBox)
                    .addComponent(loginButton))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(registerButton))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel2;
    private javax.swing.JButton loginButton;
    private javax.swing.JPasswordField passwordTextField;
    private javax.swing.JButton registerButton;
    private javax.swing.JCheckBox rememberCBox;
    private javax.swing.JTextField usernameTextField;
    // End of variables declaration//GEN-END:variables

    private RegisterForm registerUI;
    private List<User> userList;
    private User user;
    private boolean wasThereAlreadyAnUser = false;

    /**
     * Costruttore del form: quando instanziato crea tutti i componenti
     */
    public LoginForm() {
        initComponents();
        init();
    }

    //Se esiste già un utente salvato nelle info, allora effettuo il login, altrimenti mostro il form login
    private void init() {
        registerUI = new RegisterForm(this);
        userList = CSVToUserList.getUserListFromCSV(StringReferences.USERFOLDERPATH);
        initButtonListeners();

        if(checkIfThereIsAlreadyUserSaved()) {
            wasThereAlreadyAnUser = true;
            doLogin(user);
        } else {
            initFrame();
        }
    }

    /**
     * Metodo che controlla se esiste già un utente salvato nelle info
     * @return -> true se esiste, altrimenti false
     */
    private boolean checkIfThereIsAlreadyUserSaved() {
        if(UserInfo.checkIfUserInfoFileExists()) {
            user = UserInfo.getUserInfo();
            return true;
        } else {
            user = null;
            return false;
        }
    }

    /**
     * Imposto il listener per il button di login e quello di registrazione
     */
    private void initButtonListeners() {
        loginButton.addActionListener(e -> doLoginEvent());
        registerButton.addActionListener(e -> goToRegisterFormEvent(this));
    }

    /**
     * Metodo invocato al click del button di registrazione;
     *    apre il form di registrazione, nasconde quello di login e pulisce le textfield
     */
    private void goToRegisterFormEvent(LoginForm summoner) {
        registerUI.setVisible(true);
        summoner.setVisible(false);
        clearAllTextField(usernameTextField, passwordTextField);
        rememberCBox.setSelected(false);
    }

    /**
     * Metodo invocato al click del button di login.
     *    Se uno dei due campi di inserimento, oppure entrambi, risultano essere vuoti,
     *       viene informato l'utente di riempirli;
     *    Se vengono correttamente riempiti, allora si passa a cercare se esiste un utente
     *       con quei dati all'interno della lista utenti;
     *    Se viene riconosciuto come un utente esistente, allora si effettua il login,
     *       altrimenti si mostra un messaggio all'utente di dati errati;
     *    Se al momento di login viene impostato "Ricordami" si vanno a salvare i dati
     *       dell'utente all'interno di file/info.txt
     */
    private void doLoginEvent() {
        if( usernameTextField.getText().trim().equalsIgnoreCase("")
         || String.valueOf(passwordTextField.getPassword()).trim().equalsIgnoreCase("")) {
            JOptionPane.showMessageDialog(this, "Devi inserire sia username che password!");
        } else {
            //Se entriamo qua vuol dire che ha inserito qualcosa all'interno dei due textfield
            User u = new User(usernameTextField.getText().trim(), String.valueOf(passwordTextField.getPassword()));
            if(checkIfItIsAValidUserFromUserList(u)) {
                //Se entriamo qua vuol dire che l'utente esiste all'interno della lista utenti (i dati inseriti sono corretti)
                doLogin(u);
                if(rememberCBox.isSelected()) {
                    UserInfo.createUserInfoFileInUserDir(u.getName(), u.getPassword());
                }
            } else {
                //Se invece ci troviamo qua, vuol dire che non esiste un utente con quei dati all'interno della lista
                clearAllTextField(usernameTextField, passwordTextField);
                rememberCBox.setSelected(false);
                JOptionPane.showMessageDialog(this, "Nome utente o password errati!");
            }
        }
    }

    /**
     * Metodo utilizzato per effettuare realmente il login all'interno dell'applicazione
     *    Se l'utente è riconosciuto come ADMIN, allora si attiva la parte del gestore
     *    Se l'utente è riconosciuto come USER, allora si attiva la parte degli utenti
     * Visto che il metodo è utilizzato anche per loggarsi qualora esistesse un file info.txt
     *    mostro nuovamente, in caso di errore, che sono stati impostati nome utente o password errati.
     * @param u -> è l'utente da loggare
     */
    private void doLogin(User u) {
        boolean status = true;

        if(wasThereAlreadyAnUser) {
            /* Se entro qua vuol dire che esiste un file info.txt
             *    Controllo nuovamente se è presente un utente del genere nella lista
             */
            if(checkIfItIsAValidUserFromUserList(u)) {
                status = true;
            } else {
                status = false;
            }
        }

        if(status) {
            //Se entro qua dentro vuol dire che ho un utente da loggare, che sia stato salvato o appena impostato
            if(checkIfItIsManagerOrNormalUser(u)) {
                //Se entro qua vuol dire che è un gestore
                setVisible(false);
                new ManagerForm(this, u.getName());
            } else {
                //Se entro qua vuol dire che è un utente
                setVisible(false);
                new UserForm(this, u.getName());
            }
        } else {
            //Se entro qua vuol dire che c'è un problema con i dati salvati all'interno del file info.txt
            JOptionPane.showMessageDialog(this, "Nome utente o password errati!");
            clearAllTextField(usernameTextField, passwordTextField);
            if(wasThereAlreadyAnUser) {
                initFrame();
                UserInfo.deleteUserInfoFileInUserDir();
            }
            System.err.println("Login non riuscito!");
        }
    }

    /**
     * Metodo utilizzato per triggerare l'evento di logout:
     *    quando invocato cancella il file info.txt e rimanda al form di login
     */
    public void triggerLogoutEvent() {
        initFrame();
        UserInfo.deleteUserInfoFileInUserDir();
        clearAllTextField(usernameTextField, passwordTextField);
        rememberCBox.setSelected(false);
    }

    /**
     * Metodo utilizzato per controllare se è un utente valido presente nella lista utenti
     * @param u -> utente che verrà confrontato con gli altri utenti della lista
     * @return -> true se l'utente è valido, altrimenti false
     */
    private boolean checkIfItIsAValidUserFromUserList(User u) {
        boolean flag = false;
        for(User user : userList) {
            if( u.getName().trim().equals(user.getName())
             && u.getPassword().trim().equals(user.getPassword()) ) {
                flag = true;
                //Interrompo il ciclo perché tanto ha già trovato che l'utente è valido
                break;
            }
        }

        if(flag) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Metodo che controlla se l'utente è un gestore (con dati inseriti a mano per ora) o un utente
     * @param u -> l'utente da controllare
     * @return -> true se è un gestore, altrimenti false
     */
    private boolean checkIfItIsManagerOrNormalUser(User u) {
        if (u.getName().trim().equals(StringReferences.ADMINUSERNAME)
                && u.getPassword().trim().equals(StringReferences.ADMINPASSWORD)) {
            return true;
        } else {
            return false;
        }
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
        setTitle("Login");
        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
