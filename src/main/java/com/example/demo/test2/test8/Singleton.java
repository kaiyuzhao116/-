package com.example.demo.test2.test8;

public class Singleton {
    private Singleton(){

    }

    private static class SingletonHolder{
        private static final Singleton SINGLETON =  new Singleton();
    };

    public static Singleton getInstance(){
        return SingletonHolder.SINGLETON;
    }

}
