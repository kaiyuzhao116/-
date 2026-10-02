package com.example.demo4;

public class Direecter {

    //声明Builder类型的变量
    private Builder builder;

    public Direecter(Builder builder) {
        this.builder = builder;
    }


    public Bike construct() {
        builder.buildFrame();
        builder.buildSeat();
        return builder.construct();
    }


}
