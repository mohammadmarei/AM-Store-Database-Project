package com.example.am_store;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;


public class Cars implements Initializable {


    @FXML
    private Button FALSE;

    @FXML
    private ImageView ShowCars;

    @FXML
    private Button TRUE;

    @FXML
    private Button View;

    @FXML
    void FALSECAR(ActionEvent event) {

    }

    @FXML
    void TRUECAR(ActionEvent event) {

    }

    @FXML
    void ViewCars(ActionEvent event) {

    }
    @FXML
    void UPDATCAR(ActionEvent event) {

    }
    private ObservableList<orderdetails> orderDetailsList = FXCollections.observableArrayList();

    @FXML
    void add(MouseEvent event) {

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
