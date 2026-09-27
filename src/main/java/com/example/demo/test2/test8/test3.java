package com.example.demo.test2.test8;

import java.lang.reflect.Constructor;

public class test3 {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello World!");

        Class Class = Singleton.class;


        Constructor cons= Class.getDeclaredConstructor();
        cons.setAccessible(true);

        Singleton o = (Singleton)cons.newInstance();
        Singleton o2 = (Singleton)cons.newInstance();
        System.out.println(o == o2);
    }
}
