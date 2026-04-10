package com.example.am_store;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javax.swing.*;

public class HelloApplication extends Application {
    @FXML
    private Button Manager_Button;

    @FXML
    private Button employee_butoon;

    @FXML
    void Employee_button(ActionEvent event) {
        loadScene(event, "employeelogin.fxml");
    }

    @FXML
    void Manage_button(ActionEvent event) {
        loadScene(event, "login.fxml");
    }
    @Override
    public void start(Stage stage) throws Exception {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("choose manager or employee.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, 679, 402);
            stage.setTitle("AM Store");
            stage.setScene(scene);
            stage.sizeToScene();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading FXML: " + e.getMessage());
        }
    }
    private void loadScene(ActionEvent event, String fxmlFile) {
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
        public static void main (String[]args){
            launch();
        }

}