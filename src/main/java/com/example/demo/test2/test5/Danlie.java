package com.example.demo.test2.test5;

public class Danlie {

    private  Danlie() {

    }


    private static Danlie instance =  new Danlie();

    public static Danlie getInstance() {
        return instance;
    }
}
