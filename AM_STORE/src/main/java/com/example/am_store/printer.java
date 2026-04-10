package com.example.am_store;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class printer implements Initializable {

    @FXML
    private Button AddCars;

    @FXML
    private Button AddPart;

    @FXML
    private Label NUBEROFCARINCARAG;

    @FXML
    private Label TotalPrice;

    @FXML
    private GridPane gridPane;

    @FXML
    private ScrollPane scrollpaneShowCarInhome;

    @FXML
    private TableColumn<orderdetails, Integer> id_product;

    @FXML
    private TableColumn<orderdetails, String> name_product;

    @FXML
    private TableColumn<orderdetails, Integer> price;

    @FXML
    private TableColumn<orderdetails, Integer> quantity;

    @FXML
    private TableView<orderdetails> table_orderdetails;

    private ObservableList<orderdetails> orderDetailsList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupScrollPane();
        setupTableView();
        displayProducts();
    }

    private void setupScrollPane() {
        scrollpaneShowCarInhome.setFitToWidth(true);
        scrollpaneShowCarInhome.setFitToHeight(true);
        scrollpaneShowCarInhome.setPrefWidth(862);
        scrollpaneShowCarInhome.setPrefHeight(454);
    }

    private void setupTableView() {
        id_product.setCellValueFactory(new PropertyValueFactory<>("productId"));
        name_product.setCellValueFactory(new PropertyValueFactory<>("nameProduct"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        table_orderdetails.setItems(OrderController.getInstance().getOrderDetailsList());
    }

    private void displayProducts() {
        gridPane.setVgap(10); // زيادة الفاصل العمودي بين البطاقات
        gridPane.setHgap(10); // زيادة الفاصل الأفقي بين البطاقات
        gridPane.setPrefWidth(862); // عرض GridPane
        gridPane.setPrefHeight(700); // زيادة ارتفاع GridPane للسماح بعرض جميع البطاقات دون ضغط

        int column = 0;
        int row = 0;

        // تحديد حجم ثابت لكل البطاقات
        double cardWidth = 263;
        double cardHeight = 200; // زيادة ارتفاع البطاقة لتجنب ضغط المحتوى

        List<t_product> products = fetchProductsFromDatabase();
        for (t_product product : products) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Cars.fxml"));
                AnchorPane card = loader.load();

                // تحديد أبعاد البطاقة
                card.setPrefSize(cardWidth, cardHeight);
                card.setMinSize(cardWidth, cardHeight);
                card.setMaxSize(cardWidth, cardHeight);

                // تعيين محتويات البطاقة
                ImageView showCars = (ImageView) card.lookup("#ShowCars");
                Label carName = (Label) card.lookup("#carName");
                Label carPrice = (Label) card.lookup("#carPrice");

                // ضبط الصورة
                File imageFile = new File(product.getImagePath());
                if (imageFile.exists()) {
                    showCars.setImage(new Image(imageFile.toURI().toString()));
                }
                showCars.setFitWidth(177);
                showCars.setFitHeight(120); // تصغير ارتفاع الصورة لتناسب البطاقة
                showCars.setPreserveRatio(true);
                GridPane.setHalignment(showCars, HPos.CENTER);

                // ضبط النصوص
                carName.setText(product.getName());
                carName.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
                carPrice.setText("$" + product.getPrice());
                carPrice.setStyle("-fx-font-size: 14px; -fx-text-fill: green;");

                // ضبط الحقول والأزرار
                TextField quantityField = new TextField();
                quantityField.setPromptText("Amount");
                quantityField.setStyle("-fx-min-width: 100px;");

                Button addButton = new Button("Add");
                addButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");
                addButton.setOnAction(event -> addProductToCart(product, quantityField.getText()));

                // إضافة زر الحذف
                Button deleteButton = new Button("Delete");
                deleteButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                deleteButton.setOnAction(event -> deleteProductFromCart(product));

                // إنشاء بطاقة باستخدام GridPane
                GridPane cardContent = new GridPane();
                cardContent.setVgap(5); // فاصل داخل البطاقة
                cardContent.setHgap(5);
                cardContent.setStyle("-fx-background-color: #ffffff; -fx-border-color: gray; -fx-border-width: 1px; -fx-padding: 10px;");

                // إضافة المحتوى إلى البطاقة
                cardContent.add(showCars, 0, 0, 3, 1);
                cardContent.add(carName, 0, 1);
                cardContent.add(carPrice, 2, 1);
                cardContent.add(quantityField, 0, 2, 3, 1);
                cardContent.add(addButton, 1, 3);
                cardContent.add(deleteButton, 2, 3); // إضافة زر الحذف

                // إضافة البطاقة إلى GridPane
                gridPane.add(cardContent, column, row);

                // تحديث مواقع الأعمدة والصفوف
                column++;
                if (column == 3) {
                    column = 0;
                    row++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void addProductToCart(t_product product, String quantityText) {
        if (quantityText == null || quantityText.isEmpty()) {
            showAlert("Please enter the quantity.");
            return;
        }

        try {
            int quantity = Integer.parseInt(quantityText);
            if (quantity <= 0) {
                showAlert("Please enter a valid quantity.");
                return;
            }

            int availableQuantity = getAvailableQuantityFromDatabase(product.getId());
            if (quantity > availableQuantity) {
                showAlert("Requested quantity exceeds available stock.");
                return;
            }

            orderdetails orderDetail = new orderdetails(0, product.getId(), quantity, (int) product.getPrice(), product.getName(), 0.0);
            OrderController.getInstance().getOrderDetailsList().add(orderDetail);

            updateTotalPrice();
            updateProductQuantityInDatabase(product.getId(), availableQuantity - quantity);

            if (availableQuantity == 0) {
                openFinishPage();
            }
        } catch (NumberFormatException e) {
            showAlert("Please enter a valid quantity.");
        }
    }

    private void deleteProductFromCart(t_product product) {
        // تأكيد الحذف
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirm Deletion");
        confirmationAlert.setHeaderText("Are you sure you want to delete this item?");
        confirmationAlert.setContentText(product.getName());

        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // إزالة العنصر من القائمة
            OrderController.getInstance().getOrderDetailsList().removeIf(detail -> detail.getProductId() == product.getId());

            // تحديث الجدول
            table_orderdetails.refresh();

            // تحديث السعر الإجمالي
            updateTotalPrice();

            // حذف من قاعدة البيانات (اختياري)
            deleteFromDatabase(product.getId());

            showAlert("Product deleted successfully.");
        }
    }

    private void updateTotalPrice() {
        double totalPrice = OrderController.getInstance().getOrderDetailsList()
                .stream()
                .mapToDouble(detail -> detail.getPrice() * detail.getQuantity())
                .sum();

        TotalPrice.setText("Total Price: $" + String.format("%.2f", totalPrice));
    }

    private void deleteFromDatabase(int productId) {
        String query = "UPDATE product SET quantity = quantity + 1 WHERE product_id = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, productId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private List<t_product> fetchProductsFromDatabase() {
        List<t_product> products = new ArrayList<>();
        String query = "SELECT product_id, name_product, category, brand, description, quantity, price, image_path FROM product WHERE category = 'Printers'";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

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

    private int getAvailableQuantityFromDatabase(int productId) {
        String query = "SELECT quantity FROM product WHERE product_id = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, productId);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next() ? resultSet.getInt("quantity") : 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private void updateProductQuantityInDatabase(int productId, int newQuantity) {
        String query = "UPDATE product SET quantity = ? WHERE product_id = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, newQuantity);
            statement.setInt(2, productId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void openFinishPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FinishPage.fxml"));
            Parent finishPage = loader.load();

            FinishPageController controller = loader.getController();
            controller.setOrderDetailsList(OrderController.getInstance().getOrderDetailsList(), calculateTotalPrice());

            Stage stage = new Stage();
            stage.setScene(new Scene(finishPage));
            stage.setTitle("Finish Order");
            stage.show();

            clearOrderDetails();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private double calculateTotalPrice() {
        return OrderController.getInstance().getOrderDetailsList()
                .stream()
                .mapToDouble(detail -> detail.getPrice() * detail.getQuantity())
                .sum();
    }

    private void clearOrderDetails() {
        OrderController.getInstance().getOrderDetailsList().clear();
        table_orderdetails.setItems(OrderController.getInstance().getOrderDetailsList());
    }

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "12345";

    @FXML
    void Back(MouseEvent event) {
        loadScene(event, "maincategory.fxml");
    }
    @FXML
    void switchtohome(MouseEvent event) {
        loadScene(event,"manager(home).fxml");
    }
    @FXML
    void switchtocustomer(MouseEvent event) {
        loadScene(event,"Customer.fxml");

    }
    @FXML
    void switchtoemployee(MouseEvent event) {
        loadScene(event,"employee.fxml");

    }
    @FXML
    void switchtoorder(MouseEvent event) {
        loadScene(event,"Order.fxml");

    }
    @FXML
    void switchtoproduct(MouseEvent event) {
        loadScene(event,"product.fxml");

    }
    @FXML
    void switchtostatistics(MouseEvent event) {

    }

    private void loadScene(MouseEvent event, String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent view = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(view));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void finish(ActionEvent event) {
        try {
            // حساب السعر الإجمالي
            double totalPrice = calculateTotalPrice();

            // تحميل واجهة "Finish" من ملف FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FinishPage.fxml"));
            Parent root = loader.load();

            // الحصول على الـ Controller للصفحة
            FinishPageController controller = loader.getController();

            // تمرير السعر الإجمالي إلى الصفحة الثانية
            controller.setTotalPrice(totalPrice);

            // إنشاء نافذة جديدة (Stage) لعرض واجهة "Finish"
            Stage stage = new Stage();
            stage.setTitle("Finish Order");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
