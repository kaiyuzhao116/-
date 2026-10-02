package com.example.demo3;

public class test {
    public static void main(String[] args) {
        System.out.println("Hello World!");


        Phone phone = new Phone.Bulider().setBrand("苹果" +
                "iphone").setCamera("后置1200万像素，前置3200万像素").build();
        System.out.println("Phone: " + phone);

    }
}
