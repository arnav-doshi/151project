package com.example.demo.controllers;

import javafx.fxml.FXML;

import java.io.IOException;

public class StartPageController extends MainController{

    @FXML
    protected void handleEnterButtonClick() throws IOException {
        loadFXMLPage("conversion-page.fxml");
    }
}
