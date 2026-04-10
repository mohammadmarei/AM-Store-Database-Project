package com.example.am_store;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class Customer {
    @FXML
    void switchtocustomer(MouseEvent event) {
        loadScene(event,"Customer.fxml");

    }

    @FXML
    void switchtoemployee(MouseEvent event) {
        loadScene(event,"employee.fxml");

    }

    @FXML
    void switchtoorder(MouseEvent event) {
        loadScene(event,"Order.fxml");

    }

    @FXML
    void switchtoproduct(MouseEvent event) {
        loadScene(event,"product.fxml");

    }

    @FXML
    void switchtostatistics(MouseEvent event) {

    }
    @FXML
    void switchtohome(MouseEvent event) {
        loadScene(event,"manager(home).fxml");
    }
    private void loadScene(MouseEvent event, String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent view = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(view);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private TableColumn<table_customer, String> FName;

    @FXML
    private TableColumn<table_customer, Integer> ID;

    @FXML
    private TableColumn<table_customer, String> phone;

    @FXML
    private TableView<table_customer> table_customer;
    @FXML
    public void initialize() {
        ID.setCellValueFactory(new PropertyValueFactory<>("id"));
        FName.setCellValueFactory(new PropertyValueFactory<>("name"));
        phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        ObservableList<table_customer> customerData = Database.getCustomerData();
        table_customer.setItems(customerData);
        refreshTable();
    }
    private void refreshTable() {
        ObservableList<table_customer> customerData = Database.getCustomerData();
        table_customer.setItems(customerData);
    }
}
