package com.example2.demo3;

public class RmVFile implements VideoFile {
    @Override
    public void decode(String fileName) {
        System.out.println("RMVB格式视频，解码播放: " + fileName);
    }
}
