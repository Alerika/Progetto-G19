package it.unipv.controller.managerarea;

import it.unipv.controller.common.IManagerAreaTrigger;
import it.unipv.db.DBConnection;
import it.unipv.dao.PricesDao;
import it.unipv.dao.PricesDaoImpl;
import it.unipv.controller.common.GUIUtils;
import it.unipv.controller.common.ICloseablePane;
import it.unipv.model.Prices;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class PricesPanelController {

    private Prices prices = null;
    private PricesDao pricesDao;
    private IManagerAreaTrigger managerHomeController;
    @FXML TextField baseTextField;
    @FXML TextField vipTextField;
    @FXML TextField threeDTextField;
    @FXML TextField reducedTextField;
    @FXML Label saveButton;

    public void init(IManagerAreaTrigger managerHomeController, DBConnection dbConnection) {
        this.managerHomeController = managerHomeController;
        pricesDao = new PricesDaoImpl(dbConnection);

        fillUI();

        GUIUtils.setScaleTransitionOnControl(saveButton);
    }

    private void fillUI() {
        managerHomeController.triggerStartStatusEvent("Carico le informazioni riguardanti i prezzi...");
        Platform.runLater(() -> {
            initPricesIfExists();
            setComponentIfPricesExists();
        });
        managerHomeController.triggerEndStatusEvent("Prezzi correttamente caricati!");
    }

    private void initPricesIfExists(){ prices = pricesDao.retrievePrices(); }

    private void setComponentIfPricesExists(){
        if(prices!=null){
            baseTextField.setText(""+prices.getBase());
            vipTextField.setText(""+prices.getVip());
            threeDTextField.setText(""+prices.getThreed());
            reducedTextField.setText(""+prices.getReduced());
        }
    }

    @FXML
    private void doSave() throws NumberFormatException {
        managerHomeController.triggerStartStatusEvent("Salvo i nuovi prezzi...");
        Platform.runLater(this::doRealSave);
        managerHomeController.triggerEndStatusEvent("Nuovi prezzi correttamente salvati!");
    }

    private void doRealSave() {
        if( baseTextField.getText().trim().equalsIgnoreCase("")
                || vipTextField.getText().trim().equalsIgnoreCase("")
                || threeDTextField.getText().trim().equalsIgnoreCase("")
                || reducedTextField.getText().trim().equalsIgnoreCase("")) {
            GUIUtils.showAlert(Alert.AlertType.ERROR, "Errore", "Si è verificato un errore:", "Devi compilare tutti i campi!");
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
            pricesDao.updatePrices(prices);
            GUIUtils.showAlert(Alert.AlertType.INFORMATION, "Informazione", "Operazione riuscita: ", "Salvataggio prezzi riuscito con successo!");
        }
    }
}
