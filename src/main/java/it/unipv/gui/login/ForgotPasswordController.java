package it.unipv.gui.login;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import it.unipv.conversion.CSVToUserList;
import it.unipv.conversion.UserToCSV;
import it.unipv.gui.common.GUIUtils;
import it.unipv.utils.DataReferences;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ForgotPasswordController implements Initializable {

    @FXML private TextField usernameTextField, userCodeTextfield;
    @FXML private PasswordField passwordTextfield, retryPasswordTextfield;
    @FXML private Label cancelButton, infoButton;
    private List<User> users = new ArrayList<>();
    private LoginController loginController;

    @Override public void initialize(URL url, ResourceBundle rb) {
        initUserList();
    }

    public void init(LoginController loginController) {
        this.loginController = loginController;
        initUserList();
        GUIUtils.setScaleTransitionOnControl(infoButton);
    }

    private void initUserList() {
        users = CSVToUserList.getUserListFromCSV(DataReferences.USERFILEPATH);
    }

    @FXML public void doCancel() { close(); }

    @FXML
    public void infoButtonListener() {
        GUIUtils.showAlert( Alert.AlertType.INFORMATION
                          ,  "Informazione"
                          , "Hai dimenticato il codice utente?"
                          , "Contattaci al numero 0256914783 o all'indirizzo studio@goldenmovie.com.\n"
                                  +  "Uno dei nostri tecnici si occupererà di eseguire il reset della password e di informati a procedura completata!");
    }

    private void close() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.getOnCloseRequest().handle(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
        stage.close();
    }

    @FXML
    public void doConfirm() {
        if(checkIfAllTextfieldAreCompiled()) {
            if(checkIfExistAnUserLikeThat()) {
                if(checkIfPasswordContentAreEquals()) {
                    User user = getUserFromTextfield();
                    if(user!=null) {
                        users.get(users.indexOf(user)).setPassword(passwordTextfield.getText());
                    }
                    UserToCSV.createCSVFromUserList(users, DataReferences.USERFILEPATH, false);
                    GUIUtils.showAlert( Alert.AlertType.INFORMATION
                                       , "Informazione"
                                       , "Operazione riuscita: "
                                       , "La password dell'utente " + user.getName() + " è stata correttamente cambiata in: " + user.getPassword());
                    loginController.triggerResettedPasswordEvent();
                    close();
                } else {
                    GUIUtils.showAlert(Alert.AlertType.ERROR,  "Errore", "Si è verificato un errore:", "Le password non coincidono!");
                }
            } else {
                GUIUtils.showAlert(Alert.AlertType.ERROR, "Errore", "Si è verificato un errore:", "Non esiste un utente con questi dati!");
            }
        } else {
            GUIUtils.showAlert(Alert.AlertType.ERROR, "Errore", "Si è verificato un errore:", "Devi compilare tutti i campi!");
        }
    }

    private boolean checkIfPasswordContentAreEquals() {
        return passwordTextfield.getText().equals(retryPasswordTextfield.getText());
    }

    private boolean checkIfExistAnUserLikeThat() {
        for(User u : users) {
            if(u.getName().equals(usernameTextField.getText()) && u.getCodice().equals(userCodeTextfield.getText())) {
                return true;
            }
        }
        return false;
    }

    private User getUserFromTextfield() {
        User res = null;
        for(User u : users) {
            if(u.getName().equals(usernameTextField.getText()) && u.getCodice().equals(userCodeTextfield.getText())) {
                res = u;
                break;
            }
        }
        return res;
    }

    private boolean checkIfAllTextfieldAreCompiled() {
        return !usernameTextField.getText().equals("")
                && !userCodeTextfield.getText().equals("")
                && !passwordTextfield.getText().equals("")
                && !retryPasswordTextfield.getText().equals("");
    }
}
