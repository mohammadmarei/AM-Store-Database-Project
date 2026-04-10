package com.example.am_store;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class producte {
    private String selectedImagePath = null; // متغير لتخزين مسار الصورة
    private ArrayList<String> typeList;
    @FXML
    public void initialize() {

        typeList = new ArrayList<>();
        typeList.add("Smart phones");
        typeList.add("Monitors");
        typeList.add("Laptops");
        typeList.add("Gaming");
        typeList.add("Printers");
        typeList.add("Accessories");
        pr_cat.getItems().addAll(typeList);
        ID.setCellValueFactory(new PropertyValueFactory<>("id"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        category.setCellValueFactory(new PropertyValueFactory<>("category"));
        brand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        ObservableList<t_product> Data = Database.getProductData();
        t_product.setItems(Data);
    }
    @FXML
    private TextField pr_beand;
    @FXML
    private TextField pr_description;

    @FXML
    private TextField pr_id;

    @FXML
    private TextField pr_name;

    @FXML
    private TextField pr_price;

    @FXML
    private TextField pr_quantity;

    @FXML
    private ComboBox<String> pr_cat;
    @FXML
    private TableColumn<t_product, Integer> ID;
    @FXML
    private TableColumn<t_product, String> brand;

    @FXML
    private TableColumn<t_product, String> category;

    @FXML
    private Button deleteProductButton;

    @FXML
    private TableColumn<t_product, String> description;
    @FXML
    private TableColumn<t_product, String> name;
    @FXML
    private TableColumn<t_product, Double> price;

    @FXML
    private TableColumn<t_product, Integer> quantity;
    @FXML
    private TableView<t_product> t_product;
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
    void action_add(ActionEvent event) {
        if (pr_id.getText().isEmpty() || pr_name.getText().isEmpty() || pr_cat.getSelectionModel().isEmpty() ||
                pr_beand.getText().isEmpty() || pr_description.getText().isEmpty() ||
                pr_quantity.getText().isEmpty() || pr_price.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Some fields are empty", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            if (selectedImagePath == null) {
                JOptionPane.showMessageDialog(null, "No image selected. Please add an image.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "12345")) {
                con.setAutoCommit(false);

                String sql = "INSERT INTO product (product_id, name_product, category, brand, description, quantity, price, image_path) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement pst = con.prepareStatement(sql)) {
                    int id = Integer.parseInt(pr_id.getText());
                    String fullName = pr_name.getText();
                    String category = pr_cat.getSelectionModel().getSelectedItem();
                    String brand = pr_beand.getText();
                    String description = pr_description.getText();
                    int quantity = Integer.parseInt(pr_quantity.getText());
                    double price = Double.parseDouble(pr_price.getText());

                    // إعداد القيم للاستعلام
                    pst.setInt(1, id);
                    pst.setString(2, fullName);
                    pst.setString(3, category);
                    pst.setString(4, brand);
                    pst.setString(5, description);
                    pst.setInt(6, quantity);
                    pst.setDouble(7, price);
                    pst.setString(8, selectedImagePath);

                    pst.executeUpdate();
                    con.commit();

                    JOptionPane.showMessageDialog(null, "Product added successfully", "Added", JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException e) {
                    con.rollback();
                    JOptionPane.showMessageDialog(null, "Error adding product: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Database connection error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        refreshTable();
    }

    @FXML
    void add_image(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select an Image");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            selectedImagePath = selectedFile.getAbsolutePath();
            JOptionPane.showMessageDialog(null, "Image selected: " + selectedImagePath, "Image Selected", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "No image selected.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }
    @FXML
    void action_delete(ActionEvent event) {
        String id = JOptionPane.showInputDialog("Enter the ID number to delete:");
        if (id != null && !id.trim().isEmpty()) {
            try {
                Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "12345");
                Statement statement = con.createStatement();
                String deleteEmployeeSql = "DELETE FROM product WHERE product_id = " + Integer.parseInt(id);
                int rowsAffected = statement.executeUpdate(deleteEmployeeSql);

                if (rowsAffected == 1) {
                    JOptionPane.showMessageDialog(null, "The Employee with ID " + Integer.parseInt(id) + " was successfully deleted.");

                } else {
                    JOptionPane.showMessageDialog(null, "The entered ID " + Integer.parseInt(id) + " was not found.");
                }
                statement.close();
                con.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "An error occurred: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "No valid ID number was entered.");
        }
        refreshTable();
    }
    @FXML
    void action_update(ActionEvent event) {
        String idInput = pr_id.getText();
        if (idInput.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Product ID is required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int id = Integer.parseInt(idInput);
        StringBuilder sqlBuilder = new StringBuilder("UPDATE product SET ");
        boolean isFirst = true;

        // Building the SQL query dynamically based on non-empty fields
        if (!pr_name.getText().isEmpty()) {
            sqlBuilder.append("name_product = ? ");
            isFirst = false;
        }
        if (!pr_cat.getSelectionModel().isEmpty()) {
            if (!isFirst) sqlBuilder.append(", ");
            sqlBuilder.append("category = ? ");
            isFirst = false;
        }
        if (!pr_beand.getText().isEmpty()) {
            if (!isFirst) sqlBuilder.append(", ");
            sqlBuilder.append("brand = ? ");
            isFirst = false;
        }
        if (!pr_description.getText().isEmpty()) {
            if (!isFirst) sqlBuilder.append(", ");
            sqlBuilder.append("description = ? ");
            isFirst = false;
        }
        if (!pr_price.getText().isEmpty()) {
            if (!isFirst) sqlBuilder.append(", ");
            sqlBuilder.append("price = ? ");
            isFirst = false;
        }
        if (!pr_quantity.getText().isEmpty()) {
            if (!isFirst) sqlBuilder.append(", ");
            sqlBuilder.append("quantity = ? ");
            isFirst = false;
        }

        // Only update image if the "image" button is pressed and the path is set
        if (selectedImagePath != null) {
            if (!isFirst) sqlBuilder.append(", ");
            sqlBuilder.append("image_path = ? ");
        }

        sqlBuilder.append(" WHERE product_id = ?");

        try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "12345");
             PreparedStatement pst = con.prepareStatement(sqlBuilder.toString())) {
            int paramIndex = 1;

            // Setting parameters for non-empty fields
            if (!pr_name.getText().isEmpty()) {
                pst.setString(paramIndex++, pr_name.getText());
            }
            if (!pr_cat.getSelectionModel().isEmpty()) {
                pst.setString(paramIndex++, pr_cat.getSelectionModel().getSelectedItem().toString());
            }
            if (!pr_beand.getText().isEmpty()) {
                pst.setString(paramIndex++, pr_beand.getText());
            }
            if (!pr_description.getText().isEmpty()) {
                pst.setString(paramIndex++, pr_description.getText());
            }
            if (!pr_price.getText().isEmpty()) {
                pst.setDouble(paramIndex++, Double.parseDouble(pr_price.getText()));
            }
            if (!pr_quantity.getText().isEmpty()) {
                pst.setInt(paramIndex++, Integer.parseInt(pr_quantity.getText()));
            }
            // If image path was selected, set it
            if (selectedImagePath != null) {
                pst.setString(paramIndex++, selectedImagePath);
            }

            // Setting the product ID for the WHERE clause
            pst.setInt(paramIndex, id);

            // Executing the update
            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(null, "Product updated successfully", "Updated", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "No product found with that ID", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error updating product: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        refreshTable();
    }



    @FXML
    void action_search(ActionEvent event) {
        String idInput = JOptionPane.showInputDialog("Enter the Employee ID:");
        if (idInput != null && !idInput.trim().isEmpty()) {
            try {
                int id = Integer.parseInt(idInput);
                loadProductData(id);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid ID format. Please enter a numeric ID.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "No valid ID number was entered.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void loadProductData(int id) {
        String sql = "SELECT name_product, category, brand, description, price, quantity FROM product WHERE product_id = ?";
        try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "12345");
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, id); // Bind the product ID
            ResultSet rs = pst.executeQuery(); // Execute the query

            if (rs.next()) {
                // Retrieve the product data
                String name = rs.getString("name_product");
                String category = rs.getString("category");
                String brand = rs.getString("brand");
                String description = rs.getString("description");
                double price = rs.getDouble("price");
                int quantity = rs.getInt("quantity");


                // Populate the UI components
                pr_id.setText(String.valueOf(id));
                pr_name.setText(name != null ? name : "");
                pr_cat.setValue(category != null ? category : ""); // Assuming this is a ComboBox
                pr_beand.setText(brand != null ? brand : "");
                pr_description.setText(description != null ? description : "");
                pr_price.setText(String.valueOf(price));
                pr_quantity.setText(String.valueOf(quantity));
            } else {
                JOptionPane.showMessageDialog(null, "No product found with that ID.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error loading product data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    @FXML
    void action_view(ActionEvent event) {
        loadScene2(event,"maincategoryemployee.fxml");

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
    private void refreshTable() {
        ObservableList<t_product> Data = Database.getProductData();
        t_product.setItems(Data);

    }
}

