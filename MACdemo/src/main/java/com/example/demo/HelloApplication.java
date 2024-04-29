package com.example.demo;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.*;

public class HelloApplication extends Application {

    private ProgressBar progressBar;
    private Button playButton;
    private Button pauseButton;
    private ProgressBar playbackProgressBar;
    private MediaPlayer mediaPlayer;

    @Override
    public void start(Stage primaryStage) {
        StackPane welcomeScreen = createWelcomeScreen();

        StackPane conversionScreen = createConversionScreen(primaryStage);
        Button enterButton = new Button("Enter");
        enterButton.setOnAction(e -> primaryStage.setScene(new Scene(conversionScreen, 400, 400)));
        VBox welcomeLayout = new VBox(20, welcomeScreen, enterButton);
        welcomeLayout.setAlignment(Pos.CENTER);

        primaryStage.setScene(new Scene(welcomeLayout, 400, 400));
        primaryStage.setTitle("RhythmmRipper - Arnav/Owen/Sid");
        primaryStage.show();
    }

    private StackPane createWelcomeScreen() {
        StackPane stackPane = new StackPane();
        Text welcomeText = new Text("Welcome to Rhythmm Ripper");
        stackPane.getChildren().add(welcomeText);
        return stackPane;
    }

    private StackPane createConversionScreen(Stage primaryStage) {
        StackPane conversionScreen = new StackPane();

        TextField textField = new TextField();
        Button convertButton = new Button("Convert");

        TextArea outputTextArea = new TextArea();
        outputTextArea.setEditable(false);
        outputTextArea.setWrapText(true);
        outputTextArea.setMaxWidth(300);
        outputTextArea.setMaxHeight(150);

        progressBar = new ProgressBar();
        progressBar.setVisible(false);

        playButton = new Button("Play");
        playButton.setVisible(false);
        playButton.setOnAction(e -> {
            if (mediaPlayer != null) {
                if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                    mediaPlayer.pause();
                } else {
                    mediaPlayer.play();
                }
            }
        });

        pauseButton = new Button("Pause");
        pauseButton.setVisible(false);
        pauseButton.setOnAction(e -> {
            if (mediaPlayer != null) {
                mediaPlayer.pause();
            }
        });

        playbackProgressBar = new ProgressBar(0);
        playbackProgressBar.setVisible(false);

        convertButton.setOnAction(e -> {
            String youtubeLink = textField.getText();
            executePy(youtubeLink, outputTextArea);
        });

        Text title = new Text("Rhythmm Ripper");
        title.setStyle("-fx-font-size: 20px;");

        VBox vbox = new VBox(10, title, textField, convertButton, progressBar, playButton, pauseButton, outputTextArea, playbackProgressBar);
        vbox.setAlignment(Pos.CENTER);
        conversionScreen.getChildren().add(vbox);

        return conversionScreen;
    }

    private void executePy(String youtubeLink, TextArea outputTextArea) {
        try {
           // ProcessBuilder processBuilder = new ProcessBuilder("src/main/resources/com/example/demo/dist/ytmp3.exe");
            ProcessBuilder processBuilder = new ProcessBuilder("src/main/resources/com/example/demo/ytenv/build/exe.macosx-14.0-arm64-3.12/ytmp3");
            Process process = processBuilder.start();

            new Thread(() -> {
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        final String finalLine = line; // Create final variable for lambda
                        Platform.runLater(() -> {
                            outputTextArea.appendText(finalLine + "\n");
                        });

                        if (line.startsWith("Download complete:!!! ")) {

                            String mp3FilePath = line.substring("Download complete:!!! ".length()).replace("\b", "\\");

                            Platform.runLater(() -> {
                                progressBar.setVisible(false);
                                playButton.setVisible(true);
                                pauseButton.setVisible(true);
                                playbackProgressBar.setVisible(true);
                            });

                            playMP3(mp3FilePath);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()))) {
                writer.write(youtubeLink);
                writer.newLine();
                writer.flush();
            }
        } catch (IOException e) {
            System.err.println("Error starting/executing the executable: " + e.getMessage());
        }
    }

    private void playMP3(String filePath) {
        try {

            Media media = new Media(new File(filePath).toURI().toString());


            mediaPlayer = new MediaPlayer(media);

            mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
                @Override
                public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                    if (newValue != null) {
                        double progress = newValue.toSeconds() / mediaPlayer.getTotalDuration().toSeconds();
                        Platform.runLater(() -> {
                            playbackProgressBar.setProgress(progress);
                        });
                    }
                }
            });

            mediaPlayer.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
