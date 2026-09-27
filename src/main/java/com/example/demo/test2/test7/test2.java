package com.example.demo.test2.test7;

import com.example.demo.test2.test6.Singleton;

public class test2 {
    public static void main(String[] args) {
        Singlon instance = Singlon.getInstance();
        Singlon  instance2 = Singlon.getInstance();

        System.out.println(instance2 == instance);  // true
    }
}
