/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unipv.gui.user;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import it.unipv.utils.ApplicationException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author Fede
 */
public class LoginController implements Initializable {

    public AnchorPane rootPane;
    @FXML
    private Label usernameError, passwordError, login;

    @FXML
    private TextField userField;

    @FXML
    private PasswordField passField;

    @FXML
    private void enterKeyPressed(KeyEvent event) {
        if(event.getCode().equals(KeyCode.ENTER)){
            if(userField.getText().equals("")){
                passwordError.setVisible(false);
                usernameError.setVisible(true);
            }
            if(passField.getText().equals("")){
                usernameError.setVisible(false);
                passwordError.setVisible(true);
            }
            if(userField.getText().equals("admin") && passField.getText().equals("admin")){
                closeStage();
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
            } else {
                usernameError.setVisible(true);
            }
        }
    }

    @FXML
    private void cancelClose(){
        closeStage();
    }

    @FXML
    private void closeStage(){
        Stage stage = (Stage) login.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        usernameError.setVisible(false);
        passwordError.setVisible(false);
    }

}