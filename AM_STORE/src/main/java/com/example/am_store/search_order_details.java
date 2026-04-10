package com.example.am_store;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.*;
public class search_order_details {
    @FXML
    private TableColumn<orderdetails, Integer> amount;

    @FXML
    private TableColumn<orderdetails, String> name_product;
    @FXML
    private TableColumn<orderdetails, Double> price;

    @FXML
    private TableColumn<orderdetails, Integer> product_id;

    @FXML
    private TableView<orderdetails> search_order_details;

    private ObservableList<orderdetails> orderDetailsList = FXCollections.observableArrayList();

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "12345";

    // Initialize TableView columns
    public void initialize() {
        product_id.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getProductId()).asObject());
        amount.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getQuantity()).asObject());
        price.setCellValueFactory(cellData ->
                new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());

        search_order_details.setItems(orderDetailsList);
    }

    // Method to update TableView
    public void setOrderDetailsList(ObservableList<orderdetails> orderDetailsList) {
        this.orderDetailsList.clear();
        this.orderDetailsList.addAll(orderDetailsList);
    }

    // Fetch order details based on order ID
    public ObservableList<orderdetails> fetchOrderDetails(int orderId) {
        ObservableList<orderdetails> orderDetails = FXCollections.observableArrayList();
        String query = "SELECT  p.product_name, od.product_id, od.quantity, od.price " +
                "FROM order_details od " +
                "JOIN product p ON od.product_id = p.product_id WHERE od.order_id = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, orderId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {

                    String productName = resultSet.getString("product_name");
                    int productId = resultSet.getInt("product_id");
                    int quantity = resultSet.getInt("quantity");
                    double price = resultSet.getDouble("price");

                    orderdetails orderDetail = new orderdetails( productName, productId, quantity, price);
                    orderDetails.add(orderDetail);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error fetching data: " + e.getMessage());
        }
        return orderDetails;
    }
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
