package it.unipv.gui.manager;

import java.io.InputStream;
import java.net.URL;
import java.util.*;

import it.unipv.conversion.CSVToUserList;
import it.unipv.conversion.UserToCSV;
import it.unipv.gui.common.GUIUtils;
import it.unipv.gui.login.User;
import it.unipv.utils.CloseableUtils;
import it.unipv.utils.DataReferences;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;

public class UserListPanelController implements Initializable {

    @FXML ScrollPane userListPanel;
    @FXML TextField searchBarTextfield;
    @FXML Label searchButton;

    @Override public void initialize(URL url, ResourceBundle rb) { createUserListGrid(); }

    private GridPane grigliaUser = new GridPane();
    private static int rowCount = 0;
    private static int columnCount = 0;
    private List<User> users = new ArrayList<>();

    private void initMoviesList() {
        users.clear();
        users = CSVToUserList.getUserListFromCSV(DataReferences.USERFILEPATH);
        Collections.sort(users);
    }

    private void createUserListGrid() {
        grigliaUser.getChildren().clear();
        GUIUtils.setScaleTransitionOnControl(searchButton);

        initMoviesList();

        for (User user : users) {
            if(!user.getName().equalsIgnoreCase("Admin")) {
                createGridCellFromUser(user);
            }
        }

        initRowAndColumnCount();
    }

    private void createGridCellFromUser(User user) {
        Label userLabel = new Label(StringUtils.abbreviate(user.getName(), 30));
        userLabel.setFont(Font.font("system", FontWeight.NORMAL, FontPosture.REGULAR, 20));
        userLabel.setTextFill(Color.WHITE);

        grigliaUser.setHgap(15);
        grigliaUser.setVgap(15);

        ImageView deleteIconView = GUIUtils.getIconView(getClass().getResourceAsStream("/images/Bin.png"));
        GUIUtils.setFadeInOutOnControl(deleteIconView);

        ImageView editIconView = GUIUtils.getIconView(getClass().getResourceAsStream("/images/Edit.png"));
        GUIUtils.setFadeInOutOnControl(editIconView);

        AnchorPane pane = new AnchorPane();
        if(columnCount==1) {
            columnCount=0;
            rowCount++;
        }
        grigliaUser.add(pane, columnCount, rowCount);
        columnCount++;

        userListPanel.setContent(grigliaUser);
        GridPane.setMargin(pane, new Insets(5,5,5,5));

        editIconView.setY(userLabel.getLayoutY());
        editIconView.setX(userLabel.getLayoutX()+270);
        editIconView.setOnMouseClicked( event -> {
            String password = JOptionPane.showInputDialog("Inserisci la nuova password per l'utente " + user.getName());
            if(password!=null) {
                if(!Objects.requireNonNull(password).trim().equalsIgnoreCase("") || !(password.trim().length() ==0)) {
                    users.get(users.indexOf(user)).setPassword(password);
                    UserToCSV.createCSVFromUserList(users, DataReferences.USERFILEPATH, false);
                    refreshUI();
                    JOptionPane.showMessageDialog(null,"La password dell'utente " + user.getName() + " è stata correttamente cambiata in: " + password);
                } else {
                    JOptionPane.showMessageDialog(null,"Non puoi inserire una password di soli spazi!");
                }
            }
        });

        deleteIconView.setY(userLabel.getLayoutY());
        deleteIconView.setX(userLabel.getLayoutX()+305);
        deleteIconView.setOnMouseClicked(e -> {
            int reply = JOptionPane.showConfirmDialog(null, "Sei sicuro di voler eliminare questo utente?");
            if(reply == JOptionPane.YES_OPTION) {
                users.remove(user);
                UserToCSV.createCSVFromUserList(users, DataReferences.USERFILEPATH, false);
                refreshUI();
            }
        });

        pane.getChildren().addAll(userLabel);
        pane.getChildren().addAll(editIconView);
        pane.getChildren().addAll(deleteIconView);

    }

    private void refreshUI() {
        grigliaUser.getChildren().clear();
        createUserListGrid();
    }

    @FXML public void searchButtonListener() {
        String searchedUserName = searchBarTextfield.getText();
        if(searchedUserName!=null || searchedUserName.trim().equalsIgnoreCase("")) {
            List<User> filteredUser = new ArrayList<>();
            for(User u : users) {
                if(u.getName().toLowerCase().trim().contains(searchedUserName.toLowerCase())){
                    filteredUser.add(u);
                }
            }
            grigliaUser.getChildren().clear();
            for(User u : filteredUser) {
                if(!u.getName().trim().equalsIgnoreCase("Admin")) {
                    createGridCellFromUser(u);
                }
            }
            initRowAndColumnCount();
        } else {
            refreshUI();
        }
    }

    private void initRowAndColumnCount() {
        rowCount=0;
        columnCount=0;
    }

}
