package com.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class HelloController {
    @FXML
    private TextField urlTextField;

    @FXML
    private void convertButtonAction() {
        String youtubeUrl = urlTextField.getText();

    }

    @FXML
    private void downloadButtonAction() {

    }
}