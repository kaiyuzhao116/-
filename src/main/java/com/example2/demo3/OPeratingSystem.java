package com.example2.demo3;

public abstract class OPeratingSystem {
    protected VideoFile videoFile;

    public OPeratingSystem(VideoFile videoFile) {
        this.videoFile = videoFile;
    }

    public abstract void play();
}
