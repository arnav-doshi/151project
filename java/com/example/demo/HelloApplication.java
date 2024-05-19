package com.example.demo;

import javafx.animation.FillTransition;
import javafx.animation.Interpolator;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.Desktop;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HelloApplication extends Application {

    private ProgressBar progressBar;
    private Button playButton;
    private Button openVideoButton;
    private ProgressBar playbackProgressBar;
    private MediaPlayer mediaPlayer;
    private String mp4FilePath;
    private boolean isPlaying = false;

    public HelloApplication() throws FileNotFoundException {
    }

    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        Pane conversionScreen = createConversionScreen(primaryStage);
        primaryStage.setScene(new Scene(conversionScreen, 400, 550));
        primaryStage.setTitle("RhythmmRipper - Arnav/Owen/Sid");
        conversionScreen.requestFocus();
        primaryStage.show();
    }

    private StackPane createWelcomeScreen() {
        StackPane stackPane = new StackPane();
        Text welcomeText = new Text("Welcome to Rhythmm Ripper");
        stackPane.getChildren().add(welcomeText);
        return stackPane;
    }

    private Pane createConversionScreen(Stage primaryStage) throws FileNotFoundException {
        Font customFont = Font.loadFont(new FileInputStream(new File("src\\main\\resources\\SF-Pro-Rounded-Regular.ttf")), 20);

        VBox conversionScreen = new VBox(10);
        conversionScreen.setStyle("-fx-background-color: #eeeeee;");
        conversionScreen.setAlignment(Pos.CENTER);

        Text title = new Text("rhythmmripper");
        title.setFont(Font.loadFont("file:src/main/resources/moderna/MODERNA_.TTF", 45));

        for (int i = 0; i < title.getText().length(); i++) {
            FillTransition fillTransition = new FillTransition(Duration.seconds(1));
            fillTransition.setFromValue(Color.web("#B8DBD9"));
            fillTransition.setToValue(Color.web("#586F7C"));
            fillTransition.setCycleCount(Timeline.INDEFINITE);
            fillTransition.setAutoReverse(true);
            fillTransition.setShape(title);
            fillTransition.setInterpolator(Interpolator.LINEAR);
            fillTransition.setDelay(Duration.seconds(i * 0.1));
            fillTransition.play();
        }

        TextField textField = new TextField();
        textField.setFont(customFont);
        textField.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5); " +
                "-fx-text-fill: white; " +
                "-fx-padding: 8px 12px; " +
                "-fx-border-color: transparent; " +
                "-fx-border-radius: 5px; " +
                "-fx-background-radius: 5px;");
        textField.setAlignment(Pos.CENTER);
        textField.setMaxWidth(300);
        textField.setPromptText("Enter YouTube link");

        Button convertButton = new Button("Convert");

        convertButton.setStyle("-fx-background-color: #0070c9; " +
                "-fx-text-fill: white; " +
                "-fx-font-size: 14px; " +
                "-fx-padding: 8px 16px; " +
                "-fx-border-color: transparent; " +
                "-fx-border-radius: 5px; " +
                "-fx-background-radius: 5px; " +
                "-fx-cursor: hand;");
        convertButton.setFont(customFont);
        convertButton.setMaxWidth(85);

        TextArea outputTextArea = new TextArea();
        outputTextArea.setFont(customFont);
        outputTextArea.setStyle("-fx-control-inner-background: rgba(0, 0, 0, 0.5); " +
                "-fx-text-fill: white; " +
                "-fx-font-family: 'Helvetica'; " +
                "-fx-font-size: 14px; " +
                "-fx-padding: 8px; " +
                "-fx-border-color: transparent; " +
                "-fx-border-radius: 5px; " +
                "-fx-background-radius: 5px; " +
                "-fx-background-color: transparent; " +
                "-fx-hbar-policy: never; " +
                "-fx-vbar-policy: never;");

        outputTextArea.setEditable(false);
        outputTextArea.setWrapText(true);
        outputTextArea.setMaxWidth(350);
        outputTextArea.setMaxHeight(200);
        outputTextArea.setVisible(false);

        progressBar = new ProgressBar();
        progressBar.setVisible(false);

        playButton = new Button("Play/Pause");
        playButton.setFont(customFont);
        playButton.setStyle("-fx-background-color: #0070c9; " +
                "-fx-text-fill: white; " +
                "-fx-font-size: 16px; " +
                "-fx-padding: 8px 16px; " +
                "-fx-border-color: transparent; " +
                "-fx-border-radius: 5px; " +
                "-fx-background-radius: 5px; " +
                "-fx-cursor: hand;");
        playButton.setVisible(false);

        openVideoButton = new Button("Open Video");
        openVideoButton.setFont(customFont);
        openVideoButton.setStyle("-fx-background-color: #0070c9; " +
                "-fx-text-fill: white; " +
                "-fx-font-size: 16px; " +
                "-fx-padding: 8px 16px; " +
                "-fx-border-color: transparent; " +
                "-fx-border-radius: 5px; " +
                "-fx-background-radius: 5px; " +
                "-fx-cursor: hand;");
        openVideoButton.setVisible(false);

        convertButton.setOnAction(e -> {
            String youtubeLink = textField.getText();

            if (youtubeLinkVerify(youtubeLink)) {
                executePy(youtubeLink, outputTextArea);

                outputTextArea.setVisible(true);
                progressBar.setVisible(true);
                playButton.setVisible(true);
                playbackProgressBar.setVisible(true);
                openVideoButton.setVisible(true); // Show the video button when conversion is complete
            }
        });

        playbackProgressBar = new ProgressBar(0);
        playbackProgressBar.setStyle("-fx-accent: #007AFF; " +
                "-fx-background-color: #007AFF; " +
                "-fx-background-insets: 0; " +
                "-fx-padding: -1;" +
                "-fx-margin: 0;");
        playbackProgressBar.setPrefHeight(10);
        playbackProgressBar.setPrefWidth(250);
        playbackProgressBar.setVisible(false);

        playButton.setOnAction(e -> {
            if (mediaPlayer != null) {
                if (!isPlaying) {
                    mediaPlayer.play();
                    isPlaying = true;
                } else {
                    mediaPlayer.pause();
                    isPlaying = false;
                }
            }
        });

        openVideoButton.setOnAction(e -> openVideo(mp4FilePath));

        playbackProgressBar.setOnMousePressed(e -> {
            if (mediaPlayer != null) {
                double mouseX = e.getX();
                double progress = mouseX / playbackProgressBar.getWidth();
                mediaPlayer.seek(Duration.seconds(progress * mediaPlayer.getTotalDuration().toSeconds()));
            }
        });

        conversionScreen.getChildren().addAll(title, textField, convertButton, outputTextArea, playButton, openVideoButton, playbackProgressBar);

        return conversionScreen;
    }

    private void openVideo(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("File does not exist!");
            return;
        }

        if (!Desktop.isDesktopSupported()) {
            System.out.println("Desktop is not supported!");
            return;
        }

        Desktop desktop = Desktop.getDesktop();
        try {
            desktop.open(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void executePy(String youtubeLink, TextArea outputTextArea) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("src/main/resources/com/example/demo/dist/ytmp3.exe");
            Process process = processBuilder.start();

            new Thread(() -> {
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        final String finalLine = line;
                        Platform.runLater(() -> outputTextArea.appendText(finalLine + "\n"));

                        String mp3FilePath = null;
                        if (line.startsWith("Audio File: ")) {
                            mp3FilePath = line.substring("Audio File: ".length()).replace("\b", "\\");
                            System.out.println("MP3 File Path: " + mp3FilePath);
                            Platform.runLater(() -> progressBar.setVisible(false));
                            playMP3(mp3FilePath);
                        }else if(line.startsWith("Video File: ")){
                            mp4FilePath = line.substring("Video File: ".length()).replace("\b", "\\");

                        }
                    }
                } catch (Exception e) {
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

            mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    double progress = newValue.toSeconds() / mediaPlayer.getTotalDuration().toSeconds();
                    Platform.runLater(() -> playbackProgressBar.setProgress(progress));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean youtubeLinkVerify(String youtubeUrl) {
        try {
            URL url = new URL(youtubeUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode >= 200 && responseCode < 300) {
                return true;
            } else {
                showErrorPopup("YouTube link is not valid or reachable (HTTP status code: " + responseCode + ")");
                return false;
            }
        } catch (IOException e) {
            showErrorPopup("Error verifying YouTube link: " + e.getMessage());
            return false;
        }
    }

    public static void showErrorPopup(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
