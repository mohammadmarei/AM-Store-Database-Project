package com.example.am_store;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.sql.*;

public class FinishPageController {
    @FXML
    private TextField nameField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField employeeIdField;
    @FXML
    private TextField orderIdField;

    @FXML
    private ComboBox<String> statusComboBox;

    @FXML
    private Label totalPriceLabel;

    private ObservableList<orderdetails> orderDetailsList;
    private double totalPrice = 0.0;

    public void setOrderDetailsList(ObservableList<orderdetails> orderDetailsList, double totalPrice) {
        if (orderDetailsList == null || orderDetailsList.isEmpty()) {
            showAlert("Order details are empty.");
            return;
        }
        this.orderDetailsList = orderDetailsList;
        calculateTotalPrice();
        setTotalPrice(this.totalPrice);
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
        totalPriceLabel.setText("Total Price: $" + String.format("%.2f", totalPrice));
    }

    private void calculateTotalPrice() {
        totalPrice = 0.0;
        for (orderdetails detail : orderDetailsList) {
            totalPrice += detail.getQuantity() * detail.getPrice();
        }
        updateTotalPriceLabel();
    }

    private void updateTotalPriceLabel() {
        totalPriceLabel.setText("Total Price: $" + String.format("%.2f", totalPrice));
    }

    @FXML
    private void handleSubmit(MouseEvent event) {
        String customerName = nameField.getText();
        String phoneNumber = phoneField.getText();
        String employeeIdStr = employeeIdField.getText();
        String orderIdStr = orderIdField.getText();
        String status = statusComboBox.getValue();

        if (customerName.isEmpty() || phoneNumber.isEmpty() || employeeIdStr.isEmpty() || orderIdStr.isEmpty() || status == null) {
            showAlert("Please fill in all fields.");
            return;
        }

        int employeeId;
        int orderId;
        try {
            employeeId = Integer.parseInt(employeeIdStr);
            orderId = Integer.parseInt(orderIdStr);
        } catch (NumberFormatException e) {
            showAlert("Employee ID and Order ID must be valid numbers.");
            return;
        }

        if (totalPrice <= 0) {
            showAlert("Total Price must be greater than zero.");
            return;
        }

        boolean isSaved = saveOrderDetails(orderId, customerName, phoneNumber, employeeId, totalPrice, status);

        if (isSaved) {
            boolean areItemsSaved = saveOrderDetailsItems(orderId, OrderController.getInstance().getOrderDetailsList());
            if (areItemsSaved) {
                OrderController.getInstance().clearOrderDetails();
                Stage stage = (Stage) nameField.getScene().getWindow();
                stage.close();
            } else {
                showAlert("Failed to save order details items.");
            }
        } else {
            showAlert("Failed to save order details.");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Input Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean saveCustomerIfNotExists(String customerName, String phoneNumber) {
        String checkQuery = "SELECT COUNT(*) FROM customer WHERE customer_name = ? AND customer_phone = ?";
        String insertQuery = "INSERT INTO customer (customer_name, customer_phone) VALUES (?, ?)";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement checkStatement = connection.prepareStatement(checkQuery);
             PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {

            // التحقق من وجود العميل في قاعدة البيانات
            checkStatement.setString(1, customerName);
            checkStatement.setString(2, phoneNumber);
            try (ResultSet resultSet = checkStatement.executeQuery()) {
                if (resultSet.next() && resultSet.getInt(1) > 0) {
                    // العميل موجود بالفعل
                    return true;
                }
            }

            // إدخال العميل إذا لم يكن موجودًا
            insertStatement.setString(1, customerName);
            insertStatement.setString(2, phoneNumber);
            int rowsAffected = insertStatement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database error while saving customer: " + e.getMessage());
        }

        return false;
    }

    private boolean saveOrderDetails(int orderId, String customerName, String phoneNumber, int employeeId, double totalPrice, String status) {
        // حفظ العميل إذا لم يكن موجودًا
        if (!saveCustomerIfNotExists(customerName, phoneNumber)) {
            showAlert("Failed to save or verify customer details.");
            return false;
        }

        // حفظ تفاصيل الطلب
        String query = "INSERT INTO \"Order\" (order_id, customer_name, customer_phone, employee_id, total_price, status, date) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, orderId);
            statement.setString(2, customerName);
            statement.setString(3, phoneNumber);
            statement.setInt(4, employeeId);
            statement.setDouble(5, totalPrice);
            statement.setString(6, status);

            java.sql.Date currentDate = new java.sql.Date(System.currentTimeMillis());
            statement.setDate(7, currentDate);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database error: " + e.getMessage());
        }

        return false;
    }

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "12345";

    private boolean saveOrderDetailsItems(int orderId, ObservableList<orderdetails> orderDetailsList) {
        String queryCheckStock = "SELECT quantity FROM product WHERE product_id = ?";
        String queryUpdateStock = "UPDATE product SET quantity = quantity - ? WHERE product_id = ?";
        String query = "INSERT INTO order_details (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            connection.setAutoCommit(false);

            try (PreparedStatement checkStockStatement = connection.prepareStatement(queryCheckStock);
                 PreparedStatement updateStockStatement = connection.prepareStatement(queryUpdateStock);
                 PreparedStatement statement = connection.prepareStatement(query)) {

                for (orderdetails detail : orderDetailsList) {
                    // التحقق من الكمية المتوفرة في المخزون
                    checkStockStatement.setInt(1, detail.getProductId());
                    try (ResultSet resultSet = checkStockStatement.executeQuery()) {
                        if (resultSet.next()) {
                            int availableQuantity = resultSet.getInt("quantity");
                            if (detail.getQuantity() > availableQuantity) {
                                showAlert("Insufficient stock for product ID: " + detail.getProductId());
                                connection.rollback();
                                return false;
                            }

                            // تحديث الكمية في المخزون
                            updateStockStatement.setInt(1, detail.getQuantity());
                            updateStockStatement.setInt(2, detail.getProductId());
                            updateStockStatement.executeUpdate();
                        }
                    }

                    // إضافة العنصر إلى جدول order_details
                    statement.setInt(1, orderId);
                    statement.setInt(2, detail.getProductId());
                    statement.setInt(3, detail.getQuantity());
                    statement.setDouble(4, detail.getPrice());
                    statement.addBatch();
                }

                // تنفيذ العمليات في قاعدة البيانات
                int[] rowsAffected = statement.executeBatch();
                connection.commit();
                return rowsAffected.length > 0;
            } catch (SQLException e) {
                connection.rollback();
                e.printStackTrace();
                showAlert("Database error: " + e.getMessage());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database connection error: " + e.getMessage());
        }

        return false;
    }

}
