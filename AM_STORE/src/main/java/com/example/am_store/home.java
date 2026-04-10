package com.example.am_store;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javax.swing.*;
import java.io.IOException;
public class home {
    @FXML
    private Button signout;
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
    void handlesignout(ActionEvent event) {
        loadScene2(event,"choose manager or employee.fxml");
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
    public void About(){
        Alert msg = new Alert(Alert.AlertType.INFORMATION,"This Application Was Done By Anan Abukhader  , Mohammad Marei");
        msg.showAndWait();
    }
}
