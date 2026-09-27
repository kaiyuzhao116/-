package com.example.demo.test2.test8;

import com.example.demo.test2.test7.Singlon;

public class test2 {
    public static void main(String[] args) {
        Singleton instance = Singleton.getInstance();
        Singleton  instance2 = Singleton.getInstance();

        System.out.println(instance2 == instance);  // true
    }
}
