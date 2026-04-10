package com.example.am_store;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javax.swing.*;
import java.io.IOException;
import java.sql.*;

public class employee {
    @FXML
    private Button ADD;
    @FXML
    private TextField email_employee;

    @FXML
    private TextField ID_employee;

    @FXML
    private TextField Name_employee;

    @FXML
    private TextField password_employee;

    @FXML
    private TextField role_employee;

    @FXML
    private TextField salary_employee;
    @FXML
    private Button delete;

    @FXML
    private Button search;

    @FXML
    void switchtocustomer(MouseEvent event) {
        loadScene(event, "Customer.fxml");

    }

    @FXML
    void switchtoemployee(MouseEvent event) {
        loadScene(event, "employee.fxml");

    }

    @FXML
    void switchtoorder(MouseEvent event) {
        loadScene(event, "Order.fxml");

    }

    @FXML
    void switchtoproduct(MouseEvent event) {
        loadScene(event,"product.fxml");
    }

    @FXML
    void switchtostatistics(MouseEvent event) {

    }

    @FXML
    void switchtohome(MouseEvent event) {
        loadScene(event, "manager(home).fxml");
    }

    @FXML
    void Action_add(ActionEvent event) {
        if (ID_employee.getText().isEmpty() || Name_employee.getText().isEmpty() || email_employee.getText().isEmpty() || password_employee.getText().isEmpty() || role_employee.getText().isEmpty() || salary_employee.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Some fields are empty", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "12345")) {
                con.setAutoCommit(false);
                String sql = "INSERT INTO employee (employee_id, first_name, last_name, email, password, role,salary) VALUES (?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement pst = con.prepareStatement(sql)) {
                    int id = Integer.parseInt(ID_employee.getText());
                    String fullName = Name_employee.getText();
                    String[] nameParts = fullName.split(" ", 2);
                    String firstName = nameParts[0];
                    String lastName = nameParts.length > 1 ? nameParts[1] : "";
                    String email = email_employee.getText();
                    String password = password_employee.getText();
                    String role = role_employee.getText();
                    double salary = Double.parseDouble(salary_employee.getText());
                    pst.setInt(1, id);
                    pst.setString(2, firstName);
                    pst.setString(3, lastName);
                    pst.setString(4, email);
                    pst.setString(5, password);
                    pst.setString(6, role);
                    pst.setDouble(7, salary);
                    pst.executeUpdate();
                    con.commit();
                    JOptionPane.showMessageDialog(null, "Employee added successfully", "Added", JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException e) {
                    con.rollback();
                    JOptionPane.showMessageDialog(null, "Error adding employee: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Database connection error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        refreshTable();
    }

    @FXML
    void action_delete(ActionEvent event) {
        String id = JOptionPane.showInputDialog("Enter the ID number to delete:");
        if (id != null && !id.trim().isEmpty()) {
            try {
                Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "12345");
                Statement statement = con.createStatement();
                String deleteEmployeeSql = "DELETE FROM employee WHERE employee_id = " + Integer.parseInt(id);
                int rowsAffected = statement.executeUpdate(deleteEmployeeSql);

                if (rowsAffected == 1) {
                    JOptionPane.showMessageDialog(null, "The Employee with ID " + Integer.parseInt(id) + " was successfully deleted.");
                    refreshTable();
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
    void action_search(ActionEvent event) {
        String idInput = JOptionPane.showInputDialog("Enter the Employee ID:");
        if (idInput != null && !idInput.trim().isEmpty()) {
            try {
                int id = Integer.parseInt(idInput);
                loadEmployeeData(id);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid ID format. Please enter a numeric ID.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "No valid ID number was entered.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @FXML
    void action_update(ActionEvent event) {
        String idInput = ID_employee.getText();
        if (idInput.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Employee ID is required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int id = Integer.parseInt(idInput);
        StringBuilder sqlBuilder = new StringBuilder("UPDATE employee SET ");
        boolean isFirst = true;
        if (!Name_employee.getText().isEmpty()) {
            String fullName = Name_employee.getText();
            String[] nameParts = fullName.split(" ", 2);
            String firstName = nameParts[0];
            String lastName = nameParts.length > 1 ? nameParts[1] : "";

            sqlBuilder.append("first_name = ?, last_name = ? ");
            isFirst = false;
        }
        if (!email_employee.getText().isEmpty()) {
            if (!isFirst) sqlBuilder.append(", ");
            sqlBuilder.append("email = ? ");
            isFirst = false;
        }
        if (!password_employee.getText().isEmpty()) {
            if (!isFirst) sqlBuilder.append(", ");
            sqlBuilder.append("password = ? ");
            isFirst = false;
        }
        if (!role_employee.getText().isEmpty()) {
            if (!isFirst) sqlBuilder.append(", ");
            sqlBuilder.append("role = ? ");
            isFirst = false;
        }
        if (!salary_employee.getText().isEmpty()) {
            if (!isFirst) sqlBuilder.append(", ");
            sqlBuilder.append("salary = ? ");
        }
        sqlBuilder.append(" WHERE employee_id = ?");
        try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "12345");
             PreparedStatement pst = con.prepareStatement(sqlBuilder.toString())) {
            int paramIndex = 1;
            if (!Name_employee.getText().isEmpty()) {
                String fullName = Name_employee.getText();
                String[] nameParts = fullName.split(" ", 2);
                pst.setString(paramIndex++, nameParts[0]);
                pst.setString(paramIndex++, nameParts.length > 1 ? nameParts[1] : "");
            }
            if (!email_employee.getText().isEmpty()) {
                pst.setString(paramIndex++, email_employee.getText());
            }
            if (!password_employee.getText().isEmpty()) {
                pst.setString(paramIndex++, password_employee.getText());
            }
            if (!role_employee.getText().isEmpty()) {
                pst.setString(paramIndex++, role_employee.getText());
            }
            if (!salary_employee.getText().isEmpty()) {
                pst.setDouble(paramIndex++, Double.parseDouble(salary_employee.getText()));
            }
            pst.setInt(paramIndex, id);
            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(null, "Employee updated successfully", "Updated", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "No employee found with that ID", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error updating employee: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        refreshTable();
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
    private TableView<T_employee> table_employee;
    @FXML
    private TableColumn<T_employee, Integer> ID;
    @FXML
    private TableColumn<T_employee, String> Name;
    @FXML
    private TableColumn<T_employee, String> Email;
    @FXML
    private TableColumn<T_employee, String> Password;
    @FXML
    private TableColumn<T_employee, String> Role;
    @FXML
    private TableColumn<T_employee, Double> Salary;

    @FXML
    public void initialize() {
        ID.setCellValueFactory(new PropertyValueFactory<>("id"));
        Name.setCellValueFactory(new PropertyValueFactory<>("name"));
        Email.setCellValueFactory(new PropertyValueFactory<>("email"));
        Password.setCellValueFactory(new PropertyValueFactory<>("password"));
        Salary.setCellValueFactory(new PropertyValueFactory<>("salary"));
        Role.setCellValueFactory(new PropertyValueFactory<>("role"));
        ObservableList<T_employee> employeeData = Database.getEmployeeData();
        table_employee.setItems(employeeData);
    }
    private void refreshTable() {
        ObservableList<T_employee> employeeData = Database.getEmployeeData();
        table_employee.setItems(employeeData);
    }
    public void loadEmployeeData(int id) {
        String sql = "SELECT first_name, last_name, email, password, role, salary FROM employee WHERE employee_id = ?";
        try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "12345");
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String email = rs.getString("email");
                String password = rs.getString("password");
                String role = rs.getString("role");
                double salary = rs.getDouble("salary");
                ID_employee.setText(String.valueOf(id));
                Name_employee.setText(firstName + " " + lastName);
                email_employee.setText(email);
                password_employee.setText(password);
                role_employee.setText(role);
                salary_employee.setText(String.valueOf(salary));
            } else {
                JOptionPane.showMessageDialog(null, "No employee found with that ID.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error loading employee data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
