package com.example.am_store;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;
public class mainemployee {
    @FXML
    void switchtohome(MouseEvent event) {
        loadScene(event,"employeehome.fxml");
    }
    @FXML
    void switchtocustomer(MouseEvent event) {
        loadScene(event,"customere.fxml");

    }
    @FXML
    void switchtoorder(MouseEvent event) {
        loadScene(event,"orderemployee.fxml");

    }
    @FXML
    void switchtoproduct(MouseEvent event) {
        loadScene(event,"productemployee.fxml");

    }
    @FXML
    void handlea(MouseEvent event) {
        loadScene(event,"accessoryemployee.fxml");

    }

    @FXML
    void handlegaming(MouseEvent event) {
        loadScene(event,"gamingemployee.fxml");


    }

    @FXML
    void handlelaptop(MouseEvent event) {
        loadScene(event,"laptopemployee.fxml");

    }

    @FXML
    void handlemonitor(MouseEvent event) {
        loadScene(event,"mointouremployee.fxml");

    }

    @FXML
    void handleprinter(MouseEvent event) {
        loadScene(event,"printeremployee.fxml");

    }

    @FXML
    void handlesmart(MouseEvent event) {
        loadScene(event,"CarsMangemployee.fxml");

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
    private void loadScene2(ActionEvent event, String fxmlFile) {
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
}
