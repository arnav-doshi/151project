package com.example.demo.controllers;

import com.example.demo.RhythmRipperApplication;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

public class MainController extends RhythmRipperApplication {

    public void loadFXMLPage(String fileName) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(RhythmRipperApplication.class.getResource("scenes/" + fileName));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 600);
        RhythmRipperApplication.stage.setScene(scene);
        RhythmRipperApplication.stage.show();
    }
    @FXML
    protected void handleHomeButtonClick() throws IOException {
        loadFXMLPage("conversion-page.fxml");
    }
    @FXML
    protected void handleAboutButtonClick() throws IOException {
        loadFXMLPage("about-page.fxml");
    }

    @FXML
    protected void handleExitButtonClick() {
        Platform.exit();
    }

    @FXML
    protected void handleHelpButtonClick() throws IOException {
        loadFXMLPage("help-page.fxml");
    }
}
