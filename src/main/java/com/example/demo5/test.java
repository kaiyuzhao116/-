package com.example.demo5;

public class test {
    public static void main(String[] args) {
        System.out.println("Hello World!");


        OfoBuiler ofoBuiler = new OfoBuiler();
//        ofoBuiler.buildFrame();
//        ofoBuiler.buildSeat();
//        ofoBuiler.construct();
        Dirceter dirceter = new Dirceter(ofoBuiler);

        Bike bike = dirceter.construct();

        System.out.println(bike);


    }
}
