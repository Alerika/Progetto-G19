package it.unipv.controller.login;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import it.unipv.controller.common.IHomeTrigger;
import it.unipv.db.DBConnection;
import it.unipv.dao.UserDao;
import it.unipv.dao.UserDaoImpl;
import it.unipv.conversion.UserInfo;
import it.unipv.controller.common.GUIUtils;
import it.unipv.model.User;
import it.unipv.utils.ApplicationException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class LoginController implements Initializable {

    @FXML private Label loginButton, passwordResetButton;
    @FXML private TextField usernameTextfield;
    @FXML private PasswordField passwordTextfield;
    @FXML private CheckBox rememberCheckbox;
    private IHomeTrigger homeController;
    private DBConnection dbConnection;
    private UserDao userDao;
    private List<User> userList = new ArrayList<>();

    public void init(IHomeTrigger summoner, DBConnection dbConnection) {
        this.dbConnection = dbConnection;
        this.userDao = new UserDaoImpl(dbConnection);
        this.homeController = summoner;
        GUIUtils.setScaleTransitionOnControl(passwordResetButton);
        initUserListFromCSV();
    }

    private boolean isForgotPasswordStageOpened = false;
    @FXML
    public void passwordResetButtonListener() {
        if (!isForgotPasswordStageOpened) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login/ForgotPassword.fxml"));
                Parent p = loader.load();
                ForgotPasswordController fpc = loader.getController();
                fpc.init(this, dbConnection);
                Stage stage = new Stage();
                stage.setScene(new Scene(p));
                stage.setResizable(false);
                stage.setTitle("Resetta Password");
                stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/GoldenMovieStudioIcon.png")));
                stage.setOnCloseRequest( event -> isForgotPasswordStageOpened = false);
                stage.show();
                isForgotPasswordStageOpened = true;
            } catch (IOException e) {
                throw new ApplicationException(e);
            }
        }
    }

    private void initUserListFromCSV() { userList = userDao.retrieveUserList(); }

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
            User user = new User(usernameTextfield.getText().trim(), passwordTextfield.getText().trim());
            if(checkIfItIsAValidUserFromUserList(user)) {
                fullUserWithAllInfo(user);
                doRealLogin(user);

                if(rememberCheckbox.isSelected()) {
                    UserInfo.createUserInfoFileInUserDir(user.getNome(), user.getPassword(), user.getEmail(), user.getCodice());
                }

                doExit();
            } else {
                GUIUtils.showAlert(Alert.AlertType.ERROR, "Errore", "Si è verificato un errore:", "Non esiste un utente con questo username!");
            }
        }
    }

    private void doRealLogin(User user) {
        homeController.triggerNewLogin(user);
    }

    private boolean checkIfItIsAValidUserFromUserList(User u) {
        boolean flag = false;
        for(User user : userList) {
            if( u.getNome().trim().equals(user.getNome())
             && u.getPassword().trim().equals(user.getPassword()) ) {
                flag = true;
                break;
            }
        }

        return flag;
    }

    private void fullUserWithAllInfo(User u) {
        for(User user : userList) {
            if( u.getNome().trim().equals(user.getNome())
             && u.getPassword().trim().equals(user.getPassword()) ) {
                u.setEmail(user.getEmail());
                u.setCodice(user.getCodice());
                break;
            }
        }
    }

    @FXML private void doCancel(){
        doExit();
    }

    @FXML private void doExit(){ ((Stage) loginButton.getScene().getWindow()).close(); }

    @Override public void initialize(URL url, ResourceBundle rb) { }

    void triggerResettedPasswordEvent() { initUserListFromCSV(); }
}