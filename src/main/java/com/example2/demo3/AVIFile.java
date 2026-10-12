package com.example2.demo3;

public class AVIFile implements VideoFile {
    @Override
    public void decode(String fileName) {
        System.out.println("AVI格式视频，解码播放：" + fileName);
    }
}
