package com.example.demo.test2.test6;

public class Singleton {
    private Singleton(){

    }

    private static Singleton instance;
     public synchronized static Singleton getInstance(){
        if (instance == null)    instance = new Singleton();
        return instance;
    }
}
