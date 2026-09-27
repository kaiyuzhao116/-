package com.example.demo.demo1.test1;

public class test1 {
    public static void main(String[] args) {
        Singoln instance = Singoln.INSTANCE;
        Singoln instance1 = Singoln.INSTANCE;
        System.out.print(instance == instance1);
        System.out.print(instance == instance1);
        System.out.println(instance == instance1);
    }
}
