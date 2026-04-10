package com.example.am_store;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Pair;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class Database {
    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "12345";
    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
    public static ObservableList<T_employee> getEmployeeData() {
        ObservableList<T_employee> employeeList = FXCollections.observableArrayList();
        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "12345");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM employee")) {

            while (rs.next()) {
                T_employee employee = new T_employee(
                        rs.getInt("employee_id"),
                        rs.getString("first_name") + " " + rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getDouble("salary"),
                        rs.getString("role")
                );
                employeeList.add(employee);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return employeeList;
    }
    public static ObservableList<table_customer> getCustomerData() {
        ObservableList<table_customer> customerList = FXCollections.observableArrayList();
        String query = "SELECT customer_id,  customer_name, customer_phone FROM customer";

        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "12345");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                table_customer customer = new table_customer(
                        rs.getInt("customer_id"),
                        rs.getString("customer_name"),
                        rs.getString("customer_phone")
                );
                customerList.add(customer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customerList;
    }

    public static ObservableList<table_order> getOrderData() {
        ObservableList<table_order> orderList = FXCollections.observableArrayList();
        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "12345");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM \"Order\"")) {
            while (rs.next()) {
                table_order order = new table_order(
                        rs.getInt("order_id"),
                        rs.getInt("employee_id"),
                        rs.getString("customer_name"),
                        rs.getInt("total_price"),
                        rs.getString("status"),
                        rs.getString("customer_phone"),
                        rs.getDate("date")
                );
                orderList.add(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orderList;
    }
    public List<t_product> getAllProducts() {
        List<t_product> products = new ArrayList<>();
        String query = "SELECT product_id, name_product, category, brand, description, quantity, price, image_path FROM product";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            // قراءة البيانات من قاعدة البيانات
            while (resultSet.next()) {
                products.add(new t_product(
                        resultSet.getInt("product_id"),
                        resultSet.getString("name_product"),
                        resultSet.getString("category"),
                        resultSet.getString("brand"),
                        resultSet.getString("description"),
                        resultSet.getInt("quantity"),
                        resultSet.getDouble("price"),
                        resultSet.getString("image_path")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }
    public static ObservableList<t_product> getProductData() {
        ObservableList<t_product> productList = FXCollections.observableArrayList();
        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "12345");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM product")) {
            while (rs.next()) {
                t_product product = new t_product(
                        rs.getInt("product_id"),
                        rs.getString("name_product"),
                        rs.getString("category"),
                        rs.getString("brand"),
                        rs.getString("description"),
                        rs.getInt("quantity"),
                        rs.getInt("price"),
                        rs.getString("image_path") // إذا كان لديك حقل صورة
                );
                productList.add(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return productList;
    }
    public static int getProductIdByName(String productName) {
        String query = "SELECT product_id FROM product WHERE name_product = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, productName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("product_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public static int getProductPriceByName(String productName) {
        String query = "SELECT price FROM product WHERE name_product = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, productName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("price");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int saveOrder(table_order order) {
        int orderId = -1; // لتخزين id الذي سيتم إنشاؤه تلقائياً
        String query = "INSERT INTO \"order\" (status, total_price, first_name, date) VALUES (?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            // تعيين القيم في الاستعلام من كائن table_order
            stmt.setString(1, order.getStatus());
            stmt.setInt(2, order.getTotalprice());
            stmt.setString(3, order.getCustomer_name());
            stmt.setDate(4, new java.sql.Date(order.getDate().getTime()));

            // تنفيذ الإدراج
            int affectedRows = stmt.executeUpdate();

            // الحصول على orderId الذي تم إنشاؤه تلقائياً
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        orderId = generatedKeys.getInt(1);  // الحصول على orderId
                        order.setId(orderId); // تعيين id في الكائن
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orderId;
    }
    private void addOrderDetailsToDatabase(int orderId, List<orderdetails> orderDetailsList) {
        try {
            // Establish connection to the database
            Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "12345");

            // Prepare the SQL insert statement for order details
            String insertOrderDetailsSql = "INSERT INTO order_details (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = con.prepareStatement(insertOrderDetailsSql);

            // Loop through the list of order details and insert each one
            for (orderdetails orderDetail : orderDetailsList) {
                preparedStatement.setInt(1, orderId); // order_id
                preparedStatement.setInt(2, orderDetail.getProductId()); // product_id
                preparedStatement.setInt(3, orderDetail.getQuantity()); // quantity
                preparedStatement.setDouble(4, orderDetail.getPrice()); // price

                // Execute the insert
                preparedStatement.executeUpdate();
            }

            // Close resources
            preparedStatement.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "An error occurred while inserting order details: " + e.getMessage());
        }
    }
    public static boolean updateStock(int productId, int quantitySold) {
        String sql = "UPDATE product SET stock = quantity - ? WHERE product_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, quantitySold);
            pstmt.setInt(2, productId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }
    public static int getProductStockByName(String productName) {
        int stock = 0; // Default stock value

        String query = "SELECT quantity FROM product WHERE name_product = ?"; // Adjust the table and column names as necessary
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, productName);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                stock = rs.getInt("quantity"); // Retrieve the stock value from the result set
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stock; // Return the stock value (0 if not found or an error occurs)
    }


}
