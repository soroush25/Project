package src.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.extern.log4j.Log4j;
import src.model.bl.AdminBl;
import src.model.entity.Admin;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
//todo: لطفا چک شود
@Log4j
public class AdminTransactionController implements Initializable {
    @FXML
    private Button exit;

    @FXML
    private TableView<Admin> AdminTransactionTbl;

    @FXML
    private TableColumn<Admin, Integer> idTransactionCol;

    @FXML
    private TableColumn<Admin, String> ammountTransactionCol, sourceTransactionCol, destinationTransactionCol, timeTransactionCol, typeTransactionCol;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        log.info("Entered AdminTransaction");
        try {
            resetForm();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "AdminTransaction Error\n" + e.getMessage());
            alert.show();
        }
        exit.setOnAction((event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Quit?");
            if (alert.showAndWait().get().equals(ButtonType.OK)) {
                Platform.exit();
            }
            log.info("Quited");
        }));
    }

    private void showDataOnTable(List<Admin> customerList) throws Exception {
        ObservableList<Admin> observableList = FXCollections.observableList(customerList);
        idTransactionCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        ammountTransactionCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        sourceTransactionCol.setCellValueFactory(new PropertyValueFactory<>("sourceAccount"));
        destinationTransactionCol.setCellValueFactory(new PropertyValueFactory<>("destinationAccount"));
        timeTransactionCol.setCellValueFactory(new PropertyValueFactory<>("transactionDateTime"));
        typeTransactionCol.setCellValueFactory(new PropertyValueFactory<>("transactionType"));
        AdminTransactionTbl.setItems(observableList);
    }

    private void resetForm() throws Exception {
        showDataOnTable(AdminBl.getAdminBl().findAll());
    }
}