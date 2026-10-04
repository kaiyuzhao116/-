package com.example.demo8;

public class SDcardOImpL implements SDcard {
    @Override
    public String readSD() {
        return "SDcard 实现读取数据";
    }

    @Override
    public void writeSD(String data) {
        System.out.println("SDcard 实现写入数据：" + data);
    }
}
