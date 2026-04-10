package com.example.am_store;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.example.am_store.Database.getOrderData;

public class ordere {
    private List<orderdetails> orderDetailsList = new ArrayList<>();

    @FXML
    void Back(MouseEvent event) {
        loadScene(event, "maincategoryemployee.fxml");
    }
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
    private TableColumn<table_order, java.util.Date> Date;

    @FXML
    private TableColumn<table_order, Integer> ID;

    @FXML
    private TableColumn<table_order, String> Name;

    @FXML
    private TableView<table_order> T_order;

    @FXML
    private TableColumn<table_order, Integer> emp_id;

    @FXML
    private TableColumn<table_order, String> phone;

    @FXML
    private TableColumn<table_order, String> status;

    @FXML
    private TableColumn<table_order, Double> t_price;

    @FXML
    public void initialize() {
        ID.setCellValueFactory(new PropertyValueFactory<>("id"));
        emp_id.setCellValueFactory(new PropertyValueFactory<>("e_id"));
        status.setCellValueFactory(new PropertyValueFactory<>("status"));
        t_price.setCellValueFactory(new PropertyValueFactory<>("totalprice"));
        Name.setCellValueFactory(new PropertyValueFactory<>("customer_name"));
        phone.setCellValueFactory(new PropertyValueFactory<>("customer_phone"));
        Date.setCellValueFactory(new PropertyValueFactory<>("date"));
        ObservableList<table_order> orderData = getOrderData();
        T_order.setItems(orderData);
        refreshTable();
    }

    private void refreshTable() {
        ObservableList<table_order> orderData = Database.getOrderData();
        T_order.setItems(orderData);
    }

    @FXML
    private TextField customer_name;

    @FXML
    private TextField date;

    @FXML
    private TextField em_id;

    @FXML
    private TextField id;

    @FXML
    private TextField statusss;

    @FXML
    private TextField total_price;

    @FXML
    void save_button(ActionEvent event) {
       loadScene2(event,"maincategoryemployee.fxml");
    }

    @FXML
    void action_delete(ActionEvent event) {
        deleteButton();
    }

    private void deleteButton() {
        String id = JOptionPane.showInputDialog("Enter the order ID number to delete:");
        if (id != null && !id.trim().isEmpty()) {
            try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "12345")) {
                con.setAutoCommit(false);

                try {
                    String deleteOrderDetailsSql = "DELETE FROM order_details WHERE order_id = " + Integer.parseInt(id);
                    Statement statement = con.createStatement();
                    int orderDetailsRowsAffected = statement.executeUpdate(deleteOrderDetailsSql);
                    String deleteOrderSql = "DELETE FROM \"Order\" WHERE order_id = " + Integer.parseInt(id);
                    int rowsAffected = statement.executeUpdate(deleteOrderSql);

                    if (rowsAffected == 1) {
                        JOptionPane.showMessageDialog(null, "The order with ID " + Integer.parseInt(id) + " was successfully deleted.");
                        if (orderDetailsRowsAffected > 0) {
                            JOptionPane.showMessageDialog(null, "Related order details were also deleted.");
                        }
                        refreshTable();
                    } else {
                        JOptionPane.showMessageDialog(null, "The entered ID " + Integer.parseInt(id) + " was not found.");
                    }

                    con.commit();
                } catch (Exception e) {
                    con.rollback();
                    JOptionPane.showMessageDialog(null, "An error occurred: " + e.getMessage());
                }
                refreshTable();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "An error occurred: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "No valid ID number was entered.");
        }
    }

    @FXML
    void action_search(ActionEvent event) {
        String inputId = JOptionPane.showInputDialog("Enter the order ID number:");

        if (inputId != null && !inputId.trim().isEmpty()) {
            try {
                int orderId = Integer.parseInt(inputId);

                // Fetch the order details from the database
                ObservableList<orderdetails> details = fetchOrderDetails(orderId);

                if (!details.isEmpty()) {
                    // Open a new window and display the details in the new interface
                    openOrderDetailsWindow(details);
                } else {
                    JOptionPane.showMessageDialog(null, "No order details found for the given ID.");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Please enter a valid numeric order ID.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "No valid ID number was entered.");
        }
    }
    private void openOrderDetailsWindow(ObservableList<orderdetails> details) {
        try {
            // Load the FXML for the new window (orderDetails.fxml)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Search_order_details.fxml"));
            Parent root = loader.load();

            // Get the controller of the new window and pass the order details
            search_order_details  controller = loader.getController();
            controller.setOrderDetailsList(details);

            // Create a new stage (window) and show it
            Stage stage = new Stage();
            stage.setTitle("Order Details");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error loading order details window: " + e.getMessage());
        }
    }


    public ObservableList<orderdetails> fetchOrderDetails(int orderId) {
        ObservableList<orderdetails> orderDetails = FXCollections.observableArrayList();
        String query = "SELECT p.name_product, od.product_id, od.quantity, od.price " +
                "FROM order_details od " +
                "JOIN product p ON od.product_id = p.product_id WHERE od.order_id = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, orderId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String productName = resultSet.getString("name_product");
                    int productId = resultSet.getInt("product_id");
                    int quantity = resultSet.getInt("quantity");
                    double price = resultSet.getDouble("price");

                    System.out.println("Fetched product: " + productName); // Debug log
                    orderdetails orderDetail = new orderdetails(productName, productId, quantity, price);
                    orderDetails.add(orderDetail);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error fetching data: " + e.getMessage());
        }
        return orderDetails;
    }


    private void updateTableWithDetails(ObservableList<orderdetails> details) {
        // Assuming you have a TableView for order details in the UI
        // Replace this with the correct TableView reference in your code
        TableView<orderdetails> detailsTableView = new TableView<>();
        detailsTableView.setItems(details);
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "12345";
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
