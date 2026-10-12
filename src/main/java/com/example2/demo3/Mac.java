package com.example2.demo3;

public class Mac extends OPeratingSystem {

    public Mac(VideoFile videoFile) {
        super(videoFile);
    }
    @Override
    public void play() {
        videoFile.decode("在Mac系统上播放视频文件");
    }
}
