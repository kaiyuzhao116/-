package com.example.demo.test2.test6;

import com.example.demo.test2.test5.Danlie;

public class test2 {
    public static void main(String[] args) {
        Singleton d = Singleton.getInstance();
        Singleton    instance = Singleton.getInstance();
        System.out.println(d == instance);  // true
    }
}
