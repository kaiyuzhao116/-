package com.example.demo.factory.factory;

import com.example.demo.factory.factory.foctory.ItactDessttFactory;

public class TEST {
    public static void main(String[] args) {
        System.out.println("Hello World!");

        ItactDessttFactory itactDessttFactory = new ItactDessttFactory();
        Dessert dessert = itactDessttFactory.createDessert();
        dessert.show();
    }
}
