package com.example.am_store;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javax.swing.*;
import java.io.IOException;
import java.sql.*;

public class logine {
    @FXML
    private PasswordField password;

    @FXML
    private Button signin;

    @FXML
    private TextField username;

    @FXML
    void handle_sign_in(ActionEvent event) {
        String user = username.getText();
        String pass = password.getText();

        try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "12345");
             PreparedStatement stmtt = con.prepareStatement("SELECT * FROM employee WHERE role = ? AND first_name = ? AND password = ?")) {

            DriverManager.registerDriver(new org.postgresql.Driver());
            stmtt.setString(1, "Employee");
            stmtt.setString(2, user);
            stmtt.setString(3, pass);

            ResultSet rs = stmtt.executeQuery();
            if (rs.next()) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("employeehome.fxml"));
                Parent addTrainerView = loader.load();
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(addTrainerView);
                stage.setScene(scene);
                stage.show();
            } else {
                JOptionPane.showMessageDialog(null, "Invalid username, password, or you are not authorized as Employee.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading FXML: " + e.getMessage());
        }
    }
    public void About(){
        Alert msg = new Alert(Alert.AlertType.WARNING,"please contact the administrator.");
        msg.showAndWait();
    }
    @FXML
    void Back(MouseEvent event) {
        loadScene(event, "choose manager or employee.fxml");
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
}
