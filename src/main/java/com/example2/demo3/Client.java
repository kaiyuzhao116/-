package com.example2.demo3;

public class Client {
    public static void main(String[] args) {
        VideoFile videoFile = new RmVFile();
        OPeratingSystem os = new Windows(videoFile);
        os.play();

        System.out.println("----------");

        // Mac播放RMVB
        os = new Mac(videoFile);
        os.play();

        System.out.println("----------");
        // Mac播放AVI
        VideoFile videoFile2 = new AVIFile();
        os = new Mac(videoFile2);
        os.play();

    }
}
