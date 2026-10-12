package com.example2.demo3;

public class Windows extends OPeratingSystem {
    public Windows(VideoFile videoFile) {
        super(videoFile);
    }

    @Override
    public void play() {
        videoFile.decode("在Windows系统上播放视频文件");
    }
}
