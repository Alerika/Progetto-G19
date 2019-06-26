package it.unipv.controller.login;

import java.util.List;
import java.util.regex.Pattern;

import it.unipv.db.DBConnection;
import it.unipv.dao.UserDao;
import it.unipv.dao.UserDaoImpl;
import it.unipv.controller.common.GUIUtils;
import it.unipv.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.apache.commons.lang3.RandomStringUtils;

public class RegistrazioneController {

    @FXML Label cancelLabel;
    @FXML TextField usernameTextfield, emailTextfield;
    @FXML PasswordField passwordTextfield, retryPasswordTextfield;
    private List<User> users;
    private UserDao userDao;

    public void init(DBConnection dbConnection) {
        userDao = new UserDaoImpl(dbConnection);
        initUserList();
    }

    private void initUserList() {
        users = userDao.retrieveUserList();
    }

    @FXML
    public void doRegister() {
        if( usernameTextfield.getText().equalsIgnoreCase("")
         || emailTextfield.getText().equalsIgnoreCase("")
         || passwordTextfield.getText().equalsIgnoreCase("")
         || retryPasswordTextfield.getText().equalsIgnoreCase("")) {
            GUIUtils.showAlert(Alert.AlertType.ERROR, "Errore", "Si è verificato un errore:", "Devi compilare tutti i campi!");
        } else {
            if(!isAlreadyThereThisUsername()) {
                if(!isAlreadyThereThisEmail()) {
                    if(isEmailValid()) {
                        if(passwordTextfield.getText().equals(retryPasswordTextfield.getText())) {
                            doRealRegistrationAndShowConfirm();
                            doExit();
                        } else {
                            showError("Le password non coincidono!", passwordTextfield, retryPasswordTextfield);
                        }
                    } else {
                        showError("Non hai inserito una E-mail valida!", emailTextfield, passwordTextfield, retryPasswordTextfield);
                    }
                } else {
                    showError("E-mail già esistente!", emailTextfield, passwordTextfield, retryPasswordTextfield);
                }
            } else {
                showError("Nome utente già esistente!", usernameTextfield, passwordTextfield, retryPasswordTextfield);
            }
        }
    }

    private void doRealRegistrationAndShowConfirm() {
        String codice = getUserCode();
        userDao.insertNewUser(new User( usernameTextfield.getText()
                                      , passwordTextfield.getText()
                                      , emailTextfield.getText()
                                      , codice));
        GUIUtils.showAlert( Alert.AlertType.INFORMATION
                          , "Info"
                          , "Informazione:"
                          , "Registrazione avvenuta con successo!\n"
                                   + "Codice utente: " + codice + ". Ricordati di non smarrirlo, potrebbe esserti utile per reimpostare la password!");
    }

    private void showError(String dialogMessageText, TextField... toClear) {
        GUIUtils.showAlert(Alert.AlertType.ERROR,  "Errore", "Si è verificato un errore:", dialogMessageText);
        clearTextField(toClear);
    }

    private String getUserCode() {
        boolean shouldDie = false;
        String res = "";
        while(!shouldDie) {
            String codice = RandomStringUtils.random(5, "0123456789abcdefghijklmnopqrstuvzxy").toUpperCase();
            boolean status = false;
            for(User u : users) {
                if(u.getCodice().equalsIgnoreCase(codice)) {
                    status = true;
                    break;
                }
            }
            if(!status) {
                res = codice;
                shouldDie = true;
            }
        }
        return res;
    }

    private boolean isAlreadyThereThisUsername() {
        boolean status = false;
        for(User u : users) {
            if(u.getNome().equalsIgnoreCase(usernameTextfield.getText())) {
                status = true;
                break;
            }
        }
        return status;
    }

    private boolean isAlreadyThereThisEmail() {
        boolean status = false;
        for(User u : users) {
            if(u.getEmail().equalsIgnoreCase(emailTextfield.getText())) {
                status = true;
                break;
            }
        }
        return status;
    }

    private void clearTextField(TextField... toClear) {
        for(TextField tf : toClear) {
            tf.setText("");
        }
    }

    private boolean isEmailValid() {
        return Pattern.compile("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?"
                              , Pattern.CASE_INSENSITIVE)
                .matcher(emailTextfield.getText()).matches();
    }


    @FXML private void doCancel() { doExit(); }

    private void doExit(){ ((Stage) usernameTextfield.getScene().getWindow()).close(); }
}
