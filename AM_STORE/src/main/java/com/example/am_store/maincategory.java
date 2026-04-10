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
public class maincategory {
    @FXML
    void switchtohome(MouseEvent event) {
        loadScene(event,"manager(home).fxml");
    }
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
        loadScene(event,"maincategory.fxml");

    }
    @FXML
    void switchtostatistics(MouseEvent event) {

    }
    @FXML
    void handlea(MouseEvent event) {
        loadScene(event,"accessory.fxml");

    }

    @FXML
    void handlegaming(MouseEvent event) {
        loadScene(event,"gaming.fxml");


    }

    @FXML
    void handlelaptop(MouseEvent event) {
        loadScene(event,"Laptop.fxml");

    }

    @FXML
    void handlemonitor(MouseEvent event) {
        loadScene(event,"mointour.fxml");

    }

    @FXML
    void handleprinter(MouseEvent event) {
        loadScene(event,"printer.fxml");

    }

    @FXML
    void handlesmart(MouseEvent event) {
        loadScene(event,"CarsMang.fxml");

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
