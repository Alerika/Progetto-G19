package it.unipv.gui.login;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import it.unipv.conversion.CSVToUserList;
import it.unipv.conversion.UserToCSV;
import it.unipv.gui.common.GUIUtils;
import it.unipv.utils.DataReferences;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class RegistrazioneController implements Initializable {

    @FXML Label cancelLabel;
    @FXML TextField usernameTextfield, emailTextfield;
    @FXML PasswordField passwordTextfield, retryPasswordTextfield;
    private List<User> users;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        users = CSVToUserList.getUserListFromCSV(DataReferences.USERFILEPATH);

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
                            UserToCSV.appendUserToCSV(
                                    new User( usernameTextfield.getText()
                                            , passwordTextfield.getText()
                                            , emailTextfield.getText())
                                    , DataReferences.USERFILEPATH
                                    , true );
                            GUIUtils.showAlert(Alert.AlertType.INFORMATION, "Info", "Informazione:",  "Registrazione avvenuta con successo!");
                            doExit();
                        } else {
                            GUIUtils.showAlert(Alert.AlertType.ERROR,  "Errore", "Si è verificato un errore:", "Le password non coincidono!");
                            clearTextField(passwordTextfield, retryPasswordTextfield);
                        }
                    } else {
                        GUIUtils.showAlert(Alert.AlertType.ERROR, "Errore", "Si è verificato un errore:", "Non hai inserito una E-mail valida!");
                        clearTextField(emailTextfield, passwordTextfield, retryPasswordTextfield);
                    }
                } else {
                    GUIUtils.showAlert(Alert.AlertType.ERROR,  "Errore", "Si è verificato un errore:", "E-mail già esistente!");
                    clearTextField(emailTextfield, passwordTextfield, retryPasswordTextfield);
                }
            } else {
                GUIUtils.showAlert(Alert.AlertType.ERROR,  "Errore", "Si è verificato un errore:", "Nome utente già esistente!");
                clearTextField(usernameTextfield, passwordTextfield, retryPasswordTextfield);
            }
        }
    }

    private boolean isAlreadyThereThisUsername() {
        boolean status = false;
        for(User u : users) {
            if(u.getName().equalsIgnoreCase(usernameTextfield.getText())) {
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
