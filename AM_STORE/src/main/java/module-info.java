module com.example.am_store {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.postgresql.jdbc;
    requires java.desktop;
    requires jasperreports;
    opens com.example.am_store to javafx.fxml;
    exports com.example.am_store;
}