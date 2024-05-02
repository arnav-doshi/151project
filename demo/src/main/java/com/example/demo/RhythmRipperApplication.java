package com.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

public class RhythmRipperApplication extends Application {
    protected static Stage stage;
    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(RhythmRipperApplication.class.getResource("scenes/start-page.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 600);
        stage.setTitle("Rhythm Ripper");
        stage.setScene(scene);
        stage.show();
        Font.loadFont(getClass().getResourceAsStream("/resources/com.example.demo/fonts/MODERNA.TTF"), 12);
    }

    public static void main(String[] args) {
        launch();
    }
}
