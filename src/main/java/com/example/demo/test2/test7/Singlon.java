package com.example.demo.test2.test7;

import lombok.Synchronized;

public class Singlon {
    private Singlon(){

    }

    private static Singlon instance;

    public static Singlon getInstance(){
        if (instance == null){
            synchronized (Singlon.class){
                if (instance == null){
                    instance = new Singlon();
                }

                return instance;
            }
        }
        return instance;
    }
}
