package com.example.demo.controllers;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.awt.*;
import java.io.*;

public class ConversionPageController extends MainController {
    @FXML
    TextField youtubeLinkTextField;
    @FXML
    TextArea conversionTextArea;
    @FXML
    ProgressBar playbackProgressBar;
    @FXML
    ImageView rewind, play, fastForward, pause;
    @FXML
    Text convertButton, progressText, insertLinkText, openVideoButton;
    @FXML
    Rectangle convertButtonBox, openVideoButtonBox;

    private String mp4FilePath;

    static MediaPlayer mediaPlayer;

    //Ensures media pauses when navigating to diff page
    @Override
    public void loadFXMLPage(String fileName) throws IOException {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
        super.loadFXMLPage(fileName);
    }
    @FXML
    protected void handleConvertButtonClick() {
        String youtubeLink = youtubeLinkTextField.getText();
        if (!youtubeLink.isEmpty()) {
            System.out.println("Converting to mp3...");
            convertButton.setVisible(false);
            convertButtonBox.setVisible(false);
            progressText.setVisible(true);
            executePy(youtubeLink, conversionTextArea);
        }
    }

    @FXML
    protected void handlePlayButtonClick() {
        if (mediaPlayer != null) {
            mediaPlayer.play();
            play.setVisible(false);
            pause.setVisible(true);
        }
    }

    @FXML
    protected void handlePauseButtonClick() {
        if (mediaPlayer != null) {
            if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                mediaPlayer.pause();
                play.setVisible(true);
                pause.setVisible(false);
            }
        }
    }

    @FXML
    protected void handleRewindButtonClick() {
        if (mediaPlayer != null) {
            mediaPlayer.seek(Duration.ZERO);
        }
    }

    //fast forwards 10 sec
    @FXML
    protected void handleFastForwardButtonClick() {
        if (mediaPlayer != null) {
            Duration currentTime = mediaPlayer.getCurrentTime();
            Duration newTime = currentTime.add(Duration.seconds(10));
            mediaPlayer.seek(newTime);
        }
    }

    @FXML
    protected void handleOpenVideoButtonClick() {
        openVideo(mp4FilePath);
    }

    private void executePy(String youtubeLink, TextArea outputTextArea) {
        try {
            //tring projectDir = System.getProperty("user.dir");
            ProcessBuilder processBuilder = new ProcessBuilder("src/main/resources/com/example/demo/dist/ytmp3.exe");

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

                        if (line.startsWith("Audio File: ")) {

                            String mp3FilePath = line.substring("Audio File: ".length()).replace("\b", "\\");

                            Platform.runLater(() -> {
                                //progressBar.setVisible(false);
                                play.setVisible(true);
                                //pauseButton.setVisible(false);
                                playbackProgressBar.setVisible(true);

                            });

                            playMP3(mp3FilePath);
                        }
                        if (line.startsWith("Download failed!!!!")) {
                            convertButtonBox.setVisible(true);
                            convertButton.setVisible(true);
                            progressText.setVisible(false);
                            insertLinkText.setText("Invalid link - enter a new YouTube link to convert");
                            break;
                        }
                        if (line.startsWith("Video File: ")) {
                            mp4FilePath = line.substring("Video File: ".length()).replace("\b", "\\");
                            openVideoButton.setVisible(true);
                            openVideoButtonBox.setVisible(true);
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

            //mediaPlayer.play();
            fastForward.setVisible(true);
            rewind.setVisible(true);
            progressText.setText("Successfully downloaded to your desktop!");

        } catch (Exception e) {
            e.printStackTrace();
        }
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
}
