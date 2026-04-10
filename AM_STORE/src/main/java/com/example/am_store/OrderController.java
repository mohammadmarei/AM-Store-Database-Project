package com.example.am_store;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

public class OrderController {
    private static final OrderController instance = new OrderController();
    private ObservableList<orderdetails> orderDetailsList;

    // Constructor is private to prevent instantiation
    private OrderController() {}

    // Get the single instance of the class
    public static OrderController getInstance() {
        return instance;
    }

    // Initialize orderDetailsList lazily
    public ObservableList<orderdetails> getOrderDetailsList() {
        if (orderDetailsList == null) {
            orderDetailsList = FXCollections.observableArrayList();
        }
        return orderDetailsList;
    }

    // Set a new list of order details
    public void setOrderDetailsList(ObservableList<orderdetails> orderDetailsList) {
        this.orderDetailsList = orderDetailsList;
    }

    // Clear the list of order details
    public void clearOrderDetails() {
        if (orderDetailsList != null) {
            orderDetailsList.clear();
        }
    }

    // Method to load order details from the database
    public void loadOrderDetails(int orderId) {
        // Clear existing data before loading new data
        clearOrderDetails();

        // SQL query to fetch order details for a specific orderId
        String query = "SELECT od.order_details_id, p.product_name, od.product_id, od.quantity, od.price " +
                "FROM order_details od " +
                "JOIN product p ON od.product_id = p.product_id " +
                "WHERE od.order_id = ?";

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/am_store", "root", "password");
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Set the parameter for the order ID
            statement.setInt(1, orderId);

            try (ResultSet resultSet = statement.executeQuery()) {
                // Loop through the result set and populate the orderDetailsList
                while (resultSet.next()) {
                    int orderDetailsId = resultSet.getInt("order_details_id");
                    String productName = resultSet.getString("product_name");
                    int productId = resultSet.getInt("product_id");
                    int quantity = resultSet.getInt("quantity");
                    double price = resultSet.getDouble("price");

                    orderdetails orderDetail = new orderdetails(orderDetailsId, productName, productId, quantity, price);
                    orderDetailsList.add(orderDetail);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error loading order details: " + e.getMessage());
        }
    }

    // Show an alert with a given message
    private void showAlert(String message) {
        // Create and display an alert (this could be a JOptionPane or a JavaFX Alert)
        System.out.println("Alert: " + message);
    }
}
