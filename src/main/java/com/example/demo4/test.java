package com.example.demo4;

public class test {

    public static void main(String[] args) {
        System.out.println("Hello World");

        Direecter direecter = new Direecter(new OfoBulider());


        Bike bike = direecter.construct();

        System.out.println(bike.getFrame() + " " + bike.getSeat());

    }

}
