package io.xpire.ui;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class LoginForm implements Initializable {

    @FXML
    private JFXButton register;

    @FXML
    private JFXTextField username;

    @FXML
    private JFXButton login;

    @FXML
    private JFXPasswordField password;

    @FXML
    void handleLogin(ActionEvent event) {
        final String username = this.username.getText();
        final String password = this.password.getText();
        if (username.equals("test") && password.equals("password")) {
            System.out.println("Login successful!");
        } else {
            System.out.println("Wrong credentials");
        }
    }

    @FXML
    void handleRegister(ActionEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
