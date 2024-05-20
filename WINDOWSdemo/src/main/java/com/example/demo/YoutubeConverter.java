//package com.example.demo;
//
//import java.io.File;
//import java.net.URL;
//import com.github.axet.vget.VGet;
//
//public class YoutubeConverter {
//
//    private final String API_KEY = "AIzaSyCGCdcUf7_ikXjXr9Hxg6h4lWzCAwCC_zc";
//
//    // Uses Youtube Data Api v3 to download video to user's computer
//    public static void downloadVideo(String youtubeLink) {
//        try {
//            String url = youtubeLink;
//            // Specify the absolute path to the desktop
//            String desktopPath = System.getProperty("user.home") + "/Desktop/";
//
//            // Set a timeout for the download operation (in milliseconds)
//            int timeout = 60000; // 60 seconds
//
//            // Create a new VGet object with the timeout
//            VGet v = new VGet(new URL(url));
//            v.download();
//        } catch (Exception e) {
//            // Print the exception message for debugging
//            e.printStackTrace();
//            throw new RuntimeException(e);
//        }
//    }
//
//
//    // public File convertToMp3(File file) {
//    // }
//
//    public static void main(String[] args) {
//        YoutubeConverter converter = new YoutubeConverter();
//
//        // Replace the YouTube link with the desired video link
//        String youtubeLink = "https://www.youtube.com/watch?v=dQw4w9WgXcQ&ab_channel=RickAstley";
//
//        // Call the downloadVideo method
//        converter.downloadVideo(youtubeLink);
//
//        System.out.println("Video downloaded successfully to desktop!");
//    }
//}
