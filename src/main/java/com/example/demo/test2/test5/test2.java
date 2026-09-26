package com.example.demo.test2.test5;

public class test2 {
    public static void main(String[] args) {
        Danlie d = Danlie.getInstance();
        Danlie instance = Danlie.getInstance();
        System.out.println(d == instance);  // true
    }
}
