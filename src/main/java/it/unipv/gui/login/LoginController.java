package it.unipv.gui.login;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import it.unipv.conversion.CSVToUserList;
import it.unipv.gui.common.GUIUtils;
import it.unipv.gui.login.User;
import it.unipv.gui.login.UserInfo;
import it.unipv.gui.user.HomeController;
import it.unipv.utils.ApplicationException;
import it.unipv.utils.DataReferences;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class LoginController implements Initializable {

    @FXML private Label loginButton;
    @FXML private TextField usernameTextfield;
    @FXML private PasswordField passwordTextfield;
    @FXML private CheckBox rememberCheckbox;
    private HomeController homeController;
    private List<User> userList = new ArrayList<>();

    public void init(HomeController summoner) {
        this.homeController = summoner;
        initUserListFromCSV();
    }

    private void initUserListFromCSV() { userList = CSVToUserList.getUserListFromCSV(DataReferences.USERFILEPATH); }

    @FXML
    public void enterKeyPressed(KeyEvent event) {
        if(event.getCode().equals(KeyCode.ENTER)){
            doLogin();
        }
    }

    @FXML public void loginButtonListener() { doLogin(); }

    private void doLogin() {
        if(usernameTextfield.getText().equals("") || passwordTextfield.getText().equals("")){
            GUIUtils.showAlert(Alert.AlertType.ERROR, "Errore", "Si è verificato un errore:", "Devi compilare tutti i campi!");
        } else {
            if(usernameTextfield.getText().equals("admin") && passwordTextfield.getText().equals("admin")){
                doExit();
                openManagerArea();
            } else {
                User user = new User(usernameTextfield.getText().trim(), passwordTextfield.getText().trim());
                if(checkIfItIsAValidUserFromUserList(user)) {
                    doRealLogin(user);

                    if(rememberCheckbox.isSelected()) {
                        UserInfo.createUserInfoFileInUserDir(user.getName(), user.getPassword(), user.getEmail());
                    }

                    doExit();
                } else {
                    GUIUtils.showAlert(Alert.AlertType.ERROR, "Errore", "Si è verificato un errore:", "Non esiste un utente con questo username!");    
                }
            }
        }
    }

    private void doRealLogin(User user) {
        homeController.triggerNewLogin(user);
    }

    private void openManagerArea() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/manager/ManagerHome.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Area Manager");
            stage.setMinHeight(850);
            stage.setMinWidth(1100);
            stage.show();
        } catch (IOException e) {
            throw new ApplicationException(e);
        }
    }

    private boolean checkIfItIsAValidUserFromUserList(User u) {
        boolean flag = false;
        for(User user : userList) {
            if(!user.getName().equals("Admin")) {
                if( u.getName().trim().equals(user.getName())
                 && u.getPassword().trim().equals(user.getPassword()) ) {
                    u.setEmail(user.getEmail());
                    flag = true;
                    break;
                }
            }
        }

        return flag;
    }

    @FXML private void doCancel(){
        doExit();
    }

    @FXML private void doExit(){ ((Stage) loginButton.getScene().getWindow()).close(); }

    @Override public void initialize(URL url, ResourceBundle rb) { }

}